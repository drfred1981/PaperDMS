import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISimilarityDocumentComparison, NewSimilarityDocumentComparison } from '../similarity-document-comparison.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISimilarityDocumentComparison for edit and NewSimilarityDocumentComparisonFormGroupInput for create.
 */
type SimilarityDocumentComparisonFormGroupInput = ISimilarityDocumentComparison | PartialWithRequiredKeyOf<NewSimilarityDocumentComparison>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISimilarityDocumentComparison | NewSimilarityDocumentComparison> = Omit<T, 'computedDate' | 'reviewedDate'> & {
  computedDate?: string | null;
  reviewedDate?: string | null;
};

type SimilarityDocumentComparisonFormRawValue = FormValueOf<ISimilarityDocumentComparison>;

type NewSimilarityDocumentComparisonFormRawValue = FormValueOf<NewSimilarityDocumentComparison>;

type SimilarityDocumentComparisonFormDefaults = Pick<
  NewSimilarityDocumentComparison,
  'id' | 'computedDate' | 'isRelevant' | 'reviewedDate'
>;

type SimilarityDocumentComparisonFormGroupContent = {
  id: FormControl<SimilarityDocumentComparisonFormRawValue['id'] | NewSimilarityDocumentComparison['id']>;
  sourceDocumentSha256: FormControl<SimilarityDocumentComparisonFormRawValue['sourceDocumentSha256']>;
  targetDocumentSha256: FormControl<SimilarityDocumentComparisonFormRawValue['targetDocumentSha256']>;
  similarityScore: FormControl<SimilarityDocumentComparisonFormRawValue['similarityScore']>;
  algorithm: FormControl<SimilarityDocumentComparisonFormRawValue['algorithm']>;
  features: FormControl<SimilarityDocumentComparisonFormRawValue['features']>;
  computedDate: FormControl<SimilarityDocumentComparisonFormRawValue['computedDate']>;
  isRelevant: FormControl<SimilarityDocumentComparisonFormRawValue['isRelevant']>;
  reviewedBy: FormControl<SimilarityDocumentComparisonFormRawValue['reviewedBy']>;
  reviewedDate: FormControl<SimilarityDocumentComparisonFormRawValue['reviewedDate']>;
  job: FormControl<SimilarityDocumentComparisonFormRawValue['job']>;
};

export type SimilarityDocumentComparisonFormGroup = FormGroup<SimilarityDocumentComparisonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SimilarityDocumentComparisonFormService {
  createSimilarityDocumentComparisonFormGroup(
    similarityDocumentComparison: SimilarityDocumentComparisonFormGroupInput = { id: null },
  ): SimilarityDocumentComparisonFormGroup {
    const similarityDocumentComparisonRawValue = this.convertSimilarityDocumentComparisonToSimilarityDocumentComparisonRawValue({
      ...this.getFormDefaults(),
      ...similarityDocumentComparison,
    });
    return new FormGroup<SimilarityDocumentComparisonFormGroupContent>({
      id: new FormControl(
        { value: similarityDocumentComparisonRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sourceDocumentSha256: new FormControl(similarityDocumentComparisonRawValue.sourceDocumentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      targetDocumentSha256: new FormControl(similarityDocumentComparisonRawValue.targetDocumentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      similarityScore: new FormControl(similarityDocumentComparisonRawValue.similarityScore, {
        validators: [Validators.required, Validators.min(0), Validators.max(1)],
      }),
      algorithm: new FormControl(similarityDocumentComparisonRawValue.algorithm),
      features: new FormControl(similarityDocumentComparisonRawValue.features),
      computedDate: new FormControl(similarityDocumentComparisonRawValue.computedDate, {
        validators: [Validators.required],
      }),
      isRelevant: new FormControl(similarityDocumentComparisonRawValue.isRelevant),
      reviewedBy: new FormControl(similarityDocumentComparisonRawValue.reviewedBy, {
        validators: [Validators.maxLength(50)],
      }),
      reviewedDate: new FormControl(similarityDocumentComparisonRawValue.reviewedDate),
      job: new FormControl(similarityDocumentComparisonRawValue.job),
    });
  }

  getSimilarityDocumentComparison(
    form: SimilarityDocumentComparisonFormGroup,
  ): ISimilarityDocumentComparison | NewSimilarityDocumentComparison {
    return this.convertSimilarityDocumentComparisonRawValueToSimilarityDocumentComparison(
      form.getRawValue() as SimilarityDocumentComparisonFormRawValue | NewSimilarityDocumentComparisonFormRawValue,
    );
  }

  resetForm(form: SimilarityDocumentComparisonFormGroup, similarityDocumentComparison: SimilarityDocumentComparisonFormGroupInput): void {
    const similarityDocumentComparisonRawValue = this.convertSimilarityDocumentComparisonToSimilarityDocumentComparisonRawValue({
      ...this.getFormDefaults(),
      ...similarityDocumentComparison,
    });
    form.reset(
      {
        ...similarityDocumentComparisonRawValue,
        id: { value: similarityDocumentComparisonRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SimilarityDocumentComparisonFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      computedDate: currentTime,
      isRelevant: false,
      reviewedDate: currentTime,
    };
  }

  private convertSimilarityDocumentComparisonRawValueToSimilarityDocumentComparison(
    rawSimilarityDocumentComparison: SimilarityDocumentComparisonFormRawValue | NewSimilarityDocumentComparisonFormRawValue,
  ): ISimilarityDocumentComparison | NewSimilarityDocumentComparison {
    return {
      ...rawSimilarityDocumentComparison,
      computedDate: dayjs(rawSimilarityDocumentComparison.computedDate, DATE_TIME_FORMAT),
      reviewedDate: dayjs(rawSimilarityDocumentComparison.reviewedDate, DATE_TIME_FORMAT),
    };
  }

  private convertSimilarityDocumentComparisonToSimilarityDocumentComparisonRawValue(
    similarityDocumentComparison:
      | ISimilarityDocumentComparison
      | (Partial<NewSimilarityDocumentComparison> & SimilarityDocumentComparisonFormDefaults),
  ): SimilarityDocumentComparisonFormRawValue | PartialWithRequiredKeyOf<NewSimilarityDocumentComparisonFormRawValue> {
    return {
      ...similarityDocumentComparison,
      computedDate: similarityDocumentComparison.computedDate
        ? similarityDocumentComparison.computedDate.format(DATE_TIME_FORMAT)
        : undefined,
      reviewedDate: similarityDocumentComparison.reviewedDate
        ? similarityDocumentComparison.reviewedDate.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
