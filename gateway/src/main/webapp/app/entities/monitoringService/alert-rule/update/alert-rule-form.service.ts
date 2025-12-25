import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAlertRule, NewAlertRule } from '../alert-rule.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlertRule for edit and NewAlertRuleFormGroupInput for create.
 */
type AlertRuleFormGroupInput = IAlertRule | PartialWithRequiredKeyOf<NewAlertRule>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAlertRule | NewAlertRule> = Omit<T, 'lastTriggered' | 'createdDate'> & {
  lastTriggered?: string | null;
  createdDate?: string | null;
};

type AlertRuleFormRawValue = FormValueOf<IAlertRule>;

type NewAlertRuleFormRawValue = FormValueOf<NewAlertRule>;

type AlertRuleFormDefaults = Pick<NewAlertRule, 'id' | 'isActive' | 'lastTriggered' | 'createdDate'>;

type AlertRuleFormGroupContent = {
  id: FormControl<AlertRuleFormRawValue['id'] | NewAlertRule['id']>;
  name: FormControl<AlertRuleFormRawValue['name']>;
  description: FormControl<AlertRuleFormRawValue['description']>;
  alertType: FormControl<AlertRuleFormRawValue['alertType']>;
  conditions: FormControl<AlertRuleFormRawValue['conditions']>;
  severity: FormControl<AlertRuleFormRawValue['severity']>;
  recipients: FormControl<AlertRuleFormRawValue['recipients']>;
  isActive: FormControl<AlertRuleFormRawValue['isActive']>;
  triggerCount: FormControl<AlertRuleFormRawValue['triggerCount']>;
  lastTriggered: FormControl<AlertRuleFormRawValue['lastTriggered']>;
  createdBy: FormControl<AlertRuleFormRawValue['createdBy']>;
  createdDate: FormControl<AlertRuleFormRawValue['createdDate']>;
};

export type AlertRuleFormGroup = FormGroup<AlertRuleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlertRuleFormService {
  createAlertRuleFormGroup(alertRule: AlertRuleFormGroupInput = { id: null }): AlertRuleFormGroup {
    const alertRuleRawValue = this.convertAlertRuleToAlertRuleRawValue({
      ...this.getFormDefaults(),
      ...alertRule,
    });
    return new FormGroup<AlertRuleFormGroupContent>({
      id: new FormControl(
        { value: alertRuleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(alertRuleRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(alertRuleRawValue.description),
      alertType: new FormControl(alertRuleRawValue.alertType, {
        validators: [Validators.required],
      }),
      conditions: new FormControl(alertRuleRawValue.conditions, {
        validators: [Validators.required],
      }),
      severity: new FormControl(alertRuleRawValue.severity, {
        validators: [Validators.required],
      }),
      recipients: new FormControl(alertRuleRawValue.recipients, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(alertRuleRawValue.isActive, {
        validators: [Validators.required],
      }),
      triggerCount: new FormControl(alertRuleRawValue.triggerCount),
      lastTriggered: new FormControl(alertRuleRawValue.lastTriggered),
      createdBy: new FormControl(alertRuleRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(alertRuleRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getAlertRule(form: AlertRuleFormGroup): IAlertRule | NewAlertRule {
    return this.convertAlertRuleRawValueToAlertRule(form.getRawValue() as AlertRuleFormRawValue | NewAlertRuleFormRawValue);
  }

  resetForm(form: AlertRuleFormGroup, alertRule: AlertRuleFormGroupInput): void {
    const alertRuleRawValue = this.convertAlertRuleToAlertRuleRawValue({ ...this.getFormDefaults(), ...alertRule });
    form.reset(
      {
        ...alertRuleRawValue,
        id: { value: alertRuleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AlertRuleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      lastTriggered: currentTime,
      createdDate: currentTime,
    };
  }

  private convertAlertRuleRawValueToAlertRule(rawAlertRule: AlertRuleFormRawValue | NewAlertRuleFormRawValue): IAlertRule | NewAlertRule {
    return {
      ...rawAlertRule,
      lastTriggered: dayjs(rawAlertRule.lastTriggered, DATE_TIME_FORMAT),
      createdDate: dayjs(rawAlertRule.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertAlertRuleToAlertRuleRawValue(
    alertRule: IAlertRule | (Partial<NewAlertRule> & AlertRuleFormDefaults),
  ): AlertRuleFormRawValue | PartialWithRequiredKeyOf<NewAlertRuleFormRawValue> {
    return {
      ...alertRule,
      lastTriggered: alertRule.lastTriggered ? alertRule.lastTriggered.format(DATE_TIME_FORMAT) : undefined,
      createdDate: alertRule.createdDate ? alertRule.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
