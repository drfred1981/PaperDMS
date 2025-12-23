import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocumentType } from 'app/entities/documentService/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/documentService/document-type/service/document-type.service';
import { MetadataType } from 'app/entities/enumerations/metadata-type.model';
import SharedModule from 'app/shared/shared.module';
import { IDocumentTypeField } from '../document-type-field.model';
import { DocumentTypeFieldService } from '../service/document-type-field.service';

import { DocumentTypeFieldFormGroup, DocumentTypeFieldFormService } from './document-type-field-form.service';

@Component({
  selector: 'jhi-document-type-field-update',
  templateUrl: './document-type-field-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class DocumentTypeFieldUpdate implements OnInit {
  isSaving = false;
  documentTypeField: IDocumentTypeField | null = null;
  metadataTypeValues = Object.keys(MetadataType);

  documentTypesSharedCollection = signal<IDocumentType[]>([]);

  protected documentTypeFieldService = inject(DocumentTypeFieldService);
  protected documentTypeFieldFormService = inject(DocumentTypeFieldFormService);
  protected documentTypeService = inject(DocumentTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentTypeFieldFormGroup = this.documentTypeFieldFormService.createDocumentTypeFieldFormGroup();

  compareDocumentType = (o1: IDocumentType | null, o2: IDocumentType | null): boolean =>
    this.documentTypeService.compareDocumentType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentTypeField }) => {
      this.documentTypeField = documentTypeField;
      if (documentTypeField) {
        this.updateForm(documentTypeField);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentTypeField = this.documentTypeFieldFormService.getDocumentTypeField(this.editForm);
    if (documentTypeField.id === null) {
      this.subscribeToSaveResponse(this.documentTypeFieldService.create(documentTypeField));
    } else {
      this.subscribeToSaveResponse(this.documentTypeFieldService.update(documentTypeField));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentTypeField>>): void {
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

  protected updateForm(documentTypeField: IDocumentTypeField): void {
    this.documentTypeField = documentTypeField;
    this.documentTypeFieldFormService.resetForm(this.editForm, documentTypeField);

    this.documentTypesSharedCollection.set(
      this.documentTypeService.addDocumentTypeToCollectionIfMissing<IDocumentType>(
        this.documentTypesSharedCollection(),
        documentTypeField.documentType,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentTypeService
      .query()
      .pipe(map((res: HttpResponse<IDocumentType[]>) => res.body ?? []))
      .pipe(
        map((documentTypes: IDocumentType[]) =>
          this.documentTypeService.addDocumentTypeToCollectionIfMissing<IDocumentType>(documentTypes, this.documentTypeField?.documentType),
        ),
      )
      .subscribe((documentTypes: IDocumentType[]) => this.documentTypesSharedCollection.set(documentTypes));
  }
}
