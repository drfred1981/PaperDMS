import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExtractedField, NewExtractedField } from '../extracted-field.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExtractedField for edit and NewExtractedFieldFormGroupInput for create.
 */
type ExtractedFieldFormGroupInput = IExtractedField | PartialWithRequiredKeyOf<NewExtractedField>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExtractedField | NewExtractedField> = Omit<T, 'extractedDate'> & {
  extractedDate?: string | null;
};

type ExtractedFieldFormRawValue = FormValueOf<IExtractedField>;

type NewExtractedFieldFormRawValue = FormValueOf<NewExtractedField>;

type ExtractedFieldFormDefaults = Pick<NewExtractedField, 'id' | 'isVerified' | 'extractedDate'>;

type ExtractedFieldFormGroupContent = {
  id: FormControl<ExtractedFieldFormRawValue['id'] | NewExtractedField['id']>;
  documentId: FormControl<ExtractedFieldFormRawValue['documentId']>;
  fieldKey: FormControl<ExtractedFieldFormRawValue['fieldKey']>;
  fieldValue: FormControl<ExtractedFieldFormRawValue['fieldValue']>;
  confidence: FormControl<ExtractedFieldFormRawValue['confidence']>;
  extractionMethod: FormControl<ExtractedFieldFormRawValue['extractionMethod']>;
  isVerified: FormControl<ExtractedFieldFormRawValue['isVerified']>;
  extractedDate: FormControl<ExtractedFieldFormRawValue['extractedDate']>;
};

export type ExtractedFieldFormGroup = FormGroup<ExtractedFieldFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExtractedFieldFormService {
  createExtractedFieldFormGroup(extractedField?: ExtractedFieldFormGroupInput): ExtractedFieldFormGroup {
    const extractedFieldRawValue = this.convertExtractedFieldToExtractedFieldRawValue({
      ...this.getFormDefaults(),
      ...(extractedField ?? { id: null }),
    });
    return new FormGroup<ExtractedFieldFormGroupContent>({
      id: new FormControl(
        { value: extractedFieldRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(extractedFieldRawValue.documentId, {
        validators: [Validators.required],
      }),
      fieldKey: new FormControl(extractedFieldRawValue.fieldKey, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      fieldValue: new FormControl(extractedFieldRawValue.fieldValue, {
        validators: [Validators.required],
      }),
      confidence: new FormControl(extractedFieldRawValue.confidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      extractionMethod: new FormControl(extractedFieldRawValue.extractionMethod),
      isVerified: new FormControl(extractedFieldRawValue.isVerified, {
        validators: [Validators.required],
      }),
      extractedDate: new FormControl(extractedFieldRawValue.extractedDate, {
        validators: [Validators.required],
      }),
    });
  }

  getExtractedField(form: ExtractedFieldFormGroup): IExtractedField | NewExtractedField {
    return this.convertExtractedFieldRawValueToExtractedField(
      form.getRawValue() as ExtractedFieldFormRawValue | NewExtractedFieldFormRawValue,
    );
  }

  resetForm(form: ExtractedFieldFormGroup, extractedField: ExtractedFieldFormGroupInput): void {
    const extractedFieldRawValue = this.convertExtractedFieldToExtractedFieldRawValue({ ...this.getFormDefaults(), ...extractedField });
    form.reset({
      ...extractedFieldRawValue,
      id: { value: extractedFieldRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ExtractedFieldFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isVerified: false,
      extractedDate: currentTime,
    };
  }

  private convertExtractedFieldRawValueToExtractedField(
    rawExtractedField: ExtractedFieldFormRawValue | NewExtractedFieldFormRawValue,
  ): IExtractedField | NewExtractedField {
    return {
      ...rawExtractedField,
      extractedDate: dayjs(rawExtractedField.extractedDate, DATE_TIME_FORMAT),
    };
  }

  private convertExtractedFieldToExtractedFieldRawValue(
    extractedField: IExtractedField | (Partial<NewExtractedField> & ExtractedFieldFormDefaults),
  ): ExtractedFieldFormRawValue | PartialWithRequiredKeyOf<NewExtractedFieldFormRawValue> {
    return {
      ...extractedField,
      extractedDate: extractedField.extractedDate ? extractedField.extractedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
