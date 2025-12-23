import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { OcrEngine } from 'app/entities/enumerations/ocr-engine.model';
import { IOcrJob } from 'app/entities/ocrService/ocr-job/ocr-job.model';
import { OcrJobService } from 'app/entities/ocrService/ocr-job/service/ocr-job.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IOcrResult } from '../ocr-result.model';
import { OcrResultService } from '../service/ocr-result.service';

import { OcrResultFormGroup, OcrResultFormService } from './ocr-result-form.service';

@Component({
  selector: 'jhi-ocr-result-update',
  templateUrl: './ocr-result-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class OcrResultUpdate implements OnInit {
  isSaving = false;
  ocrResult: IOcrResult | null = null;
  ocrEngineValues = Object.keys(OcrEngine);

  ocrJobsSharedCollection = signal<IOcrJob[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected ocrResultService = inject(OcrResultService);
  protected ocrResultFormService = inject(OcrResultFormService);
  protected ocrJobService = inject(OcrJobService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OcrResultFormGroup = this.ocrResultFormService.createOcrResultFormGroup();

  compareOcrJob = (o1: IOcrJob | null, o2: IOcrJob | null): boolean => this.ocrJobService.compareOcrJob(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ocrResult }) => {
      this.ocrResult = ocrResult;
      if (ocrResult) {
        this.updateForm(ocrResult);
      }

      this.loadRelationshipsOptions();
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
    const ocrResult = this.ocrResultFormService.getOcrResult(this.editForm);
    if (ocrResult.id === null) {
      this.subscribeToSaveResponse(this.ocrResultService.create(ocrResult));
    } else {
      this.subscribeToSaveResponse(this.ocrResultService.update(ocrResult));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOcrResult>>): void {
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

  protected updateForm(ocrResult: IOcrResult): void {
    this.ocrResult = ocrResult;
    this.ocrResultFormService.resetForm(this.editForm, ocrResult);

    this.ocrJobsSharedCollection.set(
      this.ocrJobService.addOcrJobToCollectionIfMissing<IOcrJob>(this.ocrJobsSharedCollection(), ocrResult.job),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ocrJobService
      .query()
      .pipe(map((res: HttpResponse<IOcrJob[]>) => res.body ?? []))
      .pipe(map((ocrJobs: IOcrJob[]) => this.ocrJobService.addOcrJobToCollectionIfMissing<IOcrJob>(ocrJobs, this.ocrResult?.job)))
      .subscribe((ocrJobs: IOcrJob[]) => this.ocrJobsSharedCollection.set(ocrJobs));
  }
}
