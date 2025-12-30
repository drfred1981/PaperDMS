import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotificationEvent, NewNotificationEvent } from '../notification-event.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotificationEvent for edit and NewNotificationEventFormGroupInput for create.
 */
type NotificationEventFormGroupInput = INotificationEvent | PartialWithRequiredKeyOf<NewNotificationEvent>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INotificationEvent | NewNotificationEvent> = Omit<T, 'eventDate' | 'processedDate'> & {
  eventDate?: string | null;
  processedDate?: string | null;
};

type NotificationEventFormRawValue = FormValueOf<INotificationEvent>;

type NewNotificationEventFormRawValue = FormValueOf<NewNotificationEvent>;

type NotificationEventFormDefaults = Pick<NewNotificationEvent, 'id' | 'eventDate' | 'processed' | 'processedDate'>;

type NotificationEventFormGroupContent = {
  id: FormControl<NotificationEventFormRawValue['id'] | NewNotificationEvent['id']>;
  eventType: FormControl<NotificationEventFormRawValue['eventType']>;
  entityType: FormControl<NotificationEventFormRawValue['entityType']>;
  entityName: FormControl<NotificationEventFormRawValue['entityName']>;
  userId: FormControl<NotificationEventFormRawValue['userId']>;
  eventData: FormControl<NotificationEventFormRawValue['eventData']>;
  eventDate: FormControl<NotificationEventFormRawValue['eventDate']>;
  processed: FormControl<NotificationEventFormRawValue['processed']>;
  processedDate: FormControl<NotificationEventFormRawValue['processedDate']>;
};

export type NotificationEventFormGroup = FormGroup<NotificationEventFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotificationEventFormService {
  createNotificationEventFormGroup(notificationEvent: NotificationEventFormGroupInput = { id: null }): NotificationEventFormGroup {
    const notificationEventRawValue = this.convertNotificationEventToNotificationEventRawValue({
      ...this.getFormDefaults(),
      ...notificationEvent,
    });
    return new FormGroup<NotificationEventFormGroupContent>({
      id: new FormControl(
        { value: notificationEventRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      eventType: new FormControl(notificationEventRawValue.eventType, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      entityType: new FormControl(notificationEventRawValue.entityType, {
        validators: [Validators.maxLength(100)],
      }),
      entityName: new FormControl(notificationEventRawValue.entityName),
      userId: new FormControl(notificationEventRawValue.userId, {
        validators: [Validators.maxLength(50)],
      }),
      eventData: new FormControl(notificationEventRawValue.eventData),
      eventDate: new FormControl(notificationEventRawValue.eventDate, {
        validators: [Validators.required],
      }),
      processed: new FormControl(notificationEventRawValue.processed, {
        validators: [Validators.required],
      }),
      processedDate: new FormControl(notificationEventRawValue.processedDate),
    });
  }

  getNotificationEvent(form: NotificationEventFormGroup): INotificationEvent | NewNotificationEvent {
    return this.convertNotificationEventRawValueToNotificationEvent(
      form.getRawValue() as NotificationEventFormRawValue | NewNotificationEventFormRawValue,
    );
  }

  resetForm(form: NotificationEventFormGroup, notificationEvent: NotificationEventFormGroupInput): void {
    const notificationEventRawValue = this.convertNotificationEventToNotificationEventRawValue({
      ...this.getFormDefaults(),
      ...notificationEvent,
    });
    form.reset(
      {
        ...notificationEventRawValue,
        id: { value: notificationEventRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NotificationEventFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      eventDate: currentTime,
      processed: false,
      processedDate: currentTime,
    };
  }

  private convertNotificationEventRawValueToNotificationEvent(
    rawNotificationEvent: NotificationEventFormRawValue | NewNotificationEventFormRawValue,
  ): INotificationEvent | NewNotificationEvent {
    return {
      ...rawNotificationEvent,
      eventDate: dayjs(rawNotificationEvent.eventDate, DATE_TIME_FORMAT),
      processedDate: dayjs(rawNotificationEvent.processedDate, DATE_TIME_FORMAT),
    };
  }

  private convertNotificationEventToNotificationEventRawValue(
    notificationEvent: INotificationEvent | (Partial<NewNotificationEvent> & NotificationEventFormDefaults),
  ): NotificationEventFormRawValue | PartialWithRequiredKeyOf<NewNotificationEventFormRawValue> {
    return {
      ...notificationEvent,
      eventDate: notificationEvent.eventDate ? notificationEvent.eventDate.format(DATE_TIME_FORMAT) : undefined,
      processedDate: notificationEvent.processedDate ? notificationEvent.processedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
