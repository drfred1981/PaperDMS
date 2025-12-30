import dayjs from 'dayjs/esm';

import { IAICache, NewAICache } from './ai-cache.model';

export const sampleWithRequiredData: IAICache = {
  id: 31495,
  cacheKey: 'aw',
  inputSha256: 'so unnecessarily',
  aiProvider: 'reschedule frightfully',
  operation: 'legend stack mid',
  createdDate: dayjs('2025-12-29T14:19'),
};

export const sampleWithPartialData: IAICache = {
  id: 10933,
  cacheKey: 'meanwhile',
  inputSha256: 'nor cheap',
  aiProvider: 'huzzah finally reel',
  aiModel: 'whoa categorise because',
  operation: 'unnaturally',
  inputData: '../fake-data/blob/hipster.txt',
  hits: 16485,
  cost: 489.11,
  createdDate: dayjs('2025-12-29T08:20'),
  expirationDate: dayjs('2025-12-29T20:09'),
};

export const sampleWithFullData: IAICache = {
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
  lastAccessDate: dayjs('2025-12-29T16:04'),
  createdDate: dayjs('2025-12-29T22:18'),
  expirationDate: dayjs('2025-12-29T18:46'),
};

export const sampleWithNewData: NewAICache = {
  cacheKey: 'papa thankfully inscribe',
  inputSha256: 'scaffold oh gee',
  aiProvider: 'ham',
  operation: 'beneficial elderly',
  createdDate: dayjs('2025-12-29T15:54'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
