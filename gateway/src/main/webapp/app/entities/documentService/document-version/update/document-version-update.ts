import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import SharedModule from 'app/shared/shared.module';
import { IDocumentVersion } from '../document-version.model';
import { DocumentVersionService } from '../service/document-version.service';

import { DocumentVersionFormGroup, DocumentVersionFormService } from './document-version-form.service';

@Component({
  selector: 'jhi-document-version-update',
  templateUrl: './document-version-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class DocumentVersionUpdate implements OnInit {
  isSaving = false;
  documentVersion: IDocumentVersion | null = null;

  documentsSharedCollection = signal<IDocument[]>([]);

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
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentVersion = this.documentVersionFormService.getDocumentVersion(this.editForm);
    if (documentVersion.id === null) {
      this.subscribeToSaveResponse(this.documentVersionService.create(documentVersion));
    } else {
      this.subscribeToSaveResponse(this.documentVersionService.update(documentVersion));
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

    this.documentsSharedCollection.set(
      this.documentService.addDocumentToCollectionIfMissing<IDocument>(this.documentsSharedCollection(), documentVersion.document),
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
      .subscribe((documents: IDocument[]) => this.documentsSharedCollection.set(documents));
  }
}
