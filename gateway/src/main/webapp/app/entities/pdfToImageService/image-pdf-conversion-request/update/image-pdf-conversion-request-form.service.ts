import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IImagePdfConversionRequest, NewImagePdfConversionRequest } from '../image-pdf-conversion-request.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImagePdfConversionRequest for edit and NewImagePdfConversionRequestFormGroupInput for create.
 */
type ImagePdfConversionRequestFormGroupInput = IImagePdfConversionRequest | PartialWithRequiredKeyOf<NewImagePdfConversionRequest>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IImagePdfConversionRequest | NewImagePdfConversionRequest> = Omit<
  T,
  'requestedAt' | 'startedAt' | 'completedAt'
> & {
  requestedAt?: string | null;
  startedAt?: string | null;
  completedAt?: string | null;
};

type ImagePdfConversionRequestFormRawValue = FormValueOf<IImagePdfConversionRequest>;

type NewImagePdfConversionRequestFormRawValue = FormValueOf<NewImagePdfConversionRequest>;

type ImagePdfConversionRequestFormDefaults = Pick<NewImagePdfConversionRequest, 'id' | 'requestedAt' | 'startedAt' | 'completedAt'>;

type ImagePdfConversionRequestFormGroupContent = {
  id: FormControl<ImagePdfConversionRequestFormRawValue['id'] | NewImagePdfConversionRequest['id']>;
  sourceDocumentId: FormControl<ImagePdfConversionRequestFormRawValue['sourceDocumentId']>;
  sourceFileName: FormControl<ImagePdfConversionRequestFormRawValue['sourceFileName']>;
  sourcePdfS3Key: FormControl<ImagePdfConversionRequestFormRawValue['sourcePdfS3Key']>;
  imageQuality: FormControl<ImagePdfConversionRequestFormRawValue['imageQuality']>;
  imageFormat: FormControl<ImagePdfConversionRequestFormRawValue['imageFormat']>;
  conversionType: FormControl<ImagePdfConversionRequestFormRawValue['conversionType']>;
  startPage: FormControl<ImagePdfConversionRequestFormRawValue['startPage']>;
  endPage: FormControl<ImagePdfConversionRequestFormRawValue['endPage']>;
  totalPages: FormControl<ImagePdfConversionRequestFormRawValue['totalPages']>;
  status: FormControl<ImagePdfConversionRequestFormRawValue['status']>;
  errorMessage: FormControl<ImagePdfConversionRequestFormRawValue['errorMessage']>;
  requestedAt: FormControl<ImagePdfConversionRequestFormRawValue['requestedAt']>;
  startedAt: FormControl<ImagePdfConversionRequestFormRawValue['startedAt']>;
  completedAt: FormControl<ImagePdfConversionRequestFormRawValue['completedAt']>;
  processingDuration: FormControl<ImagePdfConversionRequestFormRawValue['processingDuration']>;
  totalImagesSize: FormControl<ImagePdfConversionRequestFormRawValue['totalImagesSize']>;
  imagesGenerated: FormControl<ImagePdfConversionRequestFormRawValue['imagesGenerated']>;
  dpi: FormControl<ImagePdfConversionRequestFormRawValue['dpi']>;
  requestedByUserId: FormControl<ImagePdfConversionRequestFormRawValue['requestedByUserId']>;
  priority: FormControl<ImagePdfConversionRequestFormRawValue['priority']>;
  additionalOptions: FormControl<ImagePdfConversionRequestFormRawValue['additionalOptions']>;
  batch: FormControl<ImagePdfConversionRequestFormRawValue['batch']>;
};

export type ImagePdfConversionRequestFormGroup = FormGroup<ImagePdfConversionRequestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImagePdfConversionRequestFormService {
  createImagePdfConversionRequestFormGroup(
    imagePdfConversionRequest: ImagePdfConversionRequestFormGroupInput = { id: null },
  ): ImagePdfConversionRequestFormGroup {
    const imagePdfConversionRequestRawValue = this.convertImagePdfConversionRequestToImagePdfConversionRequestRawValue({
      ...this.getFormDefaults(),
      ...imagePdfConversionRequest,
    });
    return new FormGroup<ImagePdfConversionRequestFormGroupContent>({
      id: new FormControl(
        { value: imagePdfConversionRequestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sourceDocumentId: new FormControl(imagePdfConversionRequestRawValue.sourceDocumentId, {
        validators: [Validators.required],
      }),
      sourceFileName: new FormControl(imagePdfConversionRequestRawValue.sourceFileName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      sourcePdfS3Key: new FormControl(imagePdfConversionRequestRawValue.sourcePdfS3Key, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      imageQuality: new FormControl(imagePdfConversionRequestRawValue.imageQuality, {
        validators: [Validators.required],
      }),
      imageFormat: new FormControl(imagePdfConversionRequestRawValue.imageFormat, {
        validators: [Validators.required],
      }),
      conversionType: new FormControl(imagePdfConversionRequestRawValue.conversionType, {
        validators: [Validators.required],
      }),
      startPage: new FormControl(imagePdfConversionRequestRawValue.startPage, {
        validators: [Validators.min(1)],
      }),
      endPage: new FormControl(imagePdfConversionRequestRawValue.endPage, {
        validators: [Validators.min(1)],
      }),
      totalPages: new FormControl(imagePdfConversionRequestRawValue.totalPages, {
        validators: [Validators.min(1)],
      }),
      status: new FormControl(imagePdfConversionRequestRawValue.status, {
        validators: [Validators.required],
      }),
      errorMessage: new FormControl(imagePdfConversionRequestRawValue.errorMessage, {
        validators: [Validators.maxLength(2000)],
      }),
      requestedAt: new FormControl(imagePdfConversionRequestRawValue.requestedAt, {
        validators: [Validators.required],
      }),
      startedAt: new FormControl(imagePdfConversionRequestRawValue.startedAt),
      completedAt: new FormControl(imagePdfConversionRequestRawValue.completedAt),
      processingDuration: new FormControl(imagePdfConversionRequestRawValue.processingDuration),
      totalImagesSize: new FormControl(imagePdfConversionRequestRawValue.totalImagesSize),
      imagesGenerated: new FormControl(imagePdfConversionRequestRawValue.imagesGenerated, {
        validators: [Validators.min(0)],
      }),
      dpi: new FormControl(imagePdfConversionRequestRawValue.dpi, {
        validators: [Validators.min(72), Validators.max(1200)],
      }),
      requestedByUserId: new FormControl(imagePdfConversionRequestRawValue.requestedByUserId),
      priority: new FormControl(imagePdfConversionRequestRawValue.priority, {
        validators: [Validators.min(1), Validators.max(5)],
      }),
      additionalOptions: new FormControl(imagePdfConversionRequestRawValue.additionalOptions),
      batch: new FormControl(imagePdfConversionRequestRawValue.batch),
    });
  }

  getImagePdfConversionRequest(form: ImagePdfConversionRequestFormGroup): IImagePdfConversionRequest | NewImagePdfConversionRequest {
    return this.convertImagePdfConversionRequestRawValueToImagePdfConversionRequest(
      form.getRawValue() as ImagePdfConversionRequestFormRawValue | NewImagePdfConversionRequestFormRawValue,
    );
  }

  resetForm(form: ImagePdfConversionRequestFormGroup, imagePdfConversionRequest: ImagePdfConversionRequestFormGroupInput): void {
    const imagePdfConversionRequestRawValue = this.convertImagePdfConversionRequestToImagePdfConversionRequestRawValue({
      ...this.getFormDefaults(),
      ...imagePdfConversionRequest,
    });
    form.reset(
      {
        ...imagePdfConversionRequestRawValue,
        id: { value: imagePdfConversionRequestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ImagePdfConversionRequestFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      requestedAt: currentTime,
      startedAt: currentTime,
      completedAt: currentTime,
    };
  }

  private convertImagePdfConversionRequestRawValueToImagePdfConversionRequest(
    rawImagePdfConversionRequest: ImagePdfConversionRequestFormRawValue | NewImagePdfConversionRequestFormRawValue,
  ): IImagePdfConversionRequest | NewImagePdfConversionRequest {
    return {
      ...rawImagePdfConversionRequest,
      requestedAt: dayjs(rawImagePdfConversionRequest.requestedAt, DATE_TIME_FORMAT),
      startedAt: dayjs(rawImagePdfConversionRequest.startedAt, DATE_TIME_FORMAT),
      completedAt: dayjs(rawImagePdfConversionRequest.completedAt, DATE_TIME_FORMAT),
    };
  }

  private convertImagePdfConversionRequestToImagePdfConversionRequestRawValue(
    imagePdfConversionRequest: IImagePdfConversionRequest | (Partial<NewImagePdfConversionRequest> & ImagePdfConversionRequestFormDefaults),
  ): ImagePdfConversionRequestFormRawValue | PartialWithRequiredKeyOf<NewImagePdfConversionRequestFormRawValue> {
    return {
      ...imagePdfConversionRequest,
      requestedAt: imagePdfConversionRequest.requestedAt ? imagePdfConversionRequest.requestedAt.format(DATE_TIME_FORMAT) : undefined,
      startedAt: imagePdfConversionRequest.startedAt ? imagePdfConversionRequest.startedAt.format(DATE_TIME_FORMAT) : undefined,
      completedAt: imagePdfConversionRequest.completedAt ? imagePdfConversionRequest.completedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
