import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { AiJobStatus } from 'app/entities/enumerations/ai-job-status.model';
import { SimilarityAlgorithm } from 'app/entities/enumerations/similarity-algorithm.model';
import { SimilarityScope } from 'app/entities/enumerations/similarity-scope.model';
import { SimilarityJobService } from '../service/similarity-job.service';
import { ISimilarityJob } from '../similarity-job.model';
import { SimilarityJobFormGroup, SimilarityJobFormService } from './similarity-job-form.service';

@Component({
  selector: 'jhi-similarity-job-update',
  templateUrl: './similarity-job-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SimilarityJobUpdateComponent implements OnInit {
  isSaving = false;
  similarityJob: ISimilarityJob | null = null;
  aiJobStatusValues = Object.keys(AiJobStatus);
  similarityAlgorithmValues = Object.keys(SimilarityAlgorithm);
  similarityScopeValues = Object.keys(SimilarityScope);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected similarityJobService = inject(SimilarityJobService);
  protected similarityJobFormService = inject(SimilarityJobFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SimilarityJobFormGroup = this.similarityJobFormService.createSimilarityJobFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ similarityJob }) => {
      this.similarityJob = similarityJob;
      if (similarityJob) {
        this.updateForm(similarityJob);
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
        this.eventManager.broadcast(new EventWithContent<AlertError>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const similarityJob = this.similarityJobFormService.getSimilarityJob(this.editForm);
    if (similarityJob.id !== null) {
      this.subscribeToSaveResponse(this.similarityJobService.update(similarityJob));
    } else {
      this.subscribeToSaveResponse(this.similarityJobService.create(similarityJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISimilarityJob>>): void {
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

  protected updateForm(similarityJob: ISimilarityJob): void {
    this.similarityJob = similarityJob;
    this.similarityJobFormService.resetForm(this.editForm, similarityJob);
  }
}
