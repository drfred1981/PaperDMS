import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IImageConversionBatch, NewImageConversionBatch } from '../image-conversion-batch.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImageConversionBatch for edit and NewImageConversionBatchFormGroupInput for create.
 */
type ImageConversionBatchFormGroupInput = IImageConversionBatch | PartialWithRequiredKeyOf<NewImageConversionBatch>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IImageConversionBatch | NewImageConversionBatch> = Omit<T, 'createdAt' | 'startedAt' | 'completedAt'> & {
  createdAt?: string | null;
  startedAt?: string | null;
  completedAt?: string | null;
};

type ImageConversionBatchFormRawValue = FormValueOf<IImageConversionBatch>;

type NewImageConversionBatchFormRawValue = FormValueOf<NewImageConversionBatch>;

type ImageConversionBatchFormDefaults = Pick<NewImageConversionBatch, 'id' | 'createdAt' | 'startedAt' | 'completedAt'>;

type ImageConversionBatchFormGroupContent = {
  id: FormControl<ImageConversionBatchFormRawValue['id'] | NewImageConversionBatch['id']>;
  batchName: FormControl<ImageConversionBatchFormRawValue['batchName']>;
  description: FormControl<ImageConversionBatchFormRawValue['description']>;
  createdAt: FormControl<ImageConversionBatchFormRawValue['createdAt']>;
  status: FormControl<ImageConversionBatchFormRawValue['status']>;
  totalConversions: FormControl<ImageConversionBatchFormRawValue['totalConversions']>;
  completedConversions: FormControl<ImageConversionBatchFormRawValue['completedConversions']>;
  failedConversions: FormControl<ImageConversionBatchFormRawValue['failedConversions']>;
  startedAt: FormControl<ImageConversionBatchFormRawValue['startedAt']>;
  completedAt: FormControl<ImageConversionBatchFormRawValue['completedAt']>;
  totalProcessingDuration: FormControl<ImageConversionBatchFormRawValue['totalProcessingDuration']>;
  createdByUserId: FormControl<ImageConversionBatchFormRawValue['createdByUserId']>;
};

export type ImageConversionBatchFormGroup = FormGroup<ImageConversionBatchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImageConversionBatchFormService {
  createImageConversionBatchFormGroup(
    imageConversionBatch: ImageConversionBatchFormGroupInput = { id: null },
  ): ImageConversionBatchFormGroup {
    const imageConversionBatchRawValue = this.convertImageConversionBatchToImageConversionBatchRawValue({
      ...this.getFormDefaults(),
      ...imageConversionBatch,
    });
    return new FormGroup<ImageConversionBatchFormGroupContent>({
      id: new FormControl(
        { value: imageConversionBatchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      batchName: new FormControl(imageConversionBatchRawValue.batchName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(imageConversionBatchRawValue.description, {
        validators: [Validators.maxLength(1000)],
      }),
      createdAt: new FormControl(imageConversionBatchRawValue.createdAt, {
        validators: [Validators.required],
      }),
      status: new FormControl(imageConversionBatchRawValue.status, {
        validators: [Validators.required],
      }),
      totalConversions: new FormControl(imageConversionBatchRawValue.totalConversions, {
        validators: [Validators.min(0)],
      }),
      completedConversions: new FormControl(imageConversionBatchRawValue.completedConversions, {
        validators: [Validators.min(0)],
      }),
      failedConversions: new FormControl(imageConversionBatchRawValue.failedConversions, {
        validators: [Validators.min(0)],
      }),
      startedAt: new FormControl(imageConversionBatchRawValue.startedAt),
      completedAt: new FormControl(imageConversionBatchRawValue.completedAt),
      totalProcessingDuration: new FormControl(imageConversionBatchRawValue.totalProcessingDuration),
      createdByUserId: new FormControl(imageConversionBatchRawValue.createdByUserId),
    });
  }

  getImageConversionBatch(form: ImageConversionBatchFormGroup): IImageConversionBatch | NewImageConversionBatch {
    return this.convertImageConversionBatchRawValueToImageConversionBatch(
      form.getRawValue() as ImageConversionBatchFormRawValue | NewImageConversionBatchFormRawValue,
    );
  }

  resetForm(form: ImageConversionBatchFormGroup, imageConversionBatch: ImageConversionBatchFormGroupInput): void {
    const imageConversionBatchRawValue = this.convertImageConversionBatchToImageConversionBatchRawValue({
      ...this.getFormDefaults(),
      ...imageConversionBatch,
    });
    form.reset(
      {
        ...imageConversionBatchRawValue,
        id: { value: imageConversionBatchRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ImageConversionBatchFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      startedAt: currentTime,
      completedAt: currentTime,
    };
  }

  private convertImageConversionBatchRawValueToImageConversionBatch(
    rawImageConversionBatch: ImageConversionBatchFormRawValue | NewImageConversionBatchFormRawValue,
  ): IImageConversionBatch | NewImageConversionBatch {
    return {
      ...rawImageConversionBatch,
      createdAt: dayjs(rawImageConversionBatch.createdAt, DATE_TIME_FORMAT),
      startedAt: dayjs(rawImageConversionBatch.startedAt, DATE_TIME_FORMAT),
      completedAt: dayjs(rawImageConversionBatch.completedAt, DATE_TIME_FORMAT),
    };
  }

  private convertImageConversionBatchToImageConversionBatchRawValue(
    imageConversionBatch: IImageConversionBatch | (Partial<NewImageConversionBatch> & ImageConversionBatchFormDefaults),
  ): ImageConversionBatchFormRawValue | PartialWithRequiredKeyOf<NewImageConversionBatchFormRawValue> {
    return {
      ...imageConversionBatch,
      createdAt: imageConversionBatch.createdAt ? imageConversionBatch.createdAt.format(DATE_TIME_FORMAT) : undefined,
      startedAt: imageConversionBatch.startedAt ? imageConversionBatch.startedAt.format(DATE_TIME_FORMAT) : undefined,
      completedAt: imageConversionBatch.completedAt ? imageConversionBatch.completedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
