import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMetaTag, NewMetaTag } from '../meta-tag.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMetaTag for edit and NewMetaTagFormGroupInput for create.
 */
type MetaTagFormGroupInput = IMetaTag | PartialWithRequiredKeyOf<NewMetaTag>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMetaTag | NewMetaTag> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type MetaTagFormRawValue = FormValueOf<IMetaTag>;

type NewMetaTagFormRawValue = FormValueOf<NewMetaTag>;

type MetaTagFormDefaults = Pick<NewMetaTag, 'id' | 'isSystem' | 'createdDate'>;

type MetaTagFormGroupContent = {
  id: FormControl<MetaTagFormRawValue['id'] | NewMetaTag['id']>;
  name: FormControl<MetaTagFormRawValue['name']>;
  color: FormControl<MetaTagFormRawValue['color']>;
  description: FormControl<MetaTagFormRawValue['description']>;
  usageCount: FormControl<MetaTagFormRawValue['usageCount']>;
  isSystem: FormControl<MetaTagFormRawValue['isSystem']>;
  createdDate: FormControl<MetaTagFormRawValue['createdDate']>;
  createdBy: FormControl<MetaTagFormRawValue['createdBy']>;
  metaMetaTagCategory: FormControl<MetaTagFormRawValue['metaMetaTagCategory']>;
};

export type MetaTagFormGroup = FormGroup<MetaTagFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MetaTagFormService {
  createMetaTagFormGroup(metaTag: MetaTagFormGroupInput = { id: null }): MetaTagFormGroup {
    const metaTagRawValue = this.convertMetaTagToMetaTagRawValue({
      ...this.getFormDefaults(),
      ...metaTag,
    });
    return new FormGroup<MetaTagFormGroupContent>({
      id: new FormControl(
        { value: metaTagRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(metaTagRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      color: new FormControl(metaTagRawValue.color, {
        validators: [Validators.maxLength(7)],
      }),
      description: new FormControl(metaTagRawValue.description, {
        validators: [Validators.maxLength(500)],
      }),
      usageCount: new FormControl(metaTagRawValue.usageCount),
      isSystem: new FormControl(metaTagRawValue.isSystem, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(metaTagRawValue.createdDate, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(metaTagRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      metaMetaTagCategory: new FormControl(metaTagRawValue.metaMetaTagCategory),
    });
  }

  getMetaTag(form: MetaTagFormGroup): IMetaTag | NewMetaTag {
    return this.convertMetaTagRawValueToMetaTag(form.getRawValue() as MetaTagFormRawValue | NewMetaTagFormRawValue);
  }

  resetForm(form: MetaTagFormGroup, metaTag: MetaTagFormGroupInput): void {
    const metaTagRawValue = this.convertMetaTagToMetaTagRawValue({ ...this.getFormDefaults(), ...metaTag });
    form.reset(
      {
        ...metaTagRawValue,
        id: { value: metaTagRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MetaTagFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isSystem: false,
      createdDate: currentTime,
    };
  }

  private convertMetaTagRawValueToMetaTag(rawMetaTag: MetaTagFormRawValue | NewMetaTagFormRawValue): IMetaTag | NewMetaTag {
    return {
      ...rawMetaTag,
      createdDate: dayjs(rawMetaTag.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertMetaTagToMetaTagRawValue(
    metaTag: IMetaTag | (Partial<NewMetaTag> & MetaTagFormDefaults),
  ): MetaTagFormRawValue | PartialWithRequiredKeyOf<NewMetaTagFormRawValue> {
    return {
      ...metaTag,
      createdDate: metaTag.createdDate ? metaTag.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
