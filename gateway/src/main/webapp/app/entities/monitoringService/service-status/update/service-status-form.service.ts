import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IServiceStatus, NewServiceStatus } from '../service-status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServiceStatus for edit and NewServiceStatusFormGroupInput for create.
 */
type ServiceStatusFormGroupInput = IServiceStatus | PartialWithRequiredKeyOf<NewServiceStatus>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IServiceStatus | NewServiceStatus> = Omit<T, 'lastPing'> & {
  lastPing?: string | null;
};

type ServiceStatusFormRawValue = FormValueOf<IServiceStatus>;

type NewServiceStatusFormRawValue = FormValueOf<NewServiceStatus>;

type ServiceStatusFormDefaults = Pick<NewServiceStatus, 'id' | 'lastPing' | 'isHealthy'>;

type ServiceStatusFormGroupContent = {
  id: FormControl<ServiceStatusFormRawValue['id'] | NewServiceStatus['id']>;
  serviceName: FormControl<ServiceStatusFormRawValue['serviceName']>;
  serviceType: FormControl<ServiceStatusFormRawValue['serviceType']>;
  status: FormControl<ServiceStatusFormRawValue['status']>;
  endpoint: FormControl<ServiceStatusFormRawValue['endpoint']>;
  port: FormControl<ServiceStatusFormRawValue['port']>;
  version: FormControl<ServiceStatusFormRawValue['version']>;
  lastPing: FormControl<ServiceStatusFormRawValue['lastPing']>;
  isHealthy: FormControl<ServiceStatusFormRawValue['isHealthy']>;
};

export type ServiceStatusFormGroup = FormGroup<ServiceStatusFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServiceStatusFormService {
  createServiceStatusFormGroup(serviceStatus?: ServiceStatusFormGroupInput): ServiceStatusFormGroup {
    const serviceStatusRawValue = this.convertServiceStatusToServiceStatusRawValue({
      ...this.getFormDefaults(),
      ...(serviceStatus ?? { id: null }),
    });
    return new FormGroup<ServiceStatusFormGroupContent>({
      id: new FormControl(
        { value: serviceStatusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      serviceName: new FormControl(serviceStatusRawValue.serviceName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      serviceType: new FormControl(serviceStatusRawValue.serviceType, {
        validators: [Validators.maxLength(50)],
      }),
      status: new FormControl(serviceStatusRawValue.status, {
        validators: [Validators.required],
      }),
      endpoint: new FormControl(serviceStatusRawValue.endpoint, {
        validators: [Validators.maxLength(500)],
      }),
      port: new FormControl(serviceStatusRawValue.port),
      version: new FormControl(serviceStatusRawValue.version, {
        validators: [Validators.maxLength(50)],
      }),
      lastPing: new FormControl(serviceStatusRawValue.lastPing),
      isHealthy: new FormControl(serviceStatusRawValue.isHealthy, {
        validators: [Validators.required],
      }),
    });
  }

  getServiceStatus(form: ServiceStatusFormGroup): IServiceStatus | NewServiceStatus {
    return this.convertServiceStatusRawValueToServiceStatus(form.getRawValue() as ServiceStatusFormRawValue | NewServiceStatusFormRawValue);
  }

  resetForm(form: ServiceStatusFormGroup, serviceStatus: ServiceStatusFormGroupInput): void {
    const serviceStatusRawValue = this.convertServiceStatusToServiceStatusRawValue({ ...this.getFormDefaults(), ...serviceStatus });
    form.reset({
      ...serviceStatusRawValue,
      id: { value: serviceStatusRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ServiceStatusFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastPing: currentTime,
      isHealthy: false,
    };
  }

  private convertServiceStatusRawValueToServiceStatus(
    rawServiceStatus: ServiceStatusFormRawValue | NewServiceStatusFormRawValue,
  ): IServiceStatus | NewServiceStatus {
    return {
      ...rawServiceStatus,
      lastPing: dayjs(rawServiceStatus.lastPing, DATE_TIME_FORMAT),
    };
  }

  private convertServiceStatusToServiceStatusRawValue(
    serviceStatus: IServiceStatus | (Partial<NewServiceStatus> & ServiceStatusFormDefaults),
  ): ServiceStatusFormRawValue | PartialWithRequiredKeyOf<NewServiceStatusFormRawValue> {
    return {
      ...serviceStatus,
      lastPing: serviceStatus.lastPing ? serviceStatus.lastPing.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
