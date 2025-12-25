import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 10110,
  title: 'reclassify makeover',
  message: '../fake-data/blob/hipster.txt',
  recipientId: 'muscat',
  isRead: true,
  sentDate: dayjs('2025-12-24T12:49'),
  createdDate: dayjs('2025-12-24T22:25'),
};

export const sampleWithPartialData: INotification = {
  id: 22854,
  title: 'ouch next',
  message: '../fake-data/blob/hipster.txt',
  recipientId: 'elver ouch hidden',
  isRead: false,
  readDate: dayjs('2025-12-25T01:05'),
  relatedEntityType: 'boohoo since',
  relatedEntityId: 3890,
  expirationDate: dayjs('2025-12-25T01:36'),
  sentDate: dayjs('2025-12-24T13:26'),
  createdDate: dayjs('2025-12-25T02:03'),
};

export const sampleWithFullData: INotification = {
  id: 5787,
  title: 'some extra-large',
  message: '../fake-data/blob/hipster.txt',
  type: 'DOCUMENT_PROCESSED',
  priority: 'NORMAL',
  recipientId: 'cinema once keel',
  isRead: false,
  readDate: dayjs('2025-12-25T00:19'),
  channel: 'PUSH',
  relatedEntityType: 'bowler fiercely',
  relatedEntityId: 22507,
  actionUrl: 'evenly',
  metadata: '../fake-data/blob/hipster.txt',
  expirationDate: dayjs('2025-12-24T22:34'),
  sentDate: dayjs('2025-12-24T22:49'),
  createdDate: dayjs('2025-12-24T15:09'),
};

export const sampleWithNewData: NewNotification = {
  title: 'consequently voluntarily ew',
  message: '../fake-data/blob/hipster.txt',
  recipientId: 'honesty depart acquaintance',
  isRead: false,
  sentDate: dayjs('2025-12-24T14:26'),
  createdDate: dayjs('2025-12-25T04:39'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
