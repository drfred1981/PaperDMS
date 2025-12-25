import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { IDocumentVersion } from '../document-version.model';
import { DocumentVersionService } from '../service/document-version.service';
import { DocumentVersionFormGroup, DocumentVersionFormService } from './document-version-form.service';

@Component({
  selector: 'jhi-document-version-update',
  templateUrl: './document-version-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentVersionUpdateComponent implements OnInit {
  isSaving = false;
  documentVersion: IDocumentVersion | null = null;

  documentsSharedCollection: IDocument[] = [];

  protected documentVersionService = inject(DocumentVersionService);
  protected documentVersionFormService = inject(DocumentVersionFormService);
  protected documentService = inject(DocumentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentVersionFormGroup = this.documentVersionFormService.createDocumentVersionFormGroup();

  compareDocument = (o1: IDocument | null, o2: IDocument | null): boolean => this.documentService.compareDocument(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentVersion }) => {
      this.documentVersion = documentVersion;
      if (documentVersion) {
        this.updateForm(documentVersion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentVersion = this.documentVersionFormService.getDocumentVersion(this.editForm);
    if (documentVersion.id !== null) {
      this.subscribeToSaveResponse(this.documentVersionService.update(documentVersion));
    } else {
      this.subscribeToSaveResponse(this.documentVersionService.create(documentVersion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentVersion>>): void {
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

  protected updateForm(documentVersion: IDocumentVersion): void {
    this.documentVersion = documentVersion;
    this.documentVersionFormService.resetForm(this.editForm, documentVersion);

    this.documentsSharedCollection = this.documentService.addDocumentToCollectionIfMissing<IDocument>(
      this.documentsSharedCollection,
      documentVersion.document,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentService
      .query()
      .pipe(map((res: HttpResponse<IDocument[]>) => res.body ?? []))
      .pipe(
        map((documents: IDocument[]) =>
          this.documentService.addDocumentToCollectionIfMissing<IDocument>(documents, this.documentVersion?.document),
        ),
      )
      .subscribe((documents: IDocument[]) => (this.documentsSharedCollection = documents));
  }
}
