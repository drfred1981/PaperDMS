import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { DocumentService } from 'app/entities/documentService/document/service/document.service';
import { IMetaTag } from 'app/entities/documentService/meta-tag/meta-tag.model';
import { MetaTagService } from 'app/entities/documentService/meta-tag/service/meta-tag.service';
import { MetaTagSource } from 'app/entities/enumerations/meta-tag-source.model';
import { DocumentTagService } from '../service/document-tag.service';
import { IDocumentTag } from '../document-tag.model';
import { DocumentTagFormGroup, DocumentTagFormService } from './document-tag-form.service';

@Component({
  selector: 'jhi-document-tag-update',
  templateUrl: './document-tag-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentTagUpdateComponent implements OnInit {
  isSaving = false;
  documentTag: IDocumentTag | null = null;
  metaTagSourceValues = Object.keys(MetaTagSource);

  documentsSharedCollection: IDocument[] = [];
  metaTagsSharedCollection: IMetaTag[] = [];

  protected documentTagService = inject(DocumentTagService);
  protected documentTagFormService = inject(DocumentTagFormService);
  protected documentService = inject(DocumentService);
  protected metaTagService = inject(MetaTagService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentTagFormGroup = this.documentTagFormService.createDocumentTagFormGroup();

  compareDocument = (o1: IDocument | null, o2: IDocument | null): boolean => this.documentService.compareDocument(o1, o2);

  compareMetaTag = (o1: IMetaTag | null, o2: IMetaTag | null): boolean => this.metaTagService.compareMetaTag(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentTag }) => {
      this.documentTag = documentTag;
      if (documentTag) {
        this.updateForm(documentTag);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentTag = this.documentTagFormService.getDocumentTag(this.editForm);
    if (documentTag.id !== null) {
      this.subscribeToSaveResponse(this.documentTagService.update(documentTag));
    } else {
      this.subscribeToSaveResponse(this.documentTagService.create(documentTag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentTag>>): void {
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

  protected updateForm(documentTag: IDocumentTag): void {
    this.documentTag = documentTag;
    this.documentTagFormService.resetForm(this.editForm, documentTag);

    this.documentsSharedCollection = this.documentService.addDocumentToCollectionIfMissing<IDocument>(
      this.documentsSharedCollection,
      documentTag.document,
    );
    this.metaTagsSharedCollection = this.metaTagService.addMetaTagToCollectionIfMissing<IMetaTag>(
      this.metaTagsSharedCollection,
      documentTag.metaTag,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentService
      .query()
      .pipe(map((res: HttpResponse<IDocument[]>) => res.body ?? []))
      .pipe(
        map((documents: IDocument[]) =>
          this.documentService.addDocumentToCollectionIfMissing<IDocument>(documents, this.documentTag?.document),
        ),
      )
      .subscribe((documents: IDocument[]) => (this.documentsSharedCollection = documents));

    this.metaTagService
      .query()
      .pipe(map((res: HttpResponse<IMetaTag[]>) => res.body ?? []))
      .pipe(
        map((metaTags: IMetaTag[]) => this.metaTagService.addMetaTagToCollectionIfMissing<IMetaTag>(metaTags, this.documentTag?.metaTag)),
      )
      .subscribe((metaTags: IMetaTag[]) => (this.metaTagsSharedCollection = metaTags));
  }
}
