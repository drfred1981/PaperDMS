import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDashboard, NewDashboard } from '../dashboard.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDashboard for edit and NewDashboardFormGroupInput for create.
 */
type DashboardFormGroupInput = IDashboard | PartialWithRequiredKeyOf<NewDashboard>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDashboard | NewDashboard> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type DashboardFormRawValue = FormValueOf<IDashboard>;

type NewDashboardFormRawValue = FormValueOf<NewDashboard>;

type DashboardFormDefaults = Pick<NewDashboard, 'id' | 'isPublic' | 'isDefault' | 'createdDate'>;

type DashboardFormGroupContent = {
  id: FormControl<DashboardFormRawValue['id'] | NewDashboard['id']>;
  name: FormControl<DashboardFormRawValue['name']>;
  description: FormControl<DashboardFormRawValue['description']>;
  userId: FormControl<DashboardFormRawValue['userId']>;
  isPublic: FormControl<DashboardFormRawValue['isPublic']>;
  layout: FormControl<DashboardFormRawValue['layout']>;
  refreshInterval: FormControl<DashboardFormRawValue['refreshInterval']>;
  isDefault: FormControl<DashboardFormRawValue['isDefault']>;
  createdDate: FormControl<DashboardFormRawValue['createdDate']>;
};

export type DashboardFormGroup = FormGroup<DashboardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DashboardFormService {
  createDashboardFormGroup(dashboard?: DashboardFormGroupInput): DashboardFormGroup {
    const dashboardRawValue = this.convertDashboardToDashboardRawValue({
      ...this.getFormDefaults(),
      ...(dashboard ?? { id: null }),
    });
    return new FormGroup<DashboardFormGroupContent>({
      id: new FormControl(
        { value: dashboardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(dashboardRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(dashboardRawValue.description),
      userId: new FormControl(dashboardRawValue.userId, {
        validators: [Validators.maxLength(50)],
      }),
      isPublic: new FormControl(dashboardRawValue.isPublic, {
        validators: [Validators.required],
      }),
      layout: new FormControl(dashboardRawValue.layout, {
        validators: [Validators.required],
      }),
      refreshInterval: new FormControl(dashboardRawValue.refreshInterval),
      isDefault: new FormControl(dashboardRawValue.isDefault),
      createdDate: new FormControl(dashboardRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getDashboard(form: DashboardFormGroup): IDashboard | NewDashboard {
    return this.convertDashboardRawValueToDashboard(form.getRawValue() as DashboardFormRawValue | NewDashboardFormRawValue);
  }

  resetForm(form: DashboardFormGroup, dashboard: DashboardFormGroupInput): void {
    const dashboardRawValue = this.convertDashboardToDashboardRawValue({ ...this.getFormDefaults(), ...dashboard });
    form.reset({
      ...dashboardRawValue,
      id: { value: dashboardRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DashboardFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isPublic: false,
      isDefault: false,
      createdDate: currentTime,
    };
  }

  private convertDashboardRawValueToDashboard(rawDashboard: DashboardFormRawValue | NewDashboardFormRawValue): IDashboard | NewDashboard {
    return {
      ...rawDashboard,
      createdDate: dayjs(rawDashboard.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertDashboardToDashboardRawValue(
    dashboard: IDashboard | (Partial<NewDashboard> & DashboardFormDefaults),
  ): DashboardFormRawValue | PartialWithRequiredKeyOf<NewDashboardFormRawValue> {
    return {
      ...dashboard,
      createdDate: dashboard.createdDate ? dashboard.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
