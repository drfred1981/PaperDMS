import dayjs from 'dayjs/esm';

import { NotificationFrequency } from 'app/entities/enumerations/notification-frequency.model';

export interface INotificationPreference {
  id: number;
  userId?: string | null;
  emailEnabled?: boolean | null;
  pushEnabled?: boolean | null;
  inAppEnabled?: boolean | null;
  notificationTypes?: string | null;
  quietHoursStart?: string | null;
  quietHoursEnd?: string | null;
  frequency?: keyof typeof NotificationFrequency | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewNotificationPreference = Omit<INotificationPreference, 'id'> & { id: null };
