import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 10110,
  title: 'reclassify makeover',
  message: '../fake-data/blob/hipster.txt',
  recipientId: 'muscat',
  isRead: true,
  sentDate: dayjs('2025-12-19T17:50'),
  createdDate: dayjs('2025-12-20T03:26'),
};

export const sampleWithPartialData: INotification = {
  id: 22854,
  title: 'ouch next',
  message: '../fake-data/blob/hipster.txt',
  recipientId: 'elver ouch hidden',
  isRead: false,
  readDate: dayjs('2025-12-20T06:06'),
  relatedEntityType: 'boohoo since',
  relatedEntityId: 3890,
  expirationDate: dayjs('2025-12-20T06:37'),
  sentDate: dayjs('2025-12-19T18:27'),
  createdDate: dayjs('2025-12-20T07:04'),
};

export const sampleWithFullData: INotification = {
  id: 5787,
  title: 'some extra-large',
  message: '../fake-data/blob/hipster.txt',
  type: 'DOCUMENT_PROCESSED',
  priority: 'NORMAL',
  recipientId: 'cinema once keel',
  isRead: false,
  readDate: dayjs('2025-12-20T05:20'),
  channel: 'PUSH',
  relatedEntityType: 'bowler fiercely',
  relatedEntityId: 22507,
  actionUrl: 'evenly',
  metadata: '../fake-data/blob/hipster.txt',
  expirationDate: dayjs('2025-12-20T03:35'),
  sentDate: dayjs('2025-12-20T03:50'),
  createdDate: dayjs('2025-12-19T20:10'),
};

export const sampleWithNewData: NewNotification = {
  title: 'consequently voluntarily ew',
  message: '../fake-data/blob/hipster.txt',
  recipientId: 'honesty depart acquaintance',
  isRead: false,
  sentDate: dayjs('2025-12-19T19:27'),
  createdDate: dayjs('2025-12-20T09:40'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
