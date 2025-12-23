import dayjs from 'dayjs/esm';

import { INotificationEvent, NewNotificationEvent } from './notification-event.model';

export const sampleWithRequiredData: INotificationEvent = {
  id: 8411,
  eventType: 'tedious',
  eventDate: dayjs('2025-12-19T22:07'),
  processed: false,
};

export const sampleWithPartialData: INotificationEvent = {
  id: 15550,
  eventType: 'brilliant orientate whenever',
  userId: 'by quicker innocently',
  eventDate: dayjs('2025-12-19T19:57'),
  processed: true,
  processedDate: dayjs('2025-12-20T15:01'),
};

export const sampleWithFullData: INotificationEvent = {
  id: 31374,
  eventType: 'enormously climb furthermore',
  entityType: 'metal',
  entityId: 19346,
  userId: 'provided although expensive',
  eventData: '../fake-data/blob/hipster.txt',
  eventDate: dayjs('2025-12-20T11:38'),
  processed: true,
  processedDate: dayjs('2025-12-20T13:06'),
};

export const sampleWithNewData: NewNotificationEvent = {
  eventType: 'oh frenetically rightfully',
  eventDate: dayjs('2025-12-19T20:42'),
  processed: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
