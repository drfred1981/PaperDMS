import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';
import { MergeJobService } from '../service/merge-job.service';
import { IMergeJob } from '../merge-job.model';
import { MergeJobFormGroup, MergeJobFormService } from './merge-job-form.service';

@Component({
  selector: 'jhi-merge-job-update',
  templateUrl: './merge-job-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MergeJobUpdateComponent implements OnInit {
  isSaving = false;
  mergeJob: IMergeJob | null = null;
  transformStatusValues = Object.keys(TransformStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected mergeJobService = inject(MergeJobService);
  protected mergeJobFormService = inject(MergeJobFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MergeJobFormGroup = this.mergeJobFormService.createMergeJobFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mergeJob }) => {
      this.mergeJob = mergeJob;
      if (mergeJob) {
        this.updateForm(mergeJob);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mergeJob = this.mergeJobFormService.getMergeJob(this.editForm);
    if (mergeJob.id !== null) {
      this.subscribeToSaveResponse(this.mergeJobService.update(mergeJob));
    } else {
      this.subscribeToSaveResponse(this.mergeJobService.create(mergeJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMergeJob>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(mergeJob: IMergeJob): void {
    this.mergeJob = mergeJob;
    this.mergeJobFormService.resetForm(this.editForm, mergeJob);
  }
}
