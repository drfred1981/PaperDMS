import dayjs from 'dayjs/esm';

import { IWebhookSubscription } from 'app/entities/notificationService/webhook-subscription/webhook-subscription.model';

export interface IWebhookLog {
  id: number;
  subscriptionId?: number | null;
  eventType?: string | null;
  payload?: string | null;
  responseStatus?: number | null;
  responseBody?: string | null;
  responseTime?: number | null;
  attemptNumber?: number | null;
  isSuccess?: boolean | null;
  errorMessage?: string | null;
  sentDate?: dayjs.Dayjs | null;
  subscription?: Pick<IWebhookSubscription, 'id'> | null;
}

export type NewWebhookLog = Omit<IWebhookLog, 'id'> & { id: null };
