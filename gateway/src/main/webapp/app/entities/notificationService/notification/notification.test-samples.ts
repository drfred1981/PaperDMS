import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 10110,
  title: 'reclassify makeover',
  message: '../fake-data/blob/hipster.txt',
  recipientId: 'muscat',
  isRead: true,
  sentDate: dayjs('2025-12-29T08:55'),
  createdDate: dayjs('2025-12-29T18:31'),
};

export const sampleWithPartialData: INotification = {
  id: 22854,
  title: 'ouch next',
  message: '../fake-data/blob/hipster.txt',
  recipientId: 'elver ouch hidden',
  isRead: false,
  readDate: dayjs('2025-12-29T21:11'),
  relatedEntityType: 'boohoo since',
  relatedEntityName: 'modulo',
  expirationDate: dayjs('2025-12-30T04:59'),
  sentDate: dayjs('2025-12-29T16:19'),
  createdDate: dayjs('2025-12-30T05:36'),
};

export const sampleWithFullData: INotification = {
  id: 5787,
  title: 'some extra-large',
  message: '../fake-data/blob/hipster.txt',
  type: 'DOCUMENT_PROCESSED',
  priority: 'NORMAL',
  recipientId: 'cinema once keel',
  isRead: false,
  readDate: dayjs('2025-12-29T20:25'),
  channel: 'PUSH',
  relatedEntityType: 'bowler fiercely',
  relatedEntityName: 'forenenst duh which',
  actionUrl: 'gadzooks till outlying',
  metadata: '../fake-data/blob/hipster.txt',
  expirationDate: dayjs('2025-12-29T09:06'),
  sentDate: dayjs('2025-12-29T07:15'),
  createdDate: dayjs('2025-12-29T08:00'),
};

export const sampleWithNewData: NewNotification = {
  title: 'consequently voluntarily ew',
  message: '../fake-data/blob/hipster.txt',
  recipientId: 'honesty depart acquaintance',
  isRead: false,
  sentDate: dayjs('2025-12-29T10:32'),
  createdDate: dayjs('2025-12-30T00:45'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
