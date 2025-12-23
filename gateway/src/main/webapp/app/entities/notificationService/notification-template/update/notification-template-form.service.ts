import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotificationTemplate, NewNotificationTemplate } from '../notification-template.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotificationTemplate for edit and NewNotificationTemplateFormGroupInput for create.
 */
type NotificationTemplateFormGroupInput = INotificationTemplate | PartialWithRequiredKeyOf<NewNotificationTemplate>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INotificationTemplate | NewNotificationTemplate> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type NotificationTemplateFormRawValue = FormValueOf<INotificationTemplate>;

type NewNotificationTemplateFormRawValue = FormValueOf<NewNotificationTemplate>;

type NotificationTemplateFormDefaults = Pick<NewNotificationTemplate, 'id' | 'isActive' | 'createdDate' | 'lastModifiedDate'>;

type NotificationTemplateFormGroupContent = {
  id: FormControl<NotificationTemplateFormRawValue['id'] | NewNotificationTemplate['id']>;
  name: FormControl<NotificationTemplateFormRawValue['name']>;
  subject: FormControl<NotificationTemplateFormRawValue['subject']>;
  bodyTemplate: FormControl<NotificationTemplateFormRawValue['bodyTemplate']>;
  type: FormControl<NotificationTemplateFormRawValue['type']>;
  channel: FormControl<NotificationTemplateFormRawValue['channel']>;
  variables: FormControl<NotificationTemplateFormRawValue['variables']>;
  isActive: FormControl<NotificationTemplateFormRawValue['isActive']>;
  createdDate: FormControl<NotificationTemplateFormRawValue['createdDate']>;
  lastModifiedDate: FormControl<NotificationTemplateFormRawValue['lastModifiedDate']>;
};

export type NotificationTemplateFormGroup = FormGroup<NotificationTemplateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotificationTemplateFormService {
  createNotificationTemplateFormGroup(notificationTemplate?: NotificationTemplateFormGroupInput): NotificationTemplateFormGroup {
    const notificationTemplateRawValue = this.convertNotificationTemplateToNotificationTemplateRawValue({
      ...this.getFormDefaults(),
      ...(notificationTemplate ?? { id: null }),
    });
    return new FormGroup<NotificationTemplateFormGroupContent>({
      id: new FormControl(
        { value: notificationTemplateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(notificationTemplateRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      subject: new FormControl(notificationTemplateRawValue.subject, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      bodyTemplate: new FormControl(notificationTemplateRawValue.bodyTemplate, {
        validators: [Validators.required],
      }),
      type: new FormControl(notificationTemplateRawValue.type),
      channel: new FormControl(notificationTemplateRawValue.channel),
      variables: new FormControl(notificationTemplateRawValue.variables),
      isActive: new FormControl(notificationTemplateRawValue.isActive, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(notificationTemplateRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedDate: new FormControl(notificationTemplateRawValue.lastModifiedDate),
    });
  }

  getNotificationTemplate(form: NotificationTemplateFormGroup): INotificationTemplate | NewNotificationTemplate {
    return this.convertNotificationTemplateRawValueToNotificationTemplate(
      form.getRawValue() as NotificationTemplateFormRawValue | NewNotificationTemplateFormRawValue,
    );
  }

  resetForm(form: NotificationTemplateFormGroup, notificationTemplate: NotificationTemplateFormGroupInput): void {
    const notificationTemplateRawValue = this.convertNotificationTemplateToNotificationTemplateRawValue({
      ...this.getFormDefaults(),
      ...notificationTemplate,
    });
    form.reset({
      ...notificationTemplateRawValue,
      id: { value: notificationTemplateRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): NotificationTemplateFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertNotificationTemplateRawValueToNotificationTemplate(
    rawNotificationTemplate: NotificationTemplateFormRawValue | NewNotificationTemplateFormRawValue,
  ): INotificationTemplate | NewNotificationTemplate {
    return {
      ...rawNotificationTemplate,
      createdDate: dayjs(rawNotificationTemplate.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawNotificationTemplate.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertNotificationTemplateToNotificationTemplateRawValue(
    notificationTemplate: INotificationTemplate | (Partial<NewNotificationTemplate> & NotificationTemplateFormDefaults),
  ): NotificationTemplateFormRawValue | PartialWithRequiredKeyOf<NewNotificationTemplateFormRawValue> {
    return {
      ...notificationTemplate,
      createdDate: notificationTemplate.createdDate ? notificationTemplate.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: notificationTemplate.lastModifiedDate ? notificationTemplate.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
