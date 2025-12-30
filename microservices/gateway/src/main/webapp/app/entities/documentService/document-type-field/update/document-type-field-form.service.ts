import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentTypeField, NewDocumentTypeField } from '../document-type-field.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentTypeField for edit and NewDocumentTypeFieldFormGroupInput for create.
 */
type DocumentTypeFieldFormGroupInput = IDocumentTypeField | PartialWithRequiredKeyOf<NewDocumentTypeField>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentTypeField | NewDocumentTypeField> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type DocumentTypeFieldFormRawValue = FormValueOf<IDocumentTypeField>;

type NewDocumentTypeFieldFormRawValue = FormValueOf<NewDocumentTypeField>;

type DocumentTypeFieldFormDefaults = Pick<NewDocumentTypeField, 'id' | 'isRequired' | 'isSearchable' | 'createdDate'>;

type DocumentTypeFieldFormGroupContent = {
  id: FormControl<DocumentTypeFieldFormRawValue['id'] | NewDocumentTypeField['id']>;
  fieldKey: FormControl<DocumentTypeFieldFormRawValue['fieldKey']>;
  fieldLabel: FormControl<DocumentTypeFieldFormRawValue['fieldLabel']>;
  dataType: FormControl<DocumentTypeFieldFormRawValue['dataType']>;
  isRequired: FormControl<DocumentTypeFieldFormRawValue['isRequired']>;
  isSearchable: FormControl<DocumentTypeFieldFormRawValue['isSearchable']>;
  createdDate: FormControl<DocumentTypeFieldFormRawValue['createdDate']>;
  documentType: FormControl<DocumentTypeFieldFormRawValue['documentType']>;
};

export type DocumentTypeFieldFormGroup = FormGroup<DocumentTypeFieldFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentTypeFieldFormService {
  createDocumentTypeFieldFormGroup(documentTypeField: DocumentTypeFieldFormGroupInput = { id: null }): DocumentTypeFieldFormGroup {
    const documentTypeFieldRawValue = this.convertDocumentTypeFieldToDocumentTypeFieldRawValue({
      ...this.getFormDefaults(),
      ...documentTypeField,
    });
    return new FormGroup<DocumentTypeFieldFormGroupContent>({
      id: new FormControl(
        { value: documentTypeFieldRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fieldKey: new FormControl(documentTypeFieldRawValue.fieldKey, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      fieldLabel: new FormControl(documentTypeFieldRawValue.fieldLabel, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      dataType: new FormControl(documentTypeFieldRawValue.dataType),
      isRequired: new FormControl(documentTypeFieldRawValue.isRequired, {
        validators: [Validators.required],
      }),
      isSearchable: new FormControl(documentTypeFieldRawValue.isSearchable, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(documentTypeFieldRawValue.createdDate, {
        validators: [Validators.required],
      }),
      documentType: new FormControl(documentTypeFieldRawValue.documentType),
    });
  }

  getDocumentTypeField(form: DocumentTypeFieldFormGroup): IDocumentTypeField | NewDocumentTypeField {
    return this.convertDocumentTypeFieldRawValueToDocumentTypeField(
      form.getRawValue() as DocumentTypeFieldFormRawValue | NewDocumentTypeFieldFormRawValue,
    );
  }

  resetForm(form: DocumentTypeFieldFormGroup, documentTypeField: DocumentTypeFieldFormGroupInput): void {
    const documentTypeFieldRawValue = this.convertDocumentTypeFieldToDocumentTypeFieldRawValue({
      ...this.getFormDefaults(),
      ...documentTypeField,
    });
    form.reset(
      {
        ...documentTypeFieldRawValue,
        id: { value: documentTypeFieldRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentTypeFieldFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isRequired: false,
      isSearchable: false,
      createdDate: currentTime,
    };
  }

  private convertDocumentTypeFieldRawValueToDocumentTypeField(
    rawDocumentTypeField: DocumentTypeFieldFormRawValue | NewDocumentTypeFieldFormRawValue,
  ): IDocumentTypeField | NewDocumentTypeField {
    return {
      ...rawDocumentTypeField,
      createdDate: dayjs(rawDocumentTypeField.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentTypeFieldToDocumentTypeFieldRawValue(
    documentTypeField: IDocumentTypeField | (Partial<NewDocumentTypeField> & DocumentTypeFieldFormDefaults),
  ): DocumentTypeFieldFormRawValue | PartialWithRequiredKeyOf<NewDocumentTypeFieldFormRawValue> {
    return {
      ...documentTypeField,
      createdDate: documentTypeField.createdDate ? documentTypeField.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
