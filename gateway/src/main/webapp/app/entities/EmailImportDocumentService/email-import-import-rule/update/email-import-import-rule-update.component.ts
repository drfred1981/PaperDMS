import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EmailImportImportRuleService } from '../service/email-import-import-rule.service';
import { IEmailImportImportRule } from '../email-import-import-rule.model';
import { EmailImportImportRuleFormGroup, EmailImportImportRuleFormService } from './email-import-import-rule-form.service';

@Component({
  selector: 'jhi-email-import-import-rule-update',
  templateUrl: './email-import-import-rule-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmailImportImportRuleUpdateComponent implements OnInit {
  isSaving = false;
  emailImportImportRule: IEmailImportImportRule | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected emailImportImportRuleService = inject(EmailImportImportRuleService);
  protected emailImportImportRuleFormService = inject(EmailImportImportRuleFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmailImportImportRuleFormGroup = this.emailImportImportRuleFormService.createEmailImportImportRuleFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emailImportImportRule }) => {
      this.emailImportImportRule = emailImportImportRule;
      if (emailImportImportRule) {
        this.updateForm(emailImportImportRule);
      }
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
    const emailImportImportRule = this.emailImportImportRuleFormService.getEmailImportImportRule(this.editForm);
    if (emailImportImportRule.id !== null) {
      this.subscribeToSaveResponse(this.emailImportImportRuleService.update(emailImportImportRule));
    } else {
      this.subscribeToSaveResponse(this.emailImportImportRuleService.create(emailImportImportRule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmailImportImportRule>>): void {
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

  protected updateForm(emailImportImportRule: IEmailImportImportRule): void {
    this.emailImportImportRule = emailImportImportRule;
    this.emailImportImportRuleFormService.resetForm(this.editForm, emailImportImportRule);
  }
}
