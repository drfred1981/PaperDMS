import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentPermission, NewDocumentPermission } from '../document-permission.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentPermission for edit and NewDocumentPermissionFormGroupInput for create.
 */
type DocumentPermissionFormGroupInput = IDocumentPermission | PartialWithRequiredKeyOf<NewDocumentPermission>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentPermission | NewDocumentPermission> = Omit<T, 'grantedDate'> & {
  grantedDate?: string | null;
};

type DocumentPermissionFormRawValue = FormValueOf<IDocumentPermission>;

type NewDocumentPermissionFormRawValue = FormValueOf<NewDocumentPermission>;

type DocumentPermissionFormDefaults = Pick<NewDocumentPermission, 'id' | 'canDelegate' | 'grantedDate'>;

type DocumentPermissionFormGroupContent = {
  id: FormControl<DocumentPermissionFormRawValue['id'] | NewDocumentPermission['id']>;
  documentId: FormControl<DocumentPermissionFormRawValue['documentId']>;
  principalType: FormControl<DocumentPermissionFormRawValue['principalType']>;
  principalId: FormControl<DocumentPermissionFormRawValue['principalId']>;
  permission: FormControl<DocumentPermissionFormRawValue['permission']>;
  canDelegate: FormControl<DocumentPermissionFormRawValue['canDelegate']>;
  grantedBy: FormControl<DocumentPermissionFormRawValue['grantedBy']>;
  grantedDate: FormControl<DocumentPermissionFormRawValue['grantedDate']>;
  permissionGroup: FormControl<DocumentPermissionFormRawValue['permissionGroup']>;
};

export type DocumentPermissionFormGroup = FormGroup<DocumentPermissionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentPermissionFormService {
  createDocumentPermissionFormGroup(documentPermission: DocumentPermissionFormGroupInput = { id: null }): DocumentPermissionFormGroup {
    const documentPermissionRawValue = this.convertDocumentPermissionToDocumentPermissionRawValue({
      ...this.getFormDefaults(),
      ...documentPermission,
    });
    return new FormGroup<DocumentPermissionFormGroupContent>({
      id: new FormControl(
        { value: documentPermissionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(documentPermissionRawValue.documentId, {
        validators: [Validators.required],
      }),
      principalType: new FormControl(documentPermissionRawValue.principalType, {
        validators: [Validators.required],
      }),
      principalId: new FormControl(documentPermissionRawValue.principalId, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      permission: new FormControl(documentPermissionRawValue.permission, {
        validators: [Validators.required],
      }),
      canDelegate: new FormControl(documentPermissionRawValue.canDelegate, {
        validators: [Validators.required],
      }),
      grantedBy: new FormControl(documentPermissionRawValue.grantedBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      grantedDate: new FormControl(documentPermissionRawValue.grantedDate, {
        validators: [Validators.required],
      }),
      permissionGroup: new FormControl(documentPermissionRawValue.permissionGroup),
    });
  }

  getDocumentPermission(form: DocumentPermissionFormGroup): IDocumentPermission | NewDocumentPermission {
    return this.convertDocumentPermissionRawValueToDocumentPermission(
      form.getRawValue() as DocumentPermissionFormRawValue | NewDocumentPermissionFormRawValue,
    );
  }

  resetForm(form: DocumentPermissionFormGroup, documentPermission: DocumentPermissionFormGroupInput): void {
    const documentPermissionRawValue = this.convertDocumentPermissionToDocumentPermissionRawValue({
      ...this.getFormDefaults(),
      ...documentPermission,
    });
    form.reset(
      {
        ...documentPermissionRawValue,
        id: { value: documentPermissionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentPermissionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      canDelegate: false,
      grantedDate: currentTime,
    };
  }

  private convertDocumentPermissionRawValueToDocumentPermission(
    rawDocumentPermission: DocumentPermissionFormRawValue | NewDocumentPermissionFormRawValue,
  ): IDocumentPermission | NewDocumentPermission {
    return {
      ...rawDocumentPermission,
      grantedDate: dayjs(rawDocumentPermission.grantedDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentPermissionToDocumentPermissionRawValue(
    documentPermission: IDocumentPermission | (Partial<NewDocumentPermission> & DocumentPermissionFormDefaults),
  ): DocumentPermissionFormRawValue | PartialWithRequiredKeyOf<NewDocumentPermissionFormRawValue> {
    return {
      ...documentPermission,
      grantedDate: documentPermission.grantedDate ? documentPermission.grantedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
