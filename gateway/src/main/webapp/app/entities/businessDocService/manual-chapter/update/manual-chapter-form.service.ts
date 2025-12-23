import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IManualChapter, NewManualChapter } from '../manual-chapter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IManualChapter for edit and NewManualChapterFormGroupInput for create.
 */
type ManualChapterFormGroupInput = IManualChapter | PartialWithRequiredKeyOf<NewManualChapter>;

type ManualChapterFormDefaults = Pick<NewManualChapter, 'id'>;

type ManualChapterFormGroupContent = {
  id: FormControl<IManualChapter['id'] | NewManualChapter['id']>;
  manualId: FormControl<IManualChapter['manualId']>;
  chapterNumber: FormControl<IManualChapter['chapterNumber']>;
  title: FormControl<IManualChapter['title']>;
  content: FormControl<IManualChapter['content']>;
  pageStart: FormControl<IManualChapter['pageStart']>;
  pageEnd: FormControl<IManualChapter['pageEnd']>;
  level: FormControl<IManualChapter['level']>;
  displayOrder: FormControl<IManualChapter['displayOrder']>;
  manual: FormControl<IManualChapter['manual']>;
  parentChapter: FormControl<IManualChapter['parentChapter']>;
};

export type ManualChapterFormGroup = FormGroup<ManualChapterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ManualChapterFormService {
  createManualChapterFormGroup(manualChapter?: ManualChapterFormGroupInput): ManualChapterFormGroup {
    const manualChapterRawValue = {
      ...this.getFormDefaults(),
      ...(manualChapter ?? { id: null }),
    };
    return new FormGroup<ManualChapterFormGroupContent>({
      id: new FormControl(
        { value: manualChapterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      manualId: new FormControl(manualChapterRawValue.manualId, {
        validators: [Validators.required],
      }),
      chapterNumber: new FormControl(manualChapterRawValue.chapterNumber, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      title: new FormControl(manualChapterRawValue.title, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      content: new FormControl(manualChapterRawValue.content),
      pageStart: new FormControl(manualChapterRawValue.pageStart),
      pageEnd: new FormControl(manualChapterRawValue.pageEnd),
      level: new FormControl(manualChapterRawValue.level, {
        validators: [Validators.required],
      }),
      displayOrder: new FormControl(manualChapterRawValue.displayOrder),
      manual: new FormControl(manualChapterRawValue.manual, {
        validators: [Validators.required],
      }),
      parentChapter: new FormControl(manualChapterRawValue.parentChapter),
    });
  }

  getManualChapter(form: ManualChapterFormGroup): IManualChapter | NewManualChapter {
    return form.getRawValue() as IManualChapter | NewManualChapter;
  }

  resetForm(form: ManualChapterFormGroup, manualChapter: ManualChapterFormGroupInput): void {
    const manualChapterRawValue = { ...this.getFormDefaults(), ...manualChapter };
    form.reset({
      ...manualChapterRawValue,
      id: { value: manualChapterRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ManualChapterFormDefaults {
    return {
      id: null,
    };
  }
}
