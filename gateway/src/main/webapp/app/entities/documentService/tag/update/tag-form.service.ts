import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITag, NewTag } from '../tag.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITag for edit and NewTagFormGroupInput for create.
 */
type TagFormGroupInput = ITag | PartialWithRequiredKeyOf<NewTag>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITag | NewTag> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type TagFormRawValue = FormValueOf<ITag>;

type NewTagFormRawValue = FormValueOf<NewTag>;

type TagFormDefaults = Pick<NewTag, 'id' | 'isSystem' | 'createdDate'>;

type TagFormGroupContent = {
  id: FormControl<TagFormRawValue['id'] | NewTag['id']>;
  name: FormControl<TagFormRawValue['name']>;
  color: FormControl<TagFormRawValue['color']>;
  description: FormControl<TagFormRawValue['description']>;
  usageCount: FormControl<TagFormRawValue['usageCount']>;
  isSystem: FormControl<TagFormRawValue['isSystem']>;
  createdDate: FormControl<TagFormRawValue['createdDate']>;
  createdBy: FormControl<TagFormRawValue['createdBy']>;
  tagCategory: FormControl<TagFormRawValue['tagCategory']>;
};

export type TagFormGroup = FormGroup<TagFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TagFormService {
  createTagFormGroup(tag?: TagFormGroupInput): TagFormGroup {
    const tagRawValue = this.convertTagToTagRawValue({
      ...this.getFormDefaults(),
      ...(tag ?? { id: null }),
    });
    return new FormGroup<TagFormGroupContent>({
      id: new FormControl(
        { value: tagRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(tagRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      color: new FormControl(tagRawValue.color, {
        validators: [Validators.maxLength(7)],
      }),
      description: new FormControl(tagRawValue.description, {
        validators: [Validators.maxLength(500)],
      }),
      usageCount: new FormControl(tagRawValue.usageCount),
      isSystem: new FormControl(tagRawValue.isSystem, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(tagRawValue.createdDate, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(tagRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      tagCategory: new FormControl(tagRawValue.tagCategory),
    });
  }

  getTag(form: TagFormGroup): ITag | NewTag {
    return this.convertTagRawValueToTag(form.getRawValue() as TagFormRawValue | NewTagFormRawValue);
  }

  resetForm(form: TagFormGroup, tag: TagFormGroupInput): void {
    const tagRawValue = this.convertTagToTagRawValue({ ...this.getFormDefaults(), ...tag });
    form.reset({
      ...tagRawValue,
      id: { value: tagRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TagFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isSystem: false,
      createdDate: currentTime,
    };
  }

  private convertTagRawValueToTag(rawTag: TagFormRawValue | NewTagFormRawValue): ITag | NewTag {
    return {
      ...rawTag,
      createdDate: dayjs(rawTag.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertTagToTagRawValue(
    tag: ITag | (Partial<NewTag> & TagFormDefaults),
  ): TagFormRawValue | PartialWithRequiredKeyOf<NewTagFormRawValue> {
    return {
      ...tag,
      createdDate: tag.createdDate ? tag.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
