import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IImageConversionStatistics, NewImageConversionStatistics } from '../image-conversion-statistics.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImageConversionStatistics for edit and NewImageConversionStatisticsFormGroupInput for create.
 */
type ImageConversionStatisticsFormGroupInput = IImageConversionStatistics | PartialWithRequiredKeyOf<NewImageConversionStatistics>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IImageConversionStatistics | NewImageConversionStatistics> = Omit<T, 'calculatedAt'> & {
  calculatedAt?: string | null;
};

type ImageConversionStatisticsFormRawValue = FormValueOf<IImageConversionStatistics>;

type NewImageConversionStatisticsFormRawValue = FormValueOf<NewImageConversionStatistics>;

type ImageConversionStatisticsFormDefaults = Pick<NewImageConversionStatistics, 'id' | 'calculatedAt'>;

type ImageConversionStatisticsFormGroupContent = {
  id: FormControl<ImageConversionStatisticsFormRawValue['id'] | NewImageConversionStatistics['id']>;
  statisticsDate: FormControl<ImageConversionStatisticsFormRawValue['statisticsDate']>;
  totalConversions: FormControl<ImageConversionStatisticsFormRawValue['totalConversions']>;
  successfulConversions: FormControl<ImageConversionStatisticsFormRawValue['successfulConversions']>;
  failedConversions: FormControl<ImageConversionStatisticsFormRawValue['failedConversions']>;
  totalPagesConverted: FormControl<ImageConversionStatisticsFormRawValue['totalPagesConverted']>;
  totalImagesGenerated: FormControl<ImageConversionStatisticsFormRawValue['totalImagesGenerated']>;
  totalImagesSize: FormControl<ImageConversionStatisticsFormRawValue['totalImagesSize']>;
  averageProcessingDuration: FormControl<ImageConversionStatisticsFormRawValue['averageProcessingDuration']>;
  maxProcessingDuration: FormControl<ImageConversionStatisticsFormRawValue['maxProcessingDuration']>;
  minProcessingDuration: FormControl<ImageConversionStatisticsFormRawValue['minProcessingDuration']>;
  calculatedAt: FormControl<ImageConversionStatisticsFormRawValue['calculatedAt']>;
};

export type ImageConversionStatisticsFormGroup = FormGroup<ImageConversionStatisticsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImageConversionStatisticsFormService {
  createImageConversionStatisticsFormGroup(
    imageConversionStatistics: ImageConversionStatisticsFormGroupInput = { id: null },
  ): ImageConversionStatisticsFormGroup {
    const imageConversionStatisticsRawValue = this.convertImageConversionStatisticsToImageConversionStatisticsRawValue({
      ...this.getFormDefaults(),
      ...imageConversionStatistics,
    });
    return new FormGroup<ImageConversionStatisticsFormGroupContent>({
      id: new FormControl(
        { value: imageConversionStatisticsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      statisticsDate: new FormControl(imageConversionStatisticsRawValue.statisticsDate, {
        validators: [Validators.required],
      }),
      totalConversions: new FormControl(imageConversionStatisticsRawValue.totalConversions, {
        validators: [Validators.min(0)],
      }),
      successfulConversions: new FormControl(imageConversionStatisticsRawValue.successfulConversions, {
        validators: [Validators.min(0)],
      }),
      failedConversions: new FormControl(imageConversionStatisticsRawValue.failedConversions, {
        validators: [Validators.min(0)],
      }),
      totalPagesConverted: new FormControl(imageConversionStatisticsRawValue.totalPagesConverted, {
        validators: [Validators.min(0)],
      }),
      totalImagesGenerated: new FormControl(imageConversionStatisticsRawValue.totalImagesGenerated, {
        validators: [Validators.min(0)],
      }),
      totalImagesSize: new FormControl(imageConversionStatisticsRawValue.totalImagesSize, {
        validators: [Validators.min(0)],
      }),
      averageProcessingDuration: new FormControl(imageConversionStatisticsRawValue.averageProcessingDuration),
      maxProcessingDuration: new FormControl(imageConversionStatisticsRawValue.maxProcessingDuration),
      minProcessingDuration: new FormControl(imageConversionStatisticsRawValue.minProcessingDuration),
      calculatedAt: new FormControl(imageConversionStatisticsRawValue.calculatedAt, {
        validators: [Validators.required],
      }),
    });
  }

  getImageConversionStatistics(form: ImageConversionStatisticsFormGroup): IImageConversionStatistics | NewImageConversionStatistics {
    return this.convertImageConversionStatisticsRawValueToImageConversionStatistics(
      form.getRawValue() as ImageConversionStatisticsFormRawValue | NewImageConversionStatisticsFormRawValue,
    );
  }

  resetForm(form: ImageConversionStatisticsFormGroup, imageConversionStatistics: ImageConversionStatisticsFormGroupInput): void {
    const imageConversionStatisticsRawValue = this.convertImageConversionStatisticsToImageConversionStatisticsRawValue({
      ...this.getFormDefaults(),
      ...imageConversionStatistics,
    });
    form.reset(
      {
        ...imageConversionStatisticsRawValue,
        id: { value: imageConversionStatisticsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ImageConversionStatisticsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      calculatedAt: currentTime,
    };
  }

  private convertImageConversionStatisticsRawValueToImageConversionStatistics(
    rawImageConversionStatistics: ImageConversionStatisticsFormRawValue | NewImageConversionStatisticsFormRawValue,
  ): IImageConversionStatistics | NewImageConversionStatistics {
    return {
      ...rawImageConversionStatistics,
      calculatedAt: dayjs(rawImageConversionStatistics.calculatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertImageConversionStatisticsToImageConversionStatisticsRawValue(
    imageConversionStatistics: IImageConversionStatistics | (Partial<NewImageConversionStatistics> & ImageConversionStatisticsFormDefaults),
  ): ImageConversionStatisticsFormRawValue | PartialWithRequiredKeyOf<NewImageConversionStatisticsFormRawValue> {
    return {
      ...imageConversionStatistics,
      calculatedAt: imageConversionStatistics.calculatedAt ? imageConversionStatistics.calculatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
