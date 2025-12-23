import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICompressionJob, NewCompressionJob } from '../compression-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompressionJob for edit and NewCompressionJobFormGroupInput for create.
 */
type CompressionJobFormGroupInput = ICompressionJob | PartialWithRequiredKeyOf<NewCompressionJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICompressionJob | NewCompressionJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type CompressionJobFormRawValue = FormValueOf<ICompressionJob>;

type NewCompressionJobFormRawValue = FormValueOf<NewCompressionJob>;

type CompressionJobFormDefaults = Pick<NewCompressionJob, 'id' | 'startDate' | 'endDate' | 'createdDate'>;

type CompressionJobFormGroupContent = {
  id: FormControl<CompressionJobFormRawValue['id'] | NewCompressionJob['id']>;
  documentId: FormControl<CompressionJobFormRawValue['documentId']>;
  compressionType: FormControl<CompressionJobFormRawValue['compressionType']>;
  quality: FormControl<CompressionJobFormRawValue['quality']>;
  targetSizeKb: FormControl<CompressionJobFormRawValue['targetSizeKb']>;
  originalSize: FormControl<CompressionJobFormRawValue['originalSize']>;
  compressedSize: FormControl<CompressionJobFormRawValue['compressedSize']>;
  compressionRatio: FormControl<CompressionJobFormRawValue['compressionRatio']>;
  outputS3Key: FormControl<CompressionJobFormRawValue['outputS3Key']>;
  outputDocumentId: FormControl<CompressionJobFormRawValue['outputDocumentId']>;
  status: FormControl<CompressionJobFormRawValue['status']>;
  startDate: FormControl<CompressionJobFormRawValue['startDate']>;
  endDate: FormControl<CompressionJobFormRawValue['endDate']>;
  errorMessage: FormControl<CompressionJobFormRawValue['errorMessage']>;
  createdBy: FormControl<CompressionJobFormRawValue['createdBy']>;
  createdDate: FormControl<CompressionJobFormRawValue['createdDate']>;
};

export type CompressionJobFormGroup = FormGroup<CompressionJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompressionJobFormService {
  createCompressionJobFormGroup(compressionJob?: CompressionJobFormGroupInput): CompressionJobFormGroup {
    const compressionJobRawValue = this.convertCompressionJobToCompressionJobRawValue({
      ...this.getFormDefaults(),
      ...(compressionJob ?? { id: null }),
    });
    return new FormGroup<CompressionJobFormGroupContent>({
      id: new FormControl(
        { value: compressionJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(compressionJobRawValue.documentId, {
        validators: [Validators.required],
      }),
      compressionType: new FormControl(compressionJobRawValue.compressionType, {
        validators: [Validators.required],
      }),
      quality: new FormControl(compressionJobRawValue.quality, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      targetSizeKb: new FormControl(compressionJobRawValue.targetSizeKb),
      originalSize: new FormControl(compressionJobRawValue.originalSize),
      compressedSize: new FormControl(compressionJobRawValue.compressedSize),
      compressionRatio: new FormControl(compressionJobRawValue.compressionRatio),
      outputS3Key: new FormControl(compressionJobRawValue.outputS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      outputDocumentId: new FormControl(compressionJobRawValue.outputDocumentId),
      status: new FormControl(compressionJobRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(compressionJobRawValue.startDate),
      endDate: new FormControl(compressionJobRawValue.endDate),
      errorMessage: new FormControl(compressionJobRawValue.errorMessage),
      createdBy: new FormControl(compressionJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(compressionJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getCompressionJob(form: CompressionJobFormGroup): ICompressionJob | NewCompressionJob {
    return this.convertCompressionJobRawValueToCompressionJob(
      form.getRawValue() as CompressionJobFormRawValue | NewCompressionJobFormRawValue,
    );
  }

  resetForm(form: CompressionJobFormGroup, compressionJob: CompressionJobFormGroupInput): void {
    const compressionJobRawValue = this.convertCompressionJobToCompressionJobRawValue({ ...this.getFormDefaults(), ...compressionJob });
    form.reset({
      ...compressionJobRawValue,
      id: { value: compressionJobRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CompressionJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertCompressionJobRawValueToCompressionJob(
    rawCompressionJob: CompressionJobFormRawValue | NewCompressionJobFormRawValue,
  ): ICompressionJob | NewCompressionJob {
    return {
      ...rawCompressionJob,
      startDate: dayjs(rawCompressionJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawCompressionJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawCompressionJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertCompressionJobToCompressionJobRawValue(
    compressionJob: ICompressionJob | (Partial<NewCompressionJob> & CompressionJobFormDefaults),
  ): CompressionJobFormRawValue | PartialWithRequiredKeyOf<NewCompressionJobFormRawValue> {
    return {
      ...compressionJob,
      startDate: compressionJob.startDate ? compressionJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: compressionJob.endDate ? compressionJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: compressionJob.createdDate ? compressionJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
