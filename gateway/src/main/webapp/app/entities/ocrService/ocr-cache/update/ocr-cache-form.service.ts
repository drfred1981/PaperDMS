import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOcrCache, NewOcrCache } from '../ocr-cache.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOcrCache for edit and NewOcrCacheFormGroupInput for create.
 */
type OcrCacheFormGroupInput = IOcrCache | PartialWithRequiredKeyOf<NewOcrCache>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOcrCache | NewOcrCache> = Omit<T, 'lastAccessDate' | 'createdDate' | 'expirationDate'> & {
  lastAccessDate?: string | null;
  createdDate?: string | null;
  expirationDate?: string | null;
};

type OcrCacheFormRawValue = FormValueOf<IOcrCache>;

type NewOcrCacheFormRawValue = FormValueOf<NewOcrCache>;

type OcrCacheFormDefaults = Pick<NewOcrCache, 'id' | 'lastAccessDate' | 'createdDate' | 'expirationDate'>;

type OcrCacheFormGroupContent = {
  id: FormControl<OcrCacheFormRawValue['id'] | NewOcrCache['id']>;
  documentSha256: FormControl<OcrCacheFormRawValue['documentSha256']>;
  ocrEngine: FormControl<OcrCacheFormRawValue['ocrEngine']>;
  language: FormControl<OcrCacheFormRawValue['language']>;
  pageCount: FormControl<OcrCacheFormRawValue['pageCount']>;
  totalConfidence: FormControl<OcrCacheFormRawValue['totalConfidence']>;
  s3ResultKey: FormControl<OcrCacheFormRawValue['s3ResultKey']>;
  s3Bucket: FormControl<OcrCacheFormRawValue['s3Bucket']>;
  extractedTextS3Key: FormControl<OcrCacheFormRawValue['extractedTextS3Key']>;
  metadata: FormControl<OcrCacheFormRawValue['metadata']>;
  hits: FormControl<OcrCacheFormRawValue['hits']>;
  lastAccessDate: FormControl<OcrCacheFormRawValue['lastAccessDate']>;
  createdDate: FormControl<OcrCacheFormRawValue['createdDate']>;
  expirationDate: FormControl<OcrCacheFormRawValue['expirationDate']>;
};

export type OcrCacheFormGroup = FormGroup<OcrCacheFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OcrCacheFormService {
  createOcrCacheFormGroup(ocrCache?: OcrCacheFormGroupInput): OcrCacheFormGroup {
    const ocrCacheRawValue = this.convertOcrCacheToOcrCacheRawValue({
      ...this.getFormDefaults(),
      ...(ocrCache ?? { id: null }),
    });
    return new FormGroup<OcrCacheFormGroupContent>({
      id: new FormControl(
        { value: ocrCacheRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentSha256: new FormControl(ocrCacheRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      ocrEngine: new FormControl(ocrCacheRawValue.ocrEngine),
      language: new FormControl(ocrCacheRawValue.language, {
        validators: [Validators.maxLength(10)],
      }),
      pageCount: new FormControl(ocrCacheRawValue.pageCount, {
        validators: [Validators.required],
      }),
      totalConfidence: new FormControl(ocrCacheRawValue.totalConfidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      s3ResultKey: new FormControl(ocrCacheRawValue.s3ResultKey, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      s3Bucket: new FormControl(ocrCacheRawValue.s3Bucket, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      extractedTextS3Key: new FormControl(ocrCacheRawValue.extractedTextS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      metadata: new FormControl(ocrCacheRawValue.metadata),
      hits: new FormControl(ocrCacheRawValue.hits),
      lastAccessDate: new FormControl(ocrCacheRawValue.lastAccessDate),
      createdDate: new FormControl(ocrCacheRawValue.createdDate, {
        validators: [Validators.required],
      }),
      expirationDate: new FormControl(ocrCacheRawValue.expirationDate),
    });
  }

  getOcrCache(form: OcrCacheFormGroup): IOcrCache | NewOcrCache {
    return this.convertOcrCacheRawValueToOcrCache(form.getRawValue() as OcrCacheFormRawValue | NewOcrCacheFormRawValue);
  }

  resetForm(form: OcrCacheFormGroup, ocrCache: OcrCacheFormGroupInput): void {
    const ocrCacheRawValue = this.convertOcrCacheToOcrCacheRawValue({ ...this.getFormDefaults(), ...ocrCache });
    form.reset({
      ...ocrCacheRawValue,
      id: { value: ocrCacheRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OcrCacheFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastAccessDate: currentTime,
      createdDate: currentTime,
      expirationDate: currentTime,
    };
  }

  private convertOcrCacheRawValueToOcrCache(rawOcrCache: OcrCacheFormRawValue | NewOcrCacheFormRawValue): IOcrCache | NewOcrCache {
    return {
      ...rawOcrCache,
      lastAccessDate: dayjs(rawOcrCache.lastAccessDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawOcrCache.createdDate, DATE_TIME_FORMAT),
      expirationDate: dayjs(rawOcrCache.expirationDate, DATE_TIME_FORMAT),
    };
  }

  private convertOcrCacheToOcrCacheRawValue(
    ocrCache: IOcrCache | (Partial<NewOcrCache> & OcrCacheFormDefaults),
  ): OcrCacheFormRawValue | PartialWithRequiredKeyOf<NewOcrCacheFormRawValue> {
    return {
      ...ocrCache,
      lastAccessDate: ocrCache.lastAccessDate ? ocrCache.lastAccessDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: ocrCache.createdDate ? ocrCache.createdDate.format(DATE_TIME_FORMAT) : undefined,
      expirationDate: ocrCache.expirationDate ? ocrCache.expirationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
