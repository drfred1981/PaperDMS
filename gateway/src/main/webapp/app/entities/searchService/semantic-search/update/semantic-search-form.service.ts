import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISemanticSearch, NewSemanticSearch } from '../semantic-search.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISemanticSearch for edit and NewSemanticSearchFormGroupInput for create.
 */
type SemanticSearchFormGroupInput = ISemanticSearch | PartialWithRequiredKeyOf<NewSemanticSearch>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISemanticSearch | NewSemanticSearch> = Omit<T, 'searchDate'> & {
  searchDate?: string | null;
};

type SemanticSearchFormRawValue = FormValueOf<ISemanticSearch>;

type NewSemanticSearchFormRawValue = FormValueOf<NewSemanticSearch>;

type SemanticSearchFormDefaults = Pick<NewSemanticSearch, 'id' | 'searchDate'>;

type SemanticSearchFormGroupContent = {
  id: FormControl<SemanticSearchFormRawValue['id'] | NewSemanticSearch['id']>;
  query: FormControl<SemanticSearchFormRawValue['query']>;
  queryEmbedding: FormControl<SemanticSearchFormRawValue['queryEmbedding']>;
  results: FormControl<SemanticSearchFormRawValue['results']>;
  relevanceScores: FormControl<SemanticSearchFormRawValue['relevanceScores']>;
  modelUsed: FormControl<SemanticSearchFormRawValue['modelUsed']>;
  executionTime: FormControl<SemanticSearchFormRawValue['executionTime']>;
  userId: FormControl<SemanticSearchFormRawValue['userId']>;
  searchDate: FormControl<SemanticSearchFormRawValue['searchDate']>;
};

export type SemanticSearchFormGroup = FormGroup<SemanticSearchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SemanticSearchFormService {
  createSemanticSearchFormGroup(semanticSearch?: SemanticSearchFormGroupInput): SemanticSearchFormGroup {
    const semanticSearchRawValue = this.convertSemanticSearchToSemanticSearchRawValue({
      ...this.getFormDefaults(),
      ...(semanticSearch ?? { id: null }),
    });
    return new FormGroup<SemanticSearchFormGroupContent>({
      id: new FormControl(
        { value: semanticSearchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      query: new FormControl(semanticSearchRawValue.query, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      queryEmbedding: new FormControl(semanticSearchRawValue.queryEmbedding, {
        validators: [Validators.required],
      }),
      results: new FormControl(semanticSearchRawValue.results, {
        validators: [Validators.required],
      }),
      relevanceScores: new FormControl(semanticSearchRawValue.relevanceScores),
      modelUsed: new FormControl(semanticSearchRawValue.modelUsed, {
        validators: [Validators.maxLength(100)],
      }),
      executionTime: new FormControl(semanticSearchRawValue.executionTime),
      userId: new FormControl(semanticSearchRawValue.userId, {
        validators: [Validators.maxLength(50)],
      }),
      searchDate: new FormControl(semanticSearchRawValue.searchDate, {
        validators: [Validators.required],
      }),
    });
  }

  getSemanticSearch(form: SemanticSearchFormGroup): ISemanticSearch | NewSemanticSearch {
    return this.convertSemanticSearchRawValueToSemanticSearch(
      form.getRawValue() as SemanticSearchFormRawValue | NewSemanticSearchFormRawValue,
    );
  }

  resetForm(form: SemanticSearchFormGroup, semanticSearch: SemanticSearchFormGroupInput): void {
    const semanticSearchRawValue = this.convertSemanticSearchToSemanticSearchRawValue({ ...this.getFormDefaults(), ...semanticSearch });
    form.reset({
      ...semanticSearchRawValue,
      id: { value: semanticSearchRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): SemanticSearchFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      searchDate: currentTime,
    };
  }

  private convertSemanticSearchRawValueToSemanticSearch(
    rawSemanticSearch: SemanticSearchFormRawValue | NewSemanticSearchFormRawValue,
  ): ISemanticSearch | NewSemanticSearch {
    return {
      ...rawSemanticSearch,
      searchDate: dayjs(rawSemanticSearch.searchDate, DATE_TIME_FORMAT),
    };
  }

  private convertSemanticSearchToSemanticSearchRawValue(
    semanticSearch: ISemanticSearch | (Partial<NewSemanticSearch> & SemanticSearchFormDefaults),
  ): SemanticSearchFormRawValue | PartialWithRequiredKeyOf<NewSemanticSearchFormRawValue> {
    return {
      ...semanticSearch,
      searchDate: semanticSearch.searchDate ? semanticSearch.searchDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
