import dayjs from 'dayjs/esm';

import { IAIAutoTagJob, NewAIAutoTagJob } from './ai-auto-tag-job.model';

export const sampleWithRequiredData: IAIAutoTagJob = {
  id: 3872,
  documentSha256: 'inferior',
  s3Key: 'boohoo given ack',
  isCached: true,
  createdDate: dayjs('2025-12-30T04:58'),
};

export const sampleWithPartialData: IAIAutoTagJob = {
  id: 23842,
  documentSha256: 'yuck enormously',
  s3Key: 'deform',
  extractedText: '../fake-data/blob/hipster.txt',
  extractedTextSha256: 'contractor across grandiose',
  modelVersion: 'quip that',
  isCached: true,
  startDate: dayjs('2025-12-29T11:06'),
  createdDate: dayjs('2025-12-29T14:11'),
};

export const sampleWithFullData: IAIAutoTagJob = {
  id: 20669,
  documentSha256: 'toothpick accidentally gosh',
  s3Key: 'daily out',
  extractedText: '../fake-data/blob/hipster.txt',
  extractedTextSha256: 't-shirt object clumsy',
  status: 'CANCELLED',
  modelVersion: 'emerge meadow',
  resultCacheKey: 'that',
  isCached: false,
  startDate: dayjs('2025-12-29T15:58'),
  endDate: dayjs('2025-12-29T09:39'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2025-12-29T22:29'),
};

export const sampleWithNewData: NewAIAutoTagJob = {
  documentSha256: 'per',
  s3Key: 'pillow',
  isCached: false,
  createdDate: dayjs('2025-12-29T12:45'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
