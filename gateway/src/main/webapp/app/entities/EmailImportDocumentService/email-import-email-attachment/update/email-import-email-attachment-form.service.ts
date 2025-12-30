import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEmailImportEmailAttachment, NewEmailImportEmailAttachment } from '../email-import-email-attachment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmailImportEmailAttachment for edit and NewEmailImportEmailAttachmentFormGroupInput for create.
 */
type EmailImportEmailAttachmentFormGroupInput = IEmailImportEmailAttachment | PartialWithRequiredKeyOf<NewEmailImportEmailAttachment>;

type EmailImportEmailAttachmentFormDefaults = Pick<NewEmailImportEmailAttachment, 'id'>;

type EmailImportEmailAttachmentFormGroupContent = {
  id: FormControl<IEmailImportEmailAttachment['id'] | NewEmailImportEmailAttachment['id']>;
  fileName: FormControl<IEmailImportEmailAttachment['fileName']>;
  fileSize: FormControl<IEmailImportEmailAttachment['fileSize']>;
  mimeType: FormControl<IEmailImportEmailAttachment['mimeType']>;
  sha256: FormControl<IEmailImportEmailAttachment['sha256']>;
  s3Key: FormControl<IEmailImportEmailAttachment['s3Key']>;
  status: FormControl<IEmailImportEmailAttachment['status']>;
  errorMessage: FormControl<IEmailImportEmailAttachment['errorMessage']>;
  documentSha256: FormControl<IEmailImportEmailAttachment['documentSha256']>;
  emailImportDocument: FormControl<IEmailImportEmailAttachment['emailImportDocument']>;
};

export type EmailImportEmailAttachmentFormGroup = FormGroup<EmailImportEmailAttachmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmailImportEmailAttachmentFormService {
  createEmailImportEmailAttachmentFormGroup(
    emailImportEmailAttachment: EmailImportEmailAttachmentFormGroupInput = { id: null },
  ): EmailImportEmailAttachmentFormGroup {
    const emailImportEmailAttachmentRawValue = {
      ...this.getFormDefaults(),
      ...emailImportEmailAttachment,
    };
    return new FormGroup<EmailImportEmailAttachmentFormGroupContent>({
      id: new FormControl(
        { value: emailImportEmailAttachmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fileName: new FormControl(emailImportEmailAttachmentRawValue.fileName, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      fileSize: new FormControl(emailImportEmailAttachmentRawValue.fileSize, {
        validators: [Validators.required],
      }),
      mimeType: new FormControl(emailImportEmailAttachmentRawValue.mimeType, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      sha256: new FormControl(emailImportEmailAttachmentRawValue.sha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      s3Key: new FormControl(emailImportEmailAttachmentRawValue.s3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      status: new FormControl(emailImportEmailAttachmentRawValue.status, {
        validators: [Validators.required],
      }),
      errorMessage: new FormControl(emailImportEmailAttachmentRawValue.errorMessage),
      documentSha256: new FormControl(emailImportEmailAttachmentRawValue.documentSha256, {
        validators: [Validators.maxLength(64)],
      }),
      emailImportDocument: new FormControl(emailImportEmailAttachmentRawValue.emailImportDocument),
    });
  }

  getEmailImportEmailAttachment(form: EmailImportEmailAttachmentFormGroup): IEmailImportEmailAttachment | NewEmailImportEmailAttachment {
    return form.getRawValue() as IEmailImportEmailAttachment | NewEmailImportEmailAttachment;
  }

  resetForm(form: EmailImportEmailAttachmentFormGroup, emailImportEmailAttachment: EmailImportEmailAttachmentFormGroupInput): void {
    const emailImportEmailAttachmentRawValue = { ...this.getFormDefaults(), ...emailImportEmailAttachment };
    form.reset(
      {
        ...emailImportEmailAttachmentRawValue,
        id: { value: emailImportEmailAttachmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmailImportEmailAttachmentFormDefaults {
    return {
      id: null,
    };
  }
}
