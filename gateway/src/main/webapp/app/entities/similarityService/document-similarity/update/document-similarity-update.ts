import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { SimilarityAlgorithm } from 'app/entities/enumerations/similarity-algorithm.model';
import { SimilarityJobService } from 'app/entities/similarityService/similarity-job/service/similarity-job.service';
import { ISimilarityJob } from 'app/entities/similarityService/similarity-job/similarity-job.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IDocumentSimilarity } from '../document-similarity.model';
import { DocumentSimilarityService } from '../service/document-similarity.service';

import { DocumentSimilarityFormGroup, DocumentSimilarityFormService } from './document-similarity-form.service';

@Component({
  selector: 'jhi-document-similarity-update',
  templateUrl: './document-similarity-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class DocumentSimilarityUpdate implements OnInit {
  isSaving = false;
  documentSimilarity: IDocumentSimilarity | null = null;
  similarityAlgorithmValues = Object.keys(SimilarityAlgorithm);

  similarityJobsSharedCollection = signal<ISimilarityJob[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected documentSimilarityService = inject(DocumentSimilarityService);
  protected documentSimilarityFormService = inject(DocumentSimilarityFormService);
  protected similarityJobService = inject(SimilarityJobService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentSimilarityFormGroup = this.documentSimilarityFormService.createDocumentSimilarityFormGroup();

  compareSimilarityJob = (o1: ISimilarityJob | null, o2: ISimilarityJob | null): boolean =>
    this.similarityJobService.compareSimilarityJob(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentSimilarity }) => {
      this.documentSimilarity = documentSimilarity;
      if (documentSimilarity) {
        this.updateForm(documentSimilarity);
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
    const documentSimilarity = this.documentSimilarityFormService.getDocumentSimilarity(this.editForm);
    if (documentSimilarity.id === null) {
      this.subscribeToSaveResponse(this.documentSimilarityService.create(documentSimilarity));
    } else {
      this.subscribeToSaveResponse(this.documentSimilarityService.update(documentSimilarity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentSimilarity>>): void {
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

  protected updateForm(documentSimilarity: IDocumentSimilarity): void {
    this.documentSimilarity = documentSimilarity;
    this.documentSimilarityFormService.resetForm(this.editForm, documentSimilarity);

    this.similarityJobsSharedCollection.set(
      this.similarityJobService.addSimilarityJobToCollectionIfMissing<ISimilarityJob>(
        this.similarityJobsSharedCollection(),
        documentSimilarity.job,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.similarityJobService
      .query()
      .pipe(map((res: HttpResponse<ISimilarityJob[]>) => res.body ?? []))
      .pipe(
        map((similarityJobs: ISimilarityJob[]) =>
          this.similarityJobService.addSimilarityJobToCollectionIfMissing<ISimilarityJob>(similarityJobs, this.documentSimilarity?.job),
        ),
      )
      .subscribe((similarityJobs: ISimilarityJob[]) => this.similarityJobsSharedCollection.set(similarityJobs));
  }
}
