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
import { TransformMergeJobService } from '../service/transform-merge-job.service';
import { ITransformMergeJob } from '../transform-merge-job.model';
import { TransformMergeJobFormGroup, TransformMergeJobFormService } from './transform-merge-job-form.service';

@Component({
  selector: 'jhi-transform-merge-job-update',
  templateUrl: './transform-merge-job-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransformMergeJobUpdateComponent implements OnInit {
  isSaving = false;
  transformMergeJob: ITransformMergeJob | null = null;
  transformStatusValues = Object.keys(TransformStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected transformMergeJobService = inject(TransformMergeJobService);
  protected transformMergeJobFormService = inject(TransformMergeJobFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransformMergeJobFormGroup = this.transformMergeJobFormService.createTransformMergeJobFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transformMergeJob }) => {
      this.transformMergeJob = transformMergeJob;
      if (transformMergeJob) {
        this.updateForm(transformMergeJob);
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
    const transformMergeJob = this.transformMergeJobFormService.getTransformMergeJob(this.editForm);
    if (transformMergeJob.id !== null) {
      this.subscribeToSaveResponse(this.transformMergeJobService.update(transformMergeJob));
    } else {
      this.subscribeToSaveResponse(this.transformMergeJobService.create(transformMergeJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransformMergeJob>>): void {
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

  protected updateForm(transformMergeJob: ITransformMergeJob): void {
    this.transformMergeJob = transformMergeJob;
    this.transformMergeJobFormService.resetForm(this.editForm, transformMergeJob);
  }
}
