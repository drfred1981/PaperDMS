import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPermissionGroup, NewPermissionGroup } from '../permission-group.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPermissionGroup for edit and NewPermissionGroupFormGroupInput for create.
 */
type PermissionGroupFormGroupInput = IPermissionGroup | PartialWithRequiredKeyOf<NewPermissionGroup>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPermissionGroup | NewPermissionGroup> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type PermissionGroupFormRawValue = FormValueOf<IPermissionGroup>;

type NewPermissionGroupFormRawValue = FormValueOf<NewPermissionGroup>;

type PermissionGroupFormDefaults = Pick<NewPermissionGroup, 'id' | 'isSystem' | 'createdDate'>;

type PermissionGroupFormGroupContent = {
  id: FormControl<PermissionGroupFormRawValue['id'] | NewPermissionGroup['id']>;
  name: FormControl<PermissionGroupFormRawValue['name']>;
  permissions: FormControl<PermissionGroupFormRawValue['permissions']>;
  isSystem: FormControl<PermissionGroupFormRawValue['isSystem']>;
  createdDate: FormControl<PermissionGroupFormRawValue['createdDate']>;
  createdBy: FormControl<PermissionGroupFormRawValue['createdBy']>;
};

export type PermissionGroupFormGroup = FormGroup<PermissionGroupFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PermissionGroupFormService {
  createPermissionGroupFormGroup(permissionGroup?: PermissionGroupFormGroupInput): PermissionGroupFormGroup {
    const permissionGroupRawValue = this.convertPermissionGroupToPermissionGroupRawValue({
      ...this.getFormDefaults(),
      ...(permissionGroup ?? { id: null }),
    });
    return new FormGroup<PermissionGroupFormGroupContent>({
      id: new FormControl(
        { value: permissionGroupRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(permissionGroupRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      permissions: new FormControl(permissionGroupRawValue.permissions, {
        validators: [Validators.required],
      }),
      isSystem: new FormControl(permissionGroupRawValue.isSystem, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(permissionGroupRawValue.createdDate, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(permissionGroupRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
    });
  }

  getPermissionGroup(form: PermissionGroupFormGroup): IPermissionGroup | NewPermissionGroup {
    return this.convertPermissionGroupRawValueToPermissionGroup(
      form.getRawValue() as PermissionGroupFormRawValue | NewPermissionGroupFormRawValue,
    );
  }

  resetForm(form: PermissionGroupFormGroup, permissionGroup: PermissionGroupFormGroupInput): void {
    const permissionGroupRawValue = this.convertPermissionGroupToPermissionGroupRawValue({ ...this.getFormDefaults(), ...permissionGroup });
    form.reset({
      ...permissionGroupRawValue,
      id: { value: permissionGroupRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PermissionGroupFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isSystem: false,
      createdDate: currentTime,
    };
  }

  private convertPermissionGroupRawValueToPermissionGroup(
    rawPermissionGroup: PermissionGroupFormRawValue | NewPermissionGroupFormRawValue,
  ): IPermissionGroup | NewPermissionGroup {
    return {
      ...rawPermissionGroup,
      createdDate: dayjs(rawPermissionGroup.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertPermissionGroupToPermissionGroupRawValue(
    permissionGroup: IPermissionGroup | (Partial<NewPermissionGroup> & PermissionGroupFormDefaults),
  ): PermissionGroupFormRawValue | PartialWithRequiredKeyOf<NewPermissionGroupFormRawValue> {
    return {
      ...permissionGroup,
      createdDate: permissionGroup.createdDate ? permissionGroup.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
