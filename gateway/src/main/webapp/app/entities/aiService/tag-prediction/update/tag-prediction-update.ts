import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAutoTagJob } from 'app/entities/aiService/auto-tag-job/auto-tag-job.model';
import { AutoTagJobService } from 'app/entities/aiService/auto-tag-job/service/auto-tag-job.service';
import SharedModule from 'app/shared/shared.module';
import { TagPredictionService } from '../service/tag-prediction.service';
import { ITagPrediction } from '../tag-prediction.model';

import { TagPredictionFormGroup, TagPredictionFormService } from './tag-prediction-form.service';

@Component({
  selector: 'jhi-tag-prediction-update',
  templateUrl: './tag-prediction-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class TagPredictionUpdate implements OnInit {
  isSaving = false;
  tagPrediction: ITagPrediction | null = null;

  autoTagJobsSharedCollection = signal<IAutoTagJob[]>([]);

  protected tagPredictionService = inject(TagPredictionService);
  protected tagPredictionFormService = inject(TagPredictionFormService);
  protected autoTagJobService = inject(AutoTagJobService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TagPredictionFormGroup = this.tagPredictionFormService.createTagPredictionFormGroup();

  compareAutoTagJob = (o1: IAutoTagJob | null, o2: IAutoTagJob | null): boolean => this.autoTagJobService.compareAutoTagJob(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tagPrediction }) => {
      this.tagPrediction = tagPrediction;
      if (tagPrediction) {
        this.updateForm(tagPrediction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tagPrediction = this.tagPredictionFormService.getTagPrediction(this.editForm);
    if (tagPrediction.id === null) {
      this.subscribeToSaveResponse(this.tagPredictionService.create(tagPrediction));
    } else {
      this.subscribeToSaveResponse(this.tagPredictionService.update(tagPrediction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITagPrediction>>): void {
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

  protected updateForm(tagPrediction: ITagPrediction): void {
    this.tagPrediction = tagPrediction;
    this.tagPredictionFormService.resetForm(this.editForm, tagPrediction);

    this.autoTagJobsSharedCollection.set(
      this.autoTagJobService.addAutoTagJobToCollectionIfMissing<IAutoTagJob>(this.autoTagJobsSharedCollection(), tagPrediction.job),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.autoTagJobService
      .query()
      .pipe(map((res: HttpResponse<IAutoTagJob[]>) => res.body ?? []))
      .pipe(
        map((autoTagJobs: IAutoTagJob[]) =>
          this.autoTagJobService.addAutoTagJobToCollectionIfMissing<IAutoTagJob>(autoTagJobs, this.tagPrediction?.job),
        ),
      )
      .subscribe((autoTagJobs: IAutoTagJob[]) => this.autoTagJobsSharedCollection.set(autoTagJobs));
  }
}
