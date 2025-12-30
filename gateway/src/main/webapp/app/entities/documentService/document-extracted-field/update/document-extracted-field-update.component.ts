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
import { ExtractionMethod } from 'app/entities/enumerations/extraction-method.model';
import { DocumentExtractedFieldService } from '../service/document-extracted-field.service';
import { IDocumentExtractedField } from '../document-extracted-field.model';
import { DocumentExtractedFieldFormGroup, DocumentExtractedFieldFormService } from './document-extracted-field-form.service';

@Component({
  selector: 'jhi-document-extracted-field-update',
  templateUrl: './document-extracted-field-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentExtractedFieldUpdateComponent implements OnInit {
  isSaving = false;
  documentExtractedField: IDocumentExtractedField | null = null;
  extractionMethodValues = Object.keys(ExtractionMethod);

  documentsSharedCollection: IDocument[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected documentExtractedFieldService = inject(DocumentExtractedFieldService);
  protected documentExtractedFieldFormService = inject(DocumentExtractedFieldFormService);
  protected documentService = inject(DocumentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentExtractedFieldFormGroup = this.documentExtractedFieldFormService.createDocumentExtractedFieldFormGroup();

  compareDocument = (o1: IDocument | null, o2: IDocument | null): boolean => this.documentService.compareDocument(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentExtractedField }) => {
      this.documentExtractedField = documentExtractedField;
      if (documentExtractedField) {
        this.updateForm(documentExtractedField);
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
    const documentExtractedField = this.documentExtractedFieldFormService.getDocumentExtractedField(this.editForm);
    if (documentExtractedField.id !== null) {
      this.subscribeToSaveResponse(this.documentExtractedFieldService.update(documentExtractedField));
    } else {
      this.subscribeToSaveResponse(this.documentExtractedFieldService.create(documentExtractedField));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentExtractedField>>): void {
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

  protected updateForm(documentExtractedField: IDocumentExtractedField): void {
    this.documentExtractedField = documentExtractedField;
    this.documentExtractedFieldFormService.resetForm(this.editForm, documentExtractedField);

    this.documentsSharedCollection = this.documentService.addDocumentToCollectionIfMissing<IDocument>(
      this.documentsSharedCollection,
      documentExtractedField.document,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentService
      .query()
      .pipe(map((res: HttpResponse<IDocument[]>) => res.body ?? []))
      .pipe(
        map((documents: IDocument[]) =>
          this.documentService.addDocumentToCollectionIfMissing<IDocument>(documents, this.documentExtractedField?.document),
        ),
      )
      .subscribe((documents: IDocument[]) => (this.documentsSharedCollection = documents));
  }
}
