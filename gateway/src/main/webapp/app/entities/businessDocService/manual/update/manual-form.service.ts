import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IManual, NewManual } from '../manual.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IManual for edit and NewManualFormGroupInput for create.
 */
type ManualFormGroupInput = IManual | PartialWithRequiredKeyOf<NewManual>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IManual | NewManual> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type ManualFormRawValue = FormValueOf<IManual>;

type NewManualFormRawValue = FormValueOf<NewManual>;

type ManualFormDefaults = Pick<NewManual, 'id' | 'isPublic' | 'createdDate'>;

type ManualFormGroupContent = {
  id: FormControl<ManualFormRawValue['id'] | NewManual['id']>;
  documentId: FormControl<ManualFormRawValue['documentId']>;
  title: FormControl<ManualFormRawValue['title']>;
  manualType: FormControl<ManualFormRawValue['manualType']>;
  version: FormControl<ManualFormRawValue['version']>;
  language: FormControl<ManualFormRawValue['language']>;
  publicationDate: FormControl<ManualFormRawValue['publicationDate']>;
  pageCount: FormControl<ManualFormRawValue['pageCount']>;
  status: FormControl<ManualFormRawValue['status']>;
  isPublic: FormControl<ManualFormRawValue['isPublic']>;
  createdDate: FormControl<ManualFormRawValue['createdDate']>;
};

export type ManualFormGroup = FormGroup<ManualFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ManualFormService {
  createManualFormGroup(manual: ManualFormGroupInput = { id: null }): ManualFormGroup {
    const manualRawValue = this.convertManualToManualRawValue({
      ...this.getFormDefaults(),
      ...manual,
    });
    return new FormGroup<ManualFormGroupContent>({
      id: new FormControl(
        { value: manualRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(manualRawValue.documentId, {
        validators: [Validators.required],
      }),
      title: new FormControl(manualRawValue.title, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      manualType: new FormControl(manualRawValue.manualType, {
        validators: [Validators.required],
      }),
      version: new FormControl(manualRawValue.version, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      language: new FormControl(manualRawValue.language, {
        validators: [Validators.required, Validators.maxLength(10)],
      }),
      publicationDate: new FormControl(manualRawValue.publicationDate),
      pageCount: new FormControl(manualRawValue.pageCount),
      status: new FormControl(manualRawValue.status, {
        validators: [Validators.required],
      }),
      isPublic: new FormControl(manualRawValue.isPublic, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(manualRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getManual(form: ManualFormGroup): IManual | NewManual {
    return this.convertManualRawValueToManual(form.getRawValue() as ManualFormRawValue | NewManualFormRawValue);
  }

  resetForm(form: ManualFormGroup, manual: ManualFormGroupInput): void {
    const manualRawValue = this.convertManualToManualRawValue({ ...this.getFormDefaults(), ...manual });
    form.reset(
      {
        ...manualRawValue,
        id: { value: manualRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ManualFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isPublic: false,
      createdDate: currentTime,
    };
  }

  private convertManualRawValueToManual(rawManual: ManualFormRawValue | NewManualFormRawValue): IManual | NewManual {
    return {
      ...rawManual,
      createdDate: dayjs(rawManual.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertManualToManualRawValue(
    manual: IManual | (Partial<NewManual> & ManualFormDefaults),
  ): ManualFormRawValue | PartialWithRequiredKeyOf<NewManualFormRawValue> {
    return {
      ...manual,
      createdDate: manual.createdDate ? manual.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
