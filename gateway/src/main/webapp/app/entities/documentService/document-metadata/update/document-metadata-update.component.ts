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
import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { MetadataType } from 'app/entities/enumerations/metadata-type.model';
import { DocumentMetadataService } from '../service/document-metadata.service';
import { IDocumentMetadata } from '../document-metadata.model';
import { DocumentMetadataFormGroup, DocumentMetadataFormService } from './document-metadata-form.service';

@Component({
  selector: 'jhi-document-metadata-update',
  templateUrl: './document-metadata-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentMetadataUpdateComponent implements OnInit {
  isSaving = false;
  documentMetadata: IDocumentMetadata | null = null;
  metadataTypeValues = Object.keys(MetadataType);

  documentsSharedCollection: IDocument[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected documentMetadataService = inject(DocumentMetadataService);
  protected documentMetadataFormService = inject(DocumentMetadataFormService);
  protected documentService = inject(DocumentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentMetadataFormGroup = this.documentMetadataFormService.createDocumentMetadataFormGroup();

  compareDocument = (o1: IDocument | null, o2: IDocument | null): boolean => this.documentService.compareDocument(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentMetadata }) => {
      this.documentMetadata = documentMetadata;
      if (documentMetadata) {
        this.updateForm(documentMetadata);
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
    const documentMetadata = this.documentMetadataFormService.getDocumentMetadata(this.editForm);
    if (documentMetadata.id !== null) {
      this.subscribeToSaveResponse(this.documentMetadataService.update(documentMetadata));
    } else {
      this.subscribeToSaveResponse(this.documentMetadataService.create(documentMetadata));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentMetadata>>): void {
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

  protected updateForm(documentMetadata: IDocumentMetadata): void {
    this.documentMetadata = documentMetadata;
    this.documentMetadataFormService.resetForm(this.editForm, documentMetadata);

    this.documentsSharedCollection = this.documentService.addDocumentToCollectionIfMissing<IDocument>(
      this.documentsSharedCollection,
      documentMetadata.document,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentService
      .query()
      .pipe(map((res: HttpResponse<IDocument[]>) => res.body ?? []))
      .pipe(
        map((documents: IDocument[]) =>
          this.documentService.addDocumentToCollectionIfMissing<IDocument>(documents, this.documentMetadata?.document),
        ),
      )
      .subscribe((documents: IDocument[]) => (this.documentsSharedCollection = documents));
  }
}
