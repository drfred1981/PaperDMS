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
import { IEmailImportImportRule } from 'app/entities/EmailImportDocumentService/email-import-import-rule/email-import-import-rule.model';
import { EmailImportImportRuleService } from 'app/entities/EmailImportDocumentService/email-import-import-rule/service/email-import-import-rule.service';
import { ImportStatus } from 'app/entities/enumerations/import-status.model';
import { EmailImportDocumentService } from '../service/email-import-document.service';
import { IEmailImportDocument } from '../email-import-document.model';
import { EmailImportDocumentFormGroup, EmailImportDocumentFormService } from './email-import-document-form.service';

@Component({
  selector: 'jhi-email-import-document-update',
  templateUrl: './email-import-document-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmailImportDocumentUpdateComponent implements OnInit {
  isSaving = false;
  emailImportDocument: IEmailImportDocument | null = null;
  importStatusValues = Object.keys(ImportStatus);

  emailImportImportRulesSharedCollection: IEmailImportImportRule[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected emailImportDocumentService = inject(EmailImportDocumentService);
  protected emailImportDocumentFormService = inject(EmailImportDocumentFormService);
  protected emailImportImportRuleService = inject(EmailImportImportRuleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmailImportDocumentFormGroup = this.emailImportDocumentFormService.createEmailImportDocumentFormGroup();

  compareEmailImportImportRule = (o1: IEmailImportImportRule | null, o2: IEmailImportImportRule | null): boolean =>
    this.emailImportImportRuleService.compareEmailImportImportRule(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emailImportDocument }) => {
      this.emailImportDocument = emailImportDocument;
      if (emailImportDocument) {
        this.updateForm(emailImportDocument);
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
    const emailImportDocument = this.emailImportDocumentFormService.getEmailImportDocument(this.editForm);
    if (emailImportDocument.id !== null) {
      this.subscribeToSaveResponse(this.emailImportDocumentService.update(emailImportDocument));
    } else {
      this.subscribeToSaveResponse(this.emailImportDocumentService.create(emailImportDocument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmailImportDocument>>): void {
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

  protected updateForm(emailImportDocument: IEmailImportDocument): void {
    this.emailImportDocument = emailImportDocument;
    this.emailImportDocumentFormService.resetForm(this.editForm, emailImportDocument);

    this.emailImportImportRulesSharedCollection =
      this.emailImportImportRuleService.addEmailImportImportRuleToCollectionIfMissing<IEmailImportImportRule>(
        this.emailImportImportRulesSharedCollection,
        emailImportDocument.appliedRule,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.emailImportImportRuleService
      .query()
      .pipe(map((res: HttpResponse<IEmailImportImportRule[]>) => res.body ?? []))
      .pipe(
        map((emailImportImportRules: IEmailImportImportRule[]) =>
          this.emailImportImportRuleService.addEmailImportImportRuleToCollectionIfMissing<IEmailImportImportRule>(
            emailImportImportRules,
            this.emailImportDocument?.appliedRule,
          ),
        ),
      )
      .subscribe(
        (emailImportImportRules: IEmailImportImportRule[]) => (this.emailImportImportRulesSharedCollection = emailImportImportRules),
      );
  }
}
