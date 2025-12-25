import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISimilarityJob, NewSimilarityJob } from '../similarity-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISimilarityJob for edit and NewSimilarityJobFormGroupInput for create.
 */
type SimilarityJobFormGroupInput = ISimilarityJob | PartialWithRequiredKeyOf<NewSimilarityJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISimilarityJob | NewSimilarityJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type SimilarityJobFormRawValue = FormValueOf<ISimilarityJob>;

type NewSimilarityJobFormRawValue = FormValueOf<NewSimilarityJob>;

type SimilarityJobFormDefaults = Pick<NewSimilarityJob, 'id' | 'startDate' | 'endDate' | 'createdDate'>;

type SimilarityJobFormGroupContent = {
  id: FormControl<SimilarityJobFormRawValue['id'] | NewSimilarityJob['id']>;
  documentId: FormControl<SimilarityJobFormRawValue['documentId']>;
  documentSha256: FormControl<SimilarityJobFormRawValue['documentSha256']>;
  status: FormControl<SimilarityJobFormRawValue['status']>;
  algorithm: FormControl<SimilarityJobFormRawValue['algorithm']>;
  scope: FormControl<SimilarityJobFormRawValue['scope']>;
  minSimilarityThreshold: FormControl<SimilarityJobFormRawValue['minSimilarityThreshold']>;
  matchesFound: FormControl<SimilarityJobFormRawValue['matchesFound']>;
  startDate: FormControl<SimilarityJobFormRawValue['startDate']>;
  endDate: FormControl<SimilarityJobFormRawValue['endDate']>;
  errorMessage: FormControl<SimilarityJobFormRawValue['errorMessage']>;
  createdDate: FormControl<SimilarityJobFormRawValue['createdDate']>;
  createdBy: FormControl<SimilarityJobFormRawValue['createdBy']>;
};

export type SimilarityJobFormGroup = FormGroup<SimilarityJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SimilarityJobFormService {
  createSimilarityJobFormGroup(similarityJob: SimilarityJobFormGroupInput = { id: null }): SimilarityJobFormGroup {
    const similarityJobRawValue = this.convertSimilarityJobToSimilarityJobRawValue({
      ...this.getFormDefaults(),
      ...similarityJob,
    });
    return new FormGroup<SimilarityJobFormGroupContent>({
      id: new FormControl(
        { value: similarityJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(similarityJobRawValue.documentId, {
        validators: [Validators.required],
      }),
      documentSha256: new FormControl(similarityJobRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      status: new FormControl(similarityJobRawValue.status),
      algorithm: new FormControl(similarityJobRawValue.algorithm),
      scope: new FormControl(similarityJobRawValue.scope),
      minSimilarityThreshold: new FormControl(similarityJobRawValue.minSimilarityThreshold, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      matchesFound: new FormControl(similarityJobRawValue.matchesFound),
      startDate: new FormControl(similarityJobRawValue.startDate),
      endDate: new FormControl(similarityJobRawValue.endDate),
      errorMessage: new FormControl(similarityJobRawValue.errorMessage),
      createdDate: new FormControl(similarityJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(similarityJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
    });
  }

  getSimilarityJob(form: SimilarityJobFormGroup): ISimilarityJob | NewSimilarityJob {
    return this.convertSimilarityJobRawValueToSimilarityJob(form.getRawValue() as SimilarityJobFormRawValue | NewSimilarityJobFormRawValue);
  }

  resetForm(form: SimilarityJobFormGroup, similarityJob: SimilarityJobFormGroupInput): void {
    const similarityJobRawValue = this.convertSimilarityJobToSimilarityJobRawValue({ ...this.getFormDefaults(), ...similarityJob });
    form.reset(
      {
        ...similarityJobRawValue,
        id: { value: similarityJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SimilarityJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertSimilarityJobRawValueToSimilarityJob(
    rawSimilarityJob: SimilarityJobFormRawValue | NewSimilarityJobFormRawValue,
  ): ISimilarityJob | NewSimilarityJob {
    return {
      ...rawSimilarityJob,
      startDate: dayjs(rawSimilarityJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawSimilarityJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawSimilarityJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertSimilarityJobToSimilarityJobRawValue(
    similarityJob: ISimilarityJob | (Partial<NewSimilarityJob> & SimilarityJobFormDefaults),
  ): SimilarityJobFormRawValue | PartialWithRequiredKeyOf<NewSimilarityJobFormRawValue> {
    return {
      ...similarityJob,
      startDate: similarityJob.startDate ? similarityJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: similarityJob.endDate ? similarityJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: similarityJob.createdDate ? similarityJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
