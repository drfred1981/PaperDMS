import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMetaMetaTagCategory, NewMetaMetaTagCategory } from '../meta-meta-tag-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMetaMetaTagCategory for edit and NewMetaMetaTagCategoryFormGroupInput for create.
 */
type MetaMetaTagCategoryFormGroupInput = IMetaMetaTagCategory | PartialWithRequiredKeyOf<NewMetaMetaTagCategory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMetaMetaTagCategory | NewMetaMetaTagCategory> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type MetaMetaTagCategoryFormRawValue = FormValueOf<IMetaMetaTagCategory>;

type NewMetaMetaTagCategoryFormRawValue = FormValueOf<NewMetaMetaTagCategory>;

type MetaMetaTagCategoryFormDefaults = Pick<NewMetaMetaTagCategory, 'id' | 'isSystem' | 'createdDate'>;

type MetaMetaTagCategoryFormGroupContent = {
  id: FormControl<MetaMetaTagCategoryFormRawValue['id'] | NewMetaMetaTagCategory['id']>;
  name: FormControl<MetaMetaTagCategoryFormRawValue['name']>;
  color: FormControl<MetaMetaTagCategoryFormRawValue['color']>;
  displayOrder: FormControl<MetaMetaTagCategoryFormRawValue['displayOrder']>;
  isSystem: FormControl<MetaMetaTagCategoryFormRawValue['isSystem']>;
  createdDate: FormControl<MetaMetaTagCategoryFormRawValue['createdDate']>;
  createdBy: FormControl<MetaMetaTagCategoryFormRawValue['createdBy']>;
  parent: FormControl<MetaMetaTagCategoryFormRawValue['parent']>;
};

export type MetaMetaTagCategoryFormGroup = FormGroup<MetaMetaTagCategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MetaMetaTagCategoryFormService {
  createMetaMetaTagCategoryFormGroup(metaMetaTagCategory: MetaMetaTagCategoryFormGroupInput = { id: null }): MetaMetaTagCategoryFormGroup {
    const metaMetaTagCategoryRawValue = this.convertMetaMetaTagCategoryToMetaMetaTagCategoryRawValue({
      ...this.getFormDefaults(),
      ...metaMetaTagCategory,
    });
    return new FormGroup<MetaMetaTagCategoryFormGroupContent>({
      id: new FormControl(
        { value: metaMetaTagCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(metaMetaTagCategoryRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      color: new FormControl(metaMetaTagCategoryRawValue.color, {
        validators: [Validators.maxLength(7)],
      }),
      displayOrder: new FormControl(metaMetaTagCategoryRawValue.displayOrder),
      isSystem: new FormControl(metaMetaTagCategoryRawValue.isSystem, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(metaMetaTagCategoryRawValue.createdDate, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(metaMetaTagCategoryRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      parent: new FormControl(metaMetaTagCategoryRawValue.parent),
    });
  }

  getMetaMetaTagCategory(form: MetaMetaTagCategoryFormGroup): IMetaMetaTagCategory | NewMetaMetaTagCategory {
    return this.convertMetaMetaTagCategoryRawValueToMetaMetaTagCategory(
      form.getRawValue() as MetaMetaTagCategoryFormRawValue | NewMetaMetaTagCategoryFormRawValue,
    );
  }

  resetForm(form: MetaMetaTagCategoryFormGroup, metaMetaTagCategory: MetaMetaTagCategoryFormGroupInput): void {
    const metaMetaTagCategoryRawValue = this.convertMetaMetaTagCategoryToMetaMetaTagCategoryRawValue({
      ...this.getFormDefaults(),
      ...metaMetaTagCategory,
    });
    form.reset(
      {
        ...metaMetaTagCategoryRawValue,
        id: { value: metaMetaTagCategoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MetaMetaTagCategoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isSystem: false,
      createdDate: currentTime,
    };
  }

  private convertMetaMetaTagCategoryRawValueToMetaMetaTagCategory(
    rawMetaMetaTagCategory: MetaMetaTagCategoryFormRawValue | NewMetaMetaTagCategoryFormRawValue,
  ): IMetaMetaTagCategory | NewMetaMetaTagCategory {
    return {
      ...rawMetaMetaTagCategory,
      createdDate: dayjs(rawMetaMetaTagCategory.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertMetaMetaTagCategoryToMetaMetaTagCategoryRawValue(
    metaMetaTagCategory: IMetaMetaTagCategory | (Partial<NewMetaMetaTagCategory> & MetaMetaTagCategoryFormDefaults),
  ): MetaMetaTagCategoryFormRawValue | PartialWithRequiredKeyOf<NewMetaMetaTagCategoryFormRawValue> {
    return {
      ...metaMetaTagCategory,
      createdDate: metaMetaTagCategory.createdDate ? metaMetaTagCategory.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
