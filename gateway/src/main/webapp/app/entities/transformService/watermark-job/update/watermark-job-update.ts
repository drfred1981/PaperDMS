import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';
import { WatermarkPosition } from 'app/entities/enumerations/watermark-position.model';
import { WatermarkType } from 'app/entities/enumerations/watermark-type.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { WatermarkJobService } from '../service/watermark-job.service';
import { IWatermarkJob } from '../watermark-job.model';

import { WatermarkJobFormGroup, WatermarkJobFormService } from './watermark-job-form.service';

@Component({
  selector: 'jhi-watermark-job-update',
  templateUrl: './watermark-job-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class WatermarkJobUpdate implements OnInit {
  isSaving = false;
  watermarkJob: IWatermarkJob | null = null;
  watermarkTypeValues = Object.keys(WatermarkType);
  watermarkPositionValues = Object.keys(WatermarkPosition);
  transformStatusValues = Object.keys(TransformStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected watermarkJobService = inject(WatermarkJobService);
  protected watermarkJobFormService = inject(WatermarkJobFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WatermarkJobFormGroup = this.watermarkJobFormService.createWatermarkJobFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ watermarkJob }) => {
      this.watermarkJob = watermarkJob;
      if (watermarkJob) {
        this.updateForm(watermarkJob);
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
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const watermarkJob = this.watermarkJobFormService.getWatermarkJob(this.editForm);
    if (watermarkJob.id === null) {
      this.subscribeToSaveResponse(this.watermarkJobService.create(watermarkJob));
    } else {
      this.subscribeToSaveResponse(this.watermarkJobService.update(watermarkJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWatermarkJob>>): void {
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

  protected updateForm(watermarkJob: IWatermarkJob): void {
    this.watermarkJob = watermarkJob;
    this.watermarkJobFormService.resetForm(this.editForm, watermarkJob);
  }
}
