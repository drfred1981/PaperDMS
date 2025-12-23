import dayjs from 'dayjs/esm';

import { IWebhookSubscription, NewWebhookSubscription } from './webhook-subscription.model';

export const sampleWithRequiredData: IWebhookSubscription = {
  id: 11235,
  name: 'deploy up',
  url: 'https://separate-appliance.info/',
  events: '../fake-data/blob/hipster.txt',
  isActive: true,
  createdBy: 'hmph',
  createdDate: dayjs('2025-12-19T17:28'),
};

export const sampleWithPartialData: IWebhookSubscription = {
  id: 21618,
  name: 'gosh toward meh',
  url: 'https://comfortable-analogy.com',
  secret: 'selfishly conservation',
  events: '../fake-data/blob/hipster.txt',
  headers: '../fake-data/blob/hipster.txt',
  isActive: false,
  maxRetries: 16537,
  retryDelay: 24785,
  lastTriggerDate: dayjs('2025-12-20T14:59'),
  lastErrorMessage: '../fake-data/blob/hipster.txt',
  failureCount: 12133,
  createdBy: 'that deduction',
  createdDate: dayjs('2025-12-20T02:50'),
  lastModifiedDate: dayjs('2025-12-20T00:20'),
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
  lastTriggerDate: dayjs('2025-12-20T05:21'),
  lastSuccessDate: dayjs('2025-12-19T19:53'),
  lastErrorMessage: '../fake-data/blob/hipster.txt',
  failureCount: 4349,
  createdBy: 'pace inject',
  createdDate: dayjs('2025-12-19T18:57'),
  lastModifiedDate: dayjs('2025-12-19T21:52'),
};

export const sampleWithNewData: NewWebhookSubscription = {
  name: 'psst before',
  url: 'https://useless-help.net/',
  events: '../fake-data/blob/hipster.txt',
  isActive: true,
  createdBy: 'fluctuate wedding bewail',
  createdDate: dayjs('2025-12-19T16:20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
