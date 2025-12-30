import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOcrResult, NewOcrResult } from '../ocr-result.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOcrResult for edit and NewOcrResultFormGroupInput for create.
 */
type OcrResultFormGroupInput = IOcrResult | PartialWithRequiredKeyOf<NewOcrResult>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOcrResult | NewOcrResult> = Omit<T, 'processedDate'> & {
  processedDate?: string | null;
};

type OcrResultFormRawValue = FormValueOf<IOcrResult>;

type NewOcrResultFormRawValue = FormValueOf<NewOcrResult>;

type OcrResultFormDefaults = Pick<NewOcrResult, 'id' | 'processedDate'>;

type OcrResultFormGroupContent = {
  id: FormControl<OcrResultFormRawValue['id'] | NewOcrResult['id']>;
  pageNumber: FormControl<OcrResultFormRawValue['pageNumber']>;
  pageSha256: FormControl<OcrResultFormRawValue['pageSha256']>;
  confidence: FormControl<OcrResultFormRawValue['confidence']>;
  s3ResultKey: FormControl<OcrResultFormRawValue['s3ResultKey']>;
  s3Bucket: FormControl<OcrResultFormRawValue['s3Bucket']>;
  s3BoundingBoxKey: FormControl<OcrResultFormRawValue['s3BoundingBoxKey']>;
  boundingBoxes: FormControl<OcrResultFormRawValue['boundingBoxes']>;
  metadata: FormControl<OcrResultFormRawValue['metadata']>;
  language: FormControl<OcrResultFormRawValue['language']>;
  wordCount: FormControl<OcrResultFormRawValue['wordCount']>;
  ocrEngine: FormControl<OcrResultFormRawValue['ocrEngine']>;
  processingTime: FormControl<OcrResultFormRawValue['processingTime']>;
  rawResponse: FormControl<OcrResultFormRawValue['rawResponse']>;
  rawResponseS3Key: FormControl<OcrResultFormRawValue['rawResponseS3Key']>;
  processedDate: FormControl<OcrResultFormRawValue['processedDate']>;
  job: FormControl<OcrResultFormRawValue['job']>;
};

export type OcrResultFormGroup = FormGroup<OcrResultFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OcrResultFormService {
  createOcrResultFormGroup(ocrResult: OcrResultFormGroupInput = { id: null }): OcrResultFormGroup {
    const ocrResultRawValue = this.convertOcrResultToOcrResultRawValue({
      ...this.getFormDefaults(),
      ...ocrResult,
    });
    return new FormGroup<OcrResultFormGroupContent>({
      id: new FormControl(
        { value: ocrResultRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      pageNumber: new FormControl(ocrResultRawValue.pageNumber, {
        validators: [Validators.required],
      }),
      pageSha256: new FormControl(ocrResultRawValue.pageSha256, {
        validators: [Validators.maxLength(64)],
      }),
      confidence: new FormControl(ocrResultRawValue.confidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      s3ResultKey: new FormControl(ocrResultRawValue.s3ResultKey, {
        validators: [Validators.maxLength(1000)],
      }),
      s3Bucket: new FormControl(ocrResultRawValue.s3Bucket, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      s3BoundingBoxKey: new FormControl(ocrResultRawValue.s3BoundingBoxKey, {
        validators: [Validators.maxLength(1000)],
      }),
      boundingBoxes: new FormControl(ocrResultRawValue.boundingBoxes),
      metadata: new FormControl(ocrResultRawValue.metadata),
      language: new FormControl(ocrResultRawValue.language, {
        validators: [Validators.maxLength(10)],
      }),
      wordCount: new FormControl(ocrResultRawValue.wordCount),
      ocrEngine: new FormControl(ocrResultRawValue.ocrEngine),
      processingTime: new FormControl(ocrResultRawValue.processingTime),
      rawResponse: new FormControl(ocrResultRawValue.rawResponse),
      rawResponseS3Key: new FormControl(ocrResultRawValue.rawResponseS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      processedDate: new FormControl(ocrResultRawValue.processedDate, {
        validators: [Validators.required],
      }),
      job: new FormControl(ocrResultRawValue.job),
    });
  }

  getOcrResult(form: OcrResultFormGroup): IOcrResult | NewOcrResult {
    return this.convertOcrResultRawValueToOcrResult(form.getRawValue() as OcrResultFormRawValue | NewOcrResultFormRawValue);
  }

  resetForm(form: OcrResultFormGroup, ocrResult: OcrResultFormGroupInput): void {
    const ocrResultRawValue = this.convertOcrResultToOcrResultRawValue({ ...this.getFormDefaults(), ...ocrResult });
    form.reset(
      {
        ...ocrResultRawValue,
        id: { value: ocrResultRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OcrResultFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      processedDate: currentTime,
    };
  }

  private convertOcrResultRawValueToOcrResult(rawOcrResult: OcrResultFormRawValue | NewOcrResultFormRawValue): IOcrResult | NewOcrResult {
    return {
      ...rawOcrResult,
      processedDate: dayjs(rawOcrResult.processedDate, DATE_TIME_FORMAT),
    };
  }

  private convertOcrResultToOcrResultRawValue(
    ocrResult: IOcrResult | (Partial<NewOcrResult> & OcrResultFormDefaults),
  ): OcrResultFormRawValue | PartialWithRequiredKeyOf<NewOcrResultFormRawValue> {
    return {
      ...ocrResult,
      processedDate: ocrResult.processedDate ? ocrResult.processedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
