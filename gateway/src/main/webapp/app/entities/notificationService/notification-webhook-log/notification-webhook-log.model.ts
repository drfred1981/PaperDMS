import dayjs from 'dayjs/esm';
import { INotificationWebhookSubscription } from 'app/entities/notificationService/notification-webhook-subscription/notification-webhook-subscription.model';

export interface INotificationWebhookLog {
  id: number;
  eventType?: string | null;
  payload?: string | null;
  responseStatus?: number | null;
  responseBody?: string | null;
  responseTime?: number | null;
  attemptNumber?: number | null;
  isSuccess?: boolean | null;
  errorMessage?: string | null;
  sentDate?: dayjs.Dayjs | null;
  subscription?: Pick<INotificationWebhookSubscription, 'id'> | null;
}

export type NewNotificationWebhookLog = Omit<INotificationWebhookLog, 'id'> & { id: null };
