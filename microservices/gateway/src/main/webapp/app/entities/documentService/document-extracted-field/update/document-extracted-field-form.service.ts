import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentExtractedField, NewDocumentExtractedField } from '../document-extracted-field.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentExtractedField for edit and NewDocumentExtractedFieldFormGroupInput for create.
 */
type DocumentExtractedFieldFormGroupInput = IDocumentExtractedField | PartialWithRequiredKeyOf<NewDocumentExtractedField>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentExtractedField | NewDocumentExtractedField> = Omit<T, 'extractedDate'> & {
  extractedDate?: string | null;
};

type DocumentExtractedFieldFormRawValue = FormValueOf<IDocumentExtractedField>;

type NewDocumentExtractedFieldFormRawValue = FormValueOf<NewDocumentExtractedField>;

type DocumentExtractedFieldFormDefaults = Pick<NewDocumentExtractedField, 'id' | 'isVerified' | 'extractedDate'>;

type DocumentExtractedFieldFormGroupContent = {
  id: FormControl<DocumentExtractedFieldFormRawValue['id'] | NewDocumentExtractedField['id']>;
  fieldKey: FormControl<DocumentExtractedFieldFormRawValue['fieldKey']>;
  fieldValue: FormControl<DocumentExtractedFieldFormRawValue['fieldValue']>;
  confidence: FormControl<DocumentExtractedFieldFormRawValue['confidence']>;
  extractionMethod: FormControl<DocumentExtractedFieldFormRawValue['extractionMethod']>;
  isVerified: FormControl<DocumentExtractedFieldFormRawValue['isVerified']>;
  extractedDate: FormControl<DocumentExtractedFieldFormRawValue['extractedDate']>;
  document: FormControl<DocumentExtractedFieldFormRawValue['document']>;
};

export type DocumentExtractedFieldFormGroup = FormGroup<DocumentExtractedFieldFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentExtractedFieldFormService {
  createDocumentExtractedFieldFormGroup(
    documentExtractedField: DocumentExtractedFieldFormGroupInput = { id: null },
  ): DocumentExtractedFieldFormGroup {
    const documentExtractedFieldRawValue = this.convertDocumentExtractedFieldToDocumentExtractedFieldRawValue({
      ...this.getFormDefaults(),
      ...documentExtractedField,
    });
    return new FormGroup<DocumentExtractedFieldFormGroupContent>({
      id: new FormControl(
        { value: documentExtractedFieldRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fieldKey: new FormControl(documentExtractedFieldRawValue.fieldKey, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      fieldValue: new FormControl(documentExtractedFieldRawValue.fieldValue, {
        validators: [Validators.required],
      }),
      confidence: new FormControl(documentExtractedFieldRawValue.confidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      extractionMethod: new FormControl(documentExtractedFieldRawValue.extractionMethod),
      isVerified: new FormControl(documentExtractedFieldRawValue.isVerified, {
        validators: [Validators.required],
      }),
      extractedDate: new FormControl(documentExtractedFieldRawValue.extractedDate, {
        validators: [Validators.required],
      }),
      document: new FormControl(documentExtractedFieldRawValue.document),
    });
  }

  getDocumentExtractedField(form: DocumentExtractedFieldFormGroup): IDocumentExtractedField | NewDocumentExtractedField {
    return this.convertDocumentExtractedFieldRawValueToDocumentExtractedField(
      form.getRawValue() as DocumentExtractedFieldFormRawValue | NewDocumentExtractedFieldFormRawValue,
    );
  }

  resetForm(form: DocumentExtractedFieldFormGroup, documentExtractedField: DocumentExtractedFieldFormGroupInput): void {
    const documentExtractedFieldRawValue = this.convertDocumentExtractedFieldToDocumentExtractedFieldRawValue({
      ...this.getFormDefaults(),
      ...documentExtractedField,
    });
    form.reset(
      {
        ...documentExtractedFieldRawValue,
        id: { value: documentExtractedFieldRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentExtractedFieldFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isVerified: false,
      extractedDate: currentTime,
    };
  }

  private convertDocumentExtractedFieldRawValueToDocumentExtractedField(
    rawDocumentExtractedField: DocumentExtractedFieldFormRawValue | NewDocumentExtractedFieldFormRawValue,
  ): IDocumentExtractedField | NewDocumentExtractedField {
    return {
      ...rawDocumentExtractedField,
      extractedDate: dayjs(rawDocumentExtractedField.extractedDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentExtractedFieldToDocumentExtractedFieldRawValue(
    documentExtractedField: IDocumentExtractedField | (Partial<NewDocumentExtractedField> & DocumentExtractedFieldFormDefaults),
  ): DocumentExtractedFieldFormRawValue | PartialWithRequiredKeyOf<NewDocumentExtractedFieldFormRawValue> {
    return {
      ...documentExtractedField,
      extractedDate: documentExtractedField.extractedDate ? documentExtractedField.extractedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
