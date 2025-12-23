import dayjs from 'dayjs/esm';

import { IAiCache, NewAiCache } from './ai-cache.model';

export const sampleWithRequiredData: IAiCache = {
  id: 31495,
  cacheKey: 'aw',
  inputSha256: 'so unnecessarily',
  aiProvider: 'reschedule frightfully',
  operation: 'legend stack mid',
  createdDate: dayjs('2025-12-19T23:14'),
};

export const sampleWithPartialData: IAiCache = {
  id: 10933,
  cacheKey: 'meanwhile',
  inputSha256: 'nor cheap',
  aiProvider: 'huzzah finally reel',
  aiModel: 'whoa categorise because',
  operation: 'unnaturally',
  inputData: '../fake-data/blob/hipster.txt',
  hits: 16485,
  cost: 489.11,
  createdDate: dayjs('2025-12-19T17:15'),
  expirationDate: dayjs('2025-12-20T05:04'),
};

export const sampleWithFullData: IAiCache = {
  id: 1108,
  cacheKey: 'rationalize scaly',
  inputSha256: 'minus',
  aiProvider: 'ostrich ack tinted',
  aiModel: 'hot along pear',
  operation: 'that which coliseum',
  inputData: '../fake-data/blob/hipster.txt',
  resultData: '../fake-data/blob/hipster.txt',
  s3ResultKey: 'concentration ha homely',
  confidence: 0.83,
  metadata: '../fake-data/blob/hipster.txt',
  hits: 24327,
  cost: 21022.57,
  lastAccessDate: dayjs('2025-12-20T00:59'),
  createdDate: dayjs('2025-12-20T07:13'),
  expirationDate: dayjs('2025-12-20T03:41'),
};

export const sampleWithNewData: NewAiCache = {
  cacheKey: 'papa thankfully inscribe',
  inputSha256: 'scaffold oh gee',
  aiProvider: 'ham',
  operation: 'beneficial elderly',
  createdDate: dayjs('2025-12-20T00:49'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
