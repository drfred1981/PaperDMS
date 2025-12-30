import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDocumentType } from 'app/entities/documentService/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/documentService/document-type/service/document-type.service';
import { IMetaFolder } from 'app/entities/documentService/meta-folder/meta-folder.model';
import { MetaFolderService } from 'app/entities/documentService/meta-folder/service/meta-folder.service';
import { DocumentService } from '../service/document.service';
import { IDocument } from '../document.model';
import { DocumentFormGroup, DocumentFormService } from './document-form.service';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentUpdateComponent implements OnInit {
  isSaving = false;
  document: IDocument | null = null;

  documentTypesSharedCollection: IDocumentType[] = [];
  metaFoldersSharedCollection: IMetaFolder[] = [];

  protected documentService = inject(DocumentService);
  protected documentFormService = inject(DocumentFormService);
  protected documentTypeService = inject(DocumentTypeService);
  protected metaFolderService = inject(MetaFolderService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentFormGroup = this.documentFormService.createDocumentFormGroup();

  compareDocumentType = (o1: IDocumentType | null, o2: IDocumentType | null): boolean =>
    this.documentTypeService.compareDocumentType(o1, o2);

  compareMetaFolder = (o1: IMetaFolder | null, o2: IMetaFolder | null): boolean => this.metaFolderService.compareMetaFolder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ document }) => {
      this.document = document;
      if (document) {
        this.updateForm(document);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const document = this.documentFormService.getDocument(this.editForm);
    if (document.id !== null) {
      this.subscribeToSaveResponse(this.documentService.update(document));
    } else {
      this.subscribeToSaveResponse(this.documentService.create(document));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocument>>): void {
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

  protected updateForm(document: IDocument): void {
    this.document = document;
    this.documentFormService.resetForm(this.editForm, document);

    this.documentTypesSharedCollection = this.documentTypeService.addDocumentTypeToCollectionIfMissing<IDocumentType>(
      this.documentTypesSharedCollection,
      document.documentType,
    );
    this.metaFoldersSharedCollection = this.metaFolderService.addMetaFolderToCollectionIfMissing<IMetaFolder>(
      this.metaFoldersSharedCollection,
      document.folder,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentTypeService
      .query()
      .pipe(map((res: HttpResponse<IDocumentType[]>) => res.body ?? []))
      .pipe(
        map((documentTypes: IDocumentType[]) =>
          this.documentTypeService.addDocumentTypeToCollectionIfMissing<IDocumentType>(documentTypes, this.document?.documentType),
        ),
      )
      .subscribe((documentTypes: IDocumentType[]) => (this.documentTypesSharedCollection = documentTypes));

    this.metaFolderService
      .query()
      .pipe(map((res: HttpResponse<IMetaFolder[]>) => res.body ?? []))
      .pipe(
        map((metaFolders: IMetaFolder[]) =>
          this.metaFolderService.addMetaFolderToCollectionIfMissing<IMetaFolder>(metaFolders, this.document?.folder),
        ),
      )
      .subscribe((metaFolders: IMetaFolder[]) => (this.metaFoldersSharedCollection = metaFolders));
  }
}
