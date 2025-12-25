import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISearchQuery, NewSearchQuery } from '../search-query.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISearchQuery for edit and NewSearchQueryFormGroupInput for create.
 */
type SearchQueryFormGroupInput = ISearchQuery | PartialWithRequiredKeyOf<NewSearchQuery>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISearchQuery | NewSearchQuery> = Omit<T, 'searchDate'> & {
  searchDate?: string | null;
};

type SearchQueryFormRawValue = FormValueOf<ISearchQuery>;

type NewSearchQueryFormRawValue = FormValueOf<NewSearchQuery>;

type SearchQueryFormDefaults = Pick<NewSearchQuery, 'id' | 'searchDate' | 'isRelevant'>;

type SearchQueryFormGroupContent = {
  id: FormControl<SearchQueryFormRawValue['id'] | NewSearchQuery['id']>;
  query: FormControl<SearchQueryFormRawValue['query']>;
  filters: FormControl<SearchQueryFormRawValue['filters']>;
  resultCount: FormControl<SearchQueryFormRawValue['resultCount']>;
  executionTime: FormControl<SearchQueryFormRawValue['executionTime']>;
  userId: FormControl<SearchQueryFormRawValue['userId']>;
  searchDate: FormControl<SearchQueryFormRawValue['searchDate']>;
  isRelevant: FormControl<SearchQueryFormRawValue['isRelevant']>;
};

export type SearchQueryFormGroup = FormGroup<SearchQueryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SearchQueryFormService {
  createSearchQueryFormGroup(searchQuery: SearchQueryFormGroupInput = { id: null }): SearchQueryFormGroup {
    const searchQueryRawValue = this.convertSearchQueryToSearchQueryRawValue({
      ...this.getFormDefaults(),
      ...searchQuery,
    });
    return new FormGroup<SearchQueryFormGroupContent>({
      id: new FormControl(
        { value: searchQueryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      query: new FormControl(searchQueryRawValue.query, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      filters: new FormControl(searchQueryRawValue.filters),
      resultCount: new FormControl(searchQueryRawValue.resultCount),
      executionTime: new FormControl(searchQueryRawValue.executionTime),
      userId: new FormControl(searchQueryRawValue.userId, {
        validators: [Validators.maxLength(50)],
      }),
      searchDate: new FormControl(searchQueryRawValue.searchDate, {
        validators: [Validators.required],
      }),
      isRelevant: new FormControl(searchQueryRawValue.isRelevant),
    });
  }

  getSearchQuery(form: SearchQueryFormGroup): ISearchQuery | NewSearchQuery {
    return this.convertSearchQueryRawValueToSearchQuery(form.getRawValue() as SearchQueryFormRawValue | NewSearchQueryFormRawValue);
  }

  resetForm(form: SearchQueryFormGroup, searchQuery: SearchQueryFormGroupInput): void {
    const searchQueryRawValue = this.convertSearchQueryToSearchQueryRawValue({ ...this.getFormDefaults(), ...searchQuery });
    form.reset(
      {
        ...searchQueryRawValue,
        id: { value: searchQueryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SearchQueryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      searchDate: currentTime,
      isRelevant: false,
    };
  }

  private convertSearchQueryRawValueToSearchQuery(
    rawSearchQuery: SearchQueryFormRawValue | NewSearchQueryFormRawValue,
  ): ISearchQuery | NewSearchQuery {
    return {
      ...rawSearchQuery,
      searchDate: dayjs(rawSearchQuery.searchDate, DATE_TIME_FORMAT),
    };
  }

  private convertSearchQueryToSearchQueryRawValue(
    searchQuery: ISearchQuery | (Partial<NewSearchQuery> & SearchQueryFormDefaults),
  ): SearchQueryFormRawValue | PartialWithRequiredKeyOf<NewSearchQueryFormRawValue> {
    return {
      ...searchQuery,
      searchDate: searchQuery.searchDate ? searchQuery.searchDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
