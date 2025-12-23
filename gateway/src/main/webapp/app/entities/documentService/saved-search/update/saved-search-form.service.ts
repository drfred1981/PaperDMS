import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISavedSearch, NewSavedSearch } from '../saved-search.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISavedSearch for edit and NewSavedSearchFormGroupInput for create.
 */
type SavedSearchFormGroupInput = ISavedSearch | PartialWithRequiredKeyOf<NewSavedSearch>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISavedSearch | NewSavedSearch> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type SavedSearchFormRawValue = FormValueOf<ISavedSearch>;

type NewSavedSearchFormRawValue = FormValueOf<NewSavedSearch>;

type SavedSearchFormDefaults = Pick<NewSavedSearch, 'id' | 'isPublic' | 'isAlert' | 'createdDate'>;

type SavedSearchFormGroupContent = {
  id: FormControl<SavedSearchFormRawValue['id'] | NewSavedSearch['id']>;
  name: FormControl<SavedSearchFormRawValue['name']>;
  query: FormControl<SavedSearchFormRawValue['query']>;
  isPublic: FormControl<SavedSearchFormRawValue['isPublic']>;
  isAlert: FormControl<SavedSearchFormRawValue['isAlert']>;
  alertFrequency: FormControl<SavedSearchFormRawValue['alertFrequency']>;
  userId: FormControl<SavedSearchFormRawValue['userId']>;
  createdDate: FormControl<SavedSearchFormRawValue['createdDate']>;
};

export type SavedSearchFormGroup = FormGroup<SavedSearchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SavedSearchFormService {
  createSavedSearchFormGroup(savedSearch?: SavedSearchFormGroupInput): SavedSearchFormGroup {
    const savedSearchRawValue = this.convertSavedSearchToSavedSearchRawValue({
      ...this.getFormDefaults(),
      ...(savedSearch ?? { id: null }),
    });
    return new FormGroup<SavedSearchFormGroupContent>({
      id: new FormControl(
        { value: savedSearchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(savedSearchRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      query: new FormControl(savedSearchRawValue.query, {
        validators: [Validators.required],
      }),
      isPublic: new FormControl(savedSearchRawValue.isPublic, {
        validators: [Validators.required],
      }),
      isAlert: new FormControl(savedSearchRawValue.isAlert, {
        validators: [Validators.required],
      }),
      alertFrequency: new FormControl(savedSearchRawValue.alertFrequency),
      userId: new FormControl(savedSearchRawValue.userId, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(savedSearchRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getSavedSearch(form: SavedSearchFormGroup): ISavedSearch | NewSavedSearch {
    return this.convertSavedSearchRawValueToSavedSearch(form.getRawValue() as SavedSearchFormRawValue | NewSavedSearchFormRawValue);
  }

  resetForm(form: SavedSearchFormGroup, savedSearch: SavedSearchFormGroupInput): void {
    const savedSearchRawValue = this.convertSavedSearchToSavedSearchRawValue({ ...this.getFormDefaults(), ...savedSearch });
    form.reset({
      ...savedSearchRawValue,
      id: { value: savedSearchRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): SavedSearchFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isPublic: false,
      isAlert: false,
      createdDate: currentTime,
    };
  }

  private convertSavedSearchRawValueToSavedSearch(
    rawSavedSearch: SavedSearchFormRawValue | NewSavedSearchFormRawValue,
  ): ISavedSearch | NewSavedSearch {
    return {
      ...rawSavedSearch,
      createdDate: dayjs(rawSavedSearch.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertSavedSearchToSavedSearchRawValue(
    savedSearch: ISavedSearch | (Partial<NewSavedSearch> & SavedSearchFormDefaults),
  ): SavedSearchFormRawValue | PartialWithRequiredKeyOf<NewSavedSearchFormRawValue> {
    return {
      ...savedSearch,
      createdDate: savedSearch.createdDate ? savedSearch.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
