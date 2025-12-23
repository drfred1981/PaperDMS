import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOcrComparison, NewOcrComparison } from '../ocr-comparison.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOcrComparison for edit and NewOcrComparisonFormGroupInput for create.
 */
type OcrComparisonFormGroupInput = IOcrComparison | PartialWithRequiredKeyOf<NewOcrComparison>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOcrComparison | NewOcrComparison> = Omit<T, 'selectedDate' | 'comparisonDate'> & {
  selectedDate?: string | null;
  comparisonDate?: string | null;
};

type OcrComparisonFormRawValue = FormValueOf<IOcrComparison>;

type NewOcrComparisonFormRawValue = FormValueOf<NewOcrComparison>;

type OcrComparisonFormDefaults = Pick<NewOcrComparison, 'id' | 'selectedDate' | 'comparisonDate'>;

type OcrComparisonFormGroupContent = {
  id: FormControl<OcrComparisonFormRawValue['id'] | NewOcrComparison['id']>;
  documentId: FormControl<OcrComparisonFormRawValue['documentId']>;
  documentSha256: FormControl<OcrComparisonFormRawValue['documentSha256']>;
  pageNumber: FormControl<OcrComparisonFormRawValue['pageNumber']>;
  tikaText: FormControl<OcrComparisonFormRawValue['tikaText']>;
  tikaConfidence: FormControl<OcrComparisonFormRawValue['tikaConfidence']>;
  aiText: FormControl<OcrComparisonFormRawValue['aiText']>;
  aiConfidence: FormControl<OcrComparisonFormRawValue['aiConfidence']>;
  similarity: FormControl<OcrComparisonFormRawValue['similarity']>;
  differences: FormControl<OcrComparisonFormRawValue['differences']>;
  differencesS3Key: FormControl<OcrComparisonFormRawValue['differencesS3Key']>;
  selectedEngine: FormControl<OcrComparisonFormRawValue['selectedEngine']>;
  selectedBy: FormControl<OcrComparisonFormRawValue['selectedBy']>;
  selectedDate: FormControl<OcrComparisonFormRawValue['selectedDate']>;
  comparisonDate: FormControl<OcrComparisonFormRawValue['comparisonDate']>;
  metadata: FormControl<OcrComparisonFormRawValue['metadata']>;
};

export type OcrComparisonFormGroup = FormGroup<OcrComparisonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OcrComparisonFormService {
  createOcrComparisonFormGroup(ocrComparison?: OcrComparisonFormGroupInput): OcrComparisonFormGroup {
    const ocrComparisonRawValue = this.convertOcrComparisonToOcrComparisonRawValue({
      ...this.getFormDefaults(),
      ...(ocrComparison ?? { id: null }),
    });
    return new FormGroup<OcrComparisonFormGroupContent>({
      id: new FormControl(
        { value: ocrComparisonRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(ocrComparisonRawValue.documentId, {
        validators: [Validators.required],
      }),
      documentSha256: new FormControl(ocrComparisonRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      pageNumber: new FormControl(ocrComparisonRawValue.pageNumber, {
        validators: [Validators.required],
      }),
      tikaText: new FormControl(ocrComparisonRawValue.tikaText),
      tikaConfidence: new FormControl(ocrComparisonRawValue.tikaConfidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      aiText: new FormControl(ocrComparisonRawValue.aiText),
      aiConfidence: new FormControl(ocrComparisonRawValue.aiConfidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      similarity: new FormControl(ocrComparisonRawValue.similarity, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      differences: new FormControl(ocrComparisonRawValue.differences),
      differencesS3Key: new FormControl(ocrComparisonRawValue.differencesS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      selectedEngine: new FormControl(ocrComparisonRawValue.selectedEngine),
      selectedBy: new FormControl(ocrComparisonRawValue.selectedBy, {
        validators: [Validators.maxLength(50)],
      }),
      selectedDate: new FormControl(ocrComparisonRawValue.selectedDate),
      comparisonDate: new FormControl(ocrComparisonRawValue.comparisonDate, {
        validators: [Validators.required],
      }),
      metadata: new FormControl(ocrComparisonRawValue.metadata),
    });
  }

  getOcrComparison(form: OcrComparisonFormGroup): IOcrComparison | NewOcrComparison {
    return this.convertOcrComparisonRawValueToOcrComparison(form.getRawValue() as OcrComparisonFormRawValue | NewOcrComparisonFormRawValue);
  }

  resetForm(form: OcrComparisonFormGroup, ocrComparison: OcrComparisonFormGroupInput): void {
    const ocrComparisonRawValue = this.convertOcrComparisonToOcrComparisonRawValue({ ...this.getFormDefaults(), ...ocrComparison });
    form.reset({
      ...ocrComparisonRawValue,
      id: { value: ocrComparisonRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OcrComparisonFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      selectedDate: currentTime,
      comparisonDate: currentTime,
    };
  }

  private convertOcrComparisonRawValueToOcrComparison(
    rawOcrComparison: OcrComparisonFormRawValue | NewOcrComparisonFormRawValue,
  ): IOcrComparison | NewOcrComparison {
    return {
      ...rawOcrComparison,
      selectedDate: dayjs(rawOcrComparison.selectedDate, DATE_TIME_FORMAT),
      comparisonDate: dayjs(rawOcrComparison.comparisonDate, DATE_TIME_FORMAT),
    };
  }

  private convertOcrComparisonToOcrComparisonRawValue(
    ocrComparison: IOcrComparison | (Partial<NewOcrComparison> & OcrComparisonFormDefaults),
  ): OcrComparisonFormRawValue | PartialWithRequiredKeyOf<NewOcrComparisonFormRawValue> {
    return {
      ...ocrComparison,
      selectedDate: ocrComparison.selectedDate ? ocrComparison.selectedDate.format(DATE_TIME_FORMAT) : undefined,
      comparisonDate: ocrComparison.comparisonDate ? ocrComparison.comparisonDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
