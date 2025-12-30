import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISearchIndex, NewSearchIndex } from '../search-index.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISearchIndex for edit and NewSearchIndexFormGroupInput for create.
 */
type SearchIndexFormGroupInput = ISearchIndex | PartialWithRequiredKeyOf<NewSearchIndex>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISearchIndex | NewSearchIndex> = Omit<T, 'indexedDate' | 'lastUpdated'> & {
  indexedDate?: string | null;
  lastUpdated?: string | null;
};

type SearchIndexFormRawValue = FormValueOf<ISearchIndex>;

type NewSearchIndexFormRawValue = FormValueOf<NewSearchIndex>;

type SearchIndexFormDefaults = Pick<NewSearchIndex, 'id' | 'indexedDate' | 'lastUpdated'>;

type SearchIndexFormGroupContent = {
  id: FormControl<SearchIndexFormRawValue['id'] | NewSearchIndex['id']>;
  documentSha256: FormControl<SearchIndexFormRawValue['documentSha256']>;
  indexedContent: FormControl<SearchIndexFormRawValue['indexedContent']>;
  metadata: FormControl<SearchIndexFormRawValue['metadata']>;
  tags: FormControl<SearchIndexFormRawValue['tags']>;
  correspondents: FormControl<SearchIndexFormRawValue['correspondents']>;
  extractedEntities: FormControl<SearchIndexFormRawValue['extractedEntities']>;
  indexedDate: FormControl<SearchIndexFormRawValue['indexedDate']>;
  lastUpdated: FormControl<SearchIndexFormRawValue['lastUpdated']>;
};

export type SearchIndexFormGroup = FormGroup<SearchIndexFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SearchIndexFormService {
  createSearchIndexFormGroup(searchIndex: SearchIndexFormGroupInput = { id: null }): SearchIndexFormGroup {
    const searchIndexRawValue = this.convertSearchIndexToSearchIndexRawValue({
      ...this.getFormDefaults(),
      ...searchIndex,
    });
    return new FormGroup<SearchIndexFormGroupContent>({
      id: new FormControl(
        { value: searchIndexRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentSha256: new FormControl(searchIndexRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      indexedContent: new FormControl(searchIndexRawValue.indexedContent, {
        validators: [Validators.required],
      }),
      metadata: new FormControl(searchIndexRawValue.metadata),
      tags: new FormControl(searchIndexRawValue.tags, {
        validators: [Validators.maxLength(2000)],
      }),
      correspondents: new FormControl(searchIndexRawValue.correspondents, {
        validators: [Validators.maxLength(1000)],
      }),
      extractedEntities: new FormControl(searchIndexRawValue.extractedEntities),
      indexedDate: new FormControl(searchIndexRawValue.indexedDate, {
        validators: [Validators.required],
      }),
      lastUpdated: new FormControl(searchIndexRawValue.lastUpdated),
    });
  }

  getSearchIndex(form: SearchIndexFormGroup): ISearchIndex | NewSearchIndex {
    return this.convertSearchIndexRawValueToSearchIndex(form.getRawValue() as SearchIndexFormRawValue | NewSearchIndexFormRawValue);
  }

  resetForm(form: SearchIndexFormGroup, searchIndex: SearchIndexFormGroupInput): void {
    const searchIndexRawValue = this.convertSearchIndexToSearchIndexRawValue({ ...this.getFormDefaults(), ...searchIndex });
    form.reset(
      {
        ...searchIndexRawValue,
        id: { value: searchIndexRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SearchIndexFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      indexedDate: currentTime,
      lastUpdated: currentTime,
    };
  }

  private convertSearchIndexRawValueToSearchIndex(
    rawSearchIndex: SearchIndexFormRawValue | NewSearchIndexFormRawValue,
  ): ISearchIndex | NewSearchIndex {
    return {
      ...rawSearchIndex,
      indexedDate: dayjs(rawSearchIndex.indexedDate, DATE_TIME_FORMAT),
      lastUpdated: dayjs(rawSearchIndex.lastUpdated, DATE_TIME_FORMAT),
    };
  }

  private convertSearchIndexToSearchIndexRawValue(
    searchIndex: ISearchIndex | (Partial<NewSearchIndex> & SearchIndexFormDefaults),
  ): SearchIndexFormRawValue | PartialWithRequiredKeyOf<NewSearchIndexFormRawValue> {
    return {
      ...searchIndex,
      indexedDate: searchIndex.indexedDate ? searchIndex.indexedDate.format(DATE_TIME_FORMAT) : undefined,
      lastUpdated: searchIndex.lastUpdated ? searchIndex.lastUpdated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
