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
import { AuditAction } from 'app/entities/enumerations/audit-action.model';
import { DocumentAuditService } from '../service/document-audit.service';
import { IDocumentAudit } from '../document-audit.model';
import { DocumentAuditFormGroup, DocumentAuditFormService } from './document-audit-form.service';

@Component({
  selector: 'jhi-document-audit-update',
  templateUrl: './document-audit-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentAuditUpdateComponent implements OnInit {
  isSaving = false;
  documentAudit: IDocumentAudit | null = null;
  auditActionValues = Object.keys(AuditAction);

  documentsSharedCollection: IDocument[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected documentAuditService = inject(DocumentAuditService);
  protected documentAuditFormService = inject(DocumentAuditFormService);
  protected documentService = inject(DocumentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentAuditFormGroup = this.documentAuditFormService.createDocumentAuditFormGroup();

  compareDocument = (o1: IDocument | null, o2: IDocument | null): boolean => this.documentService.compareDocument(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentAudit }) => {
      this.documentAudit = documentAudit;
      if (documentAudit) {
        this.updateForm(documentAudit);
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
    const documentAudit = this.documentAuditFormService.getDocumentAudit(this.editForm);
    if (documentAudit.id !== null) {
      this.subscribeToSaveResponse(this.documentAuditService.update(documentAudit));
    } else {
      this.subscribeToSaveResponse(this.documentAuditService.create(documentAudit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentAudit>>): void {
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

  protected updateForm(documentAudit: IDocumentAudit): void {
    this.documentAudit = documentAudit;
    this.documentAuditFormService.resetForm(this.editForm, documentAudit);

    this.documentsSharedCollection = this.documentService.addDocumentToCollectionIfMissing<IDocument>(
      this.documentsSharedCollection,
      documentAudit.document,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentService
      .query()
      .pipe(map((res: HttpResponse<IDocument[]>) => res.body ?? []))
      .pipe(
        map((documents: IDocument[]) =>
          this.documentService.addDocumentToCollectionIfMissing<IDocument>(documents, this.documentAudit?.document),
        ),
      )
      .subscribe((documents: IDocument[]) => (this.documentsSharedCollection = documents));
  }
}
