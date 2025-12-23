import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISystemHealth, NewSystemHealth } from '../system-health.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISystemHealth for edit and NewSystemHealthFormGroupInput for create.
 */
type SystemHealthFormGroupInput = ISystemHealth | PartialWithRequiredKeyOf<NewSystemHealth>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISystemHealth | NewSystemHealth> = Omit<T, 'lastCheck'> & {
  lastCheck?: string | null;
};

type SystemHealthFormRawValue = FormValueOf<ISystemHealth>;

type NewSystemHealthFormRawValue = FormValueOf<NewSystemHealth>;

type SystemHealthFormDefaults = Pick<NewSystemHealth, 'id' | 'lastCheck'>;

type SystemHealthFormGroupContent = {
  id: FormControl<SystemHealthFormRawValue['id'] | NewSystemHealth['id']>;
  serviceName: FormControl<SystemHealthFormRawValue['serviceName']>;
  status: FormControl<SystemHealthFormRawValue['status']>;
  version: FormControl<SystemHealthFormRawValue['version']>;
  uptime: FormControl<SystemHealthFormRawValue['uptime']>;
  cpuUsage: FormControl<SystemHealthFormRawValue['cpuUsage']>;
  memoryUsage: FormControl<SystemHealthFormRawValue['memoryUsage']>;
  errorRate: FormControl<SystemHealthFormRawValue['errorRate']>;
  lastCheck: FormControl<SystemHealthFormRawValue['lastCheck']>;
};

export type SystemHealthFormGroup = FormGroup<SystemHealthFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SystemHealthFormService {
  createSystemHealthFormGroup(systemHealth?: SystemHealthFormGroupInput): SystemHealthFormGroup {
    const systemHealthRawValue = this.convertSystemHealthToSystemHealthRawValue({
      ...this.getFormDefaults(),
      ...(systemHealth ?? { id: null }),
    });
    return new FormGroup<SystemHealthFormGroupContent>({
      id: new FormControl(
        { value: systemHealthRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      serviceName: new FormControl(systemHealthRawValue.serviceName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      status: new FormControl(systemHealthRawValue.status, {
        validators: [Validators.required],
      }),
      version: new FormControl(systemHealthRawValue.version, {
        validators: [Validators.maxLength(50)],
      }),
      uptime: new FormControl(systemHealthRawValue.uptime),
      cpuUsage: new FormControl(systemHealthRawValue.cpuUsage),
      memoryUsage: new FormControl(systemHealthRawValue.memoryUsage),
      errorRate: new FormControl(systemHealthRawValue.errorRate),
      lastCheck: new FormControl(systemHealthRawValue.lastCheck, {
        validators: [Validators.required],
      }),
    });
  }

  getSystemHealth(form: SystemHealthFormGroup): ISystemHealth | NewSystemHealth {
    return this.convertSystemHealthRawValueToSystemHealth(form.getRawValue() as SystemHealthFormRawValue | NewSystemHealthFormRawValue);
  }

  resetForm(form: SystemHealthFormGroup, systemHealth: SystemHealthFormGroupInput): void {
    const systemHealthRawValue = this.convertSystemHealthToSystemHealthRawValue({ ...this.getFormDefaults(), ...systemHealth });
    form.reset({
      ...systemHealthRawValue,
      id: { value: systemHealthRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): SystemHealthFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastCheck: currentTime,
    };
  }

  private convertSystemHealthRawValueToSystemHealth(
    rawSystemHealth: SystemHealthFormRawValue | NewSystemHealthFormRawValue,
  ): ISystemHealth | NewSystemHealth {
    return {
      ...rawSystemHealth,
      lastCheck: dayjs(rawSystemHealth.lastCheck, DATE_TIME_FORMAT),
    };
  }

  private convertSystemHealthToSystemHealthRawValue(
    systemHealth: ISystemHealth | (Partial<NewSystemHealth> & SystemHealthFormDefaults),
  ): SystemHealthFormRawValue | PartialWithRequiredKeyOf<NewSystemHealthFormRawValue> {
    return {
      ...systemHealth,
      lastCheck: systemHealth.lastCheck ? systemHealth.lastCheck.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
