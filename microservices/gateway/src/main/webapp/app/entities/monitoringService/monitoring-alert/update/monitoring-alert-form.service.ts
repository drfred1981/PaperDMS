import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMonitoringAlert, NewMonitoringAlert } from '../monitoring-alert.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMonitoringAlert for edit and NewMonitoringAlertFormGroupInput for create.
 */
type MonitoringAlertFormGroupInput = IMonitoringAlert | PartialWithRequiredKeyOf<NewMonitoringAlert>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMonitoringAlert | NewMonitoringAlert> = Omit<T, 'triggeredDate' | 'acknowledgedDate' | 'resolvedDate'> & {
  triggeredDate?: string | null;
  acknowledgedDate?: string | null;
  resolvedDate?: string | null;
};

type MonitoringAlertFormRawValue = FormValueOf<IMonitoringAlert>;

type NewMonitoringAlertFormRawValue = FormValueOf<NewMonitoringAlert>;

type MonitoringAlertFormDefaults = Pick<NewMonitoringAlert, 'id' | 'triggeredDate' | 'acknowledgedDate' | 'resolvedDate'>;

type MonitoringAlertFormGroupContent = {
  id: FormControl<MonitoringAlertFormRawValue['id'] | NewMonitoringAlert['id']>;
  severity: FormControl<MonitoringAlertFormRawValue['severity']>;
  title: FormControl<MonitoringAlertFormRawValue['title']>;
  message: FormControl<MonitoringAlertFormRawValue['message']>;
  entityType: FormControl<MonitoringAlertFormRawValue['entityType']>;
  entityName: FormControl<MonitoringAlertFormRawValue['entityName']>;
  status: FormControl<MonitoringAlertFormRawValue['status']>;
  triggeredDate: FormControl<MonitoringAlertFormRawValue['triggeredDate']>;
  acknowledgedBy: FormControl<MonitoringAlertFormRawValue['acknowledgedBy']>;
  acknowledgedDate: FormControl<MonitoringAlertFormRawValue['acknowledgedDate']>;
  resolvedBy: FormControl<MonitoringAlertFormRawValue['resolvedBy']>;
  resolvedDate: FormControl<MonitoringAlertFormRawValue['resolvedDate']>;
  alertRule: FormControl<MonitoringAlertFormRawValue['alertRule']>;
};

export type MonitoringAlertFormGroup = FormGroup<MonitoringAlertFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MonitoringAlertFormService {
  createMonitoringAlertFormGroup(monitoringAlert: MonitoringAlertFormGroupInput = { id: null }): MonitoringAlertFormGroup {
    const monitoringAlertRawValue = this.convertMonitoringAlertToMonitoringAlertRawValue({
      ...this.getFormDefaults(),
      ...monitoringAlert,
    });
    return new FormGroup<MonitoringAlertFormGroupContent>({
      id: new FormControl(
        { value: monitoringAlertRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      severity: new FormControl(monitoringAlertRawValue.severity, {
        validators: [Validators.required],
      }),
      title: new FormControl(monitoringAlertRawValue.title, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      message: new FormControl(monitoringAlertRawValue.message, {
        validators: [Validators.required],
      }),
      entityType: new FormControl(monitoringAlertRawValue.entityType, {
        validators: [Validators.maxLength(100)],
      }),
      entityName: new FormControl(monitoringAlertRawValue.entityName),
      status: new FormControl(monitoringAlertRawValue.status, {
        validators: [Validators.required],
      }),
      triggeredDate: new FormControl(monitoringAlertRawValue.triggeredDate, {
        validators: [Validators.required],
      }),
      acknowledgedBy: new FormControl(monitoringAlertRawValue.acknowledgedBy, {
        validators: [Validators.maxLength(50)],
      }),
      acknowledgedDate: new FormControl(monitoringAlertRawValue.acknowledgedDate),
      resolvedBy: new FormControl(monitoringAlertRawValue.resolvedBy, {
        validators: [Validators.maxLength(50)],
      }),
      resolvedDate: new FormControl(monitoringAlertRawValue.resolvedDate),
      alertRule: new FormControl(monitoringAlertRawValue.alertRule),
    });
  }

  getMonitoringAlert(form: MonitoringAlertFormGroup): IMonitoringAlert | NewMonitoringAlert {
    return this.convertMonitoringAlertRawValueToMonitoringAlert(
      form.getRawValue() as MonitoringAlertFormRawValue | NewMonitoringAlertFormRawValue,
    );
  }

  resetForm(form: MonitoringAlertFormGroup, monitoringAlert: MonitoringAlertFormGroupInput): void {
    const monitoringAlertRawValue = this.convertMonitoringAlertToMonitoringAlertRawValue({ ...this.getFormDefaults(), ...monitoringAlert });
    form.reset(
      {
        ...monitoringAlertRawValue,
        id: { value: monitoringAlertRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MonitoringAlertFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      triggeredDate: currentTime,
      acknowledgedDate: currentTime,
      resolvedDate: currentTime,
    };
  }

  private convertMonitoringAlertRawValueToMonitoringAlert(
    rawMonitoringAlert: MonitoringAlertFormRawValue | NewMonitoringAlertFormRawValue,
  ): IMonitoringAlert | NewMonitoringAlert {
    return {
      ...rawMonitoringAlert,
      triggeredDate: dayjs(rawMonitoringAlert.triggeredDate, DATE_TIME_FORMAT),
      acknowledgedDate: dayjs(rawMonitoringAlert.acknowledgedDate, DATE_TIME_FORMAT),
      resolvedDate: dayjs(rawMonitoringAlert.resolvedDate, DATE_TIME_FORMAT),
    };
  }

  private convertMonitoringAlertToMonitoringAlertRawValue(
    monitoringAlert: IMonitoringAlert | (Partial<NewMonitoringAlert> & MonitoringAlertFormDefaults),
  ): MonitoringAlertFormRawValue | PartialWithRequiredKeyOf<NewMonitoringAlertFormRawValue> {
    return {
      ...monitoringAlert,
      triggeredDate: monitoringAlert.triggeredDate ? monitoringAlert.triggeredDate.format(DATE_TIME_FORMAT) : undefined,
      acknowledgedDate: monitoringAlert.acknowledgedDate ? monitoringAlert.acknowledgedDate.format(DATE_TIME_FORMAT) : undefined,
      resolvedDate: monitoringAlert.resolvedDate ? monitoringAlert.resolvedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
