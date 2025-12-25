import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWatermarkJob, NewWatermarkJob } from '../watermark-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWatermarkJob for edit and NewWatermarkJobFormGroupInput for create.
 */
type WatermarkJobFormGroupInput = IWatermarkJob | PartialWithRequiredKeyOf<NewWatermarkJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWatermarkJob | NewWatermarkJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type WatermarkJobFormRawValue = FormValueOf<IWatermarkJob>;

type NewWatermarkJobFormRawValue = FormValueOf<NewWatermarkJob>;

type WatermarkJobFormDefaults = Pick<NewWatermarkJob, 'id' | 'tiled' | 'startDate' | 'endDate' | 'createdDate'>;

type WatermarkJobFormGroupContent = {
  id: FormControl<WatermarkJobFormRawValue['id'] | NewWatermarkJob['id']>;
  documentId: FormControl<WatermarkJobFormRawValue['documentId']>;
  watermarkType: FormControl<WatermarkJobFormRawValue['watermarkType']>;
  watermarkText: FormControl<WatermarkJobFormRawValue['watermarkText']>;
  watermarkImageS3Key: FormControl<WatermarkJobFormRawValue['watermarkImageS3Key']>;
  position: FormControl<WatermarkJobFormRawValue['position']>;
  opacity: FormControl<WatermarkJobFormRawValue['opacity']>;
  fontSize: FormControl<WatermarkJobFormRawValue['fontSize']>;
  color: FormControl<WatermarkJobFormRawValue['color']>;
  rotation: FormControl<WatermarkJobFormRawValue['rotation']>;
  tiled: FormControl<WatermarkJobFormRawValue['tiled']>;
  outputS3Key: FormControl<WatermarkJobFormRawValue['outputS3Key']>;
  outputDocumentId: FormControl<WatermarkJobFormRawValue['outputDocumentId']>;
  status: FormControl<WatermarkJobFormRawValue['status']>;
  startDate: FormControl<WatermarkJobFormRawValue['startDate']>;
  endDate: FormControl<WatermarkJobFormRawValue['endDate']>;
  errorMessage: FormControl<WatermarkJobFormRawValue['errorMessage']>;
  createdBy: FormControl<WatermarkJobFormRawValue['createdBy']>;
  createdDate: FormControl<WatermarkJobFormRawValue['createdDate']>;
};

export type WatermarkJobFormGroup = FormGroup<WatermarkJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WatermarkJobFormService {
  createWatermarkJobFormGroup(watermarkJob: WatermarkJobFormGroupInput = { id: null }): WatermarkJobFormGroup {
    const watermarkJobRawValue = this.convertWatermarkJobToWatermarkJobRawValue({
      ...this.getFormDefaults(),
      ...watermarkJob,
    });
    return new FormGroup<WatermarkJobFormGroupContent>({
      id: new FormControl(
        { value: watermarkJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(watermarkJobRawValue.documentId, {
        validators: [Validators.required],
      }),
      watermarkType: new FormControl(watermarkJobRawValue.watermarkType, {
        validators: [Validators.required],
      }),
      watermarkText: new FormControl(watermarkJobRawValue.watermarkText, {
        validators: [Validators.maxLength(500)],
      }),
      watermarkImageS3Key: new FormControl(watermarkJobRawValue.watermarkImageS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      position: new FormControl(watermarkJobRawValue.position, {
        validators: [Validators.required],
      }),
      opacity: new FormControl(watermarkJobRawValue.opacity, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      fontSize: new FormControl(watermarkJobRawValue.fontSize),
      color: new FormControl(watermarkJobRawValue.color, {
        validators: [Validators.maxLength(7)],
      }),
      rotation: new FormControl(watermarkJobRawValue.rotation),
      tiled: new FormControl(watermarkJobRawValue.tiled),
      outputS3Key: new FormControl(watermarkJobRawValue.outputS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      outputDocumentId: new FormControl(watermarkJobRawValue.outputDocumentId),
      status: new FormControl(watermarkJobRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(watermarkJobRawValue.startDate),
      endDate: new FormControl(watermarkJobRawValue.endDate),
      errorMessage: new FormControl(watermarkJobRawValue.errorMessage),
      createdBy: new FormControl(watermarkJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(watermarkJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getWatermarkJob(form: WatermarkJobFormGroup): IWatermarkJob | NewWatermarkJob {
    return this.convertWatermarkJobRawValueToWatermarkJob(form.getRawValue() as WatermarkJobFormRawValue | NewWatermarkJobFormRawValue);
  }

  resetForm(form: WatermarkJobFormGroup, watermarkJob: WatermarkJobFormGroupInput): void {
    const watermarkJobRawValue = this.convertWatermarkJobToWatermarkJobRawValue({ ...this.getFormDefaults(), ...watermarkJob });
    form.reset(
      {
        ...watermarkJobRawValue,
        id: { value: watermarkJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WatermarkJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      tiled: false,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertWatermarkJobRawValueToWatermarkJob(
    rawWatermarkJob: WatermarkJobFormRawValue | NewWatermarkJobFormRawValue,
  ): IWatermarkJob | NewWatermarkJob {
    return {
      ...rawWatermarkJob,
      startDate: dayjs(rawWatermarkJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawWatermarkJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawWatermarkJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertWatermarkJobToWatermarkJobRawValue(
    watermarkJob: IWatermarkJob | (Partial<NewWatermarkJob> & WatermarkJobFormDefaults),
  ): WatermarkJobFormRawValue | PartialWithRequiredKeyOf<NewWatermarkJobFormRawValue> {
    return {
      ...watermarkJob,
      startDate: watermarkJob.startDate ? watermarkJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: watermarkJob.endDate ? watermarkJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: watermarkJob.createdDate ? watermarkJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
