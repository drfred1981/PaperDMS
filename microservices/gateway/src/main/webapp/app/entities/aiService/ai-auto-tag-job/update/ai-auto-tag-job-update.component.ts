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
import { IAITypePrediction } from 'app/entities/aiService/ai-type-prediction/ai-type-prediction.model';
import { AITypePredictionService } from 'app/entities/aiService/ai-type-prediction/service/ai-type-prediction.service';
import { IAILanguageDetection } from 'app/entities/aiService/ai-language-detection/ai-language-detection.model';
import { AILanguageDetectionService } from 'app/entities/aiService/ai-language-detection/service/ai-language-detection.service';
import { AiJobStatus } from 'app/entities/enumerations/ai-job-status.model';
import { AIAutoTagJobService } from '../service/ai-auto-tag-job.service';
import { IAIAutoTagJob } from '../ai-auto-tag-job.model';
import { AIAutoTagJobFormGroup, AIAutoTagJobFormService } from './ai-auto-tag-job-form.service';

@Component({
  selector: 'jhi-ai-auto-tag-job-update',
  templateUrl: './ai-auto-tag-job-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AIAutoTagJobUpdateComponent implements OnInit {
  isSaving = false;
  aIAutoTagJob: IAIAutoTagJob | null = null;
  aiJobStatusValues = Object.keys(AiJobStatus);

  aITypePredictionsCollection: IAITypePrediction[] = [];
  languagePredictionsCollection: IAILanguageDetection[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected aIAutoTagJobService = inject(AIAutoTagJobService);
  protected aIAutoTagJobFormService = inject(AIAutoTagJobFormService);
  protected aITypePredictionService = inject(AITypePredictionService);
  protected aILanguageDetectionService = inject(AILanguageDetectionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AIAutoTagJobFormGroup = this.aIAutoTagJobFormService.createAIAutoTagJobFormGroup();

  compareAITypePrediction = (o1: IAITypePrediction | null, o2: IAITypePrediction | null): boolean =>
    this.aITypePredictionService.compareAITypePrediction(o1, o2);

  compareAILanguageDetection = (o1: IAILanguageDetection | null, o2: IAILanguageDetection | null): boolean =>
    this.aILanguageDetectionService.compareAILanguageDetection(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aIAutoTagJob }) => {
      this.aIAutoTagJob = aIAutoTagJob;
      if (aIAutoTagJob) {
        this.updateForm(aIAutoTagJob);
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
    const aIAutoTagJob = this.aIAutoTagJobFormService.getAIAutoTagJob(this.editForm);
    if (aIAutoTagJob.id !== null) {
      this.subscribeToSaveResponse(this.aIAutoTagJobService.update(aIAutoTagJob));
    } else {
      this.subscribeToSaveResponse(this.aIAutoTagJobService.create(aIAutoTagJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAIAutoTagJob>>): void {
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

  protected updateForm(aIAutoTagJob: IAIAutoTagJob): void {
    this.aIAutoTagJob = aIAutoTagJob;
    this.aIAutoTagJobFormService.resetForm(this.editForm, aIAutoTagJob);

    this.aITypePredictionsCollection = this.aITypePredictionService.addAITypePredictionToCollectionIfMissing<IAITypePrediction>(
      this.aITypePredictionsCollection,
      aIAutoTagJob.aITypePrediction,
    );
    this.languagePredictionsCollection = this.aILanguageDetectionService.addAILanguageDetectionToCollectionIfMissing<IAILanguageDetection>(
      this.languagePredictionsCollection,
      aIAutoTagJob.languagePrediction,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.aITypePredictionService
      .query({ 'jobId.specified': 'false' })
      .pipe(map((res: HttpResponse<IAITypePrediction[]>) => res.body ?? []))
      .pipe(
        map((aITypePredictions: IAITypePrediction[]) =>
          this.aITypePredictionService.addAITypePredictionToCollectionIfMissing<IAITypePrediction>(
            aITypePredictions,
            this.aIAutoTagJob?.aITypePrediction,
          ),
        ),
      )
      .subscribe((aITypePredictions: IAITypePrediction[]) => (this.aITypePredictionsCollection = aITypePredictions));

    this.aILanguageDetectionService
      .query({ 'jobId.specified': 'false' })
      .pipe(map((res: HttpResponse<IAILanguageDetection[]>) => res.body ?? []))
      .pipe(
        map((aILanguageDetections: IAILanguageDetection[]) =>
          this.aILanguageDetectionService.addAILanguageDetectionToCollectionIfMissing<IAILanguageDetection>(
            aILanguageDetections,
            this.aIAutoTagJob?.languagePrediction,
          ),
        ),
      )
      .subscribe((aILanguageDetections: IAILanguageDetection[]) => (this.languagePredictionsCollection = aILanguageDetections));
  }
}
