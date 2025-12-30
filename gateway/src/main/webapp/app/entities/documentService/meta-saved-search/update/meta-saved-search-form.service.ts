import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMetaSavedSearch, NewMetaSavedSearch } from '../meta-saved-search.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMetaSavedSearch for edit and NewMetaSavedSearchFormGroupInput for create.
 */
type MetaSavedSearchFormGroupInput = IMetaSavedSearch | PartialWithRequiredKeyOf<NewMetaSavedSearch>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMetaSavedSearch | NewMetaSavedSearch> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type MetaSavedSearchFormRawValue = FormValueOf<IMetaSavedSearch>;

type NewMetaSavedSearchFormRawValue = FormValueOf<NewMetaSavedSearch>;

type MetaSavedSearchFormDefaults = Pick<NewMetaSavedSearch, 'id' | 'isPublic' | 'isAlert' | 'createdDate'>;

type MetaSavedSearchFormGroupContent = {
  id: FormControl<MetaSavedSearchFormRawValue['id'] | NewMetaSavedSearch['id']>;
  name: FormControl<MetaSavedSearchFormRawValue['name']>;
  query: FormControl<MetaSavedSearchFormRawValue['query']>;
  isPublic: FormControl<MetaSavedSearchFormRawValue['isPublic']>;
  isAlert: FormControl<MetaSavedSearchFormRawValue['isAlert']>;
  alertFrequency: FormControl<MetaSavedSearchFormRawValue['alertFrequency']>;
  userId: FormControl<MetaSavedSearchFormRawValue['userId']>;
  createdDate: FormControl<MetaSavedSearchFormRawValue['createdDate']>;
};

export type MetaSavedSearchFormGroup = FormGroup<MetaSavedSearchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MetaSavedSearchFormService {
  createMetaSavedSearchFormGroup(metaSavedSearch: MetaSavedSearchFormGroupInput = { id: null }): MetaSavedSearchFormGroup {
    const metaSavedSearchRawValue = this.convertMetaSavedSearchToMetaSavedSearchRawValue({
      ...this.getFormDefaults(),
      ...metaSavedSearch,
    });
    return new FormGroup<MetaSavedSearchFormGroupContent>({
      id: new FormControl(
        { value: metaSavedSearchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(metaSavedSearchRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      query: new FormControl(metaSavedSearchRawValue.query, {
        validators: [Validators.required],
      }),
      isPublic: new FormControl(metaSavedSearchRawValue.isPublic, {
        validators: [Validators.required],
      }),
      isAlert: new FormControl(metaSavedSearchRawValue.isAlert, {
        validators: [Validators.required],
      }),
      alertFrequency: new FormControl(metaSavedSearchRawValue.alertFrequency),
      userId: new FormControl(metaSavedSearchRawValue.userId, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(metaSavedSearchRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getMetaSavedSearch(form: MetaSavedSearchFormGroup): IMetaSavedSearch | NewMetaSavedSearch {
    return this.convertMetaSavedSearchRawValueToMetaSavedSearch(
      form.getRawValue() as MetaSavedSearchFormRawValue | NewMetaSavedSearchFormRawValue,
    );
  }

  resetForm(form: MetaSavedSearchFormGroup, metaSavedSearch: MetaSavedSearchFormGroupInput): void {
    const metaSavedSearchRawValue = this.convertMetaSavedSearchToMetaSavedSearchRawValue({ ...this.getFormDefaults(), ...metaSavedSearch });
    form.reset(
      {
        ...metaSavedSearchRawValue,
        id: { value: metaSavedSearchRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MetaSavedSearchFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isPublic: false,
      isAlert: false,
      createdDate: currentTime,
    };
  }

  private convertMetaSavedSearchRawValueToMetaSavedSearch(
    rawMetaSavedSearch: MetaSavedSearchFormRawValue | NewMetaSavedSearchFormRawValue,
  ): IMetaSavedSearch | NewMetaSavedSearch {
    return {
      ...rawMetaSavedSearch,
      createdDate: dayjs(rawMetaSavedSearch.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertMetaSavedSearchToMetaSavedSearchRawValue(
    metaSavedSearch: IMetaSavedSearch | (Partial<NewMetaSavedSearch> & MetaSavedSearchFormDefaults),
  ): MetaSavedSearchFormRawValue | PartialWithRequiredKeyOf<NewMetaSavedSearchFormRawValue> {
    return {
      ...metaSavedSearch,
      createdDate: metaSavedSearch.createdDate ? metaSavedSearch.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
