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
import { WatermarkType } from 'app/entities/enumerations/watermark-type.model';
import { WatermarkPosition } from 'app/entities/enumerations/watermark-position.model';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';
import { TransformWatermarkJobService } from '../service/transform-watermark-job.service';
import { ITransformWatermarkJob } from '../transform-watermark-job.model';
import { TransformWatermarkJobFormGroup, TransformWatermarkJobFormService } from './transform-watermark-job-form.service';

@Component({
  selector: 'jhi-transform-watermark-job-update',
  templateUrl: './transform-watermark-job-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransformWatermarkJobUpdateComponent implements OnInit {
  isSaving = false;
  transformWatermarkJob: ITransformWatermarkJob | null = null;
  watermarkTypeValues = Object.keys(WatermarkType);
  watermarkPositionValues = Object.keys(WatermarkPosition);
  transformStatusValues = Object.keys(TransformStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected transformWatermarkJobService = inject(TransformWatermarkJobService);
  protected transformWatermarkJobFormService = inject(TransformWatermarkJobFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransformWatermarkJobFormGroup = this.transformWatermarkJobFormService.createTransformWatermarkJobFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transformWatermarkJob }) => {
      this.transformWatermarkJob = transformWatermarkJob;
      if (transformWatermarkJob) {
        this.updateForm(transformWatermarkJob);
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
    const transformWatermarkJob = this.transformWatermarkJobFormService.getTransformWatermarkJob(this.editForm);
    if (transformWatermarkJob.id !== null) {
      this.subscribeToSaveResponse(this.transformWatermarkJobService.update(transformWatermarkJob));
    } else {
      this.subscribeToSaveResponse(this.transformWatermarkJobService.create(transformWatermarkJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransformWatermarkJob>>): void {
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

  protected updateForm(transformWatermarkJob: ITransformWatermarkJob): void {
    this.transformWatermarkJob = transformWatermarkJob;
    this.transformWatermarkJobFormService.resetForm(this.editForm, transformWatermarkJob);
  }
}
