import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISystemMetric, NewSystemMetric } from '../system-metric.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISystemMetric for edit and NewSystemMetricFormGroupInput for create.
 */
type SystemMetricFormGroupInput = ISystemMetric | PartialWithRequiredKeyOf<NewSystemMetric>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISystemMetric | NewSystemMetric> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type SystemMetricFormRawValue = FormValueOf<ISystemMetric>;

type NewSystemMetricFormRawValue = FormValueOf<NewSystemMetric>;

type SystemMetricFormDefaults = Pick<NewSystemMetric, 'id' | 'timestamp'>;

type SystemMetricFormGroupContent = {
  id: FormControl<SystemMetricFormRawValue['id'] | NewSystemMetric['id']>;
  metricName: FormControl<SystemMetricFormRawValue['metricName']>;
  cpuUsage: FormControl<SystemMetricFormRawValue['cpuUsage']>;
  memoryUsage: FormControl<SystemMetricFormRawValue['memoryUsage']>;
  diskUsage: FormControl<SystemMetricFormRawValue['diskUsage']>;
  networkIn: FormControl<SystemMetricFormRawValue['networkIn']>;
  networkOut: FormControl<SystemMetricFormRawValue['networkOut']>;
  activeConnections: FormControl<SystemMetricFormRawValue['activeConnections']>;
  timestamp: FormControl<SystemMetricFormRawValue['timestamp']>;
};

export type SystemMetricFormGroup = FormGroup<SystemMetricFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SystemMetricFormService {
  createSystemMetricFormGroup(systemMetric: SystemMetricFormGroupInput = { id: null }): SystemMetricFormGroup {
    const systemMetricRawValue = this.convertSystemMetricToSystemMetricRawValue({
      ...this.getFormDefaults(),
      ...systemMetric,
    });
    return new FormGroup<SystemMetricFormGroupContent>({
      id: new FormControl(
        { value: systemMetricRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      metricName: new FormControl(systemMetricRawValue.metricName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      cpuUsage: new FormControl(systemMetricRawValue.cpuUsage),
      memoryUsage: new FormControl(systemMetricRawValue.memoryUsage),
      diskUsage: new FormControl(systemMetricRawValue.diskUsage),
      networkIn: new FormControl(systemMetricRawValue.networkIn),
      networkOut: new FormControl(systemMetricRawValue.networkOut),
      activeConnections: new FormControl(systemMetricRawValue.activeConnections),
      timestamp: new FormControl(systemMetricRawValue.timestamp, {
        validators: [Validators.required],
      }),
    });
  }

  getSystemMetric(form: SystemMetricFormGroup): ISystemMetric | NewSystemMetric {
    return this.convertSystemMetricRawValueToSystemMetric(form.getRawValue() as SystemMetricFormRawValue | NewSystemMetricFormRawValue);
  }

  resetForm(form: SystemMetricFormGroup, systemMetric: SystemMetricFormGroupInput): void {
    const systemMetricRawValue = this.convertSystemMetricToSystemMetricRawValue({ ...this.getFormDefaults(), ...systemMetric });
    form.reset(
      {
        ...systemMetricRawValue,
        id: { value: systemMetricRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SystemMetricFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertSystemMetricRawValueToSystemMetric(
    rawSystemMetric: SystemMetricFormRawValue | NewSystemMetricFormRawValue,
  ): ISystemMetric | NewSystemMetric {
    return {
      ...rawSystemMetric,
      timestamp: dayjs(rawSystemMetric.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertSystemMetricToSystemMetricRawValue(
    systemMetric: ISystemMetric | (Partial<NewSystemMetric> & SystemMetricFormDefaults),
  ): SystemMetricFormRawValue | PartialWithRequiredKeyOf<NewSystemMetricFormRawValue> {
    return {
      ...systemMetric,
      timestamp: systemMetric.timestamp ? systemMetric.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
