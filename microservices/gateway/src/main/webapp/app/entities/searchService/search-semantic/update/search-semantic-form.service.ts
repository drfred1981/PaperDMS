import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISearchSemantic, NewSearchSemantic } from '../search-semantic.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISearchSemantic for edit and NewSearchSemanticFormGroupInput for create.
 */
type SearchSemanticFormGroupInput = ISearchSemantic | PartialWithRequiredKeyOf<NewSearchSemantic>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISearchSemantic | NewSearchSemantic> = Omit<T, 'searchDate'> & {
  searchDate?: string | null;
};

type SearchSemanticFormRawValue = FormValueOf<ISearchSemantic>;

type NewSearchSemanticFormRawValue = FormValueOf<NewSearchSemantic>;

type SearchSemanticFormDefaults = Pick<NewSearchSemantic, 'id' | 'searchDate'>;

type SearchSemanticFormGroupContent = {
  id: FormControl<SearchSemanticFormRawValue['id'] | NewSearchSemantic['id']>;
  query: FormControl<SearchSemanticFormRawValue['query']>;
  queryEmbedding: FormControl<SearchSemanticFormRawValue['queryEmbedding']>;
  results: FormControl<SearchSemanticFormRawValue['results']>;
  relevanceScores: FormControl<SearchSemanticFormRawValue['relevanceScores']>;
  modelUsed: FormControl<SearchSemanticFormRawValue['modelUsed']>;
  executionTime: FormControl<SearchSemanticFormRawValue['executionTime']>;
  userId: FormControl<SearchSemanticFormRawValue['userId']>;
  searchDate: FormControl<SearchSemanticFormRawValue['searchDate']>;
};

export type SearchSemanticFormGroup = FormGroup<SearchSemanticFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SearchSemanticFormService {
  createSearchSemanticFormGroup(searchSemantic: SearchSemanticFormGroupInput = { id: null }): SearchSemanticFormGroup {
    const searchSemanticRawValue = this.convertSearchSemanticToSearchSemanticRawValue({
      ...this.getFormDefaults(),
      ...searchSemantic,
    });
    return new FormGroup<SearchSemanticFormGroupContent>({
      id: new FormControl(
        { value: searchSemanticRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      query: new FormControl(searchSemanticRawValue.query, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      queryEmbedding: new FormControl(searchSemanticRawValue.queryEmbedding, {
        validators: [Validators.required],
      }),
      results: new FormControl(searchSemanticRawValue.results, {
        validators: [Validators.required],
      }),
      relevanceScores: new FormControl(searchSemanticRawValue.relevanceScores),
      modelUsed: new FormControl(searchSemanticRawValue.modelUsed, {
        validators: [Validators.maxLength(100)],
      }),
      executionTime: new FormControl(searchSemanticRawValue.executionTime),
      userId: new FormControl(searchSemanticRawValue.userId, {
        validators: [Validators.maxLength(50)],
      }),
      searchDate: new FormControl(searchSemanticRawValue.searchDate, {
        validators: [Validators.required],
      }),
    });
  }

  getSearchSemantic(form: SearchSemanticFormGroup): ISearchSemantic | NewSearchSemantic {
    return this.convertSearchSemanticRawValueToSearchSemantic(
      form.getRawValue() as SearchSemanticFormRawValue | NewSearchSemanticFormRawValue,
    );
  }

  resetForm(form: SearchSemanticFormGroup, searchSemantic: SearchSemanticFormGroupInput): void {
    const searchSemanticRawValue = this.convertSearchSemanticToSearchSemanticRawValue({ ...this.getFormDefaults(), ...searchSemantic });
    form.reset(
      {
        ...searchSemanticRawValue,
        id: { value: searchSemanticRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SearchSemanticFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      searchDate: currentTime,
    };
  }

  private convertSearchSemanticRawValueToSearchSemantic(
    rawSearchSemantic: SearchSemanticFormRawValue | NewSearchSemanticFormRawValue,
  ): ISearchSemantic | NewSearchSemantic {
    return {
      ...rawSearchSemantic,
      searchDate: dayjs(rawSearchSemantic.searchDate, DATE_TIME_FORMAT),
    };
  }

  private convertSearchSemanticToSearchSemanticRawValue(
    searchSemantic: ISearchSemantic | (Partial<NewSearchSemantic> & SearchSemanticFormDefaults),
  ): SearchSemanticFormRawValue | PartialWithRequiredKeyOf<NewSearchSemanticFormRawValue> {
    return {
      ...searchSemantic,
      searchDate: searchSemantic.searchDate ? searchSemantic.searchDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
