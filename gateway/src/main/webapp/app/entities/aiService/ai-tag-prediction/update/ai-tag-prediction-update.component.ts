import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAIAutoTagJob } from 'app/entities/aiService/ai-auto-tag-job/ai-auto-tag-job.model';
import { AIAutoTagJobService } from 'app/entities/aiService/ai-auto-tag-job/service/ai-auto-tag-job.service';
import { IAITagPrediction } from '../ai-tag-prediction.model';
import { AITagPredictionService } from '../service/ai-tag-prediction.service';
import { AITagPredictionFormGroup, AITagPredictionFormService } from './ai-tag-prediction-form.service';

@Component({
  selector: 'jhi-ai-tag-prediction-update',
  templateUrl: './ai-tag-prediction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AITagPredictionUpdateComponent implements OnInit {
  isSaving = false;
  aITagPrediction: IAITagPrediction | null = null;

  aIAutoTagJobsSharedCollection: IAIAutoTagJob[] = [];

  protected aITagPredictionService = inject(AITagPredictionService);
  protected aITagPredictionFormService = inject(AITagPredictionFormService);
  protected aIAutoTagJobService = inject(AIAutoTagJobService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AITagPredictionFormGroup = this.aITagPredictionFormService.createAITagPredictionFormGroup();

  compareAIAutoTagJob = (o1: IAIAutoTagJob | null, o2: IAIAutoTagJob | null): boolean =>
    this.aIAutoTagJobService.compareAIAutoTagJob(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aITagPrediction }) => {
      this.aITagPrediction = aITagPrediction;
      if (aITagPrediction) {
        this.updateForm(aITagPrediction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aITagPrediction = this.aITagPredictionFormService.getAITagPrediction(this.editForm);
    if (aITagPrediction.id !== null) {
      this.subscribeToSaveResponse(this.aITagPredictionService.update(aITagPrediction));
    } else {
      this.subscribeToSaveResponse(this.aITagPredictionService.create(aITagPrediction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAITagPrediction>>): void {
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

  protected updateForm(aITagPrediction: IAITagPrediction): void {
    this.aITagPrediction = aITagPrediction;
    this.aITagPredictionFormService.resetForm(this.editForm, aITagPrediction);

    this.aIAutoTagJobsSharedCollection = this.aIAutoTagJobService.addAIAutoTagJobToCollectionIfMissing<IAIAutoTagJob>(
      this.aIAutoTagJobsSharedCollection,
      aITagPrediction.job,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.aIAutoTagJobService
      .query()
      .pipe(map((res: HttpResponse<IAIAutoTagJob[]>) => res.body ?? []))
      .pipe(
        map((aIAutoTagJobs: IAIAutoTagJob[]) =>
          this.aIAutoTagJobService.addAIAutoTagJobToCollectionIfMissing<IAIAutoTagJob>(aIAutoTagJobs, this.aITagPrediction?.job),
        ),
      )
      .subscribe((aIAutoTagJobs: IAIAutoTagJob[]) => (this.aIAutoTagJobsSharedCollection = aIAutoTagJobs));
  }
}
