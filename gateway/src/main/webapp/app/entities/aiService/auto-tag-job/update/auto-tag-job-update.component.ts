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
import { AiJobStatus } from 'app/entities/enumerations/ai-job-status.model';
import { AutoTagJobService } from '../service/auto-tag-job.service';
import { IAutoTagJob } from '../auto-tag-job.model';
import { AutoTagJobFormGroup, AutoTagJobFormService } from './auto-tag-job-form.service';

@Component({
  selector: 'jhi-auto-tag-job-update',
  templateUrl: './auto-tag-job-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AutoTagJobUpdateComponent implements OnInit {
  isSaving = false;
  autoTagJob: IAutoTagJob | null = null;
  aiJobStatusValues = Object.keys(AiJobStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected autoTagJobService = inject(AutoTagJobService);
  protected autoTagJobFormService = inject(AutoTagJobFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AutoTagJobFormGroup = this.autoTagJobFormService.createAutoTagJobFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ autoTagJob }) => {
      this.autoTagJob = autoTagJob;
      if (autoTagJob) {
        this.updateForm(autoTagJob);
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
    const autoTagJob = this.autoTagJobFormService.getAutoTagJob(this.editForm);
    if (autoTagJob.id !== null) {
      this.subscribeToSaveResponse(this.autoTagJobService.update(autoTagJob));
    } else {
      this.subscribeToSaveResponse(this.autoTagJobService.create(autoTagJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAutoTagJob>>): void {
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

  protected updateForm(autoTagJob: IAutoTagJob): void {
    this.autoTagJob = autoTagJob;
    this.autoTagJobFormService.resetForm(this.editForm, autoTagJob);
  }
}
