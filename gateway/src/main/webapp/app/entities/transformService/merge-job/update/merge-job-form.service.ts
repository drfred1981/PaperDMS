import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMergeJob, NewMergeJob } from '../merge-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMergeJob for edit and NewMergeJobFormGroupInput for create.
 */
type MergeJobFormGroupInput = IMergeJob | PartialWithRequiredKeyOf<NewMergeJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMergeJob | NewMergeJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type MergeJobFormRawValue = FormValueOf<IMergeJob>;

type NewMergeJobFormRawValue = FormValueOf<NewMergeJob>;

type MergeJobFormDefaults = Pick<
  NewMergeJob,
  'id' | 'includeBookmarks' | 'includeToc' | 'addPageNumbers' | 'startDate' | 'endDate' | 'createdDate'
>;

type MergeJobFormGroupContent = {
  id: FormControl<MergeJobFormRawValue['id'] | NewMergeJob['id']>;
  name: FormControl<MergeJobFormRawValue['name']>;
  sourceDocumentIds: FormControl<MergeJobFormRawValue['sourceDocumentIds']>;
  mergeOrder: FormControl<MergeJobFormRawValue['mergeOrder']>;
  includeBookmarks: FormControl<MergeJobFormRawValue['includeBookmarks']>;
  includeToc: FormControl<MergeJobFormRawValue['includeToc']>;
  addPageNumbers: FormControl<MergeJobFormRawValue['addPageNumbers']>;
  outputS3Key: FormControl<MergeJobFormRawValue['outputS3Key']>;
  outputDocumentId: FormControl<MergeJobFormRawValue['outputDocumentId']>;
  status: FormControl<MergeJobFormRawValue['status']>;
  startDate: FormControl<MergeJobFormRawValue['startDate']>;
  endDate: FormControl<MergeJobFormRawValue['endDate']>;
  errorMessage: FormControl<MergeJobFormRawValue['errorMessage']>;
  createdBy: FormControl<MergeJobFormRawValue['createdBy']>;
  createdDate: FormControl<MergeJobFormRawValue['createdDate']>;
};

export type MergeJobFormGroup = FormGroup<MergeJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MergeJobFormService {
  createMergeJobFormGroup(mergeJob: MergeJobFormGroupInput = { id: null }): MergeJobFormGroup {
    const mergeJobRawValue = this.convertMergeJobToMergeJobRawValue({
      ...this.getFormDefaults(),
      ...mergeJob,
    });
    return new FormGroup<MergeJobFormGroupContent>({
      id: new FormControl(
        { value: mergeJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(mergeJobRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      sourceDocumentIds: new FormControl(mergeJobRawValue.sourceDocumentIds, {
        validators: [Validators.required],
      }),
      mergeOrder: new FormControl(mergeJobRawValue.mergeOrder, {
        validators: [Validators.required],
      }),
      includeBookmarks: new FormControl(mergeJobRawValue.includeBookmarks),
      includeToc: new FormControl(mergeJobRawValue.includeToc),
      addPageNumbers: new FormControl(mergeJobRawValue.addPageNumbers),
      outputS3Key: new FormControl(mergeJobRawValue.outputS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      outputDocumentId: new FormControl(mergeJobRawValue.outputDocumentId),
      status: new FormControl(mergeJobRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(mergeJobRawValue.startDate),
      endDate: new FormControl(mergeJobRawValue.endDate),
      errorMessage: new FormControl(mergeJobRawValue.errorMessage),
      createdBy: new FormControl(mergeJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(mergeJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getMergeJob(form: MergeJobFormGroup): IMergeJob | NewMergeJob {
    return this.convertMergeJobRawValueToMergeJob(form.getRawValue() as MergeJobFormRawValue | NewMergeJobFormRawValue);
  }

  resetForm(form: MergeJobFormGroup, mergeJob: MergeJobFormGroupInput): void {
    const mergeJobRawValue = this.convertMergeJobToMergeJobRawValue({ ...this.getFormDefaults(), ...mergeJob });
    form.reset(
      {
        ...mergeJobRawValue,
        id: { value: mergeJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MergeJobFormDefaults {
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

  private convertMergeJobRawValueToMergeJob(rawMergeJob: MergeJobFormRawValue | NewMergeJobFormRawValue): IMergeJob | NewMergeJob {
    return {
      ...rawMergeJob,
      startDate: dayjs(rawMergeJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawMergeJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawMergeJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertMergeJobToMergeJobRawValue(
    mergeJob: IMergeJob | (Partial<NewMergeJob> & MergeJobFormDefaults),
  ): MergeJobFormRawValue | PartialWithRequiredKeyOf<NewMergeJobFormRawValue> {
    return {
      ...mergeJob,
      startDate: mergeJob.startDate ? mergeJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: mergeJob.endDate ? mergeJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: mergeJob.createdDate ? mergeJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
