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
import { IEmailImportDocument } from 'app/entities/EmailImportDocumentService/email-import-document/email-import-document.model';
import { EmailImportDocumentService } from 'app/entities/EmailImportDocumentService/email-import-document/service/email-import-document.service';
import { AttachmentStatus } from 'app/entities/enumerations/attachment-status.model';
import { EmailImportEmailAttachmentService } from '../service/email-import-email-attachment.service';
import { IEmailImportEmailAttachment } from '../email-import-email-attachment.model';
import { EmailImportEmailAttachmentFormGroup, EmailImportEmailAttachmentFormService } from './email-import-email-attachment-form.service';

@Component({
  selector: 'jhi-email-import-email-attachment-update',
  templateUrl: './email-import-email-attachment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmailImportEmailAttachmentUpdateComponent implements OnInit {
  isSaving = false;
  emailImportEmailAttachment: IEmailImportEmailAttachment | null = null;
  attachmentStatusValues = Object.keys(AttachmentStatus);

  emailImportDocumentsSharedCollection: IEmailImportDocument[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected emailImportEmailAttachmentService = inject(EmailImportEmailAttachmentService);
  protected emailImportEmailAttachmentFormService = inject(EmailImportEmailAttachmentFormService);
  protected emailImportDocumentService = inject(EmailImportDocumentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmailImportEmailAttachmentFormGroup = this.emailImportEmailAttachmentFormService.createEmailImportEmailAttachmentFormGroup();

  compareEmailImportDocument = (o1: IEmailImportDocument | null, o2: IEmailImportDocument | null): boolean =>
    this.emailImportDocumentService.compareEmailImportDocument(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emailImportEmailAttachment }) => {
      this.emailImportEmailAttachment = emailImportEmailAttachment;
      if (emailImportEmailAttachment) {
        this.updateForm(emailImportEmailAttachment);
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
    const emailImportEmailAttachment = this.emailImportEmailAttachmentFormService.getEmailImportEmailAttachment(this.editForm);
    if (emailImportEmailAttachment.id !== null) {
      this.subscribeToSaveResponse(this.emailImportEmailAttachmentService.update(emailImportEmailAttachment));
    } else {
      this.subscribeToSaveResponse(this.emailImportEmailAttachmentService.create(emailImportEmailAttachment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmailImportEmailAttachment>>): void {
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

  protected updateForm(emailImportEmailAttachment: IEmailImportEmailAttachment): void {
    this.emailImportEmailAttachment = emailImportEmailAttachment;
    this.emailImportEmailAttachmentFormService.resetForm(this.editForm, emailImportEmailAttachment);

    this.emailImportDocumentsSharedCollection =
      this.emailImportDocumentService.addEmailImportDocumentToCollectionIfMissing<IEmailImportDocument>(
        this.emailImportDocumentsSharedCollection,
        emailImportEmailAttachment.emailImportDocument,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.emailImportDocumentService
      .query()
      .pipe(map((res: HttpResponse<IEmailImportDocument[]>) => res.body ?? []))
      .pipe(
        map((emailImportDocuments: IEmailImportDocument[]) =>
          this.emailImportDocumentService.addEmailImportDocumentToCollectionIfMissing<IEmailImportDocument>(
            emailImportDocuments,
            this.emailImportEmailAttachment?.emailImportDocument,
          ),
        ),
      )
      .subscribe((emailImportDocuments: IEmailImportDocument[]) => (this.emailImportDocumentsSharedCollection = emailImportDocuments));
  }
}
