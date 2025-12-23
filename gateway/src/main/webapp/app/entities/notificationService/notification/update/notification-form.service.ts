import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotification, NewNotification } from '../notification.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotification for edit and NewNotificationFormGroupInput for create.
 */
type NotificationFormGroupInput = INotification | PartialWithRequiredKeyOf<NewNotification>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INotification | NewNotification> = Omit<T, 'readDate' | 'expirationDate' | 'sentDate' | 'createdDate'> & {
  readDate?: string | null;
  expirationDate?: string | null;
  sentDate?: string | null;
  createdDate?: string | null;
};

type NotificationFormRawValue = FormValueOf<INotification>;

type NewNotificationFormRawValue = FormValueOf<NewNotification>;

type NotificationFormDefaults = Pick<NewNotification, 'id' | 'isRead' | 'readDate' | 'expirationDate' | 'sentDate' | 'createdDate'>;

type NotificationFormGroupContent = {
  id: FormControl<NotificationFormRawValue['id'] | NewNotification['id']>;
  title: FormControl<NotificationFormRawValue['title']>;
  message: FormControl<NotificationFormRawValue['message']>;
  type: FormControl<NotificationFormRawValue['type']>;
  priority: FormControl<NotificationFormRawValue['priority']>;
  recipientId: FormControl<NotificationFormRawValue['recipientId']>;
  isRead: FormControl<NotificationFormRawValue['isRead']>;
  readDate: FormControl<NotificationFormRawValue['readDate']>;
  channel: FormControl<NotificationFormRawValue['channel']>;
  relatedEntityType: FormControl<NotificationFormRawValue['relatedEntityType']>;
  relatedEntityId: FormControl<NotificationFormRawValue['relatedEntityId']>;
  actionUrl: FormControl<NotificationFormRawValue['actionUrl']>;
  metadata: FormControl<NotificationFormRawValue['metadata']>;
  expirationDate: FormControl<NotificationFormRawValue['expirationDate']>;
  sentDate: FormControl<NotificationFormRawValue['sentDate']>;
  createdDate: FormControl<NotificationFormRawValue['createdDate']>;
  template: FormControl<NotificationFormRawValue['template']>;
};

export type NotificationFormGroup = FormGroup<NotificationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotificationFormService {
  createNotificationFormGroup(notification?: NotificationFormGroupInput): NotificationFormGroup {
    const notificationRawValue = this.convertNotificationToNotificationRawValue({
      ...this.getFormDefaults(),
      ...(notification ?? { id: null }),
    });
    return new FormGroup<NotificationFormGroupContent>({
      id: new FormControl(
        { value: notificationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(notificationRawValue.title, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      message: new FormControl(notificationRawValue.message, {
        validators: [Validators.required],
      }),
      type: new FormControl(notificationRawValue.type),
      priority: new FormControl(notificationRawValue.priority),
      recipientId: new FormControl(notificationRawValue.recipientId, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      isRead: new FormControl(notificationRawValue.isRead, {
        validators: [Validators.required],
      }),
      readDate: new FormControl(notificationRawValue.readDate),
      channel: new FormControl(notificationRawValue.channel),
      relatedEntityType: new FormControl(notificationRawValue.relatedEntityType, {
        validators: [Validators.maxLength(100)],
      }),
      relatedEntityId: new FormControl(notificationRawValue.relatedEntityId),
      actionUrl: new FormControl(notificationRawValue.actionUrl, {
        validators: [Validators.maxLength(500)],
      }),
      metadata: new FormControl(notificationRawValue.metadata),
      expirationDate: new FormControl(notificationRawValue.expirationDate),
      sentDate: new FormControl(notificationRawValue.sentDate, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(notificationRawValue.createdDate, {
        validators: [Validators.required],
      }),
      template: new FormControl(notificationRawValue.template),
    });
  }

  getNotification(form: NotificationFormGroup): INotification | NewNotification {
    return this.convertNotificationRawValueToNotification(form.getRawValue() as NotificationFormRawValue | NewNotificationFormRawValue);
  }

  resetForm(form: NotificationFormGroup, notification: NotificationFormGroupInput): void {
    const notificationRawValue = this.convertNotificationToNotificationRawValue({ ...this.getFormDefaults(), ...notification });
    form.reset({
      ...notificationRawValue,
      id: { value: notificationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): NotificationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isRead: false,
      readDate: currentTime,
      expirationDate: currentTime,
      sentDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertNotificationRawValueToNotification(
    rawNotification: NotificationFormRawValue | NewNotificationFormRawValue,
  ): INotification | NewNotification {
    return {
      ...rawNotification,
      readDate: dayjs(rawNotification.readDate, DATE_TIME_FORMAT),
      expirationDate: dayjs(rawNotification.expirationDate, DATE_TIME_FORMAT),
      sentDate: dayjs(rawNotification.sentDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawNotification.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertNotificationToNotificationRawValue(
    notification: INotification | (Partial<NewNotification> & NotificationFormDefaults),
  ): NotificationFormRawValue | PartialWithRequiredKeyOf<NewNotificationFormRawValue> {
    return {
      ...notification,
      readDate: notification.readDate ? notification.readDate.format(DATE_TIME_FORMAT) : undefined,
      expirationDate: notification.expirationDate ? notification.expirationDate.format(DATE_TIME_FORMAT) : undefined,
      sentDate: notification.sentDate ? notification.sentDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: notification.createdDate ? notification.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
