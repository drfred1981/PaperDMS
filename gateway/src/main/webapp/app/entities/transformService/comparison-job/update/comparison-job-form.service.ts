import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IComparisonJob, NewComparisonJob } from '../comparison-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IComparisonJob for edit and NewComparisonJobFormGroupInput for create.
 */
type ComparisonJobFormGroupInput = IComparisonJob | PartialWithRequiredKeyOf<NewComparisonJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IComparisonJob | NewComparisonJob> = Omit<T, 'comparedDate'> & {
  comparedDate?: string | null;
};

type ComparisonJobFormRawValue = FormValueOf<IComparisonJob>;

type NewComparisonJobFormRawValue = FormValueOf<NewComparisonJob>;

type ComparisonJobFormDefaults = Pick<NewComparisonJob, 'id' | 'comparedDate'>;

type ComparisonJobFormGroupContent = {
  id: FormControl<ComparisonJobFormRawValue['id'] | NewComparisonJob['id']>;
  documentId1: FormControl<ComparisonJobFormRawValue['documentId1']>;
  documentId2: FormControl<ComparisonJobFormRawValue['documentId2']>;
  comparisonType: FormControl<ComparisonJobFormRawValue['comparisonType']>;
  differences: FormControl<ComparisonJobFormRawValue['differences']>;
  differenceCount: FormControl<ComparisonJobFormRawValue['differenceCount']>;
  similarityPercentage: FormControl<ComparisonJobFormRawValue['similarityPercentage']>;
  diffReportS3Key: FormControl<ComparisonJobFormRawValue['diffReportS3Key']>;
  diffVisualS3Key: FormControl<ComparisonJobFormRawValue['diffVisualS3Key']>;
  status: FormControl<ComparisonJobFormRawValue['status']>;
  comparedDate: FormControl<ComparisonJobFormRawValue['comparedDate']>;
  comparedBy: FormControl<ComparisonJobFormRawValue['comparedBy']>;
};

export type ComparisonJobFormGroup = FormGroup<ComparisonJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ComparisonJobFormService {
  createComparisonJobFormGroup(comparisonJob?: ComparisonJobFormGroupInput): ComparisonJobFormGroup {
    const comparisonJobRawValue = this.convertComparisonJobToComparisonJobRawValue({
      ...this.getFormDefaults(),
      ...(comparisonJob ?? { id: null }),
    });
    return new FormGroup<ComparisonJobFormGroupContent>({
      id: new FormControl(
        { value: comparisonJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId1: new FormControl(comparisonJobRawValue.documentId1, {
        validators: [Validators.required],
      }),
      documentId2: new FormControl(comparisonJobRawValue.documentId2, {
        validators: [Validators.required],
      }),
      comparisonType: new FormControl(comparisonJobRawValue.comparisonType, {
        validators: [Validators.required],
      }),
      differences: new FormControl(comparisonJobRawValue.differences),
      differenceCount: new FormControl(comparisonJobRawValue.differenceCount),
      similarityPercentage: new FormControl(comparisonJobRawValue.similarityPercentage, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      diffReportS3Key: new FormControl(comparisonJobRawValue.diffReportS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      diffVisualS3Key: new FormControl(comparisonJobRawValue.diffVisualS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      status: new FormControl(comparisonJobRawValue.status, {
        validators: [Validators.required],
      }),
      comparedDate: new FormControl(comparisonJobRawValue.comparedDate, {
        validators: [Validators.required],
      }),
      comparedBy: new FormControl(comparisonJobRawValue.comparedBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
    });
  }

  getComparisonJob(form: ComparisonJobFormGroup): IComparisonJob | NewComparisonJob {
    return this.convertComparisonJobRawValueToComparisonJob(form.getRawValue() as ComparisonJobFormRawValue | NewComparisonJobFormRawValue);
  }

  resetForm(form: ComparisonJobFormGroup, comparisonJob: ComparisonJobFormGroupInput): void {
    const comparisonJobRawValue = this.convertComparisonJobToComparisonJobRawValue({ ...this.getFormDefaults(), ...comparisonJob });
    form.reset({
      ...comparisonJobRawValue,
      id: { value: comparisonJobRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ComparisonJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      comparedDate: currentTime,
    };
  }

  private convertComparisonJobRawValueToComparisonJob(
    rawComparisonJob: ComparisonJobFormRawValue | NewComparisonJobFormRawValue,
  ): IComparisonJob | NewComparisonJob {
    return {
      ...rawComparisonJob,
      comparedDate: dayjs(rawComparisonJob.comparedDate, DATE_TIME_FORMAT),
    };
  }

  private convertComparisonJobToComparisonJobRawValue(
    comparisonJob: IComparisonJob | (Partial<NewComparisonJob> & ComparisonJobFormDefaults),
  ): ComparisonJobFormRawValue | PartialWithRequiredKeyOf<NewComparisonJobFormRawValue> {
    return {
      ...comparisonJob,
      comparedDate: comparisonJob.comparedDate ? comparisonJob.comparedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
