import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConversionJob, NewConversionJob } from '../conversion-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConversionJob for edit and NewConversionJobFormGroupInput for create.
 */
type ConversionJobFormGroupInput = IConversionJob | PartialWithRequiredKeyOf<NewConversionJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IConversionJob | NewConversionJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type ConversionJobFormRawValue = FormValueOf<IConversionJob>;

type NewConversionJobFormRawValue = FormValueOf<NewConversionJob>;

type ConversionJobFormDefaults = Pick<NewConversionJob, 'id' | 'startDate' | 'endDate' | 'createdDate'>;

type ConversionJobFormGroupContent = {
  id: FormControl<ConversionJobFormRawValue['id'] | NewConversionJob['id']>;
  documentId: FormControl<ConversionJobFormRawValue['documentId']>;
  documentSha256: FormControl<ConversionJobFormRawValue['documentSha256']>;
  sourceFormat: FormControl<ConversionJobFormRawValue['sourceFormat']>;
  targetFormat: FormControl<ConversionJobFormRawValue['targetFormat']>;
  conversionEngine: FormControl<ConversionJobFormRawValue['conversionEngine']>;
  options: FormControl<ConversionJobFormRawValue['options']>;
  outputS3Key: FormControl<ConversionJobFormRawValue['outputS3Key']>;
  outputDocumentId: FormControl<ConversionJobFormRawValue['outputDocumentId']>;
  status: FormControl<ConversionJobFormRawValue['status']>;
  startDate: FormControl<ConversionJobFormRawValue['startDate']>;
  endDate: FormControl<ConversionJobFormRawValue['endDate']>;
  errorMessage: FormControl<ConversionJobFormRawValue['errorMessage']>;
  createdBy: FormControl<ConversionJobFormRawValue['createdBy']>;
  createdDate: FormControl<ConversionJobFormRawValue['createdDate']>;
};

export type ConversionJobFormGroup = FormGroup<ConversionJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConversionJobFormService {
  createConversionJobFormGroup(conversionJob: ConversionJobFormGroupInput = { id: null }): ConversionJobFormGroup {
    const conversionJobRawValue = this.convertConversionJobToConversionJobRawValue({
      ...this.getFormDefaults(),
      ...conversionJob,
    });
    return new FormGroup<ConversionJobFormGroupContent>({
      id: new FormControl(
        { value: conversionJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(conversionJobRawValue.documentId, {
        validators: [Validators.required],
      }),
      documentSha256: new FormControl(conversionJobRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      sourceFormat: new FormControl(conversionJobRawValue.sourceFormat, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      targetFormat: new FormControl(conversionJobRawValue.targetFormat, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      conversionEngine: new FormControl(conversionJobRawValue.conversionEngine, {
        validators: [Validators.maxLength(100)],
      }),
      options: new FormControl(conversionJobRawValue.options),
      outputS3Key: new FormControl(conversionJobRawValue.outputS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      outputDocumentId: new FormControl(conversionJobRawValue.outputDocumentId),
      status: new FormControl(conversionJobRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(conversionJobRawValue.startDate),
      endDate: new FormControl(conversionJobRawValue.endDate),
      errorMessage: new FormControl(conversionJobRawValue.errorMessage),
      createdBy: new FormControl(conversionJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(conversionJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getConversionJob(form: ConversionJobFormGroup): IConversionJob | NewConversionJob {
    return this.convertConversionJobRawValueToConversionJob(form.getRawValue() as ConversionJobFormRawValue | NewConversionJobFormRawValue);
  }

  resetForm(form: ConversionJobFormGroup, conversionJob: ConversionJobFormGroupInput): void {
    const conversionJobRawValue = this.convertConversionJobToConversionJobRawValue({ ...this.getFormDefaults(), ...conversionJob });
    form.reset(
      {
        ...conversionJobRawValue,
        id: { value: conversionJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConversionJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertConversionJobRawValueToConversionJob(
    rawConversionJob: ConversionJobFormRawValue | NewConversionJobFormRawValue,
  ): IConversionJob | NewConversionJob {
    return {
      ...rawConversionJob,
      startDate: dayjs(rawConversionJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawConversionJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawConversionJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertConversionJobToConversionJobRawValue(
    conversionJob: IConversionJob | (Partial<NewConversionJob> & ConversionJobFormDefaults),
  ): ConversionJobFormRawValue | PartialWithRequiredKeyOf<NewConversionJobFormRawValue> {
    return {
      ...conversionJob,
      startDate: conversionJob.startDate ? conversionJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: conversionJob.endDate ? conversionJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: conversionJob.createdDate ? conversionJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
