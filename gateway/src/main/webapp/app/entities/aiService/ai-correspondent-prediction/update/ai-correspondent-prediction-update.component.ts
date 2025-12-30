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
import { IAIAutoTagJob } from 'app/entities/aiService/ai-auto-tag-job/ai-auto-tag-job.model';
import { AIAutoTagJobService } from 'app/entities/aiService/ai-auto-tag-job/service/ai-auto-tag-job.service';
import { CorrespondentType } from 'app/entities/enumerations/correspondent-type.model';
import { CorrespondentRole } from 'app/entities/enumerations/correspondent-role.model';
import { AICorrespondentPredictionService } from '../service/ai-correspondent-prediction.service';
import { IAICorrespondentPrediction } from '../ai-correspondent-prediction.model';
import { AICorrespondentPredictionFormGroup, AICorrespondentPredictionFormService } from './ai-correspondent-prediction-form.service';

@Component({
  selector: 'jhi-ai-correspondent-prediction-update',
  templateUrl: './ai-correspondent-prediction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AICorrespondentPredictionUpdateComponent implements OnInit {
  isSaving = false;
  aICorrespondentPrediction: IAICorrespondentPrediction | null = null;
  correspondentTypeValues = Object.keys(CorrespondentType);
  correspondentRoleValues = Object.keys(CorrespondentRole);

  aIAutoTagJobsSharedCollection: IAIAutoTagJob[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected aICorrespondentPredictionService = inject(AICorrespondentPredictionService);
  protected aICorrespondentPredictionFormService = inject(AICorrespondentPredictionFormService);
  protected aIAutoTagJobService = inject(AIAutoTagJobService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AICorrespondentPredictionFormGroup = this.aICorrespondentPredictionFormService.createAICorrespondentPredictionFormGroup();

  compareAIAutoTagJob = (o1: IAIAutoTagJob | null, o2: IAIAutoTagJob | null): boolean =>
    this.aIAutoTagJobService.compareAIAutoTagJob(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aICorrespondentPrediction }) => {
      this.aICorrespondentPrediction = aICorrespondentPrediction;
      if (aICorrespondentPrediction) {
        this.updateForm(aICorrespondentPrediction);
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
    const aICorrespondentPrediction = this.aICorrespondentPredictionFormService.getAICorrespondentPrediction(this.editForm);
    if (aICorrespondentPrediction.id !== null) {
      this.subscribeToSaveResponse(this.aICorrespondentPredictionService.update(aICorrespondentPrediction));
    } else {
      this.subscribeToSaveResponse(this.aICorrespondentPredictionService.create(aICorrespondentPrediction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAICorrespondentPrediction>>): void {
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

  protected updateForm(aICorrespondentPrediction: IAICorrespondentPrediction): void {
    this.aICorrespondentPrediction = aICorrespondentPrediction;
    this.aICorrespondentPredictionFormService.resetForm(this.editForm, aICorrespondentPrediction);

    this.aIAutoTagJobsSharedCollection = this.aIAutoTagJobService.addAIAutoTagJobToCollectionIfMissing<IAIAutoTagJob>(
      this.aIAutoTagJobsSharedCollection,
      aICorrespondentPrediction.job,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.aIAutoTagJobService
      .query()
      .pipe(map((res: HttpResponse<IAIAutoTagJob[]>) => res.body ?? []))
      .pipe(
        map((aIAutoTagJobs: IAIAutoTagJob[]) =>
          this.aIAutoTagJobService.addAIAutoTagJobToCollectionIfMissing<IAIAutoTagJob>(aIAutoTagJobs, this.aICorrespondentPrediction?.job),
        ),
      )
      .subscribe((aIAutoTagJobs: IAIAutoTagJob[]) => (this.aIAutoTagJobsSharedCollection = aIAutoTagJobs));
  }
}
