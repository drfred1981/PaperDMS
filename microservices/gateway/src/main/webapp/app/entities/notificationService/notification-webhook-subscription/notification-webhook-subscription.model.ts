import dayjs from 'dayjs/esm';

export interface INotificationWebhookSubscription {
  id: number;
  name?: string | null;
  url?: string | null;
  secret?: string | null;
  events?: string | null;
  headers?: string | null;
  isActive?: boolean | null;
  retryCount?: number | null;
  maxRetries?: number | null;
  retryDelay?: number | null;
  lastTriggerDate?: dayjs.Dayjs | null;
  lastSuccessDate?: dayjs.Dayjs | null;
  lastErrorMessage?: string | null;
  failureCount?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewNotificationWebhookSubscription = Omit<INotificationWebhookSubscription, 'id'> & { id: null };
