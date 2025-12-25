import dayjs from 'dayjs/esm';

import { IWebhookSubscription, NewWebhookSubscription } from './webhook-subscription.model';

export const sampleWithRequiredData: IWebhookSubscription = {
  id: 11235,
  name: 'deploy up',
  url: 'https://separate-appliance.info/',
  events: '../fake-data/blob/hipster.txt',
  isActive: true,
  createdBy: 'hmph',
  createdDate: dayjs('2025-12-24T12:27'),
};

export const sampleWithPartialData: IWebhookSubscription = {
  id: 21618,
  name: 'gosh toward meh',
  url: 'https://comfortable-analogy.biz',
  secret: 'selfishly conservation',
  events: '../fake-data/blob/hipster.txt',
  headers: '../fake-data/blob/hipster.txt',
  isActive: false,
  maxRetries: 16537,
  retryDelay: 24785,
  lastTriggerDate: dayjs('2025-12-25T09:59'),
  lastErrorMessage: '../fake-data/blob/hipster.txt',
  failureCount: 12133,
  createdBy: 'that deduction',
  createdDate: dayjs('2025-12-24T21:49'),
  lastModifiedDate: dayjs('2025-12-24T19:19'),
};

export const sampleWithFullData: IWebhookSubscription = {
  id: 31609,
  name: 'indeed',
  url: 'https://perfumed-meatloaf.name/',
  secret: 'whether oh',
  events: '../fake-data/blob/hipster.txt',
  headers: '../fake-data/blob/hipster.txt',
  isActive: true,
  retryCount: 21056,
  maxRetries: 29212,
  retryDelay: 19591,
  lastTriggerDate: dayjs('2025-12-25T00:20'),
  lastSuccessDate: dayjs('2025-12-24T14:52'),
  lastErrorMessage: '../fake-data/blob/hipster.txt',
  failureCount: 4349,
  createdBy: 'pace inject',
  createdDate: dayjs('2025-12-24T13:56'),
  lastModifiedDate: dayjs('2025-12-24T16:51'),
};

export const sampleWithNewData: NewWebhookSubscription = {
  name: 'psst before',
  url: 'https://useless-help.net/',
  events: '../fake-data/blob/hipster.txt',
  isActive: true,
  createdBy: 'fluctuate wedding bewail',
  createdDate: dayjs('2025-12-24T11:19'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
