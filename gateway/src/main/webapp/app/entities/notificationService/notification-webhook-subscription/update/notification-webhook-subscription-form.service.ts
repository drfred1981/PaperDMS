import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotificationWebhookSubscription, NewNotificationWebhookSubscription } from '../notification-webhook-subscription.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotificationWebhookSubscription for edit and NewNotificationWebhookSubscriptionFormGroupInput for create.
 */
type NotificationWebhookSubscriptionFormGroupInput =
  | INotificationWebhookSubscription
  | PartialWithRequiredKeyOf<NewNotificationWebhookSubscription>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INotificationWebhookSubscription | NewNotificationWebhookSubscription> = Omit<
  T,
  'lastTriggerDate' | 'lastSuccessDate' | 'createdDate' | 'lastModifiedDate'
> & {
  lastTriggerDate?: string | null;
  lastSuccessDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type NotificationWebhookSubscriptionFormRawValue = FormValueOf<INotificationWebhookSubscription>;

type NewNotificationWebhookSubscriptionFormRawValue = FormValueOf<NewNotificationWebhookSubscription>;

type NotificationWebhookSubscriptionFormDefaults = Pick<
  NewNotificationWebhookSubscription,
  'id' | 'isActive' | 'lastTriggerDate' | 'lastSuccessDate' | 'createdDate' | 'lastModifiedDate'
>;

type NotificationWebhookSubscriptionFormGroupContent = {
  id: FormControl<NotificationWebhookSubscriptionFormRawValue['id'] | NewNotificationWebhookSubscription['id']>;
  name: FormControl<NotificationWebhookSubscriptionFormRawValue['name']>;
  url: FormControl<NotificationWebhookSubscriptionFormRawValue['url']>;
  secret: FormControl<NotificationWebhookSubscriptionFormRawValue['secret']>;
  events: FormControl<NotificationWebhookSubscriptionFormRawValue['events']>;
  headers: FormControl<NotificationWebhookSubscriptionFormRawValue['headers']>;
  isActive: FormControl<NotificationWebhookSubscriptionFormRawValue['isActive']>;
  retryCount: FormControl<NotificationWebhookSubscriptionFormRawValue['retryCount']>;
  maxRetries: FormControl<NotificationWebhookSubscriptionFormRawValue['maxRetries']>;
  retryDelay: FormControl<NotificationWebhookSubscriptionFormRawValue['retryDelay']>;
  lastTriggerDate: FormControl<NotificationWebhookSubscriptionFormRawValue['lastTriggerDate']>;
  lastSuccessDate: FormControl<NotificationWebhookSubscriptionFormRawValue['lastSuccessDate']>;
  lastErrorMessage: FormControl<NotificationWebhookSubscriptionFormRawValue['lastErrorMessage']>;
  failureCount: FormControl<NotificationWebhookSubscriptionFormRawValue['failureCount']>;
  createdBy: FormControl<NotificationWebhookSubscriptionFormRawValue['createdBy']>;
  createdDate: FormControl<NotificationWebhookSubscriptionFormRawValue['createdDate']>;
  lastModifiedDate: FormControl<NotificationWebhookSubscriptionFormRawValue['lastModifiedDate']>;
};

export type NotificationWebhookSubscriptionFormGroup = FormGroup<NotificationWebhookSubscriptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotificationWebhookSubscriptionFormService {
  createNotificationWebhookSubscriptionFormGroup(
    notificationWebhookSubscription: NotificationWebhookSubscriptionFormGroupInput = { id: null },
  ): NotificationWebhookSubscriptionFormGroup {
    const notificationWebhookSubscriptionRawValue = this.convertNotificationWebhookSubscriptionToNotificationWebhookSubscriptionRawValue({
      ...this.getFormDefaults(),
      ...notificationWebhookSubscription,
    });
    return new FormGroup<NotificationWebhookSubscriptionFormGroupContent>({
      id: new FormControl(
        { value: notificationWebhookSubscriptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(notificationWebhookSubscriptionRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      url: new FormControl(notificationWebhookSubscriptionRawValue.url, {
        validators: [Validators.required, Validators.maxLength(1000)],
      }),
      secret: new FormControl(notificationWebhookSubscriptionRawValue.secret, {
        validators: [Validators.maxLength(255)],
      }),
      events: new FormControl(notificationWebhookSubscriptionRawValue.events, {
        validators: [Validators.required],
      }),
      headers: new FormControl(notificationWebhookSubscriptionRawValue.headers),
      isActive: new FormControl(notificationWebhookSubscriptionRawValue.isActive, {
        validators: [Validators.required],
      }),
      retryCount: new FormControl(notificationWebhookSubscriptionRawValue.retryCount),
      maxRetries: new FormControl(notificationWebhookSubscriptionRawValue.maxRetries),
      retryDelay: new FormControl(notificationWebhookSubscriptionRawValue.retryDelay),
      lastTriggerDate: new FormControl(notificationWebhookSubscriptionRawValue.lastTriggerDate),
      lastSuccessDate: new FormControl(notificationWebhookSubscriptionRawValue.lastSuccessDate),
      lastErrorMessage: new FormControl(notificationWebhookSubscriptionRawValue.lastErrorMessage),
      failureCount: new FormControl(notificationWebhookSubscriptionRawValue.failureCount),
      createdBy: new FormControl(notificationWebhookSubscriptionRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(notificationWebhookSubscriptionRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedDate: new FormControl(notificationWebhookSubscriptionRawValue.lastModifiedDate),
    });
  }

  getNotificationWebhookSubscription(
    form: NotificationWebhookSubscriptionFormGroup,
  ): INotificationWebhookSubscription | NewNotificationWebhookSubscription {
    return this.convertNotificationWebhookSubscriptionRawValueToNotificationWebhookSubscription(
      form.getRawValue() as NotificationWebhookSubscriptionFormRawValue | NewNotificationWebhookSubscriptionFormRawValue,
    );
  }

  resetForm(
    form: NotificationWebhookSubscriptionFormGroup,
    notificationWebhookSubscription: NotificationWebhookSubscriptionFormGroupInput,
  ): void {
    const notificationWebhookSubscriptionRawValue = this.convertNotificationWebhookSubscriptionToNotificationWebhookSubscriptionRawValue({
      ...this.getFormDefaults(),
      ...notificationWebhookSubscription,
    });
    form.reset(
      {
        ...notificationWebhookSubscriptionRawValue,
        id: { value: notificationWebhookSubscriptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NotificationWebhookSubscriptionFormDefaults {
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

  private convertNotificationWebhookSubscriptionRawValueToNotificationWebhookSubscription(
    rawNotificationWebhookSubscription: NotificationWebhookSubscriptionFormRawValue | NewNotificationWebhookSubscriptionFormRawValue,
  ): INotificationWebhookSubscription | NewNotificationWebhookSubscription {
    return {
      ...rawNotificationWebhookSubscription,
      lastTriggerDate: dayjs(rawNotificationWebhookSubscription.lastTriggerDate, DATE_TIME_FORMAT),
      lastSuccessDate: dayjs(rawNotificationWebhookSubscription.lastSuccessDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawNotificationWebhookSubscription.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawNotificationWebhookSubscription.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertNotificationWebhookSubscriptionToNotificationWebhookSubscriptionRawValue(
    notificationWebhookSubscription:
      | INotificationWebhookSubscription
      | (Partial<NewNotificationWebhookSubscription> & NotificationWebhookSubscriptionFormDefaults),
  ): NotificationWebhookSubscriptionFormRawValue | PartialWithRequiredKeyOf<NewNotificationWebhookSubscriptionFormRawValue> {
    return {
      ...notificationWebhookSubscription,
      lastTriggerDate: notificationWebhookSubscription.lastTriggerDate
        ? notificationWebhookSubscription.lastTriggerDate.format(DATE_TIME_FORMAT)
        : undefined,
      lastSuccessDate: notificationWebhookSubscription.lastSuccessDate
        ? notificationWebhookSubscription.lastSuccessDate.format(DATE_TIME_FORMAT)
        : undefined,
      createdDate: notificationWebhookSubscription.createdDate
        ? notificationWebhookSubscription.createdDate.format(DATE_TIME_FORMAT)
        : undefined,
      lastModifiedDate: notificationWebhookSubscription.lastModifiedDate
        ? notificationWebhookSubscription.lastModifiedDate.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
