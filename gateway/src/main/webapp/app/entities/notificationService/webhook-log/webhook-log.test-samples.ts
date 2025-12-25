import dayjs from 'dayjs/esm';

import { IWebhookLog, NewWebhookLog } from './webhook-log.model';

export const sampleWithRequiredData: IWebhookLog = {
  id: 21000,
  subscriptionId: 16017,
  eventType: 'proselytise fooey',
  isSuccess: false,
  sentDate: dayjs('2025-12-24T22:03'),
};

export const sampleWithPartialData: IWebhookLog = {
  id: 12206,
  subscriptionId: 24019,
  eventType: 'switch deduct',
  responseStatus: 19418,
  responseBody: '../fake-data/blob/hipster.txt',
  responseTime: 11728,
  isSuccess: false,
  sentDate: dayjs('2025-12-25T08:28'),
};

export const sampleWithFullData: IWebhookLog = {
  id: 19075,
  subscriptionId: 10120,
  eventType: 'pricey whoever dutiful',
  payload: '../fake-data/blob/hipster.txt',
  responseStatus: 3020,
  responseBody: '../fake-data/blob/hipster.txt',
  responseTime: 32709,
  attemptNumber: 10104,
  isSuccess: false,
  errorMessage: '../fake-data/blob/hipster.txt',
  sentDate: dayjs('2025-12-24T14:25'),
};

export const sampleWithNewData: NewWebhookLog = {
  subscriptionId: 4750,
  eventType: 'huzzah opposite bloom',
  isSuccess: false,
  sentDate: dayjs('2025-12-25T04:02'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
