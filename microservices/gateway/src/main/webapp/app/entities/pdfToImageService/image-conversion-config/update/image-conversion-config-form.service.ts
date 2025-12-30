import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IImageConversionConfig, NewImageConversionConfig } from '../image-conversion-config.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImageConversionConfig for edit and NewImageConversionConfigFormGroupInput for create.
 */
type ImageConversionConfigFormGroupInput = IImageConversionConfig | PartialWithRequiredKeyOf<NewImageConversionConfig>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IImageConversionConfig | NewImageConversionConfig> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type ImageConversionConfigFormRawValue = FormValueOf<IImageConversionConfig>;

type NewImageConversionConfigFormRawValue = FormValueOf<NewImageConversionConfig>;

type ImageConversionConfigFormDefaults = Pick<NewImageConversionConfig, 'id' | 'isActive' | 'isDefault' | 'createdAt' | 'updatedAt'>;

type ImageConversionConfigFormGroupContent = {
  id: FormControl<ImageConversionConfigFormRawValue['id'] | NewImageConversionConfig['id']>;
  configName: FormControl<ImageConversionConfigFormRawValue['configName']>;
  description: FormControl<ImageConversionConfigFormRawValue['description']>;
  defaultQuality: FormControl<ImageConversionConfigFormRawValue['defaultQuality']>;
  defaultFormat: FormControl<ImageConversionConfigFormRawValue['defaultFormat']>;
  defaultDpi: FormControl<ImageConversionConfigFormRawValue['defaultDpi']>;
  defaultConversionType: FormControl<ImageConversionConfigFormRawValue['defaultConversionType']>;
  defaultPriority: FormControl<ImageConversionConfigFormRawValue['defaultPriority']>;
  isActive: FormControl<ImageConversionConfigFormRawValue['isActive']>;
  isDefault: FormControl<ImageConversionConfigFormRawValue['isDefault']>;
  createdAt: FormControl<ImageConversionConfigFormRawValue['createdAt']>;
  updatedAt: FormControl<ImageConversionConfigFormRawValue['updatedAt']>;
};

export type ImageConversionConfigFormGroup = FormGroup<ImageConversionConfigFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImageConversionConfigFormService {
  createImageConversionConfigFormGroup(
    imageConversionConfig: ImageConversionConfigFormGroupInput = { id: null },
  ): ImageConversionConfigFormGroup {
    const imageConversionConfigRawValue = this.convertImageConversionConfigToImageConversionConfigRawValue({
      ...this.getFormDefaults(),
      ...imageConversionConfig,
    });
    return new FormGroup<ImageConversionConfigFormGroupContent>({
      id: new FormControl(
        { value: imageConversionConfigRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      configName: new FormControl(imageConversionConfigRawValue.configName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      description: new FormControl(imageConversionConfigRawValue.description, {
        validators: [Validators.maxLength(500)],
      }),
      defaultQuality: new FormControl(imageConversionConfigRawValue.defaultQuality, {
        validators: [Validators.required],
      }),
      defaultFormat: new FormControl(imageConversionConfigRawValue.defaultFormat, {
        validators: [Validators.required],
      }),
      defaultDpi: new FormControl(imageConversionConfigRawValue.defaultDpi, {
        validators: [Validators.required, Validators.min(72), Validators.max(1200)],
      }),
      defaultConversionType: new FormControl(imageConversionConfigRawValue.defaultConversionType, {
        validators: [Validators.required],
      }),
      defaultPriority: new FormControl(imageConversionConfigRawValue.defaultPriority, {
        validators: [Validators.min(1), Validators.max(5)],
      }),
      isActive: new FormControl(imageConversionConfigRawValue.isActive, {
        validators: [Validators.required],
      }),
      isDefault: new FormControl(imageConversionConfigRawValue.isDefault, {
        validators: [Validators.required],
      }),
      createdAt: new FormControl(imageConversionConfigRawValue.createdAt, {
        validators: [Validators.required],
      }),
      updatedAt: new FormControl(imageConversionConfigRawValue.updatedAt),
    });
  }

  getImageConversionConfig(form: ImageConversionConfigFormGroup): IImageConversionConfig | NewImageConversionConfig {
    return this.convertImageConversionConfigRawValueToImageConversionConfig(
      form.getRawValue() as ImageConversionConfigFormRawValue | NewImageConversionConfigFormRawValue,
    );
  }

  resetForm(form: ImageConversionConfigFormGroup, imageConversionConfig: ImageConversionConfigFormGroupInput): void {
    const imageConversionConfigRawValue = this.convertImageConversionConfigToImageConversionConfigRawValue({
      ...this.getFormDefaults(),
      ...imageConversionConfig,
    });
    form.reset(
      {
        ...imageConversionConfigRawValue,
        id: { value: imageConversionConfigRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ImageConversionConfigFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      isDefault: false,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertImageConversionConfigRawValueToImageConversionConfig(
    rawImageConversionConfig: ImageConversionConfigFormRawValue | NewImageConversionConfigFormRawValue,
  ): IImageConversionConfig | NewImageConversionConfig {
    return {
      ...rawImageConversionConfig,
      createdAt: dayjs(rawImageConversionConfig.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawImageConversionConfig.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertImageConversionConfigToImageConversionConfigRawValue(
    imageConversionConfig: IImageConversionConfig | (Partial<NewImageConversionConfig> & ImageConversionConfigFormDefaults),
  ): ImageConversionConfigFormRawValue | PartialWithRequiredKeyOf<NewImageConversionConfigFormRawValue> {
    return {
      ...imageConversionConfig,
      createdAt: imageConversionConfig.createdAt ? imageConversionConfig.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: imageConversionConfig.updatedAt ? imageConversionConfig.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
