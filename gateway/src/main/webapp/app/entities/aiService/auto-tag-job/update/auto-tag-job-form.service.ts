import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAutoTagJob, NewAutoTagJob } from '../auto-tag-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAutoTagJob for edit and NewAutoTagJobFormGroupInput for create.
 */
type AutoTagJobFormGroupInput = IAutoTagJob | PartialWithRequiredKeyOf<NewAutoTagJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAutoTagJob | NewAutoTagJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type AutoTagJobFormRawValue = FormValueOf<IAutoTagJob>;

type NewAutoTagJobFormRawValue = FormValueOf<NewAutoTagJob>;

type AutoTagJobFormDefaults = Pick<NewAutoTagJob, 'id' | 'isCached' | 'startDate' | 'endDate' | 'createdDate'>;

type AutoTagJobFormGroupContent = {
  id: FormControl<AutoTagJobFormRawValue['id'] | NewAutoTagJob['id']>;
  documentId: FormControl<AutoTagJobFormRawValue['documentId']>;
  documentSha256: FormControl<AutoTagJobFormRawValue['documentSha256']>;
  s3Key: FormControl<AutoTagJobFormRawValue['s3Key']>;
  extractedText: FormControl<AutoTagJobFormRawValue['extractedText']>;
  extractedTextSha256: FormControl<AutoTagJobFormRawValue['extractedTextSha256']>;
  detectedLanguage: FormControl<AutoTagJobFormRawValue['detectedLanguage']>;
  languageConfidence: FormControl<AutoTagJobFormRawValue['languageConfidence']>;
  status: FormControl<AutoTagJobFormRawValue['status']>;
  modelVersion: FormControl<AutoTagJobFormRawValue['modelVersion']>;
  resultCacheKey: FormControl<AutoTagJobFormRawValue['resultCacheKey']>;
  isCached: FormControl<AutoTagJobFormRawValue['isCached']>;
  startDate: FormControl<AutoTagJobFormRawValue['startDate']>;
  endDate: FormControl<AutoTagJobFormRawValue['endDate']>;
  errorMessage: FormControl<AutoTagJobFormRawValue['errorMessage']>;
  confidence: FormControl<AutoTagJobFormRawValue['confidence']>;
  createdDate: FormControl<AutoTagJobFormRawValue['createdDate']>;
};

export type AutoTagJobFormGroup = FormGroup<AutoTagJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AutoTagJobFormService {
  createAutoTagJobFormGroup(autoTagJob?: AutoTagJobFormGroupInput): AutoTagJobFormGroup {
    const autoTagJobRawValue = this.convertAutoTagJobToAutoTagJobRawValue({
      ...this.getFormDefaults(),
      ...(autoTagJob ?? { id: null }),
    });
    return new FormGroup<AutoTagJobFormGroupContent>({
      id: new FormControl(
        { value: autoTagJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(autoTagJobRawValue.documentId, {
        validators: [Validators.required],
      }),
      documentSha256: new FormControl(autoTagJobRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      s3Key: new FormControl(autoTagJobRawValue.s3Key, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      extractedText: new FormControl(autoTagJobRawValue.extractedText),
      extractedTextSha256: new FormControl(autoTagJobRawValue.extractedTextSha256, {
        validators: [Validators.maxLength(64)],
      }),
      detectedLanguage: new FormControl(autoTagJobRawValue.detectedLanguage, {
        validators: [Validators.maxLength(10)],
      }),
      languageConfidence: new FormControl(autoTagJobRawValue.languageConfidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      status: new FormControl(autoTagJobRawValue.status),
      modelVersion: new FormControl(autoTagJobRawValue.modelVersion, {
        validators: [Validators.maxLength(50)],
      }),
      resultCacheKey: new FormControl(autoTagJobRawValue.resultCacheKey, {
        validators: [Validators.maxLength(128)],
      }),
      isCached: new FormControl(autoTagJobRawValue.isCached, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(autoTagJobRawValue.startDate),
      endDate: new FormControl(autoTagJobRawValue.endDate),
      errorMessage: new FormControl(autoTagJobRawValue.errorMessage),
      confidence: new FormControl(autoTagJobRawValue.confidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      createdDate: new FormControl(autoTagJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getAutoTagJob(form: AutoTagJobFormGroup): IAutoTagJob | NewAutoTagJob {
    return this.convertAutoTagJobRawValueToAutoTagJob(form.getRawValue() as AutoTagJobFormRawValue | NewAutoTagJobFormRawValue);
  }

  resetForm(form: AutoTagJobFormGroup, autoTagJob: AutoTagJobFormGroupInput): void {
    const autoTagJobRawValue = this.convertAutoTagJobToAutoTagJobRawValue({ ...this.getFormDefaults(), ...autoTagJob });
    form.reset({
      ...autoTagJobRawValue,
      id: { value: autoTagJobRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): AutoTagJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isCached: false,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertAutoTagJobRawValueToAutoTagJob(
    rawAutoTagJob: AutoTagJobFormRawValue | NewAutoTagJobFormRawValue,
  ): IAutoTagJob | NewAutoTagJob {
    return {
      ...rawAutoTagJob,
      startDate: dayjs(rawAutoTagJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawAutoTagJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawAutoTagJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertAutoTagJobToAutoTagJobRawValue(
    autoTagJob: IAutoTagJob | (Partial<NewAutoTagJob> & AutoTagJobFormDefaults),
  ): AutoTagJobFormRawValue | PartialWithRequiredKeyOf<NewAutoTagJobFormRawValue> {
    return {
      ...autoTagJob,
      startDate: autoTagJob.startDate ? autoTagJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: autoTagJob.endDate ? autoTagJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: autoTagJob.createdDate ? autoTagJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
