import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWebhookSubscription, NewWebhookSubscription } from '../webhook-subscription.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWebhookSubscription for edit and NewWebhookSubscriptionFormGroupInput for create.
 */
type WebhookSubscriptionFormGroupInput = IWebhookSubscription | PartialWithRequiredKeyOf<NewWebhookSubscription>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWebhookSubscription | NewWebhookSubscription> = Omit<
  T,
  'lastTriggerDate' | 'lastSuccessDate' | 'createdDate' | 'lastModifiedDate'
> & {
  lastTriggerDate?: string | null;
  lastSuccessDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type WebhookSubscriptionFormRawValue = FormValueOf<IWebhookSubscription>;

type NewWebhookSubscriptionFormRawValue = FormValueOf<NewWebhookSubscription>;

type WebhookSubscriptionFormDefaults = Pick<
  NewWebhookSubscription,
  'id' | 'isActive' | 'lastTriggerDate' | 'lastSuccessDate' | 'createdDate' | 'lastModifiedDate'
>;

type WebhookSubscriptionFormGroupContent = {
  id: FormControl<WebhookSubscriptionFormRawValue['id'] | NewWebhookSubscription['id']>;
  name: FormControl<WebhookSubscriptionFormRawValue['name']>;
  url: FormControl<WebhookSubscriptionFormRawValue['url']>;
  secret: FormControl<WebhookSubscriptionFormRawValue['secret']>;
  events: FormControl<WebhookSubscriptionFormRawValue['events']>;
  headers: FormControl<WebhookSubscriptionFormRawValue['headers']>;
  isActive: FormControl<WebhookSubscriptionFormRawValue['isActive']>;
  retryCount: FormControl<WebhookSubscriptionFormRawValue['retryCount']>;
  maxRetries: FormControl<WebhookSubscriptionFormRawValue['maxRetries']>;
  retryDelay: FormControl<WebhookSubscriptionFormRawValue['retryDelay']>;
  lastTriggerDate: FormControl<WebhookSubscriptionFormRawValue['lastTriggerDate']>;
  lastSuccessDate: FormControl<WebhookSubscriptionFormRawValue['lastSuccessDate']>;
  lastErrorMessage: FormControl<WebhookSubscriptionFormRawValue['lastErrorMessage']>;
  failureCount: FormControl<WebhookSubscriptionFormRawValue['failureCount']>;
  createdBy: FormControl<WebhookSubscriptionFormRawValue['createdBy']>;
  createdDate: FormControl<WebhookSubscriptionFormRawValue['createdDate']>;
  lastModifiedDate: FormControl<WebhookSubscriptionFormRawValue['lastModifiedDate']>;
};

export type WebhookSubscriptionFormGroup = FormGroup<WebhookSubscriptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WebhookSubscriptionFormService {
  createWebhookSubscriptionFormGroup(webhookSubscription: WebhookSubscriptionFormGroupInput = { id: null }): WebhookSubscriptionFormGroup {
    const webhookSubscriptionRawValue = this.convertWebhookSubscriptionToWebhookSubscriptionRawValue({
      ...this.getFormDefaults(),
      ...webhookSubscription,
    });
    return new FormGroup<WebhookSubscriptionFormGroupContent>({
      id: new FormControl(
        { value: webhookSubscriptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(webhookSubscriptionRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      url: new FormControl(webhookSubscriptionRawValue.url, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      secret: new FormControl(webhookSubscriptionRawValue.secret, {
        validators: [Validators.maxLength(255)],
      }),
      events: new FormControl(webhookSubscriptionRawValue.events, {
        validators: [Validators.required],
      }),
      headers: new FormControl(webhookSubscriptionRawValue.headers),
      isActive: new FormControl(webhookSubscriptionRawValue.isActive, {
        validators: [Validators.required],
      }),
      retryCount: new FormControl(webhookSubscriptionRawValue.retryCount),
      maxRetries: new FormControl(webhookSubscriptionRawValue.maxRetries),
      retryDelay: new FormControl(webhookSubscriptionRawValue.retryDelay),
      lastTriggerDate: new FormControl(webhookSubscriptionRawValue.lastTriggerDate),
      lastSuccessDate: new FormControl(webhookSubscriptionRawValue.lastSuccessDate),
      lastErrorMessage: new FormControl(webhookSubscriptionRawValue.lastErrorMessage),
      failureCount: new FormControl(webhookSubscriptionRawValue.failureCount),
      createdBy: new FormControl(webhookSubscriptionRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(webhookSubscriptionRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedDate: new FormControl(webhookSubscriptionRawValue.lastModifiedDate),
    });
  }

  getWebhookSubscription(form: WebhookSubscriptionFormGroup): IWebhookSubscription | NewWebhookSubscription {
    return this.convertWebhookSubscriptionRawValueToWebhookSubscription(
      form.getRawValue() as WebhookSubscriptionFormRawValue | NewWebhookSubscriptionFormRawValue,
    );
  }

  resetForm(form: WebhookSubscriptionFormGroup, webhookSubscription: WebhookSubscriptionFormGroupInput): void {
    const webhookSubscriptionRawValue = this.convertWebhookSubscriptionToWebhookSubscriptionRawValue({
      ...this.getFormDefaults(),
      ...webhookSubscription,
    });
    form.reset(
      {
        ...webhookSubscriptionRawValue,
        id: { value: webhookSubscriptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WebhookSubscriptionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      lastTriggerDate: currentTime,
      lastSuccessDate: currentTime,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertWebhookSubscriptionRawValueToWebhookSubscription(
    rawWebhookSubscription: WebhookSubscriptionFormRawValue | NewWebhookSubscriptionFormRawValue,
  ): IWebhookSubscription | NewWebhookSubscription {
    return {
      ...rawWebhookSubscription,
      lastTriggerDate: dayjs(rawWebhookSubscription.lastTriggerDate, DATE_TIME_FORMAT),
      lastSuccessDate: dayjs(rawWebhookSubscription.lastSuccessDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawWebhookSubscription.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawWebhookSubscription.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertWebhookSubscriptionToWebhookSubscriptionRawValue(
    webhookSubscription: IWebhookSubscription | (Partial<NewWebhookSubscription> & WebhookSubscriptionFormDefaults),
  ): WebhookSubscriptionFormRawValue | PartialWithRequiredKeyOf<NewWebhookSubscriptionFormRawValue> {
    return {
      ...webhookSubscription,
      lastTriggerDate: webhookSubscription.lastTriggerDate ? webhookSubscription.lastTriggerDate.format(DATE_TIME_FORMAT) : undefined,
      lastSuccessDate: webhookSubscription.lastSuccessDate ? webhookSubscription.lastSuccessDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: webhookSubscription.createdDate ? webhookSubscription.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: webhookSubscription.lastModifiedDate ? webhookSubscription.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
