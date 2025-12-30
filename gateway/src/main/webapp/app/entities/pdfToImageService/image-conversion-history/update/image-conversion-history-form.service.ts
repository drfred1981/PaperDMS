import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IImageConversionHistory, NewImageConversionHistory } from '../image-conversion-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImageConversionHistory for edit and NewImageConversionHistoryFormGroupInput for create.
 */
type ImageConversionHistoryFormGroupInput = IImageConversionHistory | PartialWithRequiredKeyOf<NewImageConversionHistory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IImageConversionHistory | NewImageConversionHistory> = Omit<T, 'archivedAt'> & {
  archivedAt?: string | null;
};

type ImageConversionHistoryFormRawValue = FormValueOf<IImageConversionHistory>;

type NewImageConversionHistoryFormRawValue = FormValueOf<NewImageConversionHistory>;

type ImageConversionHistoryFormDefaults = Pick<NewImageConversionHistory, 'id' | 'archivedAt'>;

type ImageConversionHistoryFormGroupContent = {
  id: FormControl<ImageConversionHistoryFormRawValue['id'] | NewImageConversionHistory['id']>;
  originalRequestId: FormControl<ImageConversionHistoryFormRawValue['originalRequestId']>;
  archivedAt: FormControl<ImageConversionHistoryFormRawValue['archivedAt']>;
  conversionData: FormControl<ImageConversionHistoryFormRawValue['conversionData']>;
  imagesCount: FormControl<ImageConversionHistoryFormRawValue['imagesCount']>;
  totalSize: FormControl<ImageConversionHistoryFormRawValue['totalSize']>;
  finalStatus: FormControl<ImageConversionHistoryFormRawValue['finalStatus']>;
  processingDuration: FormControl<ImageConversionHistoryFormRawValue['processingDuration']>;
};

export type ImageConversionHistoryFormGroup = FormGroup<ImageConversionHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImageConversionHistoryFormService {
  createImageConversionHistoryFormGroup(
    imageConversionHistory: ImageConversionHistoryFormGroupInput = { id: null },
  ): ImageConversionHistoryFormGroup {
    const imageConversionHistoryRawValue = this.convertImageConversionHistoryToImageConversionHistoryRawValue({
      ...this.getFormDefaults(),
      ...imageConversionHistory,
    });
    return new FormGroup<ImageConversionHistoryFormGroupContent>({
      id: new FormControl(
        { value: imageConversionHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      originalRequestId: new FormControl(imageConversionHistoryRawValue.originalRequestId, {
        validators: [Validators.required],
      }),
      archivedAt: new FormControl(imageConversionHistoryRawValue.archivedAt, {
        validators: [Validators.required],
      }),
      conversionData: new FormControl(imageConversionHistoryRawValue.conversionData, {
        validators: [Validators.required],
      }),
      imagesCount: new FormControl(imageConversionHistoryRawValue.imagesCount, {
        validators: [Validators.min(0)],
      }),
      totalSize: new FormControl(imageConversionHistoryRawValue.totalSize, {
        validators: [Validators.min(0)],
      }),
      finalStatus: new FormControl(imageConversionHistoryRawValue.finalStatus, {
        validators: [Validators.required],
      }),
      processingDuration: new FormControl(imageConversionHistoryRawValue.processingDuration),
    });
  }

  getImageConversionHistory(form: ImageConversionHistoryFormGroup): IImageConversionHistory | NewImageConversionHistory {
    return this.convertImageConversionHistoryRawValueToImageConversionHistory(
      form.getRawValue() as ImageConversionHistoryFormRawValue | NewImageConversionHistoryFormRawValue,
    );
  }

  resetForm(form: ImageConversionHistoryFormGroup, imageConversionHistory: ImageConversionHistoryFormGroupInput): void {
    const imageConversionHistoryRawValue = this.convertImageConversionHistoryToImageConversionHistoryRawValue({
      ...this.getFormDefaults(),
      ...imageConversionHistory,
    });
    form.reset(
      {
        ...imageConversionHistoryRawValue,
        id: { value: imageConversionHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ImageConversionHistoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      archivedAt: currentTime,
    };
  }

  private convertImageConversionHistoryRawValueToImageConversionHistory(
    rawImageConversionHistory: ImageConversionHistoryFormRawValue | NewImageConversionHistoryFormRawValue,
  ): IImageConversionHistory | NewImageConversionHistory {
    return {
      ...rawImageConversionHistory,
      archivedAt: dayjs(rawImageConversionHistory.archivedAt, DATE_TIME_FORMAT),
    };
  }

  private convertImageConversionHistoryToImageConversionHistoryRawValue(
    imageConversionHistory: IImageConversionHistory | (Partial<NewImageConversionHistory> & ImageConversionHistoryFormDefaults),
  ): ImageConversionHistoryFormRawValue | PartialWithRequiredKeyOf<NewImageConversionHistoryFormRawValue> {
    return {
      ...imageConversionHistory,
      archivedAt: imageConversionHistory.archivedAt ? imageConversionHistory.archivedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
