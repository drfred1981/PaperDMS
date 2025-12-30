import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMonitoringAlertRule, NewMonitoringAlertRule } from '../monitoring-alert-rule.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMonitoringAlertRule for edit and NewMonitoringAlertRuleFormGroupInput for create.
 */
type MonitoringAlertRuleFormGroupInput = IMonitoringAlertRule | PartialWithRequiredKeyOf<NewMonitoringAlertRule>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMonitoringAlertRule | NewMonitoringAlertRule> = Omit<T, 'lastTriggered' | 'createdDate'> & {
  lastTriggered?: string | null;
  createdDate?: string | null;
};

type MonitoringAlertRuleFormRawValue = FormValueOf<IMonitoringAlertRule>;

type NewMonitoringAlertRuleFormRawValue = FormValueOf<NewMonitoringAlertRule>;

type MonitoringAlertRuleFormDefaults = Pick<NewMonitoringAlertRule, 'id' | 'isActive' | 'lastTriggered' | 'createdDate'>;

type MonitoringAlertRuleFormGroupContent = {
  id: FormControl<MonitoringAlertRuleFormRawValue['id'] | NewMonitoringAlertRule['id']>;
  name: FormControl<MonitoringAlertRuleFormRawValue['name']>;
  description: FormControl<MonitoringAlertRuleFormRawValue['description']>;
  alertType: FormControl<MonitoringAlertRuleFormRawValue['alertType']>;
  conditions: FormControl<MonitoringAlertRuleFormRawValue['conditions']>;
  severity: FormControl<MonitoringAlertRuleFormRawValue['severity']>;
  recipients: FormControl<MonitoringAlertRuleFormRawValue['recipients']>;
  isActive: FormControl<MonitoringAlertRuleFormRawValue['isActive']>;
  triggerCount: FormControl<MonitoringAlertRuleFormRawValue['triggerCount']>;
  lastTriggered: FormControl<MonitoringAlertRuleFormRawValue['lastTriggered']>;
  createdBy: FormControl<MonitoringAlertRuleFormRawValue['createdBy']>;
  createdDate: FormControl<MonitoringAlertRuleFormRawValue['createdDate']>;
};

export type MonitoringAlertRuleFormGroup = FormGroup<MonitoringAlertRuleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MonitoringAlertRuleFormService {
  createMonitoringAlertRuleFormGroup(monitoringAlertRule: MonitoringAlertRuleFormGroupInput = { id: null }): MonitoringAlertRuleFormGroup {
    const monitoringAlertRuleRawValue = this.convertMonitoringAlertRuleToMonitoringAlertRuleRawValue({
      ...this.getFormDefaults(),
      ...monitoringAlertRule,
    });
    return new FormGroup<MonitoringAlertRuleFormGroupContent>({
      id: new FormControl(
        { value: monitoringAlertRuleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(monitoringAlertRuleRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(monitoringAlertRuleRawValue.description),
      alertType: new FormControl(monitoringAlertRuleRawValue.alertType, {
        validators: [Validators.required],
      }),
      conditions: new FormControl(monitoringAlertRuleRawValue.conditions, {
        validators: [Validators.required],
      }),
      severity: new FormControl(monitoringAlertRuleRawValue.severity, {
        validators: [Validators.required],
      }),
      recipients: new FormControl(monitoringAlertRuleRawValue.recipients, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(monitoringAlertRuleRawValue.isActive, {
        validators: [Validators.required],
      }),
      triggerCount: new FormControl(monitoringAlertRuleRawValue.triggerCount),
      lastTriggered: new FormControl(monitoringAlertRuleRawValue.lastTriggered),
      createdBy: new FormControl(monitoringAlertRuleRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(monitoringAlertRuleRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getMonitoringAlertRule(form: MonitoringAlertRuleFormGroup): IMonitoringAlertRule | NewMonitoringAlertRule {
    return this.convertMonitoringAlertRuleRawValueToMonitoringAlertRule(
      form.getRawValue() as MonitoringAlertRuleFormRawValue | NewMonitoringAlertRuleFormRawValue,
    );
  }

  resetForm(form: MonitoringAlertRuleFormGroup, monitoringAlertRule: MonitoringAlertRuleFormGroupInput): void {
    const monitoringAlertRuleRawValue = this.convertMonitoringAlertRuleToMonitoringAlertRuleRawValue({
      ...this.getFormDefaults(),
      ...monitoringAlertRule,
    });
    form.reset(
      {
        ...monitoringAlertRuleRawValue,
        id: { value: monitoringAlertRuleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MonitoringAlertRuleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      lastTriggered: currentTime,
      createdDate: currentTime,
    };
  }

  private convertMonitoringAlertRuleRawValueToMonitoringAlertRule(
    rawMonitoringAlertRule: MonitoringAlertRuleFormRawValue | NewMonitoringAlertRuleFormRawValue,
  ): IMonitoringAlertRule | NewMonitoringAlertRule {
    return {
      ...rawMonitoringAlertRule,
      lastTriggered: dayjs(rawMonitoringAlertRule.lastTriggered, DATE_TIME_FORMAT),
      createdDate: dayjs(rawMonitoringAlertRule.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertMonitoringAlertRuleToMonitoringAlertRuleRawValue(
    monitoringAlertRule: IMonitoringAlertRule | (Partial<NewMonitoringAlertRule> & MonitoringAlertRuleFormDefaults),
  ): MonitoringAlertRuleFormRawValue | PartialWithRequiredKeyOf<NewMonitoringAlertRuleFormRawValue> {
    return {
      ...monitoringAlertRule,
      lastTriggered: monitoringAlertRule.lastTriggered ? monitoringAlertRule.lastTriggered.format(DATE_TIME_FORMAT) : undefined,
      createdDate: monitoringAlertRule.createdDate ? monitoringAlertRule.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
