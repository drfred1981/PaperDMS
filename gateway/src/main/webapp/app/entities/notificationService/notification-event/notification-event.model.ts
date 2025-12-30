import dayjs from 'dayjs/esm';

export interface INotificationEvent {
  id: number;
  eventType?: string | null;
  entityType?: string | null;
  entityName?: string | null;
  userId?: string | null;
  eventData?: string | null;
  eventDate?: dayjs.Dayjs | null;
  processed?: boolean | null;
  processedDate?: dayjs.Dayjs | null;
}

export type NewNotificationEvent = Omit<INotificationEvent, 'id'> & { id: null };
