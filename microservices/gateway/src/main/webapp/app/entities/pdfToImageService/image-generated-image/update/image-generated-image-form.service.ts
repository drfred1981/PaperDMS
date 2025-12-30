import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IImageGeneratedImage, NewImageGeneratedImage } from '../image-generated-image.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImageGeneratedImage for edit and NewImageGeneratedImageFormGroupInput for create.
 */
type ImageGeneratedImageFormGroupInput = IImageGeneratedImage | PartialWithRequiredKeyOf<NewImageGeneratedImage>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IImageGeneratedImage | NewImageGeneratedImage> = Omit<T, 'urlExpiresAt' | 'generatedAt'> & {
  urlExpiresAt?: string | null;
  generatedAt?: string | null;
};

type ImageGeneratedImageFormRawValue = FormValueOf<IImageGeneratedImage>;

type NewImageGeneratedImageFormRawValue = FormValueOf<NewImageGeneratedImage>;

type ImageGeneratedImageFormDefaults = Pick<NewImageGeneratedImage, 'id' | 'urlExpiresAt' | 'generatedAt'>;

type ImageGeneratedImageFormGroupContent = {
  id: FormControl<ImageGeneratedImageFormRawValue['id'] | NewImageGeneratedImage['id']>;
  pageNumber: FormControl<ImageGeneratedImageFormRawValue['pageNumber']>;
  fileName: FormControl<ImageGeneratedImageFormRawValue['fileName']>;
  s3Key: FormControl<ImageGeneratedImageFormRawValue['s3Key']>;
  preSignedUrl: FormControl<ImageGeneratedImageFormRawValue['preSignedUrl']>;
  urlExpiresAt: FormControl<ImageGeneratedImageFormRawValue['urlExpiresAt']>;
  format: FormControl<ImageGeneratedImageFormRawValue['format']>;
  quality: FormControl<ImageGeneratedImageFormRawValue['quality']>;
  width: FormControl<ImageGeneratedImageFormRawValue['width']>;
  height: FormControl<ImageGeneratedImageFormRawValue['height']>;
  fileSize: FormControl<ImageGeneratedImageFormRawValue['fileSize']>;
  dpi: FormControl<ImageGeneratedImageFormRawValue['dpi']>;
  sha256Hash: FormControl<ImageGeneratedImageFormRawValue['sha256Hash']>;
  generatedAt: FormControl<ImageGeneratedImageFormRawValue['generatedAt']>;
  metadata: FormControl<ImageGeneratedImageFormRawValue['metadata']>;
  conversionRequest: FormControl<ImageGeneratedImageFormRawValue['conversionRequest']>;
};

export type ImageGeneratedImageFormGroup = FormGroup<ImageGeneratedImageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImageGeneratedImageFormService {
  createImageGeneratedImageFormGroup(imageGeneratedImage: ImageGeneratedImageFormGroupInput = { id: null }): ImageGeneratedImageFormGroup {
    const imageGeneratedImageRawValue = this.convertImageGeneratedImageToImageGeneratedImageRawValue({
      ...this.getFormDefaults(),
      ...imageGeneratedImage,
    });
    return new FormGroup<ImageGeneratedImageFormGroupContent>({
      id: new FormControl(
        { value: imageGeneratedImageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      pageNumber: new FormControl(imageGeneratedImageRawValue.pageNumber, {
        validators: [Validators.required, Validators.min(1)],
      }),
      fileName: new FormControl(imageGeneratedImageRawValue.fileName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      s3Key: new FormControl(imageGeneratedImageRawValue.s3Key, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      preSignedUrl: new FormControl(imageGeneratedImageRawValue.preSignedUrl, {
        validators: [Validators.maxLength(1000)],
      }),
      urlExpiresAt: new FormControl(imageGeneratedImageRawValue.urlExpiresAt),
      format: new FormControl(imageGeneratedImageRawValue.format, {
        validators: [Validators.required],
      }),
      quality: new FormControl(imageGeneratedImageRawValue.quality, {
        validators: [Validators.required],
      }),
      width: new FormControl(imageGeneratedImageRawValue.width, {
        validators: [Validators.required, Validators.min(1)],
      }),
      height: new FormControl(imageGeneratedImageRawValue.height, {
        validators: [Validators.required, Validators.min(1)],
      }),
      fileSize: new FormControl(imageGeneratedImageRawValue.fileSize, {
        validators: [Validators.required, Validators.min(0)],
      }),
      dpi: new FormControl(imageGeneratedImageRawValue.dpi, {
        validators: [Validators.required, Validators.min(72)],
      }),
      sha256Hash: new FormControl(imageGeneratedImageRawValue.sha256Hash, {
        validators: [Validators.maxLength(64)],
      }),
      generatedAt: new FormControl(imageGeneratedImageRawValue.generatedAt, {
        validators: [Validators.required],
      }),
      metadata: new FormControl(imageGeneratedImageRawValue.metadata),
      conversionRequest: new FormControl(imageGeneratedImageRawValue.conversionRequest, {
        validators: [Validators.required],
      }),
    });
  }

  getImageGeneratedImage(form: ImageGeneratedImageFormGroup): IImageGeneratedImage | NewImageGeneratedImage {
    return this.convertImageGeneratedImageRawValueToImageGeneratedImage(
      form.getRawValue() as ImageGeneratedImageFormRawValue | NewImageGeneratedImageFormRawValue,
    );
  }

  resetForm(form: ImageGeneratedImageFormGroup, imageGeneratedImage: ImageGeneratedImageFormGroupInput): void {
    const imageGeneratedImageRawValue = this.convertImageGeneratedImageToImageGeneratedImageRawValue({
      ...this.getFormDefaults(),
      ...imageGeneratedImage,
    });
    form.reset(
      {
        ...imageGeneratedImageRawValue,
        id: { value: imageGeneratedImageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ImageGeneratedImageFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      urlExpiresAt: currentTime,
      generatedAt: currentTime,
    };
  }

  private convertImageGeneratedImageRawValueToImageGeneratedImage(
    rawImageGeneratedImage: ImageGeneratedImageFormRawValue | NewImageGeneratedImageFormRawValue,
  ): IImageGeneratedImage | NewImageGeneratedImage {
    return {
      ...rawImageGeneratedImage,
      urlExpiresAt: dayjs(rawImageGeneratedImage.urlExpiresAt, DATE_TIME_FORMAT),
      generatedAt: dayjs(rawImageGeneratedImage.generatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertImageGeneratedImageToImageGeneratedImageRawValue(
    imageGeneratedImage: IImageGeneratedImage | (Partial<NewImageGeneratedImage> & ImageGeneratedImageFormDefaults),
  ): ImageGeneratedImageFormRawValue | PartialWithRequiredKeyOf<NewImageGeneratedImageFormRawValue> {
    return {
      ...imageGeneratedImage,
      urlExpiresAt: imageGeneratedImage.urlExpiresAt ? imageGeneratedImage.urlExpiresAt.format(DATE_TIME_FORMAT) : undefined,
      generatedAt: imageGeneratedImage.generatedAt ? imageGeneratedImage.generatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
