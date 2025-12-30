import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMetaFolder, NewMetaFolder } from '../meta-folder.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMetaFolder for edit and NewMetaFolderFormGroupInput for create.
 */
type MetaFolderFormGroupInput = IMetaFolder | PartialWithRequiredKeyOf<NewMetaFolder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMetaFolder | NewMetaFolder> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type MetaFolderFormRawValue = FormValueOf<IMetaFolder>;

type NewMetaFolderFormRawValue = FormValueOf<NewMetaFolder>;

type MetaFolderFormDefaults = Pick<NewMetaFolder, 'id' | 'isShared' | 'createdDate'>;

type MetaFolderFormGroupContent = {
  id: FormControl<MetaFolderFormRawValue['id'] | NewMetaFolder['id']>;
  name: FormControl<MetaFolderFormRawValue['name']>;
  description: FormControl<MetaFolderFormRawValue['description']>;
  path: FormControl<MetaFolderFormRawValue['path']>;
  isShared: FormControl<MetaFolderFormRawValue['isShared']>;
  createdDate: FormControl<MetaFolderFormRawValue['createdDate']>;
  createdBy: FormControl<MetaFolderFormRawValue['createdBy']>;
  parent: FormControl<MetaFolderFormRawValue['parent']>;
};

export type MetaFolderFormGroup = FormGroup<MetaFolderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MetaFolderFormService {
  createMetaFolderFormGroup(metaFolder: MetaFolderFormGroupInput = { id: null }): MetaFolderFormGroup {
    const metaFolderRawValue = this.convertMetaFolderToMetaFolderRawValue({
      ...this.getFormDefaults(),
      ...metaFolder,
    });
    return new FormGroup<MetaFolderFormGroupContent>({
      id: new FormControl(
        { value: metaFolderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(metaFolderRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(metaFolderRawValue.description),
      path: new FormControl(metaFolderRawValue.path, {
        validators: [Validators.maxLength(1000)],
      }),
      isShared: new FormControl(metaFolderRawValue.isShared, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(metaFolderRawValue.createdDate, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(metaFolderRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      parent: new FormControl(metaFolderRawValue.parent),
    });
  }

  getMetaFolder(form: MetaFolderFormGroup): IMetaFolder | NewMetaFolder {
    return this.convertMetaFolderRawValueToMetaFolder(form.getRawValue() as MetaFolderFormRawValue | NewMetaFolderFormRawValue);
  }

  resetForm(form: MetaFolderFormGroup, metaFolder: MetaFolderFormGroupInput): void {
    const metaFolderRawValue = this.convertMetaFolderToMetaFolderRawValue({ ...this.getFormDefaults(), ...metaFolder });
    form.reset(
      {
        ...metaFolderRawValue,
        id: { value: metaFolderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MetaFolderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isShared: false,
      createdDate: currentTime,
    };
  }

  private convertMetaFolderRawValueToMetaFolder(
    rawMetaFolder: MetaFolderFormRawValue | NewMetaFolderFormRawValue,
  ): IMetaFolder | NewMetaFolder {
    return {
      ...rawMetaFolder,
      createdDate: dayjs(rawMetaFolder.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertMetaFolderToMetaFolderRawValue(
    metaFolder: IMetaFolder | (Partial<NewMetaFolder> & MetaFolderFormDefaults),
  ): MetaFolderFormRawValue | PartialWithRequiredKeyOf<NewMetaFolderFormRawValue> {
    return {
      ...metaFolder,
      createdDate: metaFolder.createdDate ? metaFolder.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
