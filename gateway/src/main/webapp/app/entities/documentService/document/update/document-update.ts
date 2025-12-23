import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocumentType } from 'app/entities/documentService/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/documentService/document-type/service/document-type.service';
import { IFolder } from 'app/entities/documentService/folder/folder.model';
import { FolderService } from 'app/entities/documentService/folder/service/folder.service';
import { DocumentStatus } from 'app/entities/enumerations/document-status.model';
import SharedModule from 'app/shared/shared.module';
import { IDocument } from '../document.model';
import { DocumentService } from '../service/document.service';

import { DocumentFormGroup, DocumentFormService } from './document-form.service';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class DocumentUpdate implements OnInit {
  isSaving = false;
  document: IDocument | null = null;
  documentStatusValues = Object.keys(DocumentStatus);

  foldersSharedCollection = signal<IFolder[]>([]);
  documentTypesSharedCollection = signal<IDocumentType[]>([]);

  protected documentService = inject(DocumentService);
  protected documentFormService = inject(DocumentFormService);
  protected folderService = inject(FolderService);
  protected documentTypeService = inject(DocumentTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentFormGroup = this.documentFormService.createDocumentFormGroup();

  compareFolder = (o1: IFolder | null, o2: IFolder | null): boolean => this.folderService.compareFolder(o1, o2);

  compareDocumentType = (o1: IDocumentType | null, o2: IDocumentType | null): boolean =>
    this.documentTypeService.compareDocumentType(o1, o2);

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
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const document = this.documentFormService.getDocument(this.editForm);
    if (document.id === null) {
      this.subscribeToSaveResponse(this.documentService.create(document));
    } else {
      this.subscribeToSaveResponse(this.documentService.update(document));
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

    this.foldersSharedCollection.set(
      this.folderService.addFolderToCollectionIfMissing<IFolder>(this.foldersSharedCollection(), document.folder),
    );
    this.documentTypesSharedCollection.set(
      this.documentTypeService.addDocumentTypeToCollectionIfMissing<IDocumentType>(
        this.documentTypesSharedCollection(),
        document.documentType,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.folderService
      .query()
      .pipe(map((res: HttpResponse<IFolder[]>) => res.body ?? []))
      .pipe(map((folders: IFolder[]) => this.folderService.addFolderToCollectionIfMissing<IFolder>(folders, this.document?.folder)))
      .subscribe((folders: IFolder[]) => this.foldersSharedCollection.set(folders));

    this.documentTypeService
      .query()
      .pipe(map((res: HttpResponse<IDocumentType[]>) => res.body ?? []))
      .pipe(
        map((documentTypes: IDocumentType[]) =>
          this.documentTypeService.addDocumentTypeToCollectionIfMissing<IDocumentType>(documentTypes, this.document?.documentType),
        ),
      )
      .subscribe((documentTypes: IDocumentType[]) => this.documentTypesSharedCollection.set(documentTypes));
  }
}
