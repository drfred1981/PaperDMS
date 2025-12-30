import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAIAutoTagJob, NewAIAutoTagJob } from '../ai-auto-tag-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAIAutoTagJob for edit and NewAIAutoTagJobFormGroupInput for create.
 */
type AIAutoTagJobFormGroupInput = IAIAutoTagJob | PartialWithRequiredKeyOf<NewAIAutoTagJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAIAutoTagJob | NewAIAutoTagJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type AIAutoTagJobFormRawValue = FormValueOf<IAIAutoTagJob>;

type NewAIAutoTagJobFormRawValue = FormValueOf<NewAIAutoTagJob>;

type AIAutoTagJobFormDefaults = Pick<NewAIAutoTagJob, 'id' | 'isCached' | 'startDate' | 'endDate' | 'createdDate'>;

type AIAutoTagJobFormGroupContent = {
  id: FormControl<AIAutoTagJobFormRawValue['id'] | NewAIAutoTagJob['id']>;
  documentSha256: FormControl<AIAutoTagJobFormRawValue['documentSha256']>;
  s3Key: FormControl<AIAutoTagJobFormRawValue['s3Key']>;
  extractedText: FormControl<AIAutoTagJobFormRawValue['extractedText']>;
  extractedTextSha256: FormControl<AIAutoTagJobFormRawValue['extractedTextSha256']>;
  status: FormControl<AIAutoTagJobFormRawValue['status']>;
  modelVersion: FormControl<AIAutoTagJobFormRawValue['modelVersion']>;
  resultCacheKey: FormControl<AIAutoTagJobFormRawValue['resultCacheKey']>;
  isCached: FormControl<AIAutoTagJobFormRawValue['isCached']>;
  startDate: FormControl<AIAutoTagJobFormRawValue['startDate']>;
  endDate: FormControl<AIAutoTagJobFormRawValue['endDate']>;
  errorMessage: FormControl<AIAutoTagJobFormRawValue['errorMessage']>;
  createdDate: FormControl<AIAutoTagJobFormRawValue['createdDate']>;
  aITypePrediction: FormControl<AIAutoTagJobFormRawValue['aITypePrediction']>;
  languagePrediction: FormControl<AIAutoTagJobFormRawValue['languagePrediction']>;
};

export type AIAutoTagJobFormGroup = FormGroup<AIAutoTagJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AIAutoTagJobFormService {
  createAIAutoTagJobFormGroup(aIAutoTagJob: AIAutoTagJobFormGroupInput = { id: null }): AIAutoTagJobFormGroup {
    const aIAutoTagJobRawValue = this.convertAIAutoTagJobToAIAutoTagJobRawValue({
      ...this.getFormDefaults(),
      ...aIAutoTagJob,
    });
    return new FormGroup<AIAutoTagJobFormGroupContent>({
      id: new FormControl(
        { value: aIAutoTagJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentSha256: new FormControl(aIAutoTagJobRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      s3Key: new FormControl(aIAutoTagJobRawValue.s3Key, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      extractedText: new FormControl(aIAutoTagJobRawValue.extractedText),
      extractedTextSha256: new FormControl(aIAutoTagJobRawValue.extractedTextSha256, {
        validators: [Validators.maxLength(64)],
      }),
      status: new FormControl(aIAutoTagJobRawValue.status),
      modelVersion: new FormControl(aIAutoTagJobRawValue.modelVersion, {
        validators: [Validators.maxLength(50)],
      }),
      resultCacheKey: new FormControl(aIAutoTagJobRawValue.resultCacheKey, {
        validators: [Validators.maxLength(128)],
      }),
      isCached: new FormControl(aIAutoTagJobRawValue.isCached, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(aIAutoTagJobRawValue.startDate),
      endDate: new FormControl(aIAutoTagJobRawValue.endDate),
      errorMessage: new FormControl(aIAutoTagJobRawValue.errorMessage),
      createdDate: new FormControl(aIAutoTagJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
      aITypePrediction: new FormControl(aIAutoTagJobRawValue.aITypePrediction),
      languagePrediction: new FormControl(aIAutoTagJobRawValue.languagePrediction),
    });
  }

  getAIAutoTagJob(form: AIAutoTagJobFormGroup): IAIAutoTagJob | NewAIAutoTagJob {
    return this.convertAIAutoTagJobRawValueToAIAutoTagJob(form.getRawValue() as AIAutoTagJobFormRawValue | NewAIAutoTagJobFormRawValue);
  }

  resetForm(form: AIAutoTagJobFormGroup, aIAutoTagJob: AIAutoTagJobFormGroupInput): void {
    const aIAutoTagJobRawValue = this.convertAIAutoTagJobToAIAutoTagJobRawValue({ ...this.getFormDefaults(), ...aIAutoTagJob });
    form.reset(
      {
        ...aIAutoTagJobRawValue,
        id: { value: aIAutoTagJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AIAutoTagJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isCached: false,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertAIAutoTagJobRawValueToAIAutoTagJob(
    rawAIAutoTagJob: AIAutoTagJobFormRawValue | NewAIAutoTagJobFormRawValue,
  ): IAIAutoTagJob | NewAIAutoTagJob {
    return {
      ...rawAIAutoTagJob,
      startDate: dayjs(rawAIAutoTagJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawAIAutoTagJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawAIAutoTagJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertAIAutoTagJobToAIAutoTagJobRawValue(
    aIAutoTagJob: IAIAutoTagJob | (Partial<NewAIAutoTagJob> & AIAutoTagJobFormDefaults),
  ): AIAutoTagJobFormRawValue | PartialWithRequiredKeyOf<NewAIAutoTagJobFormRawValue> {
    return {
      ...aIAutoTagJob,
      startDate: aIAutoTagJob.startDate ? aIAutoTagJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: aIAutoTagJob.endDate ? aIAutoTagJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: aIAutoTagJob.createdDate ? aIAutoTagJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
