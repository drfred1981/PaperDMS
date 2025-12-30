import dayjs from 'dayjs/esm';

import { INotificationTemplate, NewNotificationTemplate } from './notification-template.model';

export const sampleWithRequiredData: INotificationTemplate = {
  id: 12221,
  name: 'microchip red restaurant',
  subject: 'tensely',
  bodyTemplate: '../fake-data/blob/hipster.txt',
  isActive: false,
  createdDate: dayjs('2025-12-29T10:29'),
};

export const sampleWithPartialData: INotificationTemplate = {
  id: 14470,
  name: 'sizzling mostly drat',
  subject: 'hm drab',
  bodyTemplate: '../fake-data/blob/hipster.txt',
  type: 'DOCUMENT_SHARED',
  variables: '../fake-data/blob/hipster.txt',
  isActive: false,
  createdDate: dayjs('2025-12-30T05:43'),
  lastModifiedDate: dayjs('2025-12-29T12:31'),
};

export const sampleWithFullData: INotificationTemplate = {
  id: 27418,
  name: 'correctly how reasonable',
  subject: 'instead tepid duh',
  bodyTemplate: '../fake-data/blob/hipster.txt',
  type: 'DOCUMENT_SHARED',
  channel: 'EMAIL',
  variables: '../fake-data/blob/hipster.txt',
  isActive: false,
  createdDate: dayjs('2025-12-29T12:54'),
  lastModifiedDate: dayjs('2025-12-29T23:50'),
};

export const sampleWithNewData: NewNotificationTemplate = {
  name: 'vague',
  subject: 'tremendously',
  bodyTemplate: '../fake-data/blob/hipster.txt',
  isActive: false,
  createdDate: dayjs('2025-12-29T23:17'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
