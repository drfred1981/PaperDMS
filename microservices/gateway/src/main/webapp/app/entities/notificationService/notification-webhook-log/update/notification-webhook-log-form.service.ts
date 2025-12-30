import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotificationWebhookLog, NewNotificationWebhookLog } from '../notification-webhook-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotificationWebhookLog for edit and NewNotificationWebhookLogFormGroupInput for create.
 */
type NotificationWebhookLogFormGroupInput = INotificationWebhookLog | PartialWithRequiredKeyOf<NewNotificationWebhookLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INotificationWebhookLog | NewNotificationWebhookLog> = Omit<T, 'sentDate'> & {
  sentDate?: string | null;
};

type NotificationWebhookLogFormRawValue = FormValueOf<INotificationWebhookLog>;

type NewNotificationWebhookLogFormRawValue = FormValueOf<NewNotificationWebhookLog>;

type NotificationWebhookLogFormDefaults = Pick<NewNotificationWebhookLog, 'id' | 'isSuccess' | 'sentDate'>;

type NotificationWebhookLogFormGroupContent = {
  id: FormControl<NotificationWebhookLogFormRawValue['id'] | NewNotificationWebhookLog['id']>;
  eventType: FormControl<NotificationWebhookLogFormRawValue['eventType']>;
  payload: FormControl<NotificationWebhookLogFormRawValue['payload']>;
  responseStatus: FormControl<NotificationWebhookLogFormRawValue['responseStatus']>;
  responseBody: FormControl<NotificationWebhookLogFormRawValue['responseBody']>;
  responseTime: FormControl<NotificationWebhookLogFormRawValue['responseTime']>;
  attemptNumber: FormControl<NotificationWebhookLogFormRawValue['attemptNumber']>;
  isSuccess: FormControl<NotificationWebhookLogFormRawValue['isSuccess']>;
  errorMessage: FormControl<NotificationWebhookLogFormRawValue['errorMessage']>;
  sentDate: FormControl<NotificationWebhookLogFormRawValue['sentDate']>;
  subscription: FormControl<NotificationWebhookLogFormRawValue['subscription']>;
};

export type NotificationWebhookLogFormGroup = FormGroup<NotificationWebhookLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotificationWebhookLogFormService {
  createNotificationWebhookLogFormGroup(
    notificationWebhookLog: NotificationWebhookLogFormGroupInput = { id: null },
  ): NotificationWebhookLogFormGroup {
    const notificationWebhookLogRawValue = this.convertNotificationWebhookLogToNotificationWebhookLogRawValue({
      ...this.getFormDefaults(),
      ...notificationWebhookLog,
    });
    return new FormGroup<NotificationWebhookLogFormGroupContent>({
      id: new FormControl(
        { value: notificationWebhookLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      eventType: new FormControl(notificationWebhookLogRawValue.eventType, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      payload: new FormControl(notificationWebhookLogRawValue.payload),
      responseStatus: new FormControl(notificationWebhookLogRawValue.responseStatus),
      responseBody: new FormControl(notificationWebhookLogRawValue.responseBody),
      responseTime: new FormControl(notificationWebhookLogRawValue.responseTime),
      attemptNumber: new FormControl(notificationWebhookLogRawValue.attemptNumber),
      isSuccess: new FormControl(notificationWebhookLogRawValue.isSuccess, {
        validators: [Validators.required],
      }),
      errorMessage: new FormControl(notificationWebhookLogRawValue.errorMessage),
      sentDate: new FormControl(notificationWebhookLogRawValue.sentDate, {
        validators: [Validators.required],
      }),
      subscription: new FormControl(notificationWebhookLogRawValue.subscription),
    });
  }

  getNotificationWebhookLog(form: NotificationWebhookLogFormGroup): INotificationWebhookLog | NewNotificationWebhookLog {
    return this.convertNotificationWebhookLogRawValueToNotificationWebhookLog(
      form.getRawValue() as NotificationWebhookLogFormRawValue | NewNotificationWebhookLogFormRawValue,
    );
  }

  resetForm(form: NotificationWebhookLogFormGroup, notificationWebhookLog: NotificationWebhookLogFormGroupInput): void {
    const notificationWebhookLogRawValue = this.convertNotificationWebhookLogToNotificationWebhookLogRawValue({
      ...this.getFormDefaults(),
      ...notificationWebhookLog,
    });
    form.reset(
      {
        ...notificationWebhookLogRawValue,
        id: { value: notificationWebhookLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NotificationWebhookLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isSuccess: false,
      sentDate: currentTime,
    };
  }

  private convertNotificationWebhookLogRawValueToNotificationWebhookLog(
    rawNotificationWebhookLog: NotificationWebhookLogFormRawValue | NewNotificationWebhookLogFormRawValue,
  ): INotificationWebhookLog | NewNotificationWebhookLog {
    return {
      ...rawNotificationWebhookLog,
      sentDate: dayjs(rawNotificationWebhookLog.sentDate, DATE_TIME_FORMAT),
    };
  }

  private convertNotificationWebhookLogToNotificationWebhookLogRawValue(
    notificationWebhookLog: INotificationWebhookLog | (Partial<NewNotificationWebhookLog> & NotificationWebhookLogFormDefaults),
  ): NotificationWebhookLogFormRawValue | PartialWithRequiredKeyOf<NewNotificationWebhookLogFormRawValue> {
    return {
      ...notificationWebhookLog,
      sentDate: notificationWebhookLog.sentDate ? notificationWebhookLog.sentDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
