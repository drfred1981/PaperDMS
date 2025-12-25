import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IOcrJob } from 'app/entities/ocrService/ocr-job/ocr-job.model';
import { OcrJobService } from 'app/entities/ocrService/ocr-job/service/ocr-job.service';
import { ExtractedTextService } from '../service/extracted-text.service';
import { IExtractedText } from '../extracted-text.model';
import { ExtractedTextFormGroup, ExtractedTextFormService } from './extracted-text-form.service';

@Component({
  selector: 'jhi-extracted-text-update',
  templateUrl: './extracted-text-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExtractedTextUpdateComponent implements OnInit {
  isSaving = false;
  extractedText: IExtractedText | null = null;

  ocrJobsSharedCollection: IOcrJob[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected extractedTextService = inject(ExtractedTextService);
  protected extractedTextFormService = inject(ExtractedTextFormService);
  protected ocrJobService = inject(OcrJobService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExtractedTextFormGroup = this.extractedTextFormService.createExtractedTextFormGroup();

  compareOcrJob = (o1: IOcrJob | null, o2: IOcrJob | null): boolean => this.ocrJobService.compareOcrJob(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ extractedText }) => {
      this.extractedText = extractedText;
      if (extractedText) {
        this.updateForm(extractedText);
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
        this.eventManager.broadcast(new EventWithContent<AlertError>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const extractedText = this.extractedTextFormService.getExtractedText(this.editForm);
    if (extractedText.id !== null) {
      this.subscribeToSaveResponse(this.extractedTextService.update(extractedText));
    } else {
      this.subscribeToSaveResponse(this.extractedTextService.create(extractedText));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExtractedText>>): void {
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

  protected updateForm(extractedText: IExtractedText): void {
    this.extractedText = extractedText;
    this.extractedTextFormService.resetForm(this.editForm, extractedText);

    this.ocrJobsSharedCollection = this.ocrJobService.addOcrJobToCollectionIfMissing<IOcrJob>(
      this.ocrJobsSharedCollection,
      extractedText.job,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ocrJobService
      .query()
      .pipe(map((res: HttpResponse<IOcrJob[]>) => res.body ?? []))
      .pipe(map((ocrJobs: IOcrJob[]) => this.ocrJobService.addOcrJobToCollectionIfMissing<IOcrJob>(ocrJobs, this.extractedText?.job)))
      .subscribe((ocrJobs: IOcrJob[]) => (this.ocrJobsSharedCollection = ocrJobs));
  }
}
