import dayjs from 'dayjs/esm';

import { INotificationWebhookLog, NewNotificationWebhookLog } from './notification-webhook-log.model';

export const sampleWithRequiredData: INotificationWebhookLog = {
  id: 15413,
  eventType: 'over mmm marvelous',
  isSuccess: false,
  sentDate: dayjs('2025-12-29T13:59'),
};

export const sampleWithPartialData: INotificationWebhookLog = {
  id: 32699,
  eventType: 'unto',
  responseBody: '../fake-data/blob/hipster.txt',
  isSuccess: true,
  errorMessage: '../fake-data/blob/hipster.txt',
  sentDate: dayjs('2025-12-29T15:21'),
};

export const sampleWithFullData: INotificationWebhookLog = {
  id: 24341,
  eventType: 'spiffy unto whose',
  payload: '../fake-data/blob/hipster.txt',
  responseStatus: 7536,
  responseBody: '../fake-data/blob/hipster.txt',
  responseTime: 29683,
  attemptNumber: 70,
  isSuccess: false,
  errorMessage: '../fake-data/blob/hipster.txt',
  sentDate: dayjs('2025-12-30T06:43'),
};

export const sampleWithNewData: NewNotificationWebhookLog = {
  eventType: 'welcome gah',
  isSuccess: false,
  sentDate: dayjs('2025-12-29T19:34'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
