import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOcrJob, NewOcrJob } from '../ocr-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOcrJob for edit and NewOcrJobFormGroupInput for create.
 */
type OcrJobFormGroupInput = IOcrJob | PartialWithRequiredKeyOf<NewOcrJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOcrJob | NewOcrJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type OcrJobFormRawValue = FormValueOf<IOcrJob>;

type NewOcrJobFormRawValue = FormValueOf<NewOcrJob>;

type OcrJobFormDefaults = Pick<NewOcrJob, 'id' | 'isCached' | 'startDate' | 'endDate' | 'createdDate'>;

type OcrJobFormGroupContent = {
  id: FormControl<OcrJobFormRawValue['id'] | NewOcrJob['id']>;
  status: FormControl<OcrJobFormRawValue['status']>;
  documentId: FormControl<OcrJobFormRawValue['documentId']>;
  documentSha256: FormControl<OcrJobFormRawValue['documentSha256']>;
  s3Key: FormControl<OcrJobFormRawValue['s3Key']>;
  s3Bucket: FormControl<OcrJobFormRawValue['s3Bucket']>;
  requestedLanguage: FormControl<OcrJobFormRawValue['requestedLanguage']>;
  detectedLanguage: FormControl<OcrJobFormRawValue['detectedLanguage']>;
  languageConfidence: FormControl<OcrJobFormRawValue['languageConfidence']>;
  ocrEngine: FormControl<OcrJobFormRawValue['ocrEngine']>;
  tikaEndpoint: FormControl<OcrJobFormRawValue['tikaEndpoint']>;
  aiProvider: FormControl<OcrJobFormRawValue['aiProvider']>;
  aiModel: FormControl<OcrJobFormRawValue['aiModel']>;
  resultCacheKey: FormControl<OcrJobFormRawValue['resultCacheKey']>;
  isCached: FormControl<OcrJobFormRawValue['isCached']>;
  startDate: FormControl<OcrJobFormRawValue['startDate']>;
  endDate: FormControl<OcrJobFormRawValue['endDate']>;
  errorMessage: FormControl<OcrJobFormRawValue['errorMessage']>;
  pageCount: FormControl<OcrJobFormRawValue['pageCount']>;
  progress: FormControl<OcrJobFormRawValue['progress']>;
  retryCount: FormControl<OcrJobFormRawValue['retryCount']>;
  priority: FormControl<OcrJobFormRawValue['priority']>;
  processingTime: FormControl<OcrJobFormRawValue['processingTime']>;
  costEstimate: FormControl<OcrJobFormRawValue['costEstimate']>;
  createdDate: FormControl<OcrJobFormRawValue['createdDate']>;
  createdBy: FormControl<OcrJobFormRawValue['createdBy']>;
};

export type OcrJobFormGroup = FormGroup<OcrJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OcrJobFormService {
  createOcrJobFormGroup(ocrJob?: OcrJobFormGroupInput): OcrJobFormGroup {
    const ocrJobRawValue = this.convertOcrJobToOcrJobRawValue({
      ...this.getFormDefaults(),
      ...(ocrJob ?? { id: null }),
    });
    return new FormGroup<OcrJobFormGroupContent>({
      id: new FormControl(
        { value: ocrJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(ocrJobRawValue.status, {
        validators: [Validators.required],
      }),
      documentId: new FormControl(ocrJobRawValue.documentId, {
        validators: [Validators.required],
      }),
      documentSha256: new FormControl(ocrJobRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      s3Key: new FormControl(ocrJobRawValue.s3Key, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      s3Bucket: new FormControl(ocrJobRawValue.s3Bucket, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      requestedLanguage: new FormControl(ocrJobRawValue.requestedLanguage, {
        validators: [Validators.maxLength(10)],
      }),
      detectedLanguage: new FormControl(ocrJobRawValue.detectedLanguage, {
        validators: [Validators.maxLength(10)],
      }),
      languageConfidence: new FormControl(ocrJobRawValue.languageConfidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      ocrEngine: new FormControl(ocrJobRawValue.ocrEngine),
      tikaEndpoint: new FormControl(ocrJobRawValue.tikaEndpoint, {
        validators: [Validators.maxLength(500)],
      }),
      aiProvider: new FormControl(ocrJobRawValue.aiProvider, {
        validators: [Validators.maxLength(100)],
      }),
      aiModel: new FormControl(ocrJobRawValue.aiModel, {
        validators: [Validators.maxLength(100)],
      }),
      resultCacheKey: new FormControl(ocrJobRawValue.resultCacheKey, {
        validators: [Validators.maxLength(128)],
      }),
      isCached: new FormControl(ocrJobRawValue.isCached, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(ocrJobRawValue.startDate),
      endDate: new FormControl(ocrJobRawValue.endDate),
      errorMessage: new FormControl(ocrJobRawValue.errorMessage),
      pageCount: new FormControl(ocrJobRawValue.pageCount),
      progress: new FormControl(ocrJobRawValue.progress, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      retryCount: new FormControl(ocrJobRawValue.retryCount),
      priority: new FormControl(ocrJobRawValue.priority),
      processingTime: new FormControl(ocrJobRawValue.processingTime),
      costEstimate: new FormControl(ocrJobRawValue.costEstimate),
      createdDate: new FormControl(ocrJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(ocrJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
    });
  }

  getOcrJob(form: OcrJobFormGroup): IOcrJob | NewOcrJob {
    return this.convertOcrJobRawValueToOcrJob(form.getRawValue() as OcrJobFormRawValue | NewOcrJobFormRawValue);
  }

  resetForm(form: OcrJobFormGroup, ocrJob: OcrJobFormGroupInput): void {
    const ocrJobRawValue = this.convertOcrJobToOcrJobRawValue({ ...this.getFormDefaults(), ...ocrJob });
    form.reset({
      ...ocrJobRawValue,
      id: { value: ocrJobRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OcrJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isCached: false,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertOcrJobRawValueToOcrJob(rawOcrJob: OcrJobFormRawValue | NewOcrJobFormRawValue): IOcrJob | NewOcrJob {
    return {
      ...rawOcrJob,
      startDate: dayjs(rawOcrJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawOcrJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawOcrJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertOcrJobToOcrJobRawValue(
    ocrJob: IOcrJob | (Partial<NewOcrJob> & OcrJobFormDefaults),
  ): OcrJobFormRawValue | PartialWithRequiredKeyOf<NewOcrJobFormRawValue> {
    return {
      ...ocrJob,
      startDate: ocrJob.startDate ? ocrJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: ocrJob.endDate ? ocrJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: ocrJob.createdDate ? ocrJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
