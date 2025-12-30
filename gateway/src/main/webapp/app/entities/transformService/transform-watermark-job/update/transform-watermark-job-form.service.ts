import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransformWatermarkJob, NewTransformWatermarkJob } from '../transform-watermark-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransformWatermarkJob for edit and NewTransformWatermarkJobFormGroupInput for create.
 */
type TransformWatermarkJobFormGroupInput = ITransformWatermarkJob | PartialWithRequiredKeyOf<NewTransformWatermarkJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransformWatermarkJob | NewTransformWatermarkJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type TransformWatermarkJobFormRawValue = FormValueOf<ITransformWatermarkJob>;

type NewTransformWatermarkJobFormRawValue = FormValueOf<NewTransformWatermarkJob>;

type TransformWatermarkJobFormDefaults = Pick<NewTransformWatermarkJob, 'id' | 'tiled' | 'startDate' | 'endDate' | 'createdDate'>;

type TransformWatermarkJobFormGroupContent = {
  id: FormControl<TransformWatermarkJobFormRawValue['id'] | NewTransformWatermarkJob['id']>;
  documentSha256: FormControl<TransformWatermarkJobFormRawValue['documentSha256']>;
  watermarkType: FormControl<TransformWatermarkJobFormRawValue['watermarkType']>;
  watermarkText: FormControl<TransformWatermarkJobFormRawValue['watermarkText']>;
  watermarkImageS3Key: FormControl<TransformWatermarkJobFormRawValue['watermarkImageS3Key']>;
  position: FormControl<TransformWatermarkJobFormRawValue['position']>;
  opacity: FormControl<TransformWatermarkJobFormRawValue['opacity']>;
  fontSize: FormControl<TransformWatermarkJobFormRawValue['fontSize']>;
  color: FormControl<TransformWatermarkJobFormRawValue['color']>;
  rotation: FormControl<TransformWatermarkJobFormRawValue['rotation']>;
  tiled: FormControl<TransformWatermarkJobFormRawValue['tiled']>;
  outputS3Key: FormControl<TransformWatermarkJobFormRawValue['outputS3Key']>;
  outputDocumentSha256: FormControl<TransformWatermarkJobFormRawValue['outputDocumentSha256']>;
  status: FormControl<TransformWatermarkJobFormRawValue['status']>;
  startDate: FormControl<TransformWatermarkJobFormRawValue['startDate']>;
  endDate: FormControl<TransformWatermarkJobFormRawValue['endDate']>;
  errorMessage: FormControl<TransformWatermarkJobFormRawValue['errorMessage']>;
  createdBy: FormControl<TransformWatermarkJobFormRawValue['createdBy']>;
  createdDate: FormControl<TransformWatermarkJobFormRawValue['createdDate']>;
};

export type TransformWatermarkJobFormGroup = FormGroup<TransformWatermarkJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransformWatermarkJobFormService {
  createTransformWatermarkJobFormGroup(
    transformWatermarkJob: TransformWatermarkJobFormGroupInput = { id: null },
  ): TransformWatermarkJobFormGroup {
    const transformWatermarkJobRawValue = this.convertTransformWatermarkJobToTransformWatermarkJobRawValue({
      ...this.getFormDefaults(),
      ...transformWatermarkJob,
    });
    return new FormGroup<TransformWatermarkJobFormGroupContent>({
      id: new FormControl(
        { value: transformWatermarkJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentSha256: new FormControl(transformWatermarkJobRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      watermarkType: new FormControl(transformWatermarkJobRawValue.watermarkType, {
        validators: [Validators.required],
      }),
      watermarkText: new FormControl(transformWatermarkJobRawValue.watermarkText, {
        validators: [Validators.maxLength(500)],
      }),
      watermarkImageS3Key: new FormControl(transformWatermarkJobRawValue.watermarkImageS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      position: new FormControl(transformWatermarkJobRawValue.position, {
        validators: [Validators.required],
      }),
      opacity: new FormControl(transformWatermarkJobRawValue.opacity, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      fontSize: new FormControl(transformWatermarkJobRawValue.fontSize),
      color: new FormControl(transformWatermarkJobRawValue.color, {
        validators: [Validators.maxLength(7)],
      }),
      rotation: new FormControl(transformWatermarkJobRawValue.rotation),
      tiled: new FormControl(transformWatermarkJobRawValue.tiled),
      outputS3Key: new FormControl(transformWatermarkJobRawValue.outputS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      outputDocumentSha256: new FormControl(transformWatermarkJobRawValue.outputDocumentSha256, {
        validators: [Validators.maxLength(64)],
      }),
      status: new FormControl(transformWatermarkJobRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(transformWatermarkJobRawValue.startDate),
      endDate: new FormControl(transformWatermarkJobRawValue.endDate),
      errorMessage: new FormControl(transformWatermarkJobRawValue.errorMessage),
      createdBy: new FormControl(transformWatermarkJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(transformWatermarkJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getTransformWatermarkJob(form: TransformWatermarkJobFormGroup): ITransformWatermarkJob | NewTransformWatermarkJob {
    return this.convertTransformWatermarkJobRawValueToTransformWatermarkJob(
      form.getRawValue() as TransformWatermarkJobFormRawValue | NewTransformWatermarkJobFormRawValue,
    );
  }

  resetForm(form: TransformWatermarkJobFormGroup, transformWatermarkJob: TransformWatermarkJobFormGroupInput): void {
    const transformWatermarkJobRawValue = this.convertTransformWatermarkJobToTransformWatermarkJobRawValue({
      ...this.getFormDefaults(),
      ...transformWatermarkJob,
    });
    form.reset(
      {
        ...transformWatermarkJobRawValue,
        id: { value: transformWatermarkJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransformWatermarkJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      tiled: false,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertTransformWatermarkJobRawValueToTransformWatermarkJob(
    rawTransformWatermarkJob: TransformWatermarkJobFormRawValue | NewTransformWatermarkJobFormRawValue,
  ): ITransformWatermarkJob | NewTransformWatermarkJob {
    return {
      ...rawTransformWatermarkJob,
      startDate: dayjs(rawTransformWatermarkJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawTransformWatermarkJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawTransformWatermarkJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertTransformWatermarkJobToTransformWatermarkJobRawValue(
    transformWatermarkJob: ITransformWatermarkJob | (Partial<NewTransformWatermarkJob> & TransformWatermarkJobFormDefaults),
  ): TransformWatermarkJobFormRawValue | PartialWithRequiredKeyOf<NewTransformWatermarkJobFormRawValue> {
    return {
      ...transformWatermarkJob,
      startDate: transformWatermarkJob.startDate ? transformWatermarkJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: transformWatermarkJob.endDate ? transformWatermarkJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: transformWatermarkJob.createdDate ? transformWatermarkJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
