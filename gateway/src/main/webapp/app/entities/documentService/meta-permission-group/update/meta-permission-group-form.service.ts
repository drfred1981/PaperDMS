import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMetaPermissionGroup, NewMetaPermissionGroup } from '../meta-permission-group.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMetaPermissionGroup for edit and NewMetaPermissionGroupFormGroupInput for create.
 */
type MetaPermissionGroupFormGroupInput = IMetaPermissionGroup | PartialWithRequiredKeyOf<NewMetaPermissionGroup>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMetaPermissionGroup | NewMetaPermissionGroup> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type MetaPermissionGroupFormRawValue = FormValueOf<IMetaPermissionGroup>;

type NewMetaPermissionGroupFormRawValue = FormValueOf<NewMetaPermissionGroup>;

type MetaPermissionGroupFormDefaults = Pick<NewMetaPermissionGroup, 'id' | 'isSystem' | 'createdDate'>;

type MetaPermissionGroupFormGroupContent = {
  id: FormControl<MetaPermissionGroupFormRawValue['id'] | NewMetaPermissionGroup['id']>;
  name: FormControl<MetaPermissionGroupFormRawValue['name']>;
  permissions: FormControl<MetaPermissionGroupFormRawValue['permissions']>;
  isSystem: FormControl<MetaPermissionGroupFormRawValue['isSystem']>;
  createdDate: FormControl<MetaPermissionGroupFormRawValue['createdDate']>;
  createdBy: FormControl<MetaPermissionGroupFormRawValue['createdBy']>;
};

export type MetaPermissionGroupFormGroup = FormGroup<MetaPermissionGroupFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MetaPermissionGroupFormService {
  createMetaPermissionGroupFormGroup(metaPermissionGroup: MetaPermissionGroupFormGroupInput = { id: null }): MetaPermissionGroupFormGroup {
    const metaPermissionGroupRawValue = this.convertMetaPermissionGroupToMetaPermissionGroupRawValue({
      ...this.getFormDefaults(),
      ...metaPermissionGroup,
    });
    return new FormGroup<MetaPermissionGroupFormGroupContent>({
      id: new FormControl(
        { value: metaPermissionGroupRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(metaPermissionGroupRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      permissions: new FormControl(metaPermissionGroupRawValue.permissions, {
        validators: [Validators.required],
      }),
      isSystem: new FormControl(metaPermissionGroupRawValue.isSystem, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(metaPermissionGroupRawValue.createdDate, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(metaPermissionGroupRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
    });
  }

  getMetaPermissionGroup(form: MetaPermissionGroupFormGroup): IMetaPermissionGroup | NewMetaPermissionGroup {
    return this.convertMetaPermissionGroupRawValueToMetaPermissionGroup(
      form.getRawValue() as MetaPermissionGroupFormRawValue | NewMetaPermissionGroupFormRawValue,
    );
  }

  resetForm(form: MetaPermissionGroupFormGroup, metaPermissionGroup: MetaPermissionGroupFormGroupInput): void {
    const metaPermissionGroupRawValue = this.convertMetaPermissionGroupToMetaPermissionGroupRawValue({
      ...this.getFormDefaults(),
      ...metaPermissionGroup,
    });
    form.reset(
      {
        ...metaPermissionGroupRawValue,
        id: { value: metaPermissionGroupRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MetaPermissionGroupFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isSystem: false,
      createdDate: currentTime,
    };
  }

  private convertMetaPermissionGroupRawValueToMetaPermissionGroup(
    rawMetaPermissionGroup: MetaPermissionGroupFormRawValue | NewMetaPermissionGroupFormRawValue,
  ): IMetaPermissionGroup | NewMetaPermissionGroup {
    return {
      ...rawMetaPermissionGroup,
      createdDate: dayjs(rawMetaPermissionGroup.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertMetaPermissionGroupToMetaPermissionGroupRawValue(
    metaPermissionGroup: IMetaPermissionGroup | (Partial<NewMetaPermissionGroup> & MetaPermissionGroupFormDefaults),
  ): MetaPermissionGroupFormRawValue | PartialWithRequiredKeyOf<NewMetaPermissionGroupFormRawValue> {
    return {
      ...metaPermissionGroup,
      createdDate: metaPermissionGroup.createdDate ? metaPermissionGroup.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
