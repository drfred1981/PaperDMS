import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { OcrEngine } from 'app/entities/enumerations/ocr-engine.model';
import { OcrStatus } from 'app/entities/enumerations/ocr-status.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IOcrJob } from '../ocr-job.model';
import { OcrJobService } from '../service/ocr-job.service';

import { OcrJobFormGroup, OcrJobFormService } from './ocr-job-form.service';

@Component({
  selector: 'jhi-ocr-job-update',
  templateUrl: './ocr-job-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class OcrJobUpdate implements OnInit {
  isSaving = false;
  ocrJob: IOcrJob | null = null;
  ocrStatusValues = Object.keys(OcrStatus);
  ocrEngineValues = Object.keys(OcrEngine);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected ocrJobService = inject(OcrJobService);
  protected ocrJobFormService = inject(OcrJobFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OcrJobFormGroup = this.ocrJobFormService.createOcrJobFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ocrJob }) => {
      this.ocrJob = ocrJob;
      if (ocrJob) {
        this.updateForm(ocrJob);
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
    const ocrJob = this.ocrJobFormService.getOcrJob(this.editForm);
    if (ocrJob.id === null) {
      this.subscribeToSaveResponse(this.ocrJobService.create(ocrJob));
    } else {
      this.subscribeToSaveResponse(this.ocrJobService.update(ocrJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOcrJob>>): void {
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

  protected updateForm(ocrJob: IOcrJob): void {
    this.ocrJob = ocrJob;
    this.ocrJobFormService.resetForm(this.editForm, ocrJob);
  }
}
