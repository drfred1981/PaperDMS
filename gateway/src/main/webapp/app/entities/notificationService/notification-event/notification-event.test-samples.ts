import dayjs from 'dayjs/esm';

import { INotificationEvent, NewNotificationEvent } from './notification-event.model';

export const sampleWithRequiredData: INotificationEvent = {
  id: 8411,
  eventType: 'tedious',
  eventDate: dayjs('2025-12-29T13:12'),
  processed: false,
};

export const sampleWithPartialData: INotificationEvent = {
  id: 15550,
  eventType: 'brilliant orientate whenever',
  userId: 'by quicker innocently',
  eventDate: dayjs('2025-12-29T11:02'),
  processed: true,
  processedDate: dayjs('2025-12-30T06:06'),
};

export const sampleWithFullData: INotificationEvent = {
  id: 31374,
  eventType: 'enormously climb furthermore',
  entityType: 'metal',
  entityName: 'overstay optimistically',
  userId: 'expensive',
  eventData: '../fake-data/blob/hipster.txt',
  eventDate: dayjs('2025-12-30T02:43'),
  processed: true,
  processedDate: dayjs('2025-12-30T04:11'),
};

export const sampleWithNewData: NewNotificationEvent = {
  eventType: 'oh frenetically rightfully',
  eventDate: dayjs('2025-12-29T11:47'),
  processed: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
