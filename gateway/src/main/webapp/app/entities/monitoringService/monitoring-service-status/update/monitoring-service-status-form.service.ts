import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMonitoringServiceStatus, NewMonitoringServiceStatus } from '../monitoring-service-status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMonitoringServiceStatus for edit and NewMonitoringServiceStatusFormGroupInput for create.
 */
type MonitoringServiceStatusFormGroupInput = IMonitoringServiceStatus | PartialWithRequiredKeyOf<NewMonitoringServiceStatus>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMonitoringServiceStatus | NewMonitoringServiceStatus> = Omit<T, 'lastPing'> & {
  lastPing?: string | null;
};

type MonitoringServiceStatusFormRawValue = FormValueOf<IMonitoringServiceStatus>;

type NewMonitoringServiceStatusFormRawValue = FormValueOf<NewMonitoringServiceStatus>;

type MonitoringServiceStatusFormDefaults = Pick<NewMonitoringServiceStatus, 'id' | 'lastPing' | 'isHealthy'>;

type MonitoringServiceStatusFormGroupContent = {
  id: FormControl<MonitoringServiceStatusFormRawValue['id'] | NewMonitoringServiceStatus['id']>;
  serviceName: FormControl<MonitoringServiceStatusFormRawValue['serviceName']>;
  serviceType: FormControl<MonitoringServiceStatusFormRawValue['serviceType']>;
  status: FormControl<MonitoringServiceStatusFormRawValue['status']>;
  endpoint: FormControl<MonitoringServiceStatusFormRawValue['endpoint']>;
  port: FormControl<MonitoringServiceStatusFormRawValue['port']>;
  version: FormControl<MonitoringServiceStatusFormRawValue['version']>;
  lastPing: FormControl<MonitoringServiceStatusFormRawValue['lastPing']>;
  isHealthy: FormControl<MonitoringServiceStatusFormRawValue['isHealthy']>;
};

export type MonitoringServiceStatusFormGroup = FormGroup<MonitoringServiceStatusFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MonitoringServiceStatusFormService {
  createMonitoringServiceStatusFormGroup(
    monitoringServiceStatus: MonitoringServiceStatusFormGroupInput = { id: null },
  ): MonitoringServiceStatusFormGroup {
    const monitoringServiceStatusRawValue = this.convertMonitoringServiceStatusToMonitoringServiceStatusRawValue({
      ...this.getFormDefaults(),
      ...monitoringServiceStatus,
    });
    return new FormGroup<MonitoringServiceStatusFormGroupContent>({
      id: new FormControl(
        { value: monitoringServiceStatusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      serviceName: new FormControl(monitoringServiceStatusRawValue.serviceName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      serviceType: new FormControl(monitoringServiceStatusRawValue.serviceType, {
        validators: [Validators.maxLength(50)],
      }),
      status: new FormControl(monitoringServiceStatusRawValue.status, {
        validators: [Validators.required],
      }),
      endpoint: new FormControl(monitoringServiceStatusRawValue.endpoint, {
        validators: [Validators.maxLength(500)],
      }),
      port: new FormControl(monitoringServiceStatusRawValue.port),
      version: new FormControl(monitoringServiceStatusRawValue.version, {
        validators: [Validators.maxLength(50)],
      }),
      lastPing: new FormControl(monitoringServiceStatusRawValue.lastPing),
      isHealthy: new FormControl(monitoringServiceStatusRawValue.isHealthy, {
        validators: [Validators.required],
      }),
    });
  }

  getMonitoringServiceStatus(form: MonitoringServiceStatusFormGroup): IMonitoringServiceStatus | NewMonitoringServiceStatus {
    return this.convertMonitoringServiceStatusRawValueToMonitoringServiceStatus(
      form.getRawValue() as MonitoringServiceStatusFormRawValue | NewMonitoringServiceStatusFormRawValue,
    );
  }

  resetForm(form: MonitoringServiceStatusFormGroup, monitoringServiceStatus: MonitoringServiceStatusFormGroupInput): void {
    const monitoringServiceStatusRawValue = this.convertMonitoringServiceStatusToMonitoringServiceStatusRawValue({
      ...this.getFormDefaults(),
      ...monitoringServiceStatus,
    });
    form.reset(
      {
        ...monitoringServiceStatusRawValue,
        id: { value: monitoringServiceStatusRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MonitoringServiceStatusFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastPing: currentTime,
      isHealthy: false,
    };
  }

  private convertMonitoringServiceStatusRawValueToMonitoringServiceStatus(
    rawMonitoringServiceStatus: MonitoringServiceStatusFormRawValue | NewMonitoringServiceStatusFormRawValue,
  ): IMonitoringServiceStatus | NewMonitoringServiceStatus {
    return {
      ...rawMonitoringServiceStatus,
      lastPing: dayjs(rawMonitoringServiceStatus.lastPing, DATE_TIME_FORMAT),
    };
  }

  private convertMonitoringServiceStatusToMonitoringServiceStatusRawValue(
    monitoringServiceStatus: IMonitoringServiceStatus | (Partial<NewMonitoringServiceStatus> & MonitoringServiceStatusFormDefaults),
  ): MonitoringServiceStatusFormRawValue | PartialWithRequiredKeyOf<NewMonitoringServiceStatusFormRawValue> {
    return {
      ...monitoringServiceStatus,
      lastPing: monitoringServiceStatus.lastPing ? monitoringServiceStatus.lastPing.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
