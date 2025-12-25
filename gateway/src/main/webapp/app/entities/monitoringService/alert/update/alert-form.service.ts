import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAlert, NewAlert } from '../alert.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlert for edit and NewAlertFormGroupInput for create.
 */
type AlertFormGroupInput = IAlert | PartialWithRequiredKeyOf<NewAlert>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAlert | NewAlert> = Omit<T, 'triggeredDate' | 'acknowledgedDate' | 'resolvedDate'> & {
  triggeredDate?: string | null;
  acknowledgedDate?: string | null;
  resolvedDate?: string | null;
};

type AlertFormRawValue = FormValueOf<IAlert>;

type NewAlertFormRawValue = FormValueOf<NewAlert>;

type AlertFormDefaults = Pick<NewAlert, 'id' | 'triggeredDate' | 'acknowledgedDate' | 'resolvedDate'>;

type AlertFormGroupContent = {
  id: FormControl<AlertFormRawValue['id'] | NewAlert['id']>;
  alertRuleId: FormControl<AlertFormRawValue['alertRuleId']>;
  severity: FormControl<AlertFormRawValue['severity']>;
  title: FormControl<AlertFormRawValue['title']>;
  message: FormControl<AlertFormRawValue['message']>;
  entityType: FormControl<AlertFormRawValue['entityType']>;
  entityId: FormControl<AlertFormRawValue['entityId']>;
  status: FormControl<AlertFormRawValue['status']>;
  triggeredDate: FormControl<AlertFormRawValue['triggeredDate']>;
  acknowledgedBy: FormControl<AlertFormRawValue['acknowledgedBy']>;
  acknowledgedDate: FormControl<AlertFormRawValue['acknowledgedDate']>;
  resolvedBy: FormControl<AlertFormRawValue['resolvedBy']>;
  resolvedDate: FormControl<AlertFormRawValue['resolvedDate']>;
  alertRule: FormControl<AlertFormRawValue['alertRule']>;
};

export type AlertFormGroup = FormGroup<AlertFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlertFormService {
  createAlertFormGroup(alert: AlertFormGroupInput = { id: null }): AlertFormGroup {
    const alertRawValue = this.convertAlertToAlertRawValue({
      ...this.getFormDefaults(),
      ...alert,
    });
    return new FormGroup<AlertFormGroupContent>({
      id: new FormControl(
        { value: alertRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      alertRuleId: new FormControl(alertRawValue.alertRuleId, {
        validators: [Validators.required],
      }),
      severity: new FormControl(alertRawValue.severity, {
        validators: [Validators.required],
      }),
      title: new FormControl(alertRawValue.title, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      message: new FormControl(alertRawValue.message, {
        validators: [Validators.required],
      }),
      entityType: new FormControl(alertRawValue.entityType, {
        validators: [Validators.maxLength(100)],
      }),
      entityId: new FormControl(alertRawValue.entityId),
      status: new FormControl(alertRawValue.status, {
        validators: [Validators.required],
      }),
      triggeredDate: new FormControl(alertRawValue.triggeredDate, {
        validators: [Validators.required],
      }),
      acknowledgedBy: new FormControl(alertRawValue.acknowledgedBy, {
        validators: [Validators.maxLength(50)],
      }),
      acknowledgedDate: new FormControl(alertRawValue.acknowledgedDate),
      resolvedBy: new FormControl(alertRawValue.resolvedBy, {
        validators: [Validators.maxLength(50)],
      }),
      resolvedDate: new FormControl(alertRawValue.resolvedDate),
      alertRule: new FormControl(alertRawValue.alertRule, {
        validators: [Validators.required],
      }),
    });
  }

  getAlert(form: AlertFormGroup): IAlert | NewAlert {
    return this.convertAlertRawValueToAlert(form.getRawValue() as AlertFormRawValue | NewAlertFormRawValue);
  }

  resetForm(form: AlertFormGroup, alert: AlertFormGroupInput): void {
    const alertRawValue = this.convertAlertToAlertRawValue({ ...this.getFormDefaults(), ...alert });
    form.reset(
      {
        ...alertRawValue,
        id: { value: alertRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AlertFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      triggeredDate: currentTime,
      acknowledgedDate: currentTime,
      resolvedDate: currentTime,
    };
  }

  private convertAlertRawValueToAlert(rawAlert: AlertFormRawValue | NewAlertFormRawValue): IAlert | NewAlert {
    return {
      ...rawAlert,
      triggeredDate: dayjs(rawAlert.triggeredDate, DATE_TIME_FORMAT),
      acknowledgedDate: dayjs(rawAlert.acknowledgedDate, DATE_TIME_FORMAT),
      resolvedDate: dayjs(rawAlert.resolvedDate, DATE_TIME_FORMAT),
    };
  }

  private convertAlertToAlertRawValue(
    alert: IAlert | (Partial<NewAlert> & AlertFormDefaults),
  ): AlertFormRawValue | PartialWithRequiredKeyOf<NewAlertFormRawValue> {
    return {
      ...alert,
      triggeredDate: alert.triggeredDate ? alert.triggeredDate.format(DATE_TIME_FORMAT) : undefined,
      acknowledgedDate: alert.acknowledgedDate ? alert.acknowledgedDate.format(DATE_TIME_FORMAT) : undefined,
      resolvedDate: alert.resolvedDate ? alert.resolvedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
