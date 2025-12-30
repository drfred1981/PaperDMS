import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMonitoringSystemHealth, NewMonitoringSystemHealth } from '../monitoring-system-health.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMonitoringSystemHealth for edit and NewMonitoringSystemHealthFormGroupInput for create.
 */
type MonitoringSystemHealthFormGroupInput = IMonitoringSystemHealth | PartialWithRequiredKeyOf<NewMonitoringSystemHealth>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMonitoringSystemHealth | NewMonitoringSystemHealth> = Omit<T, 'lastCheck'> & {
  lastCheck?: string | null;
};

type MonitoringSystemHealthFormRawValue = FormValueOf<IMonitoringSystemHealth>;

type NewMonitoringSystemHealthFormRawValue = FormValueOf<NewMonitoringSystemHealth>;

type MonitoringSystemHealthFormDefaults = Pick<NewMonitoringSystemHealth, 'id' | 'lastCheck'>;

type MonitoringSystemHealthFormGroupContent = {
  id: FormControl<MonitoringSystemHealthFormRawValue['id'] | NewMonitoringSystemHealth['id']>;
  serviceName: FormControl<MonitoringSystemHealthFormRawValue['serviceName']>;
  status: FormControl<MonitoringSystemHealthFormRawValue['status']>;
  version: FormControl<MonitoringSystemHealthFormRawValue['version']>;
  uptime: FormControl<MonitoringSystemHealthFormRawValue['uptime']>;
  cpuUsage: FormControl<MonitoringSystemHealthFormRawValue['cpuUsage']>;
  memoryUsage: FormControl<MonitoringSystemHealthFormRawValue['memoryUsage']>;
  errorRate: FormControl<MonitoringSystemHealthFormRawValue['errorRate']>;
  lastCheck: FormControl<MonitoringSystemHealthFormRawValue['lastCheck']>;
};

export type MonitoringSystemHealthFormGroup = FormGroup<MonitoringSystemHealthFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MonitoringSystemHealthFormService {
  createMonitoringSystemHealthFormGroup(
    monitoringSystemHealth: MonitoringSystemHealthFormGroupInput = { id: null },
  ): MonitoringSystemHealthFormGroup {
    const monitoringSystemHealthRawValue = this.convertMonitoringSystemHealthToMonitoringSystemHealthRawValue({
      ...this.getFormDefaults(),
      ...monitoringSystemHealth,
    });
    return new FormGroup<MonitoringSystemHealthFormGroupContent>({
      id: new FormControl(
        { value: monitoringSystemHealthRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      serviceName: new FormControl(monitoringSystemHealthRawValue.serviceName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      status: new FormControl(monitoringSystemHealthRawValue.status, {
        validators: [Validators.required],
      }),
      version: new FormControl(monitoringSystemHealthRawValue.version, {
        validators: [Validators.maxLength(50)],
      }),
      uptime: new FormControl(monitoringSystemHealthRawValue.uptime),
      cpuUsage: new FormControl(monitoringSystemHealthRawValue.cpuUsage),
      memoryUsage: new FormControl(monitoringSystemHealthRawValue.memoryUsage),
      errorRate: new FormControl(monitoringSystemHealthRawValue.errorRate),
      lastCheck: new FormControl(monitoringSystemHealthRawValue.lastCheck, {
        validators: [Validators.required],
      }),
    });
  }

  getMonitoringSystemHealth(form: MonitoringSystemHealthFormGroup): IMonitoringSystemHealth | NewMonitoringSystemHealth {
    return this.convertMonitoringSystemHealthRawValueToMonitoringSystemHealth(
      form.getRawValue() as MonitoringSystemHealthFormRawValue | NewMonitoringSystemHealthFormRawValue,
    );
  }

  resetForm(form: MonitoringSystemHealthFormGroup, monitoringSystemHealth: MonitoringSystemHealthFormGroupInput): void {
    const monitoringSystemHealthRawValue = this.convertMonitoringSystemHealthToMonitoringSystemHealthRawValue({
      ...this.getFormDefaults(),
      ...monitoringSystemHealth,
    });
    form.reset(
      {
        ...monitoringSystemHealthRawValue,
        id: { value: monitoringSystemHealthRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MonitoringSystemHealthFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastCheck: currentTime,
    };
  }

  private convertMonitoringSystemHealthRawValueToMonitoringSystemHealth(
    rawMonitoringSystemHealth: MonitoringSystemHealthFormRawValue | NewMonitoringSystemHealthFormRawValue,
  ): IMonitoringSystemHealth | NewMonitoringSystemHealth {
    return {
      ...rawMonitoringSystemHealth,
      lastCheck: dayjs(rawMonitoringSystemHealth.lastCheck, DATE_TIME_FORMAT),
    };
  }

  private convertMonitoringSystemHealthToMonitoringSystemHealthRawValue(
    monitoringSystemHealth: IMonitoringSystemHealth | (Partial<NewMonitoringSystemHealth> & MonitoringSystemHealthFormDefaults),
  ): MonitoringSystemHealthFormRawValue | PartialWithRequiredKeyOf<NewMonitoringSystemHealthFormRawValue> {
    return {
      ...monitoringSystemHealth,
      lastCheck: monitoringSystemHealth.lastCheck ? monitoringSystemHealth.lastCheck.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
