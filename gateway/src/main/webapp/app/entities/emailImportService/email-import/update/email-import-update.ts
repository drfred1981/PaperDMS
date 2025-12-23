import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { IImportRule } from 'app/entities/emailImportService/import-rule/import-rule.model';
import { ImportRuleService } from 'app/entities/emailImportService/import-rule/service/import-rule.service';
import { ImportStatus } from 'app/entities/enumerations/import-status.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IEmailImport } from '../email-import.model';
import { EmailImportService } from '../service/email-import.service';

import { EmailImportFormGroup, EmailImportFormService } from './email-import-form.service';

@Component({
  selector: 'jhi-email-import-update',
  templateUrl: './email-import-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class EmailImportUpdate implements OnInit {
  isSaving = false;
  emailImport: IEmailImport | null = null;
  importStatusValues = Object.keys(ImportStatus);

  importRulesSharedCollection = signal<IImportRule[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected emailImportService = inject(EmailImportService);
  protected emailImportFormService = inject(EmailImportFormService);
  protected importRuleService = inject(ImportRuleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmailImportFormGroup = this.emailImportFormService.createEmailImportFormGroup();

  compareImportRule = (o1: IImportRule | null, o2: IImportRule | null): boolean => this.importRuleService.compareImportRule(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emailImport }) => {
      this.emailImport = emailImport;
      if (emailImport) {
        this.updateForm(emailImport);
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
    const emailImport = this.emailImportFormService.getEmailImport(this.editForm);
    if (emailImport.id === null) {
      this.subscribeToSaveResponse(this.emailImportService.create(emailImport));
    } else {
      this.subscribeToSaveResponse(this.emailImportService.update(emailImport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmailImport>>): void {
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

  protected updateForm(emailImport: IEmailImport): void {
    this.emailImport = emailImport;
    this.emailImportFormService.resetForm(this.editForm, emailImport);

    this.importRulesSharedCollection.set(
      this.importRuleService.addImportRuleToCollectionIfMissing<IImportRule>(this.importRulesSharedCollection(), emailImport.appliedRule),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.importRuleService
      .query()
      .pipe(map((res: HttpResponse<IImportRule[]>) => res.body ?? []))
      .pipe(
        map((importRules: IImportRule[]) =>
          this.importRuleService.addImportRuleToCollectionIfMissing<IImportRule>(importRules, this.emailImport?.appliedRule),
        ),
      )
      .subscribe((importRules: IImportRule[]) => this.importRulesSharedCollection.set(importRules));
  }
}
