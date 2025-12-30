import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISimilarityDocumentFingerprint, NewSimilarityDocumentFingerprint } from '../similarity-document-fingerprint.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISimilarityDocumentFingerprint for edit and NewSimilarityDocumentFingerprintFormGroupInput for create.
 */
type SimilarityDocumentFingerprintFormGroupInput =
  | ISimilarityDocumentFingerprint
  | PartialWithRequiredKeyOf<NewSimilarityDocumentFingerprint>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISimilarityDocumentFingerprint | NewSimilarityDocumentFingerprint> = Omit<T, 'computedDate' | 'lastUpdated'> & {
  computedDate?: string | null;
  lastUpdated?: string | null;
};

type SimilarityDocumentFingerprintFormRawValue = FormValueOf<ISimilarityDocumentFingerprint>;

type NewSimilarityDocumentFingerprintFormRawValue = FormValueOf<NewSimilarityDocumentFingerprint>;

type SimilarityDocumentFingerprintFormDefaults = Pick<NewSimilarityDocumentFingerprint, 'id' | 'computedDate' | 'lastUpdated'>;

type SimilarityDocumentFingerprintFormGroupContent = {
  id: FormControl<SimilarityDocumentFingerprintFormRawValue['id'] | NewSimilarityDocumentFingerprint['id']>;
  fingerprintType: FormControl<SimilarityDocumentFingerprintFormRawValue['fingerprintType']>;
  fingerprint: FormControl<SimilarityDocumentFingerprintFormRawValue['fingerprint']>;
  vectorEmbedding: FormControl<SimilarityDocumentFingerprintFormRawValue['vectorEmbedding']>;
  metadata: FormControl<SimilarityDocumentFingerprintFormRawValue['metadata']>;
  computedDate: FormControl<SimilarityDocumentFingerprintFormRawValue['computedDate']>;
  lastUpdated: FormControl<SimilarityDocumentFingerprintFormRawValue['lastUpdated']>;
};

export type SimilarityDocumentFingerprintFormGroup = FormGroup<SimilarityDocumentFingerprintFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SimilarityDocumentFingerprintFormService {
  createSimilarityDocumentFingerprintFormGroup(
    similarityDocumentFingerprint: SimilarityDocumentFingerprintFormGroupInput = { id: null },
  ): SimilarityDocumentFingerprintFormGroup {
    const similarityDocumentFingerprintRawValue = this.convertSimilarityDocumentFingerprintToSimilarityDocumentFingerprintRawValue({
      ...this.getFormDefaults(),
      ...similarityDocumentFingerprint,
    });
    return new FormGroup<SimilarityDocumentFingerprintFormGroupContent>({
      id: new FormControl(
        { value: similarityDocumentFingerprintRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fingerprintType: new FormControl(similarityDocumentFingerprintRawValue.fingerprintType),
      fingerprint: new FormControl(similarityDocumentFingerprintRawValue.fingerprint, {
        validators: [Validators.required],
      }),
      vectorEmbedding: new FormControl(similarityDocumentFingerprintRawValue.vectorEmbedding),
      metadata: new FormControl(similarityDocumentFingerprintRawValue.metadata),
      computedDate: new FormControl(similarityDocumentFingerprintRawValue.computedDate, {
        validators: [Validators.required],
      }),
      lastUpdated: new FormControl(similarityDocumentFingerprintRawValue.lastUpdated),
    });
  }

  getSimilarityDocumentFingerprint(
    form: SimilarityDocumentFingerprintFormGroup,
  ): ISimilarityDocumentFingerprint | NewSimilarityDocumentFingerprint {
    return this.convertSimilarityDocumentFingerprintRawValueToSimilarityDocumentFingerprint(
      form.getRawValue() as SimilarityDocumentFingerprintFormRawValue | NewSimilarityDocumentFingerprintFormRawValue,
    );
  }

  resetForm(
    form: SimilarityDocumentFingerprintFormGroup,
    similarityDocumentFingerprint: SimilarityDocumentFingerprintFormGroupInput,
  ): void {
    const similarityDocumentFingerprintRawValue = this.convertSimilarityDocumentFingerprintToSimilarityDocumentFingerprintRawValue({
      ...this.getFormDefaults(),
      ...similarityDocumentFingerprint,
    });
    form.reset(
      {
        ...similarityDocumentFingerprintRawValue,
        id: { value: similarityDocumentFingerprintRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SimilarityDocumentFingerprintFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      computedDate: currentTime,
      lastUpdated: currentTime,
    };
  }

  private convertSimilarityDocumentFingerprintRawValueToSimilarityDocumentFingerprint(
    rawSimilarityDocumentFingerprint: SimilarityDocumentFingerprintFormRawValue | NewSimilarityDocumentFingerprintFormRawValue,
  ): ISimilarityDocumentFingerprint | NewSimilarityDocumentFingerprint {
    return {
      ...rawSimilarityDocumentFingerprint,
      computedDate: dayjs(rawSimilarityDocumentFingerprint.computedDate, DATE_TIME_FORMAT),
      lastUpdated: dayjs(rawSimilarityDocumentFingerprint.lastUpdated, DATE_TIME_FORMAT),
    };
  }

  private convertSimilarityDocumentFingerprintToSimilarityDocumentFingerprintRawValue(
    similarityDocumentFingerprint:
      | ISimilarityDocumentFingerprint
      | (Partial<NewSimilarityDocumentFingerprint> & SimilarityDocumentFingerprintFormDefaults),
  ): SimilarityDocumentFingerprintFormRawValue | PartialWithRequiredKeyOf<NewSimilarityDocumentFingerprintFormRawValue> {
    return {
      ...similarityDocumentFingerprint,
      computedDate: similarityDocumentFingerprint.computedDate
        ? similarityDocumentFingerprint.computedDate.format(DATE_TIME_FORMAT)
        : undefined,
      lastUpdated: similarityDocumentFingerprint.lastUpdated
        ? similarityDocumentFingerprint.lastUpdated.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
