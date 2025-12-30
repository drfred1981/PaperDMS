import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransformRedactionJob, NewTransformRedactionJob } from '../transform-redaction-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransformRedactionJob for edit and NewTransformRedactionJobFormGroupInput for create.
 */
type TransformRedactionJobFormGroupInput = ITransformRedactionJob | PartialWithRequiredKeyOf<NewTransformRedactionJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransformRedactionJob | NewTransformRedactionJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type TransformRedactionJobFormRawValue = FormValueOf<ITransformRedactionJob>;

type NewTransformRedactionJobFormRawValue = FormValueOf<NewTransformRedactionJob>;

type TransformRedactionJobFormDefaults = Pick<NewTransformRedactionJob, 'id' | 'startDate' | 'endDate' | 'createdDate'>;

type TransformRedactionJobFormGroupContent = {
  id: FormControl<TransformRedactionJobFormRawValue['id'] | NewTransformRedactionJob['id']>;
  documentSha256: FormControl<TransformRedactionJobFormRawValue['documentSha256']>;
  redactionAreas: FormControl<TransformRedactionJobFormRawValue['redactionAreas']>;
  redactionType: FormControl<TransformRedactionJobFormRawValue['redactionType']>;
  redactionColor: FormControl<TransformRedactionJobFormRawValue['redactionColor']>;
  replaceWith: FormControl<TransformRedactionJobFormRawValue['replaceWith']>;
  outputS3Key: FormControl<TransformRedactionJobFormRawValue['outputS3Key']>;
  outputDocumentSha256: FormControl<TransformRedactionJobFormRawValue['outputDocumentSha256']>;
  status: FormControl<TransformRedactionJobFormRawValue['status']>;
  startDate: FormControl<TransformRedactionJobFormRawValue['startDate']>;
  endDate: FormControl<TransformRedactionJobFormRawValue['endDate']>;
  errorMessage: FormControl<TransformRedactionJobFormRawValue['errorMessage']>;
  createdBy: FormControl<TransformRedactionJobFormRawValue['createdBy']>;
  createdDate: FormControl<TransformRedactionJobFormRawValue['createdDate']>;
};

export type TransformRedactionJobFormGroup = FormGroup<TransformRedactionJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransformRedactionJobFormService {
  createTransformRedactionJobFormGroup(
    transformRedactionJob: TransformRedactionJobFormGroupInput = { id: null },
  ): TransformRedactionJobFormGroup {
    const transformRedactionJobRawValue = this.convertTransformRedactionJobToTransformRedactionJobRawValue({
      ...this.getFormDefaults(),
      ...transformRedactionJob,
    });
    return new FormGroup<TransformRedactionJobFormGroupContent>({
      id: new FormControl(
        { value: transformRedactionJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentSha256: new FormControl(transformRedactionJobRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      redactionAreas: new FormControl(transformRedactionJobRawValue.redactionAreas, {
        validators: [Validators.required],
      }),
      redactionType: new FormControl(transformRedactionJobRawValue.redactionType, {
        validators: [Validators.required],
      }),
      redactionColor: new FormControl(transformRedactionJobRawValue.redactionColor, {
        validators: [Validators.maxLength(7)],
      }),
      replaceWith: new FormControl(transformRedactionJobRawValue.replaceWith, {
        validators: [Validators.maxLength(500)],
      }),
      outputS3Key: new FormControl(transformRedactionJobRawValue.outputS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      outputDocumentSha256: new FormControl(transformRedactionJobRawValue.outputDocumentSha256, {
        validators: [Validators.maxLength(64)],
      }),
      status: new FormControl(transformRedactionJobRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(transformRedactionJobRawValue.startDate),
      endDate: new FormControl(transformRedactionJobRawValue.endDate),
      errorMessage: new FormControl(transformRedactionJobRawValue.errorMessage),
      createdBy: new FormControl(transformRedactionJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(transformRedactionJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getTransformRedactionJob(form: TransformRedactionJobFormGroup): ITransformRedactionJob | NewTransformRedactionJob {
    return this.convertTransformRedactionJobRawValueToTransformRedactionJob(
      form.getRawValue() as TransformRedactionJobFormRawValue | NewTransformRedactionJobFormRawValue,
    );
  }

  resetForm(form: TransformRedactionJobFormGroup, transformRedactionJob: TransformRedactionJobFormGroupInput): void {
    const transformRedactionJobRawValue = this.convertTransformRedactionJobToTransformRedactionJobRawValue({
      ...this.getFormDefaults(),
      ...transformRedactionJob,
    });
    form.reset(
      {
        ...transformRedactionJobRawValue,
        id: { value: transformRedactionJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransformRedactionJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertTransformRedactionJobRawValueToTransformRedactionJob(
    rawTransformRedactionJob: TransformRedactionJobFormRawValue | NewTransformRedactionJobFormRawValue,
  ): ITransformRedactionJob | NewTransformRedactionJob {
    return {
      ...rawTransformRedactionJob,
      startDate: dayjs(rawTransformRedactionJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawTransformRedactionJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawTransformRedactionJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertTransformRedactionJobToTransformRedactionJobRawValue(
    transformRedactionJob: ITransformRedactionJob | (Partial<NewTransformRedactionJob> & TransformRedactionJobFormDefaults),
  ): TransformRedactionJobFormRawValue | PartialWithRequiredKeyOf<NewTransformRedactionJobFormRawValue> {
    return {
      ...transformRedactionJob,
      startDate: transformRedactionJob.startDate ? transformRedactionJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: transformRedactionJob.endDate ? transformRedactionJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: transformRedactionJob.createdDate ? transformRedactionJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
