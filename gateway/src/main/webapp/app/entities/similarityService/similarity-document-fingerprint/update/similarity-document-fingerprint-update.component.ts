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
import { FingerprintType } from 'app/entities/enumerations/fingerprint-type.model';
import { SimilarityDocumentFingerprintService } from '../service/similarity-document-fingerprint.service';
import { ISimilarityDocumentFingerprint } from '../similarity-document-fingerprint.model';
import {
  SimilarityDocumentFingerprintFormGroup,
  SimilarityDocumentFingerprintFormService,
} from './similarity-document-fingerprint-form.service';

@Component({
  selector: 'jhi-similarity-document-fingerprint-update',
  templateUrl: './similarity-document-fingerprint-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SimilarityDocumentFingerprintUpdateComponent implements OnInit {
  isSaving = false;
  similarityDocumentFingerprint: ISimilarityDocumentFingerprint | null = null;
  fingerprintTypeValues = Object.keys(FingerprintType);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected similarityDocumentFingerprintService = inject(SimilarityDocumentFingerprintService);
  protected similarityDocumentFingerprintFormService = inject(SimilarityDocumentFingerprintFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SimilarityDocumentFingerprintFormGroup =
    this.similarityDocumentFingerprintFormService.createSimilarityDocumentFingerprintFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ similarityDocumentFingerprint }) => {
      this.similarityDocumentFingerprint = similarityDocumentFingerprint;
      if (similarityDocumentFingerprint) {
        this.updateForm(similarityDocumentFingerprint);
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
    const similarityDocumentFingerprint = this.similarityDocumentFingerprintFormService.getSimilarityDocumentFingerprint(this.editForm);
    if (similarityDocumentFingerprint.id !== null) {
      this.subscribeToSaveResponse(this.similarityDocumentFingerprintService.update(similarityDocumentFingerprint));
    } else {
      this.subscribeToSaveResponse(this.similarityDocumentFingerprintService.create(similarityDocumentFingerprint));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISimilarityDocumentFingerprint>>): void {
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

  protected updateForm(similarityDocumentFingerprint: ISimilarityDocumentFingerprint): void {
    this.similarityDocumentFingerprint = similarityDocumentFingerprint;
    this.similarityDocumentFingerprintFormService.resetForm(this.editForm, similarityDocumentFingerprint);
  }
}
