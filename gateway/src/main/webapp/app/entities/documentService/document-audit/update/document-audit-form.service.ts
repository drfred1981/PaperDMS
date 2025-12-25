import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentAudit, NewDocumentAudit } from '../document-audit.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentAudit for edit and NewDocumentAuditFormGroupInput for create.
 */
type DocumentAuditFormGroupInput = IDocumentAudit | PartialWithRequiredKeyOf<NewDocumentAudit>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentAudit | NewDocumentAudit> = Omit<T, 'actionDate'> & {
  actionDate?: string | null;
};

type DocumentAuditFormRawValue = FormValueOf<IDocumentAudit>;

type NewDocumentAuditFormRawValue = FormValueOf<NewDocumentAudit>;

type DocumentAuditFormDefaults = Pick<NewDocumentAudit, 'id' | 'actionDate'>;

type DocumentAuditFormGroupContent = {
  id: FormControl<DocumentAuditFormRawValue['id'] | NewDocumentAudit['id']>;
  documentId: FormControl<DocumentAuditFormRawValue['documentId']>;
  documentSha256: FormControl<DocumentAuditFormRawValue['documentSha256']>;
  action: FormControl<DocumentAuditFormRawValue['action']>;
  userId: FormControl<DocumentAuditFormRawValue['userId']>;
  userIp: FormControl<DocumentAuditFormRawValue['userIp']>;
  actionDate: FormControl<DocumentAuditFormRawValue['actionDate']>;
  additionalInfo: FormControl<DocumentAuditFormRawValue['additionalInfo']>;
};

export type DocumentAuditFormGroup = FormGroup<DocumentAuditFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentAuditFormService {
  createDocumentAuditFormGroup(documentAudit: DocumentAuditFormGroupInput = { id: null }): DocumentAuditFormGroup {
    const documentAuditRawValue = this.convertDocumentAuditToDocumentAuditRawValue({
      ...this.getFormDefaults(),
      ...documentAudit,
    });
    return new FormGroup<DocumentAuditFormGroupContent>({
      id: new FormControl(
        { value: documentAuditRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(documentAuditRawValue.documentId, {
        validators: [Validators.required],
      }),
      documentSha256: new FormControl(documentAuditRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      action: new FormControl(documentAuditRawValue.action, {
        validators: [Validators.required],
      }),
      userId: new FormControl(documentAuditRawValue.userId, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      userIp: new FormControl(documentAuditRawValue.userIp, {
        validators: [Validators.maxLength(45)],
      }),
      actionDate: new FormControl(documentAuditRawValue.actionDate, {
        validators: [Validators.required],
      }),
      additionalInfo: new FormControl(documentAuditRawValue.additionalInfo),
    });
  }

  getDocumentAudit(form: DocumentAuditFormGroup): IDocumentAudit | NewDocumentAudit {
    return this.convertDocumentAuditRawValueToDocumentAudit(form.getRawValue() as DocumentAuditFormRawValue | NewDocumentAuditFormRawValue);
  }

  resetForm(form: DocumentAuditFormGroup, documentAudit: DocumentAuditFormGroupInput): void {
    const documentAuditRawValue = this.convertDocumentAuditToDocumentAuditRawValue({ ...this.getFormDefaults(), ...documentAudit });
    form.reset(
      {
        ...documentAuditRawValue,
        id: { value: documentAuditRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentAuditFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      actionDate: currentTime,
    };
  }

  private convertDocumentAuditRawValueToDocumentAudit(
    rawDocumentAudit: DocumentAuditFormRawValue | NewDocumentAuditFormRawValue,
  ): IDocumentAudit | NewDocumentAudit {
    return {
      ...rawDocumentAudit,
      actionDate: dayjs(rawDocumentAudit.actionDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentAuditToDocumentAuditRawValue(
    documentAudit: IDocumentAudit | (Partial<NewDocumentAudit> & DocumentAuditFormDefaults),
  ): DocumentAuditFormRawValue | PartialWithRequiredKeyOf<NewDocumentAuditFormRawValue> {
    return {
      ...documentAudit,
      actionDate: documentAudit.actionDate ? documentAudit.actionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
