import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBookmark, NewBookmark } from '../bookmark.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBookmark for edit and NewBookmarkFormGroupInput for create.
 */
type BookmarkFormGroupInput = IBookmark | PartialWithRequiredKeyOf<NewBookmark>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBookmark | NewBookmark> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type BookmarkFormRawValue = FormValueOf<IBookmark>;

type NewBookmarkFormRawValue = FormValueOf<NewBookmark>;

type BookmarkFormDefaults = Pick<NewBookmark, 'id' | 'createdDate'>;

type BookmarkFormGroupContent = {
  id: FormControl<BookmarkFormRawValue['id'] | NewBookmark['id']>;
  userId: FormControl<BookmarkFormRawValue['userId']>;
  entityType: FormControl<BookmarkFormRawValue['entityType']>;
  entityId: FormControl<BookmarkFormRawValue['entityId']>;
  createdDate: FormControl<BookmarkFormRawValue['createdDate']>;
};

export type BookmarkFormGroup = FormGroup<BookmarkFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BookmarkFormService {
  createBookmarkFormGroup(bookmark?: BookmarkFormGroupInput): BookmarkFormGroup {
    const bookmarkRawValue = this.convertBookmarkToBookmarkRawValue({
      ...this.getFormDefaults(),
      ...(bookmark ?? { id: null }),
    });
    return new FormGroup<BookmarkFormGroupContent>({
      id: new FormControl(
        { value: bookmarkRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      userId: new FormControl(bookmarkRawValue.userId, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      entityType: new FormControl(bookmarkRawValue.entityType, {
        validators: [Validators.required],
      }),
      entityId: new FormControl(bookmarkRawValue.entityId, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(bookmarkRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getBookmark(form: BookmarkFormGroup): IBookmark | NewBookmark {
    return this.convertBookmarkRawValueToBookmark(form.getRawValue() as BookmarkFormRawValue | NewBookmarkFormRawValue);
  }

  resetForm(form: BookmarkFormGroup, bookmark: BookmarkFormGroupInput): void {
    const bookmarkRawValue = this.convertBookmarkToBookmarkRawValue({ ...this.getFormDefaults(), ...bookmark });
    form.reset({
      ...bookmarkRawValue,
      id: { value: bookmarkRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): BookmarkFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertBookmarkRawValueToBookmark(rawBookmark: BookmarkFormRawValue | NewBookmarkFormRawValue): IBookmark | NewBookmark {
    return {
      ...rawBookmark,
      createdDate: dayjs(rawBookmark.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertBookmarkToBookmarkRawValue(
    bookmark: IBookmark | (Partial<NewBookmark> & BookmarkFormDefaults),
  ): BookmarkFormRawValue | PartialWithRequiredKeyOf<NewBookmarkFormRawValue> {
    return {
      ...bookmark,
      createdDate: bookmark.createdDate ? bookmark.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
