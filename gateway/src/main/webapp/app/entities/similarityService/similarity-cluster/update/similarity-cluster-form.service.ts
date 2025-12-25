import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISimilarityCluster, NewSimilarityCluster } from '../similarity-cluster.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISimilarityCluster for edit and NewSimilarityClusterFormGroupInput for create.
 */
type SimilarityClusterFormGroupInput = ISimilarityCluster | PartialWithRequiredKeyOf<NewSimilarityCluster>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISimilarityCluster | NewSimilarityCluster> = Omit<T, 'createdDate' | 'lastUpdated'> & {
  createdDate?: string | null;
  lastUpdated?: string | null;
};

type SimilarityClusterFormRawValue = FormValueOf<ISimilarityCluster>;

type NewSimilarityClusterFormRawValue = FormValueOf<NewSimilarityCluster>;

type SimilarityClusterFormDefaults = Pick<NewSimilarityCluster, 'id' | 'createdDate' | 'lastUpdated'>;

type SimilarityClusterFormGroupContent = {
  id: FormControl<SimilarityClusterFormRawValue['id'] | NewSimilarityCluster['id']>;
  name: FormControl<SimilarityClusterFormRawValue['name']>;
  description: FormControl<SimilarityClusterFormRawValue['description']>;
  algorithm: FormControl<SimilarityClusterFormRawValue['algorithm']>;
  centroid: FormControl<SimilarityClusterFormRawValue['centroid']>;
  documentCount: FormControl<SimilarityClusterFormRawValue['documentCount']>;
  avgSimilarity: FormControl<SimilarityClusterFormRawValue['avgSimilarity']>;
  createdDate: FormControl<SimilarityClusterFormRawValue['createdDate']>;
  lastUpdated: FormControl<SimilarityClusterFormRawValue['lastUpdated']>;
};

export type SimilarityClusterFormGroup = FormGroup<SimilarityClusterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SimilarityClusterFormService {
  createSimilarityClusterFormGroup(similarityCluster: SimilarityClusterFormGroupInput = { id: null }): SimilarityClusterFormGroup {
    const similarityClusterRawValue = this.convertSimilarityClusterToSimilarityClusterRawValue({
      ...this.getFormDefaults(),
      ...similarityCluster,
    });
    return new FormGroup<SimilarityClusterFormGroupContent>({
      id: new FormControl(
        { value: similarityClusterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(similarityClusterRawValue.name, {
        validators: [Validators.maxLength(255)],
      }),
      description: new FormControl(similarityClusterRawValue.description),
      algorithm: new FormControl(similarityClusterRawValue.algorithm),
      centroid: new FormControl(similarityClusterRawValue.centroid),
      documentCount: new FormControl(similarityClusterRawValue.documentCount),
      avgSimilarity: new FormControl(similarityClusterRawValue.avgSimilarity, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      createdDate: new FormControl(similarityClusterRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastUpdated: new FormControl(similarityClusterRawValue.lastUpdated),
    });
  }

  getSimilarityCluster(form: SimilarityClusterFormGroup): ISimilarityCluster | NewSimilarityCluster {
    return this.convertSimilarityClusterRawValueToSimilarityCluster(
      form.getRawValue() as SimilarityClusterFormRawValue | NewSimilarityClusterFormRawValue,
    );
  }

  resetForm(form: SimilarityClusterFormGroup, similarityCluster: SimilarityClusterFormGroupInput): void {
    const similarityClusterRawValue = this.convertSimilarityClusterToSimilarityClusterRawValue({
      ...this.getFormDefaults(),
      ...similarityCluster,
    });
    form.reset(
      {
        ...similarityClusterRawValue,
        id: { value: similarityClusterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SimilarityClusterFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastUpdated: currentTime,
    };
  }

  private convertSimilarityClusterRawValueToSimilarityCluster(
    rawSimilarityCluster: SimilarityClusterFormRawValue | NewSimilarityClusterFormRawValue,
  ): ISimilarityCluster | NewSimilarityCluster {
    return {
      ...rawSimilarityCluster,
      createdDate: dayjs(rawSimilarityCluster.createdDate, DATE_TIME_FORMAT),
      lastUpdated: dayjs(rawSimilarityCluster.lastUpdated, DATE_TIME_FORMAT),
    };
  }

  private convertSimilarityClusterToSimilarityClusterRawValue(
    similarityCluster: ISimilarityCluster | (Partial<NewSimilarityCluster> & SimilarityClusterFormDefaults),
  ): SimilarityClusterFormRawValue | PartialWithRequiredKeyOf<NewSimilarityClusterFormRawValue> {
    return {
      ...similarityCluster,
      createdDate: similarityCluster.createdDate ? similarityCluster.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastUpdated: similarityCluster.lastUpdated ? similarityCluster.lastUpdated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
