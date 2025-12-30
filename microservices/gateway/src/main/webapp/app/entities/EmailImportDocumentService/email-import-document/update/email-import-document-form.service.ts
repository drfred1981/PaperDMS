import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmailImportDocument, NewEmailImportDocument } from '../email-import-document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmailImportDocument for edit and NewEmailImportDocumentFormGroupInput for create.
 */
type EmailImportDocumentFormGroupInput = IEmailImportDocument | PartialWithRequiredKeyOf<NewEmailImportDocument>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmailImportDocument | NewEmailImportDocument> = Omit<T, 'receivedDate' | 'processedDate'> & {
  receivedDate?: string | null;
  processedDate?: string | null;
};

type EmailImportDocumentFormRawValue = FormValueOf<IEmailImportDocument>;

type NewEmailImportDocumentFormRawValue = FormValueOf<NewEmailImportDocument>;

type EmailImportDocumentFormDefaults = Pick<NewEmailImportDocument, 'id' | 'receivedDate' | 'processedDate'>;

type EmailImportDocumentFormGroupContent = {
  id: FormControl<EmailImportDocumentFormRawValue['id'] | NewEmailImportDocument['id']>;
  sha256: FormControl<EmailImportDocumentFormRawValue['sha256']>;
  fromEmail: FormControl<EmailImportDocumentFormRawValue['fromEmail']>;
  toEmail: FormControl<EmailImportDocumentFormRawValue['toEmail']>;
  subject: FormControl<EmailImportDocumentFormRawValue['subject']>;
  body: FormControl<EmailImportDocumentFormRawValue['body']>;
  bodyHtml: FormControl<EmailImportDocumentFormRawValue['bodyHtml']>;
  receivedDate: FormControl<EmailImportDocumentFormRawValue['receivedDate']>;
  processedDate: FormControl<EmailImportDocumentFormRawValue['processedDate']>;
  status: FormControl<EmailImportDocumentFormRawValue['status']>;
  attachmentCount: FormControl<EmailImportDocumentFormRawValue['attachmentCount']>;
  documentsCreated: FormControl<EmailImportDocumentFormRawValue['documentsCreated']>;
  errorMessage: FormControl<EmailImportDocumentFormRawValue['errorMessage']>;
  metadata: FormControl<EmailImportDocumentFormRawValue['metadata']>;
  documentSha256: FormControl<EmailImportDocumentFormRawValue['documentSha256']>;
  appliedRule: FormControl<EmailImportDocumentFormRawValue['appliedRule']>;
};

export type EmailImportDocumentFormGroup = FormGroup<EmailImportDocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmailImportDocumentFormService {
  createEmailImportDocumentFormGroup(emailImportDocument: EmailImportDocumentFormGroupInput = { id: null }): EmailImportDocumentFormGroup {
    const emailImportDocumentRawValue = this.convertEmailImportDocumentToEmailImportDocumentRawValue({
      ...this.getFormDefaults(),
      ...emailImportDocument,
    });
    return new FormGroup<EmailImportDocumentFormGroupContent>({
      id: new FormControl(
        { value: emailImportDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sha256: new FormControl(emailImportDocumentRawValue.sha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      fromEmail: new FormControl(emailImportDocumentRawValue.fromEmail, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      toEmail: new FormControl(emailImportDocumentRawValue.toEmail, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      subject: new FormControl(emailImportDocumentRawValue.subject, {
        validators: [Validators.maxLength(1000)],
      }),
      body: new FormControl(emailImportDocumentRawValue.body),
      bodyHtml: new FormControl(emailImportDocumentRawValue.bodyHtml),
      receivedDate: new FormControl(emailImportDocumentRawValue.receivedDate, {
        validators: [Validators.required],
      }),
      processedDate: new FormControl(emailImportDocumentRawValue.processedDate),
      status: new FormControl(emailImportDocumentRawValue.status, {
        validators: [Validators.required],
      }),
      attachmentCount: new FormControl(emailImportDocumentRawValue.attachmentCount),
      documentsCreated: new FormControl(emailImportDocumentRawValue.documentsCreated),
      errorMessage: new FormControl(emailImportDocumentRawValue.errorMessage),
      metadata: new FormControl(emailImportDocumentRawValue.metadata),
      documentSha256: new FormControl(emailImportDocumentRawValue.documentSha256, {
        validators: [Validators.maxLength(64)],
      }),
      appliedRule: new FormControl(emailImportDocumentRawValue.appliedRule),
    });
  }

  getEmailImportDocument(form: EmailImportDocumentFormGroup): IEmailImportDocument | NewEmailImportDocument {
    return this.convertEmailImportDocumentRawValueToEmailImportDocument(
      form.getRawValue() as EmailImportDocumentFormRawValue | NewEmailImportDocumentFormRawValue,
    );
  }

  resetForm(form: EmailImportDocumentFormGroup, emailImportDocument: EmailImportDocumentFormGroupInput): void {
    const emailImportDocumentRawValue = this.convertEmailImportDocumentToEmailImportDocumentRawValue({
      ...this.getFormDefaults(),
      ...emailImportDocument,
    });
    form.reset(
      {
        ...emailImportDocumentRawValue,
        id: { value: emailImportDocumentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmailImportDocumentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      receivedDate: currentTime,
      processedDate: currentTime,
    };
  }

  private convertEmailImportDocumentRawValueToEmailImportDocument(
    rawEmailImportDocument: EmailImportDocumentFormRawValue | NewEmailImportDocumentFormRawValue,
  ): IEmailImportDocument | NewEmailImportDocument {
    return {
      ...rawEmailImportDocument,
      receivedDate: dayjs(rawEmailImportDocument.receivedDate, DATE_TIME_FORMAT),
      processedDate: dayjs(rawEmailImportDocument.processedDate, DATE_TIME_FORMAT),
    };
  }

  private convertEmailImportDocumentToEmailImportDocumentRawValue(
    emailImportDocument: IEmailImportDocument | (Partial<NewEmailImportDocument> & EmailImportDocumentFormDefaults),
  ): EmailImportDocumentFormRawValue | PartialWithRequiredKeyOf<NewEmailImportDocumentFormRawValue> {
    return {
      ...emailImportDocument,
      receivedDate: emailImportDocument.receivedDate ? emailImportDocument.receivedDate.format(DATE_TIME_FORMAT) : undefined,
      processedDate: emailImportDocument.processedDate ? emailImportDocument.processedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
