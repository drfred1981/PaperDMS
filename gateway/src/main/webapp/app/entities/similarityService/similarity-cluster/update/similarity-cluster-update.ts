import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { SimilarityAlgorithm } from 'app/entities/enumerations/similarity-algorithm.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { SimilarityClusterService } from '../service/similarity-cluster.service';
import { ISimilarityCluster } from '../similarity-cluster.model';

import { SimilarityClusterFormGroup, SimilarityClusterFormService } from './similarity-cluster-form.service';

@Component({
  selector: 'jhi-similarity-cluster-update',
  templateUrl: './similarity-cluster-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class SimilarityClusterUpdate implements OnInit {
  isSaving = false;
  similarityCluster: ISimilarityCluster | null = null;
  similarityAlgorithmValues = Object.keys(SimilarityAlgorithm);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected similarityClusterService = inject(SimilarityClusterService);
  protected similarityClusterFormService = inject(SimilarityClusterFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SimilarityClusterFormGroup = this.similarityClusterFormService.createSimilarityClusterFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ similarityCluster }) => {
      this.similarityCluster = similarityCluster;
      if (similarityCluster) {
        this.updateForm(similarityCluster);
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
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const similarityCluster = this.similarityClusterFormService.getSimilarityCluster(this.editForm);
    if (similarityCluster.id === null) {
      this.subscribeToSaveResponse(this.similarityClusterService.create(similarityCluster));
    } else {
      this.subscribeToSaveResponse(this.similarityClusterService.update(similarityCluster));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISimilarityCluster>>): void {
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

  protected updateForm(similarityCluster: ISimilarityCluster): void {
    this.similarityCluster = similarityCluster;
    this.similarityClusterFormService.resetForm(this.editForm, similarityCluster);
  }
}
