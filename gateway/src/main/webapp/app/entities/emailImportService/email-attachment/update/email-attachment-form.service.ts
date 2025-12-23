import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEmailAttachment, NewEmailAttachment } from '../email-attachment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmailAttachment for edit and NewEmailAttachmentFormGroupInput for create.
 */
type EmailAttachmentFormGroupInput = IEmailAttachment | PartialWithRequiredKeyOf<NewEmailAttachment>;

type EmailAttachmentFormDefaults = Pick<NewEmailAttachment, 'id'>;

type EmailAttachmentFormGroupContent = {
  id: FormControl<IEmailAttachment['id'] | NewEmailAttachment['id']>;
  emailImportId: FormControl<IEmailAttachment['emailImportId']>;
  fileName: FormControl<IEmailAttachment['fileName']>;
  fileSize: FormControl<IEmailAttachment['fileSize']>;
  mimeType: FormControl<IEmailAttachment['mimeType']>;
  sha256: FormControl<IEmailAttachment['sha256']>;
  s3Key: FormControl<IEmailAttachment['s3Key']>;
  documentId: FormControl<IEmailAttachment['documentId']>;
  status: FormControl<IEmailAttachment['status']>;
  errorMessage: FormControl<IEmailAttachment['errorMessage']>;
  emailImport: FormControl<IEmailAttachment['emailImport']>;
};

export type EmailAttachmentFormGroup = FormGroup<EmailAttachmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmailAttachmentFormService {
  createEmailAttachmentFormGroup(emailAttachment?: EmailAttachmentFormGroupInput): EmailAttachmentFormGroup {
    const emailAttachmentRawValue = {
      ...this.getFormDefaults(),
      ...(emailAttachment ?? { id: null }),
    };
    return new FormGroup<EmailAttachmentFormGroupContent>({
      id: new FormControl(
        { value: emailAttachmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      emailImportId: new FormControl(emailAttachmentRawValue.emailImportId, {
        validators: [Validators.required],
      }),
      fileName: new FormControl(emailAttachmentRawValue.fileName, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      fileSize: new FormControl(emailAttachmentRawValue.fileSize, {
        validators: [Validators.required],
      }),
      mimeType: new FormControl(emailAttachmentRawValue.mimeType, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      sha256: new FormControl(emailAttachmentRawValue.sha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      s3Key: new FormControl(emailAttachmentRawValue.s3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      documentId: new FormControl(emailAttachmentRawValue.documentId),
      status: new FormControl(emailAttachmentRawValue.status, {
        validators: [Validators.required],
      }),
      errorMessage: new FormControl(emailAttachmentRawValue.errorMessage),
      emailImport: new FormControl(emailAttachmentRawValue.emailImport, {
        validators: [Validators.required],
      }),
    });
  }

  getEmailAttachment(form: EmailAttachmentFormGroup): IEmailAttachment | NewEmailAttachment {
    return form.getRawValue() as IEmailAttachment | NewEmailAttachment;
  }

  resetForm(form: EmailAttachmentFormGroup, emailAttachment: EmailAttachmentFormGroupInput): void {
    const emailAttachmentRawValue = { ...this.getFormDefaults(), ...emailAttachment };
    form.reset({
      ...emailAttachmentRawValue,
      id: { value: emailAttachmentRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): EmailAttachmentFormDefaults {
    return {
      id: null,
    };
  }
}
