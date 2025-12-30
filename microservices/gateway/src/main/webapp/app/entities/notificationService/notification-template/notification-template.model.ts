import dayjs from 'dayjs/esm';
import { NotificationType } from 'app/entities/enumerations/notification-type.model';
import { NotificationChannel } from 'app/entities/enumerations/notification-channel.model';

export interface INotificationTemplate {
  id: number;
  name?: string | null;
  subject?: string | null;
  bodyTemplate?: string | null;
  type?: keyof typeof NotificationType | null;
  channel?: keyof typeof NotificationChannel | null;
  variables?: string | null;
  isActive?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewNotificationTemplate = Omit<INotificationTemplate, 'id'> & { id: null };
