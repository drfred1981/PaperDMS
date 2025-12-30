import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransformCompressionJob, NewTransformCompressionJob } from '../transform-compression-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransformCompressionJob for edit and NewTransformCompressionJobFormGroupInput for create.
 */
type TransformCompressionJobFormGroupInput = ITransformCompressionJob | PartialWithRequiredKeyOf<NewTransformCompressionJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransformCompressionJob | NewTransformCompressionJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type TransformCompressionJobFormRawValue = FormValueOf<ITransformCompressionJob>;

type NewTransformCompressionJobFormRawValue = FormValueOf<NewTransformCompressionJob>;

type TransformCompressionJobFormDefaults = Pick<NewTransformCompressionJob, 'id' | 'startDate' | 'endDate' | 'createdDate'>;

type TransformCompressionJobFormGroupContent = {
  id: FormControl<TransformCompressionJobFormRawValue['id'] | NewTransformCompressionJob['id']>;
  documentSha256: FormControl<TransformCompressionJobFormRawValue['documentSha256']>;
  compressionType: FormControl<TransformCompressionJobFormRawValue['compressionType']>;
  quality: FormControl<TransformCompressionJobFormRawValue['quality']>;
  targetSizeKb: FormControl<TransformCompressionJobFormRawValue['targetSizeKb']>;
  originalSize: FormControl<TransformCompressionJobFormRawValue['originalSize']>;
  compressedSize: FormControl<TransformCompressionJobFormRawValue['compressedSize']>;
  compressionRatio: FormControl<TransformCompressionJobFormRawValue['compressionRatio']>;
  outputS3Key: FormControl<TransformCompressionJobFormRawValue['outputS3Key']>;
  outputDocumentSha256: FormControl<TransformCompressionJobFormRawValue['outputDocumentSha256']>;
  status: FormControl<TransformCompressionJobFormRawValue['status']>;
  startDate: FormControl<TransformCompressionJobFormRawValue['startDate']>;
  endDate: FormControl<TransformCompressionJobFormRawValue['endDate']>;
  errorMessage: FormControl<TransformCompressionJobFormRawValue['errorMessage']>;
  createdBy: FormControl<TransformCompressionJobFormRawValue['createdBy']>;
  createdDate: FormControl<TransformCompressionJobFormRawValue['createdDate']>;
};

export type TransformCompressionJobFormGroup = FormGroup<TransformCompressionJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransformCompressionJobFormService {
  createTransformCompressionJobFormGroup(
    transformCompressionJob: TransformCompressionJobFormGroupInput = { id: null },
  ): TransformCompressionJobFormGroup {
    const transformCompressionJobRawValue = this.convertTransformCompressionJobToTransformCompressionJobRawValue({
      ...this.getFormDefaults(),
      ...transformCompressionJob,
    });
    return new FormGroup<TransformCompressionJobFormGroupContent>({
      id: new FormControl(
        { value: transformCompressionJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentSha256: new FormControl(transformCompressionJobRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      compressionType: new FormControl(transformCompressionJobRawValue.compressionType, {
        validators: [Validators.required],
      }),
      quality: new FormControl(transformCompressionJobRawValue.quality, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      targetSizeKb: new FormControl(transformCompressionJobRawValue.targetSizeKb),
      originalSize: new FormControl(transformCompressionJobRawValue.originalSize),
      compressedSize: new FormControl(transformCompressionJobRawValue.compressedSize),
      compressionRatio: new FormControl(transformCompressionJobRawValue.compressionRatio),
      outputS3Key: new FormControl(transformCompressionJobRawValue.outputS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      outputDocumentSha256: new FormControl(transformCompressionJobRawValue.outputDocumentSha256, {
        validators: [Validators.maxLength(64)],
      }),
      status: new FormControl(transformCompressionJobRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(transformCompressionJobRawValue.startDate),
      endDate: new FormControl(transformCompressionJobRawValue.endDate),
      errorMessage: new FormControl(transformCompressionJobRawValue.errorMessage),
      createdBy: new FormControl(transformCompressionJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(transformCompressionJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getTransformCompressionJob(form: TransformCompressionJobFormGroup): ITransformCompressionJob | NewTransformCompressionJob {
    return this.convertTransformCompressionJobRawValueToTransformCompressionJob(
      form.getRawValue() as TransformCompressionJobFormRawValue | NewTransformCompressionJobFormRawValue,
    );
  }

  resetForm(form: TransformCompressionJobFormGroup, transformCompressionJob: TransformCompressionJobFormGroupInput): void {
    const transformCompressionJobRawValue = this.convertTransformCompressionJobToTransformCompressionJobRawValue({
      ...this.getFormDefaults(),
      ...transformCompressionJob,
    });
    form.reset(
      {
        ...transformCompressionJobRawValue,
        id: { value: transformCompressionJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransformCompressionJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertTransformCompressionJobRawValueToTransformCompressionJob(
    rawTransformCompressionJob: TransformCompressionJobFormRawValue | NewTransformCompressionJobFormRawValue,
  ): ITransformCompressionJob | NewTransformCompressionJob {
    return {
      ...rawTransformCompressionJob,
      startDate: dayjs(rawTransformCompressionJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawTransformCompressionJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawTransformCompressionJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertTransformCompressionJobToTransformCompressionJobRawValue(
    transformCompressionJob: ITransformCompressionJob | (Partial<NewTransformCompressionJob> & TransformCompressionJobFormDefaults),
  ): TransformCompressionJobFormRawValue | PartialWithRequiredKeyOf<NewTransformCompressionJobFormRawValue> {
    return {
      ...transformCompressionJob,
      startDate: transformCompressionJob.startDate ? transformCompressionJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: transformCompressionJob.endDate ? transformCompressionJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: transformCompressionJob.createdDate ? transformCompressionJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
