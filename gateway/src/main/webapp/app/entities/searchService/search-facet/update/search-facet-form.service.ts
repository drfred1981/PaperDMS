import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISearchFacet, NewSearchFacet } from '../search-facet.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISearchFacet for edit and NewSearchFacetFormGroupInput for create.
 */
type SearchFacetFormGroupInput = ISearchFacet | PartialWithRequiredKeyOf<NewSearchFacet>;

type SearchFacetFormDefaults = Pick<NewSearchFacet, 'id'>;

type SearchFacetFormGroupContent = {
  id: FormControl<ISearchFacet['id'] | NewSearchFacet['id']>;
  searchQueryId: FormControl<ISearchFacet['searchQueryId']>;
  facetName: FormControl<ISearchFacet['facetName']>;
  facetType: FormControl<ISearchFacet['facetType']>;
  values: FormControl<ISearchFacet['values']>;
  counts: FormControl<ISearchFacet['counts']>;
  searchQuery: FormControl<ISearchFacet['searchQuery']>;
};

export type SearchFacetFormGroup = FormGroup<SearchFacetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SearchFacetFormService {
  createSearchFacetFormGroup(searchFacet?: SearchFacetFormGroupInput): SearchFacetFormGroup {
    const searchFacetRawValue = {
      ...this.getFormDefaults(),
      ...(searchFacet ?? { id: null }),
    };
    return new FormGroup<SearchFacetFormGroupContent>({
      id: new FormControl(
        { value: searchFacetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      searchQueryId: new FormControl(searchFacetRawValue.searchQueryId),
      facetName: new FormControl(searchFacetRawValue.facetName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      facetType: new FormControl(searchFacetRawValue.facetType, {
        validators: [Validators.required],
      }),
      values: new FormControl(searchFacetRawValue.values, {
        validators: [Validators.required],
      }),
      counts: new FormControl(searchFacetRawValue.counts, {
        validators: [Validators.required],
      }),
      searchQuery: new FormControl(searchFacetRawValue.searchQuery),
    });
  }

  getSearchFacet(form: SearchFacetFormGroup): ISearchFacet | NewSearchFacet {
    return form.getRawValue() as ISearchFacet | NewSearchFacet;
  }

  resetForm(form: SearchFacetFormGroup, searchFacet: SearchFacetFormGroupInput): void {
    const searchFacetRawValue = { ...this.getFormDefaults(), ...searchFacet };
    form.reset({
      ...searchFacetRawValue,
      id: { value: searchFacetRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): SearchFacetFormDefaults {
    return {
      id: null,
    };
  }
}
