import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDocumentType } from 'app/entities/documentService/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/documentService/document-type/service/document-type.service';
import { IDocumentTemplate } from '../document-template.model';
import { DocumentTemplateService } from '../service/document-template.service';
import { DocumentTemplateFormGroup, DocumentTemplateFormService } from './document-template-form.service';

@Component({
  selector: 'jhi-document-template-update',
  templateUrl: './document-template-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentTemplateUpdateComponent implements OnInit {
  isSaving = false;
  documentTemplate: IDocumentTemplate | null = null;

  documentTypesSharedCollection: IDocumentType[] = [];

  protected documentTemplateService = inject(DocumentTemplateService);
  protected documentTemplateFormService = inject(DocumentTemplateFormService);
  protected documentTypeService = inject(DocumentTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentTemplateFormGroup = this.documentTemplateFormService.createDocumentTemplateFormGroup();

  compareDocumentType = (o1: IDocumentType | null, o2: IDocumentType | null): boolean =>
    this.documentTypeService.compareDocumentType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentTemplate }) => {
      this.documentTemplate = documentTemplate;
      if (documentTemplate) {
        this.updateForm(documentTemplate);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentTemplate = this.documentTemplateFormService.getDocumentTemplate(this.editForm);
    if (documentTemplate.id !== null) {
      this.subscribeToSaveResponse(this.documentTemplateService.update(documentTemplate));
    } else {
      this.subscribeToSaveResponse(this.documentTemplateService.create(documentTemplate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentTemplate>>): void {
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

  protected updateForm(documentTemplate: IDocumentTemplate): void {
    this.documentTemplate = documentTemplate;
    this.documentTemplateFormService.resetForm(this.editForm, documentTemplate);

    this.documentTypesSharedCollection = this.documentTypeService.addDocumentTypeToCollectionIfMissing<IDocumentType>(
      this.documentTypesSharedCollection,
      documentTemplate.documentType,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentTypeService
      .query()
      .pipe(map((res: HttpResponse<IDocumentType[]>) => res.body ?? []))
      .pipe(
        map((documentTypes: IDocumentType[]) =>
          this.documentTypeService.addDocumentTypeToCollectionIfMissing<IDocumentType>(documentTypes, this.documentTemplate?.documentType),
        ),
      )
      .subscribe((documentTypes: IDocumentType[]) => (this.documentTypesSharedCollection = documentTypes));
  }
}
