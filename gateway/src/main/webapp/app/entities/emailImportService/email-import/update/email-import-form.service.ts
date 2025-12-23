import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmailImport, NewEmailImport } from '../email-import.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmailImport for edit and NewEmailImportFormGroupInput for create.
 */
type EmailImportFormGroupInput = IEmailImport | PartialWithRequiredKeyOf<NewEmailImport>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmailImport | NewEmailImport> = Omit<T, 'receivedDate' | 'processedDate'> & {
  receivedDate?: string | null;
  processedDate?: string | null;
};

type EmailImportFormRawValue = FormValueOf<IEmailImport>;

type NewEmailImportFormRawValue = FormValueOf<NewEmailImport>;

type EmailImportFormDefaults = Pick<NewEmailImport, 'id' | 'receivedDate' | 'processedDate'>;

type EmailImportFormGroupContent = {
  id: FormControl<EmailImportFormRawValue['id'] | NewEmailImport['id']>;
  fromEmail: FormControl<EmailImportFormRawValue['fromEmail']>;
  toEmail: FormControl<EmailImportFormRawValue['toEmail']>;
  subject: FormControl<EmailImportFormRawValue['subject']>;
  body: FormControl<EmailImportFormRawValue['body']>;
  bodyHtml: FormControl<EmailImportFormRawValue['bodyHtml']>;
  receivedDate: FormControl<EmailImportFormRawValue['receivedDate']>;
  processedDate: FormControl<EmailImportFormRawValue['processedDate']>;
  status: FormControl<EmailImportFormRawValue['status']>;
  folderId: FormControl<EmailImportFormRawValue['folderId']>;
  documentTypeId: FormControl<EmailImportFormRawValue['documentTypeId']>;
  attachmentCount: FormControl<EmailImportFormRawValue['attachmentCount']>;
  documentsCreated: FormControl<EmailImportFormRawValue['documentsCreated']>;
  appliedRuleId: FormControl<EmailImportFormRawValue['appliedRuleId']>;
  errorMessage: FormControl<EmailImportFormRawValue['errorMessage']>;
  metadata: FormControl<EmailImportFormRawValue['metadata']>;
  appliedRule: FormControl<EmailImportFormRawValue['appliedRule']>;
};

export type EmailImportFormGroup = FormGroup<EmailImportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmailImportFormService {
  createEmailImportFormGroup(emailImport?: EmailImportFormGroupInput): EmailImportFormGroup {
    const emailImportRawValue = this.convertEmailImportToEmailImportRawValue({
      ...this.getFormDefaults(),
      ...(emailImport ?? { id: null }),
    });
    return new FormGroup<EmailImportFormGroupContent>({
      id: new FormControl(
        { value: emailImportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromEmail: new FormControl(emailImportRawValue.fromEmail, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      toEmail: new FormControl(emailImportRawValue.toEmail, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      subject: new FormControl(emailImportRawValue.subject, {
        validators: [Validators.maxLength(1000)],
      }),
      body: new FormControl(emailImportRawValue.body),
      bodyHtml: new FormControl(emailImportRawValue.bodyHtml),
      receivedDate: new FormControl(emailImportRawValue.receivedDate, {
        validators: [Validators.required],
      }),
      processedDate: new FormControl(emailImportRawValue.processedDate),
      status: new FormControl(emailImportRawValue.status, {
        validators: [Validators.required],
      }),
      folderId: new FormControl(emailImportRawValue.folderId),
      documentTypeId: new FormControl(emailImportRawValue.documentTypeId),
      attachmentCount: new FormControl(emailImportRawValue.attachmentCount),
      documentsCreated: new FormControl(emailImportRawValue.documentsCreated),
      appliedRuleId: new FormControl(emailImportRawValue.appliedRuleId),
      errorMessage: new FormControl(emailImportRawValue.errorMessage),
      metadata: new FormControl(emailImportRawValue.metadata),
      appliedRule: new FormControl(emailImportRawValue.appliedRule),
    });
  }

  getEmailImport(form: EmailImportFormGroup): IEmailImport | NewEmailImport {
    return this.convertEmailImportRawValueToEmailImport(form.getRawValue() as EmailImportFormRawValue | NewEmailImportFormRawValue);
  }

  resetForm(form: EmailImportFormGroup, emailImport: EmailImportFormGroupInput): void {
    const emailImportRawValue = this.convertEmailImportToEmailImportRawValue({ ...this.getFormDefaults(), ...emailImport });
    form.reset({
      ...emailImportRawValue,
      id: { value: emailImportRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): EmailImportFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      receivedDate: currentTime,
      processedDate: currentTime,
    };
  }

  private convertEmailImportRawValueToEmailImport(
    rawEmailImport: EmailImportFormRawValue | NewEmailImportFormRawValue,
  ): IEmailImport | NewEmailImport {
    return {
      ...rawEmailImport,
      receivedDate: dayjs(rawEmailImport.receivedDate, DATE_TIME_FORMAT),
      processedDate: dayjs(rawEmailImport.processedDate, DATE_TIME_FORMAT),
    };
  }

  private convertEmailImportToEmailImportRawValue(
    emailImport: IEmailImport | (Partial<NewEmailImport> & EmailImportFormDefaults),
  ): EmailImportFormRawValue | PartialWithRequiredKeyOf<NewEmailImportFormRawValue> {
    return {
      ...emailImport,
      receivedDate: emailImport.receivedDate ? emailImport.receivedDate.format(DATE_TIME_FORMAT) : undefined,
      processedDate: emailImport.processedDate ? emailImport.processedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
