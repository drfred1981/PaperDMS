import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransformConversionJob, NewTransformConversionJob } from '../transform-conversion-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransformConversionJob for edit and NewTransformConversionJobFormGroupInput for create.
 */
type TransformConversionJobFormGroupInput = ITransformConversionJob | PartialWithRequiredKeyOf<NewTransformConversionJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransformConversionJob | NewTransformConversionJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type TransformConversionJobFormRawValue = FormValueOf<ITransformConversionJob>;

type NewTransformConversionJobFormRawValue = FormValueOf<NewTransformConversionJob>;

type TransformConversionJobFormDefaults = Pick<NewTransformConversionJob, 'id' | 'startDate' | 'endDate' | 'createdDate'>;

type TransformConversionJobFormGroupContent = {
  id: FormControl<TransformConversionJobFormRawValue['id'] | NewTransformConversionJob['id']>;
  documentSha256: FormControl<TransformConversionJobFormRawValue['documentSha256']>;
  sourceFormat: FormControl<TransformConversionJobFormRawValue['sourceFormat']>;
  targetFormat: FormControl<TransformConversionJobFormRawValue['targetFormat']>;
  conversionEngine: FormControl<TransformConversionJobFormRawValue['conversionEngine']>;
  options: FormControl<TransformConversionJobFormRawValue['options']>;
  outputS3Key: FormControl<TransformConversionJobFormRawValue['outputS3Key']>;
  outputDocumentSha256: FormControl<TransformConversionJobFormRawValue['outputDocumentSha256']>;
  status: FormControl<TransformConversionJobFormRawValue['status']>;
  startDate: FormControl<TransformConversionJobFormRawValue['startDate']>;
  endDate: FormControl<TransformConversionJobFormRawValue['endDate']>;
  errorMessage: FormControl<TransformConversionJobFormRawValue['errorMessage']>;
  createdBy: FormControl<TransformConversionJobFormRawValue['createdBy']>;
  createdDate: FormControl<TransformConversionJobFormRawValue['createdDate']>;
};

export type TransformConversionJobFormGroup = FormGroup<TransformConversionJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransformConversionJobFormService {
  createTransformConversionJobFormGroup(
    transformConversionJob: TransformConversionJobFormGroupInput = { id: null },
  ): TransformConversionJobFormGroup {
    const transformConversionJobRawValue = this.convertTransformConversionJobToTransformConversionJobRawValue({
      ...this.getFormDefaults(),
      ...transformConversionJob,
    });
    return new FormGroup<TransformConversionJobFormGroupContent>({
      id: new FormControl(
        { value: transformConversionJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentSha256: new FormControl(transformConversionJobRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      sourceFormat: new FormControl(transformConversionJobRawValue.sourceFormat, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      targetFormat: new FormControl(transformConversionJobRawValue.targetFormat, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      conversionEngine: new FormControl(transformConversionJobRawValue.conversionEngine, {
        validators: [Validators.maxLength(100)],
      }),
      options: new FormControl(transformConversionJobRawValue.options),
      outputS3Key: new FormControl(transformConversionJobRawValue.outputS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      outputDocumentSha256: new FormControl(transformConversionJobRawValue.outputDocumentSha256, {
        validators: [Validators.maxLength(64)],
      }),
      status: new FormControl(transformConversionJobRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(transformConversionJobRawValue.startDate),
      endDate: new FormControl(transformConversionJobRawValue.endDate),
      errorMessage: new FormControl(transformConversionJobRawValue.errorMessage),
      createdBy: new FormControl(transformConversionJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(transformConversionJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getTransformConversionJob(form: TransformConversionJobFormGroup): ITransformConversionJob | NewTransformConversionJob {
    return this.convertTransformConversionJobRawValueToTransformConversionJob(
      form.getRawValue() as TransformConversionJobFormRawValue | NewTransformConversionJobFormRawValue,
    );
  }

  resetForm(form: TransformConversionJobFormGroup, transformConversionJob: TransformConversionJobFormGroupInput): void {
    const transformConversionJobRawValue = this.convertTransformConversionJobToTransformConversionJobRawValue({
      ...this.getFormDefaults(),
      ...transformConversionJob,
    });
    form.reset(
      {
        ...transformConversionJobRawValue,
        id: { value: transformConversionJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransformConversionJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertTransformConversionJobRawValueToTransformConversionJob(
    rawTransformConversionJob: TransformConversionJobFormRawValue | NewTransformConversionJobFormRawValue,
  ): ITransformConversionJob | NewTransformConversionJob {
    return {
      ...rawTransformConversionJob,
      startDate: dayjs(rawTransformConversionJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawTransformConversionJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawTransformConversionJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertTransformConversionJobToTransformConversionJobRawValue(
    transformConversionJob: ITransformConversionJob | (Partial<NewTransformConversionJob> & TransformConversionJobFormDefaults),
  ): TransformConversionJobFormRawValue | PartialWithRequiredKeyOf<NewTransformConversionJobFormRawValue> {
    return {
      ...transformConversionJob,
      startDate: transformConversionJob.startDate ? transformConversionJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: transformConversionJob.endDate ? transformConversionJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: transformConversionJob.createdDate ? transformConversionJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
