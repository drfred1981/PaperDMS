import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentType, NewDocumentType } from '../document-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentType for edit and NewDocumentTypeFormGroupInput for create.
 */
type DocumentTypeFormGroupInput = IDocumentType | PartialWithRequiredKeyOf<NewDocumentType>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentType | NewDocumentType> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type DocumentTypeFormRawValue = FormValueOf<IDocumentType>;

type NewDocumentTypeFormRawValue = FormValueOf<NewDocumentType>;

type DocumentTypeFormDefaults = Pick<NewDocumentType, 'id' | 'isActive' | 'createdDate'>;

type DocumentTypeFormGroupContent = {
  id: FormControl<DocumentTypeFormRawValue['id'] | NewDocumentType['id']>;
  name: FormControl<DocumentTypeFormRawValue['name']>;
  code: FormControl<DocumentTypeFormRawValue['code']>;
  icon: FormControl<DocumentTypeFormRawValue['icon']>;
  color: FormControl<DocumentTypeFormRawValue['color']>;
  isActive: FormControl<DocumentTypeFormRawValue['isActive']>;
  createdDate: FormControl<DocumentTypeFormRawValue['createdDate']>;
  createdBy: FormControl<DocumentTypeFormRawValue['createdBy']>;
};

export type DocumentTypeFormGroup = FormGroup<DocumentTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentTypeFormService {
  createDocumentTypeFormGroup(documentType?: DocumentTypeFormGroupInput): DocumentTypeFormGroup {
    const documentTypeRawValue = this.convertDocumentTypeToDocumentTypeRawValue({
      ...this.getFormDefaults(),
      ...(documentType ?? { id: null }),
    });
    return new FormGroup<DocumentTypeFormGroupContent>({
      id: new FormControl(
        { value: documentTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(documentTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      code: new FormControl(documentTypeRawValue.code, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      icon: new FormControl(documentTypeRawValue.icon, {
        validators: [Validators.maxLength(50)],
      }),
      color: new FormControl(documentTypeRawValue.color, {
        validators: [Validators.maxLength(7)],
      }),
      isActive: new FormControl(documentTypeRawValue.isActive, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(documentTypeRawValue.createdDate, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(documentTypeRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
    });
  }

  getDocumentType(form: DocumentTypeFormGroup): IDocumentType | NewDocumentType {
    return this.convertDocumentTypeRawValueToDocumentType(form.getRawValue() as DocumentTypeFormRawValue | NewDocumentTypeFormRawValue);
  }

  resetForm(form: DocumentTypeFormGroup, documentType: DocumentTypeFormGroupInput): void {
    const documentTypeRawValue = this.convertDocumentTypeToDocumentTypeRawValue({ ...this.getFormDefaults(), ...documentType });
    form.reset({
      ...documentTypeRawValue,
      id: { value: documentTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DocumentTypeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      createdDate: currentTime,
    };
  }

  private convertDocumentTypeRawValueToDocumentType(
    rawDocumentType: DocumentTypeFormRawValue | NewDocumentTypeFormRawValue,
  ): IDocumentType | NewDocumentType {
    return {
      ...rawDocumentType,
      createdDate: dayjs(rawDocumentType.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentTypeToDocumentTypeRawValue(
    documentType: IDocumentType | (Partial<NewDocumentType> & DocumentTypeFormDefaults),
  ): DocumentTypeFormRawValue | PartialWithRequiredKeyOf<NewDocumentTypeFormRawValue> {
    return {
      ...documentType,
      createdDate: documentType.createdDate ? documentType.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
