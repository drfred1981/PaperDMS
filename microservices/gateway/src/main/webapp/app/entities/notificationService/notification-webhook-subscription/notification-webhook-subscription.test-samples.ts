import dayjs from 'dayjs/esm';

import { INotificationWebhookSubscription, NewNotificationWebhookSubscription } from './notification-webhook-subscription.model';

export const sampleWithRequiredData: INotificationWebhookSubscription = {
  id: 32114,
  name: 'superficial',
  url: 'https://stunning-dredger.name/',
  events: '../fake-data/blob/hipster.txt',
  isActive: true,
  createdBy: 'ouch statue',
  createdDate: dayjs('2025-12-29T08:48'),
};

export const sampleWithPartialData: INotificationWebhookSubscription = {
  id: 1036,
  name: 'suspension',
  url: 'https://blue-countess.biz',
  events: '../fake-data/blob/hipster.txt',
  headers: '../fake-data/blob/hipster.txt',
  isActive: true,
  retryCount: 27707,
  lastTriggerDate: dayjs('2025-12-29T20:21'),
  lastErrorMessage: '../fake-data/blob/hipster.txt',
  failureCount: 1778,
  createdBy: 'against',
  createdDate: dayjs('2025-12-29T08:16'),
};

export const sampleWithFullData: INotificationWebhookSubscription = {
  id: 30582,
  name: 'far joshingly sniveling',
  url: 'https://stiff-mythology.net',
  secret: 'tinderbox per',
  events: '../fake-data/blob/hipster.txt',
  headers: '../fake-data/blob/hipster.txt',
  isActive: false,
  retryCount: 32365,
  maxRetries: 1617,
  retryDelay: 8990,
  lastTriggerDate: dayjs('2025-12-29T21:16'),
  lastSuccessDate: dayjs('2025-12-29T09:15'),
  lastErrorMessage: '../fake-data/blob/hipster.txt',
  failureCount: 28123,
  createdBy: 'evenly',
  createdDate: dayjs('2025-12-29T23:33'),
  lastModifiedDate: dayjs('2025-12-29T08:08'),
};

export const sampleWithNewData: NewNotificationWebhookSubscription = {
  name: 'about whoa',
  url: 'https://jittery-popularity.info',
  events: '../fake-data/blob/hipster.txt',
  isActive: false,
  createdBy: 'hoarse recede',
  createdDate: dayjs('2025-12-29T07:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
