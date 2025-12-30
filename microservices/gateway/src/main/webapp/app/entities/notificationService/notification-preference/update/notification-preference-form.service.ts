import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotificationPreference, NewNotificationPreference } from '../notification-preference.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotificationPreference for edit and NewNotificationPreferenceFormGroupInput for create.
 */
type NotificationPreferenceFormGroupInput = INotificationPreference | PartialWithRequiredKeyOf<NewNotificationPreference>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INotificationPreference | NewNotificationPreference> = Omit<T, 'lastModifiedDate'> & {
  lastModifiedDate?: string | null;
};

type NotificationPreferenceFormRawValue = FormValueOf<INotificationPreference>;

type NewNotificationPreferenceFormRawValue = FormValueOf<NewNotificationPreference>;

type NotificationPreferenceFormDefaults = Pick<
  NewNotificationPreference,
  'id' | 'emailEnabled' | 'pushEnabled' | 'inAppEnabled' | 'lastModifiedDate'
>;

type NotificationPreferenceFormGroupContent = {
  id: FormControl<NotificationPreferenceFormRawValue['id'] | NewNotificationPreference['id']>;
  userId: FormControl<NotificationPreferenceFormRawValue['userId']>;
  emailEnabled: FormControl<NotificationPreferenceFormRawValue['emailEnabled']>;
  pushEnabled: FormControl<NotificationPreferenceFormRawValue['pushEnabled']>;
  inAppEnabled: FormControl<NotificationPreferenceFormRawValue['inAppEnabled']>;
  notificationTypes: FormControl<NotificationPreferenceFormRawValue['notificationTypes']>;
  quietHoursStart: FormControl<NotificationPreferenceFormRawValue['quietHoursStart']>;
  quietHoursEnd: FormControl<NotificationPreferenceFormRawValue['quietHoursEnd']>;
  frequency: FormControl<NotificationPreferenceFormRawValue['frequency']>;
  lastModifiedDate: FormControl<NotificationPreferenceFormRawValue['lastModifiedDate']>;
};

export type NotificationPreferenceFormGroup = FormGroup<NotificationPreferenceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotificationPreferenceFormService {
  createNotificationPreferenceFormGroup(
    notificationPreference: NotificationPreferenceFormGroupInput = { id: null },
  ): NotificationPreferenceFormGroup {
    const notificationPreferenceRawValue = this.convertNotificationPreferenceToNotificationPreferenceRawValue({
      ...this.getFormDefaults(),
      ...notificationPreference,
    });
    return new FormGroup<NotificationPreferenceFormGroupContent>({
      id: new FormControl(
        { value: notificationPreferenceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      userId: new FormControl(notificationPreferenceRawValue.userId, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      emailEnabled: new FormControl(notificationPreferenceRawValue.emailEnabled, {
        validators: [Validators.required],
      }),
      pushEnabled: new FormControl(notificationPreferenceRawValue.pushEnabled, {
        validators: [Validators.required],
      }),
      inAppEnabled: new FormControl(notificationPreferenceRawValue.inAppEnabled, {
        validators: [Validators.required],
      }),
      notificationTypes: new FormControl(notificationPreferenceRawValue.notificationTypes),
      quietHoursStart: new FormControl(notificationPreferenceRawValue.quietHoursStart, {
        validators: [Validators.maxLength(5)],
      }),
      quietHoursEnd: new FormControl(notificationPreferenceRawValue.quietHoursEnd, {
        validators: [Validators.maxLength(5)],
      }),
      frequency: new FormControl(notificationPreferenceRawValue.frequency),
      lastModifiedDate: new FormControl(notificationPreferenceRawValue.lastModifiedDate),
    });
  }

  getNotificationPreference(form: NotificationPreferenceFormGroup): INotificationPreference | NewNotificationPreference {
    return this.convertNotificationPreferenceRawValueToNotificationPreference(
      form.getRawValue() as NotificationPreferenceFormRawValue | NewNotificationPreferenceFormRawValue,
    );
  }

  resetForm(form: NotificationPreferenceFormGroup, notificationPreference: NotificationPreferenceFormGroupInput): void {
    const notificationPreferenceRawValue = this.convertNotificationPreferenceToNotificationPreferenceRawValue({
      ...this.getFormDefaults(),
      ...notificationPreference,
    });
    form.reset(
      {
        ...notificationPreferenceRawValue,
        id: { value: notificationPreferenceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NotificationPreferenceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      emailEnabled: false,
      pushEnabled: false,
      inAppEnabled: false,
      lastModifiedDate: currentTime,
    };
  }

  private convertNotificationPreferenceRawValueToNotificationPreference(
    rawNotificationPreference: NotificationPreferenceFormRawValue | NewNotificationPreferenceFormRawValue,
  ): INotificationPreference | NewNotificationPreference {
    return {
      ...rawNotificationPreference,
      lastModifiedDate: dayjs(rawNotificationPreference.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertNotificationPreferenceToNotificationPreferenceRawValue(
    notificationPreference: INotificationPreference | (Partial<NewNotificationPreference> & NotificationPreferenceFormDefaults),
  ): NotificationPreferenceFormRawValue | PartialWithRequiredKeyOf<NewNotificationPreferenceFormRawValue> {
    return {
      ...notificationPreference,
      lastModifiedDate: notificationPreference.lastModifiedDate
        ? notificationPreference.lastModifiedDate.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
