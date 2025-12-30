import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMetaSmartFolder, NewMetaSmartFolder } from '../meta-smart-folder.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMetaSmartFolder for edit and NewMetaSmartFolderFormGroupInput for create.
 */
type MetaSmartFolderFormGroupInput = IMetaSmartFolder | PartialWithRequiredKeyOf<NewMetaSmartFolder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMetaSmartFolder | NewMetaSmartFolder> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type MetaSmartFolderFormRawValue = FormValueOf<IMetaSmartFolder>;

type NewMetaSmartFolderFormRawValue = FormValueOf<NewMetaSmartFolder>;

type MetaSmartFolderFormDefaults = Pick<NewMetaSmartFolder, 'id' | 'autoRefresh' | 'isPublic' | 'createdDate'>;

type MetaSmartFolderFormGroupContent = {
  id: FormControl<MetaSmartFolderFormRawValue['id'] | NewMetaSmartFolder['id']>;
  name: FormControl<MetaSmartFolderFormRawValue['name']>;
  queryJson: FormControl<MetaSmartFolderFormRawValue['queryJson']>;
  autoRefresh: FormControl<MetaSmartFolderFormRawValue['autoRefresh']>;
  isPublic: FormControl<MetaSmartFolderFormRawValue['isPublic']>;
  createdBy: FormControl<MetaSmartFolderFormRawValue['createdBy']>;
  createdDate: FormControl<MetaSmartFolderFormRawValue['createdDate']>;
};

export type MetaSmartFolderFormGroup = FormGroup<MetaSmartFolderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MetaSmartFolderFormService {
  createMetaSmartFolderFormGroup(metaSmartFolder: MetaSmartFolderFormGroupInput = { id: null }): MetaSmartFolderFormGroup {
    const metaSmartFolderRawValue = this.convertMetaSmartFolderToMetaSmartFolderRawValue({
      ...this.getFormDefaults(),
      ...metaSmartFolder,
    });
    return new FormGroup<MetaSmartFolderFormGroupContent>({
      id: new FormControl(
        { value: metaSmartFolderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(metaSmartFolderRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      queryJson: new FormControl(metaSmartFolderRawValue.queryJson, {
        validators: [Validators.required],
      }),
      autoRefresh: new FormControl(metaSmartFolderRawValue.autoRefresh, {
        validators: [Validators.required],
      }),
      isPublic: new FormControl(metaSmartFolderRawValue.isPublic, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(metaSmartFolderRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(metaSmartFolderRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getMetaSmartFolder(form: MetaSmartFolderFormGroup): IMetaSmartFolder | NewMetaSmartFolder {
    return this.convertMetaSmartFolderRawValueToMetaSmartFolder(
      form.getRawValue() as MetaSmartFolderFormRawValue | NewMetaSmartFolderFormRawValue,
    );
  }

  resetForm(form: MetaSmartFolderFormGroup, metaSmartFolder: MetaSmartFolderFormGroupInput): void {
    const metaSmartFolderRawValue = this.convertMetaSmartFolderToMetaSmartFolderRawValue({ ...this.getFormDefaults(), ...metaSmartFolder });
    form.reset(
      {
        ...metaSmartFolderRawValue,
        id: { value: metaSmartFolderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MetaSmartFolderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      autoRefresh: false,
      isPublic: false,
      createdDate: currentTime,
    };
  }

  private convertMetaSmartFolderRawValueToMetaSmartFolder(
    rawMetaSmartFolder: MetaSmartFolderFormRawValue | NewMetaSmartFolderFormRawValue,
  ): IMetaSmartFolder | NewMetaSmartFolder {
    return {
      ...rawMetaSmartFolder,
      createdDate: dayjs(rawMetaSmartFolder.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertMetaSmartFolderToMetaSmartFolderRawValue(
    metaSmartFolder: IMetaSmartFolder | (Partial<NewMetaSmartFolder> & MetaSmartFolderFormDefaults),
  ): MetaSmartFolderFormRawValue | PartialWithRequiredKeyOf<NewMetaSmartFolderFormRawValue> {
    return {
      ...metaSmartFolder,
      createdDate: metaSmartFolder.createdDate ? metaSmartFolder.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
