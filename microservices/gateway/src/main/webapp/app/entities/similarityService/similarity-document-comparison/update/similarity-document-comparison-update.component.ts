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
import { ISimilarityJob } from 'app/entities/similarityService/similarity-job/similarity-job.model';
import { SimilarityJobService } from 'app/entities/similarityService/similarity-job/service/similarity-job.service';
import { SimilarityAlgorithm } from 'app/entities/enumerations/similarity-algorithm.model';
import { SimilarityDocumentComparisonService } from '../service/similarity-document-comparison.service';
import { ISimilarityDocumentComparison } from '../similarity-document-comparison.model';
import {
  SimilarityDocumentComparisonFormGroup,
  SimilarityDocumentComparisonFormService,
} from './similarity-document-comparison-form.service';

@Component({
  selector: 'jhi-similarity-document-comparison-update',
  templateUrl: './similarity-document-comparison-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SimilarityDocumentComparisonUpdateComponent implements OnInit {
  isSaving = false;
  similarityDocumentComparison: ISimilarityDocumentComparison | null = null;
  similarityAlgorithmValues = Object.keys(SimilarityAlgorithm);

  similarityJobsSharedCollection: ISimilarityJob[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected similarityDocumentComparisonService = inject(SimilarityDocumentComparisonService);
  protected similarityDocumentComparisonFormService = inject(SimilarityDocumentComparisonFormService);
  protected similarityJobService = inject(SimilarityJobService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SimilarityDocumentComparisonFormGroup =
    this.similarityDocumentComparisonFormService.createSimilarityDocumentComparisonFormGroup();

  compareSimilarityJob = (o1: ISimilarityJob | null, o2: ISimilarityJob | null): boolean =>
    this.similarityJobService.compareSimilarityJob(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ similarityDocumentComparison }) => {
      this.similarityDocumentComparison = similarityDocumentComparison;
      if (similarityDocumentComparison) {
        this.updateForm(similarityDocumentComparison);
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
    const similarityDocumentComparison = this.similarityDocumentComparisonFormService.getSimilarityDocumentComparison(this.editForm);
    if (similarityDocumentComparison.id !== null) {
      this.subscribeToSaveResponse(this.similarityDocumentComparisonService.update(similarityDocumentComparison));
    } else {
      this.subscribeToSaveResponse(this.similarityDocumentComparisonService.create(similarityDocumentComparison));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISimilarityDocumentComparison>>): void {
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

  protected updateForm(similarityDocumentComparison: ISimilarityDocumentComparison): void {
    this.similarityDocumentComparison = similarityDocumentComparison;
    this.similarityDocumentComparisonFormService.resetForm(this.editForm, similarityDocumentComparison);

    this.similarityJobsSharedCollection = this.similarityJobService.addSimilarityJobToCollectionIfMissing<ISimilarityJob>(
      this.similarityJobsSharedCollection,
      similarityDocumentComparison.job,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.similarityJobService
      .query()
      .pipe(map((res: HttpResponse<ISimilarityJob[]>) => res.body ?? []))
      .pipe(
        map((similarityJobs: ISimilarityJob[]) =>
          this.similarityJobService.addSimilarityJobToCollectionIfMissing<ISimilarityJob>(
            similarityJobs,
            this.similarityDocumentComparison?.job,
          ),
        ),
      )
      .subscribe((similarityJobs: ISimilarityJob[]) => (this.similarityJobsSharedCollection = similarityJobs));
  }
}
