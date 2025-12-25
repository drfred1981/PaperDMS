import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWebhookLog, NewWebhookLog } from '../webhook-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWebhookLog for edit and NewWebhookLogFormGroupInput for create.
 */
type WebhookLogFormGroupInput = IWebhookLog | PartialWithRequiredKeyOf<NewWebhookLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWebhookLog | NewWebhookLog> = Omit<T, 'sentDate'> & {
  sentDate?: string | null;
};

type WebhookLogFormRawValue = FormValueOf<IWebhookLog>;

type NewWebhookLogFormRawValue = FormValueOf<NewWebhookLog>;

type WebhookLogFormDefaults = Pick<NewWebhookLog, 'id' | 'isSuccess' | 'sentDate'>;

type WebhookLogFormGroupContent = {
  id: FormControl<WebhookLogFormRawValue['id'] | NewWebhookLog['id']>;
  subscriptionId: FormControl<WebhookLogFormRawValue['subscriptionId']>;
  eventType: FormControl<WebhookLogFormRawValue['eventType']>;
  payload: FormControl<WebhookLogFormRawValue['payload']>;
  responseStatus: FormControl<WebhookLogFormRawValue['responseStatus']>;
  responseBody: FormControl<WebhookLogFormRawValue['responseBody']>;
  responseTime: FormControl<WebhookLogFormRawValue['responseTime']>;
  attemptNumber: FormControl<WebhookLogFormRawValue['attemptNumber']>;
  isSuccess: FormControl<WebhookLogFormRawValue['isSuccess']>;
  errorMessage: FormControl<WebhookLogFormRawValue['errorMessage']>;
  sentDate: FormControl<WebhookLogFormRawValue['sentDate']>;
  subscription: FormControl<WebhookLogFormRawValue['subscription']>;
};

export type WebhookLogFormGroup = FormGroup<WebhookLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WebhookLogFormService {
  createWebhookLogFormGroup(webhookLog: WebhookLogFormGroupInput = { id: null }): WebhookLogFormGroup {
    const webhookLogRawValue = this.convertWebhookLogToWebhookLogRawValue({
      ...this.getFormDefaults(),
      ...webhookLog,
    });
    return new FormGroup<WebhookLogFormGroupContent>({
      id: new FormControl(
        { value: webhookLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      subscriptionId: new FormControl(webhookLogRawValue.subscriptionId, {
        validators: [Validators.required],
      }),
      eventType: new FormControl(webhookLogRawValue.eventType, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      payload: new FormControl(webhookLogRawValue.payload),
      responseStatus: new FormControl(webhookLogRawValue.responseStatus),
      responseBody: new FormControl(webhookLogRawValue.responseBody),
      responseTime: new FormControl(webhookLogRawValue.responseTime),
      attemptNumber: new FormControl(webhookLogRawValue.attemptNumber),
      isSuccess: new FormControl(webhookLogRawValue.isSuccess, {
        validators: [Validators.required],
      }),
      errorMessage: new FormControl(webhookLogRawValue.errorMessage),
      sentDate: new FormControl(webhookLogRawValue.sentDate, {
        validators: [Validators.required],
      }),
      subscription: new FormControl(webhookLogRawValue.subscription, {
        validators: [Validators.required],
      }),
    });
  }

  getWebhookLog(form: WebhookLogFormGroup): IWebhookLog | NewWebhookLog {
    return this.convertWebhookLogRawValueToWebhookLog(form.getRawValue() as WebhookLogFormRawValue | NewWebhookLogFormRawValue);
  }

  resetForm(form: WebhookLogFormGroup, webhookLog: WebhookLogFormGroupInput): void {
    const webhookLogRawValue = this.convertWebhookLogToWebhookLogRawValue({ ...this.getFormDefaults(), ...webhookLog });
    form.reset(
      {
        ...webhookLogRawValue,
        id: { value: webhookLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WebhookLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isSuccess: false,
      sentDate: currentTime,
    };
  }

  private convertWebhookLogRawValueToWebhookLog(
    rawWebhookLog: WebhookLogFormRawValue | NewWebhookLogFormRawValue,
  ): IWebhookLog | NewWebhookLog {
    return {
      ...rawWebhookLog,
      sentDate: dayjs(rawWebhookLog.sentDate, DATE_TIME_FORMAT),
    };
  }

  private convertWebhookLogToWebhookLogRawValue(
    webhookLog: IWebhookLog | (Partial<NewWebhookLog> & WebhookLogFormDefaults),
  ): WebhookLogFormRawValue | PartialWithRequiredKeyOf<NewWebhookLogFormRawValue> {
    return {
      ...webhookLog,
      sentDate: webhookLog.sentDate ? webhookLog.sentDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
