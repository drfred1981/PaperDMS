import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IDocumentComment } from '../document-comment.model';
import { DocumentCommentService } from '../service/document-comment.service';

import { DocumentCommentFormGroup, DocumentCommentFormService } from './document-comment-form.service';

@Component({
  selector: 'jhi-document-comment-update',
  templateUrl: './document-comment-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class DocumentCommentUpdate implements OnInit {
  isSaving = false;
  documentComment: IDocumentComment | null = null;

  documentCommentsSharedCollection = signal<IDocumentComment[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected documentCommentService = inject(DocumentCommentService);
  protected documentCommentFormService = inject(DocumentCommentFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentCommentFormGroup = this.documentCommentFormService.createDocumentCommentFormGroup();

  compareDocumentComment = (o1: IDocumentComment | null, o2: IDocumentComment | null): boolean =>
    this.documentCommentService.compareDocumentComment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentComment }) => {
      this.documentComment = documentComment;
      if (documentComment) {
        this.updateForm(documentComment);
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
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentComment = this.documentCommentFormService.getDocumentComment(this.editForm);
    if (documentComment.id === null) {
      this.subscribeToSaveResponse(this.documentCommentService.create(documentComment));
    } else {
      this.subscribeToSaveResponse(this.documentCommentService.update(documentComment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentComment>>): void {
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

  protected updateForm(documentComment: IDocumentComment): void {
    this.documentComment = documentComment;
    this.documentCommentFormService.resetForm(this.editForm, documentComment);

    this.documentCommentsSharedCollection.set(
      this.documentCommentService.addDocumentCommentToCollectionIfMissing<IDocumentComment>(
        this.documentCommentsSharedCollection(),
        documentComment.parentComment,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentCommentService
      .query()
      .pipe(map((res: HttpResponse<IDocumentComment[]>) => res.body ?? []))
      .pipe(
        map((documentComments: IDocumentComment[]) =>
          this.documentCommentService.addDocumentCommentToCollectionIfMissing<IDocumentComment>(
            documentComments,
            this.documentComment?.parentComment,
          ),
        ),
      )
      .subscribe((documentComments: IDocumentComment[]) => this.documentCommentsSharedCollection.set(documentComments));
  }
}
