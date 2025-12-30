import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMetaBookmark, NewMetaBookmark } from '../meta-bookmark.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMetaBookmark for edit and NewMetaBookmarkFormGroupInput for create.
 */
type MetaBookmarkFormGroupInput = IMetaBookmark | PartialWithRequiredKeyOf<NewMetaBookmark>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMetaBookmark | NewMetaBookmark> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type MetaBookmarkFormRawValue = FormValueOf<IMetaBookmark>;

type NewMetaBookmarkFormRawValue = FormValueOf<NewMetaBookmark>;

type MetaBookmarkFormDefaults = Pick<NewMetaBookmark, 'id' | 'createdDate'>;

type MetaBookmarkFormGroupContent = {
  id: FormControl<MetaBookmarkFormRawValue['id'] | NewMetaBookmark['id']>;
  userId: FormControl<MetaBookmarkFormRawValue['userId']>;
  entityType: FormControl<MetaBookmarkFormRawValue['entityType']>;
  entityName: FormControl<MetaBookmarkFormRawValue['entityName']>;
  createdDate: FormControl<MetaBookmarkFormRawValue['createdDate']>;
};

export type MetaBookmarkFormGroup = FormGroup<MetaBookmarkFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MetaBookmarkFormService {
  createMetaBookmarkFormGroup(metaBookmark: MetaBookmarkFormGroupInput = { id: null }): MetaBookmarkFormGroup {
    const metaBookmarkRawValue = this.convertMetaBookmarkToMetaBookmarkRawValue({
      ...this.getFormDefaults(),
      ...metaBookmark,
    });
    return new FormGroup<MetaBookmarkFormGroupContent>({
      id: new FormControl(
        { value: metaBookmarkRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      userId: new FormControl(metaBookmarkRawValue.userId, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      entityType: new FormControl(metaBookmarkRawValue.entityType, {
        validators: [Validators.required],
      }),
      entityName: new FormControl(metaBookmarkRawValue.entityName, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(metaBookmarkRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getMetaBookmark(form: MetaBookmarkFormGroup): IMetaBookmark | NewMetaBookmark {
    return this.convertMetaBookmarkRawValueToMetaBookmark(form.getRawValue() as MetaBookmarkFormRawValue | NewMetaBookmarkFormRawValue);
  }

  resetForm(form: MetaBookmarkFormGroup, metaBookmark: MetaBookmarkFormGroupInput): void {
    const metaBookmarkRawValue = this.convertMetaBookmarkToMetaBookmarkRawValue({ ...this.getFormDefaults(), ...metaBookmark });
    form.reset(
      {
        ...metaBookmarkRawValue,
        id: { value: metaBookmarkRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MetaBookmarkFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertMetaBookmarkRawValueToMetaBookmark(
    rawMetaBookmark: MetaBookmarkFormRawValue | NewMetaBookmarkFormRawValue,
  ): IMetaBookmark | NewMetaBookmark {
    return {
      ...rawMetaBookmark,
      createdDate: dayjs(rawMetaBookmark.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertMetaBookmarkToMetaBookmarkRawValue(
    metaBookmark: IMetaBookmark | (Partial<NewMetaBookmark> & MetaBookmarkFormDefaults),
  ): MetaBookmarkFormRawValue | PartialWithRequiredKeyOf<NewMetaBookmarkFormRawValue> {
    return {
      ...metaBookmark,
      createdDate: metaBookmark.createdDate ? metaBookmark.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
