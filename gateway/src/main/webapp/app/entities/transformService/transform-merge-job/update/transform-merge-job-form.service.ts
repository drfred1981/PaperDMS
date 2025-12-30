import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransformMergeJob, NewTransformMergeJob } from '../transform-merge-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransformMergeJob for edit and NewTransformMergeJobFormGroupInput for create.
 */
type TransformMergeJobFormGroupInput = ITransformMergeJob | PartialWithRequiredKeyOf<NewTransformMergeJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransformMergeJob | NewTransformMergeJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type TransformMergeJobFormRawValue = FormValueOf<ITransformMergeJob>;

type NewTransformMergeJobFormRawValue = FormValueOf<NewTransformMergeJob>;

type TransformMergeJobFormDefaults = Pick<
  NewTransformMergeJob,
  'id' | 'includeBookmarks' | 'includeToc' | 'addPageNumbers' | 'startDate' | 'endDate' | 'createdDate'
>;

type TransformMergeJobFormGroupContent = {
  id: FormControl<TransformMergeJobFormRawValue['id'] | NewTransformMergeJob['id']>;
  name: FormControl<TransformMergeJobFormRawValue['name']>;
  sourceDocumentSha256: FormControl<TransformMergeJobFormRawValue['sourceDocumentSha256']>;
  mergeOrder: FormControl<TransformMergeJobFormRawValue['mergeOrder']>;
  includeBookmarks: FormControl<TransformMergeJobFormRawValue['includeBookmarks']>;
  includeToc: FormControl<TransformMergeJobFormRawValue['includeToc']>;
  addPageNumbers: FormControl<TransformMergeJobFormRawValue['addPageNumbers']>;
  outputS3Key: FormControl<TransformMergeJobFormRawValue['outputS3Key']>;
  outputDocumentSha256: FormControl<TransformMergeJobFormRawValue['outputDocumentSha256']>;
  status: FormControl<TransformMergeJobFormRawValue['status']>;
  startDate: FormControl<TransformMergeJobFormRawValue['startDate']>;
  endDate: FormControl<TransformMergeJobFormRawValue['endDate']>;
  errorMessage: FormControl<TransformMergeJobFormRawValue['errorMessage']>;
  createdBy: FormControl<TransformMergeJobFormRawValue['createdBy']>;
  createdDate: FormControl<TransformMergeJobFormRawValue['createdDate']>;
};

export type TransformMergeJobFormGroup = FormGroup<TransformMergeJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransformMergeJobFormService {
  createTransformMergeJobFormGroup(transformMergeJob: TransformMergeJobFormGroupInput = { id: null }): TransformMergeJobFormGroup {
    const transformMergeJobRawValue = this.convertTransformMergeJobToTransformMergeJobRawValue({
      ...this.getFormDefaults(),
      ...transformMergeJob,
    });
    return new FormGroup<TransformMergeJobFormGroupContent>({
      id: new FormControl(
        { value: transformMergeJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(transformMergeJobRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      sourceDocumentSha256: new FormControl(transformMergeJobRawValue.sourceDocumentSha256, {
        validators: [Validators.required],
      }),
      mergeOrder: new FormControl(transformMergeJobRawValue.mergeOrder, {
        validators: [Validators.required],
      }),
      includeBookmarks: new FormControl(transformMergeJobRawValue.includeBookmarks),
      includeToc: new FormControl(transformMergeJobRawValue.includeToc),
      addPageNumbers: new FormControl(transformMergeJobRawValue.addPageNumbers),
      outputS3Key: new FormControl(transformMergeJobRawValue.outputS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      outputDocumentSha256: new FormControl(transformMergeJobRawValue.outputDocumentSha256, {
        validators: [Validators.maxLength(64)],
      }),
      status: new FormControl(transformMergeJobRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(transformMergeJobRawValue.startDate),
      endDate: new FormControl(transformMergeJobRawValue.endDate),
      errorMessage: new FormControl(transformMergeJobRawValue.errorMessage),
      createdBy: new FormControl(transformMergeJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(transformMergeJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getTransformMergeJob(form: TransformMergeJobFormGroup): ITransformMergeJob | NewTransformMergeJob {
    return this.convertTransformMergeJobRawValueToTransformMergeJob(
      form.getRawValue() as TransformMergeJobFormRawValue | NewTransformMergeJobFormRawValue,
    );
  }

  resetForm(form: TransformMergeJobFormGroup, transformMergeJob: TransformMergeJobFormGroupInput): void {
    const transformMergeJobRawValue = this.convertTransformMergeJobToTransformMergeJobRawValue({
      ...this.getFormDefaults(),
      ...transformMergeJob,
    });
    form.reset(
      {
        ...transformMergeJobRawValue,
        id: { value: transformMergeJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransformMergeJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      includeBookmarks: false,
      includeToc: false,
      addPageNumbers: false,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertTransformMergeJobRawValueToTransformMergeJob(
    rawTransformMergeJob: TransformMergeJobFormRawValue | NewTransformMergeJobFormRawValue,
  ): ITransformMergeJob | NewTransformMergeJob {
    return {
      ...rawTransformMergeJob,
      startDate: dayjs(rawTransformMergeJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawTransformMergeJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawTransformMergeJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertTransformMergeJobToTransformMergeJobRawValue(
    transformMergeJob: ITransformMergeJob | (Partial<NewTransformMergeJob> & TransformMergeJobFormDefaults),
  ): TransformMergeJobFormRawValue | PartialWithRequiredKeyOf<NewTransformMergeJobFormRawValue> {
    return {
      ...transformMergeJob,
      startDate: transformMergeJob.startDate ? transformMergeJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: transformMergeJob.endDate ? transformMergeJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: transformMergeJob.createdDate ? transformMergeJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
