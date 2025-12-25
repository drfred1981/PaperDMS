import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFolder, NewFolder } from '../folder.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFolder for edit and NewFolderFormGroupInput for create.
 */
type FolderFormGroupInput = IFolder | PartialWithRequiredKeyOf<NewFolder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFolder | NewFolder> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type FolderFormRawValue = FormValueOf<IFolder>;

type NewFolderFormRawValue = FormValueOf<NewFolder>;

type FolderFormDefaults = Pick<NewFolder, 'id' | 'isShared' | 'createdDate'>;

type FolderFormGroupContent = {
  id: FormControl<FolderFormRawValue['id'] | NewFolder['id']>;
  name: FormControl<FolderFormRawValue['name']>;
  description: FormControl<FolderFormRawValue['description']>;
  path: FormControl<FolderFormRawValue['path']>;
  isShared: FormControl<FolderFormRawValue['isShared']>;
  createdDate: FormControl<FolderFormRawValue['createdDate']>;
  createdBy: FormControl<FolderFormRawValue['createdBy']>;
  parent: FormControl<FolderFormRawValue['parent']>;
};

export type FolderFormGroup = FormGroup<FolderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FolderFormService {
  createFolderFormGroup(folder: FolderFormGroupInput = { id: null }): FolderFormGroup {
    const folderRawValue = this.convertFolderToFolderRawValue({
      ...this.getFormDefaults(),
      ...folder,
    });
    return new FormGroup<FolderFormGroupContent>({
      id: new FormControl(
        { value: folderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(folderRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(folderRawValue.description),
      path: new FormControl(folderRawValue.path, {
        validators: [Validators.maxLength(1000)],
      }),
      isShared: new FormControl(folderRawValue.isShared, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(folderRawValue.createdDate, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(folderRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      parent: new FormControl(folderRawValue.parent),
    });
  }

  getFolder(form: FolderFormGroup): IFolder | NewFolder {
    return this.convertFolderRawValueToFolder(form.getRawValue() as FolderFormRawValue | NewFolderFormRawValue);
  }

  resetForm(form: FolderFormGroup, folder: FolderFormGroupInput): void {
    const folderRawValue = this.convertFolderToFolderRawValue({ ...this.getFormDefaults(), ...folder });
    form.reset(
      {
        ...folderRawValue,
        id: { value: folderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FolderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isShared: false,
      createdDate: currentTime,
    };
  }

  private convertFolderRawValueToFolder(rawFolder: FolderFormRawValue | NewFolderFormRawValue): IFolder | NewFolder {
    return {
      ...rawFolder,
      createdDate: dayjs(rawFolder.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertFolderToFolderRawValue(
    folder: IFolder | (Partial<NewFolder> & FolderFormDefaults),
  ): FolderFormRawValue | PartialWithRequiredKeyOf<NewFolderFormRawValue> {
    return {
      ...folder,
      createdDate: folder.createdDate ? folder.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
