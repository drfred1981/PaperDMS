import dayjs from 'dayjs/esm';

import { INotificationEvent, NewNotificationEvent } from './notification-event.model';

export const sampleWithRequiredData: INotificationEvent = {
  id: 8411,
  eventType: 'tedious',
  eventDate: dayjs('2025-12-24T17:06'),
  processed: false,
};

export const sampleWithPartialData: INotificationEvent = {
  id: 15550,
  eventType: 'brilliant orientate whenever',
  userId: 'by quicker innocently',
  eventDate: dayjs('2025-12-24T14:56'),
  processed: true,
  processedDate: dayjs('2025-12-25T10:00'),
};

export const sampleWithFullData: INotificationEvent = {
  id: 31374,
  eventType: 'enormously climb furthermore',
  entityType: 'metal',
  entityId: 19346,
  userId: 'provided although expensive',
  eventData: '../fake-data/blob/hipster.txt',
  eventDate: dayjs('2025-12-25T06:37'),
  processed: true,
  processedDate: dayjs('2025-12-25T08:05'),
};

export const sampleWithNewData: NewNotificationEvent = {
  eventType: 'oh frenetically rightfully',
  eventDate: dayjs('2025-12-24T15:41'),
  processed: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
