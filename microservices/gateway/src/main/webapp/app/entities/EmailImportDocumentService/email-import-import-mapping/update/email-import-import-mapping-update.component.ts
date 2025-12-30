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
import { EmailField } from 'app/entities/enumerations/email-field.model';
import { MappingTransformation } from 'app/entities/enumerations/mapping-transformation.model';
import { EmailImportImportMappingService } from '../service/email-import-import-mapping.service';
import { IEmailImportImportMapping } from '../email-import-import-mapping.model';
import { EmailImportImportMappingFormGroup, EmailImportImportMappingFormService } from './email-import-import-mapping-form.service';

@Component({
  selector: 'jhi-email-import-import-mapping-update',
  templateUrl: './email-import-import-mapping-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmailImportImportMappingUpdateComponent implements OnInit {
  isSaving = false;
  emailImportImportMapping: IEmailImportImportMapping | null = null;
  emailFieldValues = Object.keys(EmailField);
  mappingTransformationValues = Object.keys(MappingTransformation);

  emailImportImportRulesSharedCollection: IEmailImportImportRule[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected emailImportImportMappingService = inject(EmailImportImportMappingService);
  protected emailImportImportMappingFormService = inject(EmailImportImportMappingFormService);
  protected emailImportImportRuleService = inject(EmailImportImportRuleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmailImportImportMappingFormGroup = this.emailImportImportMappingFormService.createEmailImportImportMappingFormGroup();

  compareEmailImportImportRule = (o1: IEmailImportImportRule | null, o2: IEmailImportImportRule | null): boolean =>
    this.emailImportImportRuleService.compareEmailImportImportRule(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emailImportImportMapping }) => {
      this.emailImportImportMapping = emailImportImportMapping;
      if (emailImportImportMapping) {
        this.updateForm(emailImportImportMapping);
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
    const emailImportImportMapping = this.emailImportImportMappingFormService.getEmailImportImportMapping(this.editForm);
    if (emailImportImportMapping.id !== null) {
      this.subscribeToSaveResponse(this.emailImportImportMappingService.update(emailImportImportMapping));
    } else {
      this.subscribeToSaveResponse(this.emailImportImportMappingService.create(emailImportImportMapping));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmailImportImportMapping>>): void {
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

  protected updateForm(emailImportImportMapping: IEmailImportImportMapping): void {
    this.emailImportImportMapping = emailImportImportMapping;
    this.emailImportImportMappingFormService.resetForm(this.editForm, emailImportImportMapping);

    this.emailImportImportRulesSharedCollection =
      this.emailImportImportRuleService.addEmailImportImportRuleToCollectionIfMissing<IEmailImportImportRule>(
        this.emailImportImportRulesSharedCollection,
        emailImportImportMapping.rule,
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
            this.emailImportImportMapping?.rule,
          ),
        ),
      )
      .subscribe(
        (emailImportImportRules: IEmailImportImportRule[]) => (this.emailImportImportRulesSharedCollection = emailImportImportRules),
      );
  }
}
