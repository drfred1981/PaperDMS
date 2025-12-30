import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmailImportImportRule, NewEmailImportImportRule } from '../email-import-import-rule.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmailImportImportRule for edit and NewEmailImportImportRuleFormGroupInput for create.
 */
type EmailImportImportRuleFormGroupInput = IEmailImportImportRule | PartialWithRequiredKeyOf<NewEmailImportImportRule>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmailImportImportRule | NewEmailImportImportRule> = Omit<
  T,
  'lastMatchDate' | 'createdDate' | 'lastModifiedDate'
> & {
  lastMatchDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type EmailImportImportRuleFormRawValue = FormValueOf<IEmailImportImportRule>;

type NewEmailImportImportRuleFormRawValue = FormValueOf<NewEmailImportImportRule>;

type EmailImportImportRuleFormDefaults = Pick<
  NewEmailImportImportRule,
  'id' | 'isActive' | 'lastMatchDate' | 'createdDate' | 'lastModifiedDate'
>;

type EmailImportImportRuleFormGroupContent = {
  id: FormControl<EmailImportImportRuleFormRawValue['id'] | NewEmailImportImportRule['id']>;
  name: FormControl<EmailImportImportRuleFormRawValue['name']>;
  description: FormControl<EmailImportImportRuleFormRawValue['description']>;
  priority: FormControl<EmailImportImportRuleFormRawValue['priority']>;
  isActive: FormControl<EmailImportImportRuleFormRawValue['isActive']>;
  conditions: FormControl<EmailImportImportRuleFormRawValue['conditions']>;
  actions: FormControl<EmailImportImportRuleFormRawValue['actions']>;
  notifyUsers: FormControl<EmailImportImportRuleFormRawValue['notifyUsers']>;
  matchCount: FormControl<EmailImportImportRuleFormRawValue['matchCount']>;
  lastMatchDate: FormControl<EmailImportImportRuleFormRawValue['lastMatchDate']>;
  createdBy: FormControl<EmailImportImportRuleFormRawValue['createdBy']>;
  createdDate: FormControl<EmailImportImportRuleFormRawValue['createdDate']>;
  lastModifiedDate: FormControl<EmailImportImportRuleFormRawValue['lastModifiedDate']>;
};

export type EmailImportImportRuleFormGroup = FormGroup<EmailImportImportRuleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmailImportImportRuleFormService {
  createEmailImportImportRuleFormGroup(
    emailImportImportRule: EmailImportImportRuleFormGroupInput = { id: null },
  ): EmailImportImportRuleFormGroup {
    const emailImportImportRuleRawValue = this.convertEmailImportImportRuleToEmailImportImportRuleRawValue({
      ...this.getFormDefaults(),
      ...emailImportImportRule,
    });
    return new FormGroup<EmailImportImportRuleFormGroupContent>({
      id: new FormControl(
        { value: emailImportImportRuleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(emailImportImportRuleRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(emailImportImportRuleRawValue.description),
      priority: new FormControl(emailImportImportRuleRawValue.priority, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(emailImportImportRuleRawValue.isActive, {
        validators: [Validators.required],
      }),
      conditions: new FormControl(emailImportImportRuleRawValue.conditions, {
        validators: [Validators.required],
      }),
      actions: new FormControl(emailImportImportRuleRawValue.actions, {
        validators: [Validators.required],
      }),
      notifyUsers: new FormControl(emailImportImportRuleRawValue.notifyUsers),
      matchCount: new FormControl(emailImportImportRuleRawValue.matchCount),
      lastMatchDate: new FormControl(emailImportImportRuleRawValue.lastMatchDate),
      createdBy: new FormControl(emailImportImportRuleRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(emailImportImportRuleRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedDate: new FormControl(emailImportImportRuleRawValue.lastModifiedDate),
    });
  }

  getEmailImportImportRule(form: EmailImportImportRuleFormGroup): IEmailImportImportRule | NewEmailImportImportRule {
    return this.convertEmailImportImportRuleRawValueToEmailImportImportRule(
      form.getRawValue() as EmailImportImportRuleFormRawValue | NewEmailImportImportRuleFormRawValue,
    );
  }

  resetForm(form: EmailImportImportRuleFormGroup, emailImportImportRule: EmailImportImportRuleFormGroupInput): void {
    const emailImportImportRuleRawValue = this.convertEmailImportImportRuleToEmailImportImportRuleRawValue({
      ...this.getFormDefaults(),
      ...emailImportImportRule,
    });
    form.reset(
      {
        ...emailImportImportRuleRawValue,
        id: { value: emailImportImportRuleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmailImportImportRuleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      lastMatchDate: currentTime,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertEmailImportImportRuleRawValueToEmailImportImportRule(
    rawEmailImportImportRule: EmailImportImportRuleFormRawValue | NewEmailImportImportRuleFormRawValue,
  ): IEmailImportImportRule | NewEmailImportImportRule {
    return {
      ...rawEmailImportImportRule,
      lastMatchDate: dayjs(rawEmailImportImportRule.lastMatchDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawEmailImportImportRule.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawEmailImportImportRule.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertEmailImportImportRuleToEmailImportImportRuleRawValue(
    emailImportImportRule: IEmailImportImportRule | (Partial<NewEmailImportImportRule> & EmailImportImportRuleFormDefaults),
  ): EmailImportImportRuleFormRawValue | PartialWithRequiredKeyOf<NewEmailImportImportRuleFormRawValue> {
    return {
      ...emailImportImportRule,
      lastMatchDate: emailImportImportRule.lastMatchDate ? emailImportImportRule.lastMatchDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: emailImportImportRule.createdDate ? emailImportImportRule.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: emailImportImportRule.lastModifiedDate
        ? emailImportImportRule.lastModifiedDate.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
