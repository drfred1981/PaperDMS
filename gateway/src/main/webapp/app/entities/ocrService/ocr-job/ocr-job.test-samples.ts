import dayjs from 'dayjs/esm';

import { IOcrJob, NewOcrJob } from './ocr-job.model';

export const sampleWithRequiredData: IOcrJob = {
  id: 6011,
  status: 'FAILED',
  documentId: 30158,
  documentSha256: 'abseil fill',
  s3Key: 'bob',
  s3Bucket: 'so',
  isCached: true,
  createdDate: dayjs('2025-12-20T10:42'),
  createdBy: 'and skyscraper than',
};

export const sampleWithPartialData: IOcrJob = {
  id: 8514,
  status: 'COMPARING',
  documentId: 2348,
  documentSha256: 'apropos hm plus',
  s3Key: 'farm kiddingly',
  s3Bucket: 'yum bah meanwhile',
  detectedLanguage: 'inasmuch y',
  aiModel: 'pity abscond fall',
  resultCacheKey: 'nor lest guard',
  isCached: true,
  startDate: dayjs('2025-12-19T23:01'),
  endDate: dayjs('2025-12-20T12:04'),
  progress: 64,
  createdDate: dayjs('2025-12-19T21:04'),
  createdBy: 'feather joyful',
};

export const sampleWithFullData: IOcrJob = {
  id: 16434,
  status: 'CANCELLED',
  documentId: 5292,
  documentSha256: 'wrongly that voluntarily',
  s3Key: 'inasmuch',
  s3Bucket: 'developing soggy',
  requestedLanguage: 'barring hm',
  detectedLanguage: 'jubilant q',
  languageConfidence: 0.54,
  ocrEngine: 'AZURE_VISION',
  tikaEndpoint: 'like bouncy',
  aiProvider: 'adjourn wearily zowie',
  aiModel: 'daintily topsail',
  resultCacheKey: 'unto',
  isCached: true,
  startDate: dayjs('2025-12-20T01:23'),
  endDate: dayjs('2025-12-20T14:46'),
  errorMessage: '../fake-data/blob/hipster.txt',
  pageCount: 24811,
  progress: 22,
  retryCount: 31380,
  priority: 12036,
  processingTime: 27245,
  costEstimate: 25853.9,
  createdDate: dayjs('2025-12-20T14:39'),
  createdBy: 'molasses remark',
};

export const sampleWithNewData: NewOcrJob = {
  status: 'RETRYING',
  documentId: 16869,
  documentSha256: 'hm youthful edge',
  s3Key: 'vice nor',
  s3Bucket: 'acidly in boohoo',
  isCached: false,
  createdDate: dayjs('2025-12-20T10:19'),
  createdBy: 'strict properly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
