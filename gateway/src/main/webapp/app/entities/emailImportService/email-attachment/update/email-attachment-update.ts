import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { IEmailImport } from 'app/entities/emailImportService/email-import/email-import.model';
import { EmailImportService } from 'app/entities/emailImportService/email-import/service/email-import.service';
import { AttachmentStatus } from 'app/entities/enumerations/attachment-status.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IEmailAttachment } from '../email-attachment.model';
import { EmailAttachmentService } from '../service/email-attachment.service';

import { EmailAttachmentFormGroup, EmailAttachmentFormService } from './email-attachment-form.service';

@Component({
  selector: 'jhi-email-attachment-update',
  templateUrl: './email-attachment-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class EmailAttachmentUpdate implements OnInit {
  isSaving = false;
  emailAttachment: IEmailAttachment | null = null;
  attachmentStatusValues = Object.keys(AttachmentStatus);

  emailImportsSharedCollection = signal<IEmailImport[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected emailAttachmentService = inject(EmailAttachmentService);
  protected emailAttachmentFormService = inject(EmailAttachmentFormService);
  protected emailImportService = inject(EmailImportService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmailAttachmentFormGroup = this.emailAttachmentFormService.createEmailAttachmentFormGroup();

  compareEmailImport = (o1: IEmailImport | null, o2: IEmailImport | null): boolean => this.emailImportService.compareEmailImport(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emailAttachment }) => {
      this.emailAttachment = emailAttachment;
      if (emailAttachment) {
        this.updateForm(emailAttachment);
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
    const emailAttachment = this.emailAttachmentFormService.getEmailAttachment(this.editForm);
    if (emailAttachment.id === null) {
      this.subscribeToSaveResponse(this.emailAttachmentService.create(emailAttachment));
    } else {
      this.subscribeToSaveResponse(this.emailAttachmentService.update(emailAttachment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmailAttachment>>): void {
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

  protected updateForm(emailAttachment: IEmailAttachment): void {
    this.emailAttachment = emailAttachment;
    this.emailAttachmentFormService.resetForm(this.editForm, emailAttachment);

    this.emailImportsSharedCollection.set(
      this.emailImportService.addEmailImportToCollectionIfMissing<IEmailImport>(
        this.emailImportsSharedCollection(),
        emailAttachment.emailImport,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.emailImportService
      .query()
      .pipe(map((res: HttpResponse<IEmailImport[]>) => res.body ?? []))
      .pipe(
        map((emailImports: IEmailImport[]) =>
          this.emailImportService.addEmailImportToCollectionIfMissing<IEmailImport>(emailImports, this.emailAttachment?.emailImport),
        ),
      )
      .subscribe((emailImports: IEmailImport[]) => this.emailImportsSharedCollection.set(emailImports));
  }
}
