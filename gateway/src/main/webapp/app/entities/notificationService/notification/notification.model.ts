import dayjs from 'dayjs/esm';
import { INotificationTemplate } from 'app/entities/notificationService/notification-template/notification-template.model';
import { NotificationType } from 'app/entities/enumerations/notification-type.model';
import { NotificationPriority } from 'app/entities/enumerations/notification-priority.model';
import { NotificationChannel } from 'app/entities/enumerations/notification-channel.model';

export interface INotification {
  id: number;
  title?: string | null;
  message?: string | null;
  type?: keyof typeof NotificationType | null;
  priority?: keyof typeof NotificationPriority | null;
  recipientId?: string | null;
  isRead?: boolean | null;
  readDate?: dayjs.Dayjs | null;
  channel?: keyof typeof NotificationChannel | null;
  relatedEntityType?: string | null;
  relatedEntityName?: string | null;
  actionUrl?: string | null;
  metadata?: string | null;
  expirationDate?: dayjs.Dayjs | null;
  sentDate?: dayjs.Dayjs | null;
  createdDate?: dayjs.Dayjs | null;
  template?: Pick<INotificationTemplate, 'id'> | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
