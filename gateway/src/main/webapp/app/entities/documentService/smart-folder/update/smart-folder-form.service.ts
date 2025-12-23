import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISmartFolder, NewSmartFolder } from '../smart-folder.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISmartFolder for edit and NewSmartFolderFormGroupInput for create.
 */
type SmartFolderFormGroupInput = ISmartFolder | PartialWithRequiredKeyOf<NewSmartFolder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISmartFolder | NewSmartFolder> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type SmartFolderFormRawValue = FormValueOf<ISmartFolder>;

type NewSmartFolderFormRawValue = FormValueOf<NewSmartFolder>;

type SmartFolderFormDefaults = Pick<NewSmartFolder, 'id' | 'autoRefresh' | 'isPublic' | 'createdDate'>;

type SmartFolderFormGroupContent = {
  id: FormControl<SmartFolderFormRawValue['id'] | NewSmartFolder['id']>;
  name: FormControl<SmartFolderFormRawValue['name']>;
  queryJson: FormControl<SmartFolderFormRawValue['queryJson']>;
  autoRefresh: FormControl<SmartFolderFormRawValue['autoRefresh']>;
  isPublic: FormControl<SmartFolderFormRawValue['isPublic']>;
  createdBy: FormControl<SmartFolderFormRawValue['createdBy']>;
  createdDate: FormControl<SmartFolderFormRawValue['createdDate']>;
};

export type SmartFolderFormGroup = FormGroup<SmartFolderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SmartFolderFormService {
  createSmartFolderFormGroup(smartFolder?: SmartFolderFormGroupInput): SmartFolderFormGroup {
    const smartFolderRawValue = this.convertSmartFolderToSmartFolderRawValue({
      ...this.getFormDefaults(),
      ...(smartFolder ?? { id: null }),
    });
    return new FormGroup<SmartFolderFormGroupContent>({
      id: new FormControl(
        { value: smartFolderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(smartFolderRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      queryJson: new FormControl(smartFolderRawValue.queryJson, {
        validators: [Validators.required],
      }),
      autoRefresh: new FormControl(smartFolderRawValue.autoRefresh, {
        validators: [Validators.required],
      }),
      isPublic: new FormControl(smartFolderRawValue.isPublic, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(smartFolderRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(smartFolderRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getSmartFolder(form: SmartFolderFormGroup): ISmartFolder | NewSmartFolder {
    return this.convertSmartFolderRawValueToSmartFolder(form.getRawValue() as SmartFolderFormRawValue | NewSmartFolderFormRawValue);
  }

  resetForm(form: SmartFolderFormGroup, smartFolder: SmartFolderFormGroupInput): void {
    const smartFolderRawValue = this.convertSmartFolderToSmartFolderRawValue({ ...this.getFormDefaults(), ...smartFolder });
    form.reset({
      ...smartFolderRawValue,
      id: { value: smartFolderRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): SmartFolderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      autoRefresh: false,
      isPublic: false,
      createdDate: currentTime,
    };
  }

  private convertSmartFolderRawValueToSmartFolder(
    rawSmartFolder: SmartFolderFormRawValue | NewSmartFolderFormRawValue,
  ): ISmartFolder | NewSmartFolder {
    return {
      ...rawSmartFolder,
      createdDate: dayjs(rawSmartFolder.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertSmartFolderToSmartFolderRawValue(
    smartFolder: ISmartFolder | (Partial<NewSmartFolder> & SmartFolderFormDefaults),
  ): SmartFolderFormRawValue | PartialWithRequiredKeyOf<NewSmartFolderFormRawValue> {
    return {
      ...smartFolder,
      createdDate: smartFolder.createdDate ? smartFolder.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
