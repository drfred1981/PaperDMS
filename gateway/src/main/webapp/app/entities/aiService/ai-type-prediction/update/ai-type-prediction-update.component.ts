import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAITypePrediction } from '../ai-type-prediction.model';
import { AITypePredictionService } from '../service/ai-type-prediction.service';
import { AITypePredictionFormGroup, AITypePredictionFormService } from './ai-type-prediction-form.service';

@Component({
  selector: 'jhi-ai-type-prediction-update',
  templateUrl: './ai-type-prediction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AITypePredictionUpdateComponent implements OnInit {
  isSaving = false;
  aITypePrediction: IAITypePrediction | null = null;

  protected aITypePredictionService = inject(AITypePredictionService);
  protected aITypePredictionFormService = inject(AITypePredictionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AITypePredictionFormGroup = this.aITypePredictionFormService.createAITypePredictionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aITypePrediction }) => {
      this.aITypePrediction = aITypePrediction;
      if (aITypePrediction) {
        this.updateForm(aITypePrediction);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aITypePrediction = this.aITypePredictionFormService.getAITypePrediction(this.editForm);
    if (aITypePrediction.id !== null) {
      this.subscribeToSaveResponse(this.aITypePredictionService.update(aITypePrediction));
    } else {
      this.subscribeToSaveResponse(this.aITypePredictionService.create(aITypePrediction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAITypePrediction>>): void {
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

  protected updateForm(aITypePrediction: IAITypePrediction): void {
    this.aITypePrediction = aITypePrediction;
    this.aITypePredictionFormService.resetForm(this.editForm, aITypePrediction);
  }
}
