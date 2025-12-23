import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITagCategory, NewTagCategory } from '../tag-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITagCategory for edit and NewTagCategoryFormGroupInput for create.
 */
type TagCategoryFormGroupInput = ITagCategory | PartialWithRequiredKeyOf<NewTagCategory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITagCategory | NewTagCategory> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type TagCategoryFormRawValue = FormValueOf<ITagCategory>;

type NewTagCategoryFormRawValue = FormValueOf<NewTagCategory>;

type TagCategoryFormDefaults = Pick<NewTagCategory, 'id' | 'isSystem' | 'createdDate'>;

type TagCategoryFormGroupContent = {
  id: FormControl<TagCategoryFormRawValue['id'] | NewTagCategory['id']>;
  name: FormControl<TagCategoryFormRawValue['name']>;
  color: FormControl<TagCategoryFormRawValue['color']>;
  displayOrder: FormControl<TagCategoryFormRawValue['displayOrder']>;
  isSystem: FormControl<TagCategoryFormRawValue['isSystem']>;
  createdDate: FormControl<TagCategoryFormRawValue['createdDate']>;
  createdBy: FormControl<TagCategoryFormRawValue['createdBy']>;
  parent: FormControl<TagCategoryFormRawValue['parent']>;
};

export type TagCategoryFormGroup = FormGroup<TagCategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TagCategoryFormService {
  createTagCategoryFormGroup(tagCategory?: TagCategoryFormGroupInput): TagCategoryFormGroup {
    const tagCategoryRawValue = this.convertTagCategoryToTagCategoryRawValue({
      ...this.getFormDefaults(),
      ...(tagCategory ?? { id: null }),
    });
    return new FormGroup<TagCategoryFormGroupContent>({
      id: new FormControl(
        { value: tagCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(tagCategoryRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      color: new FormControl(tagCategoryRawValue.color, {
        validators: [Validators.maxLength(7)],
      }),
      displayOrder: new FormControl(tagCategoryRawValue.displayOrder),
      isSystem: new FormControl(tagCategoryRawValue.isSystem, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(tagCategoryRawValue.createdDate, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(tagCategoryRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      parent: new FormControl(tagCategoryRawValue.parent),
    });
  }

  getTagCategory(form: TagCategoryFormGroup): ITagCategory | NewTagCategory {
    return this.convertTagCategoryRawValueToTagCategory(form.getRawValue() as TagCategoryFormRawValue | NewTagCategoryFormRawValue);
  }

  resetForm(form: TagCategoryFormGroup, tagCategory: TagCategoryFormGroupInput): void {
    const tagCategoryRawValue = this.convertTagCategoryToTagCategoryRawValue({ ...this.getFormDefaults(), ...tagCategory });
    form.reset({
      ...tagCategoryRawValue,
      id: { value: tagCategoryRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TagCategoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isSystem: false,
      createdDate: currentTime,
    };
  }

  private convertTagCategoryRawValueToTagCategory(
    rawTagCategory: TagCategoryFormRawValue | NewTagCategoryFormRawValue,
  ): ITagCategory | NewTagCategory {
    return {
      ...rawTagCategory,
      createdDate: dayjs(rawTagCategory.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertTagCategoryToTagCategoryRawValue(
    tagCategory: ITagCategory | (Partial<NewTagCategory> & TagCategoryFormDefaults),
  ): TagCategoryFormRawValue | PartialWithRequiredKeyOf<NewTagCategoryFormRawValue> {
    return {
      ...tagCategory,
      createdDate: tagCategory.createdDate ? tagCategory.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
