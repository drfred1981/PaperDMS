import dayjs from 'dayjs/esm';

import { IAutoTagJob, NewAutoTagJob } from './auto-tag-job.model';

export const sampleWithRequiredData: IAutoTagJob = {
  id: 28241,
  documentId: 27043,
  documentSha256: 'geez',
  s3Key: 'wicked safely',
  isCached: false,
  createdDate: dayjs('2025-12-20T06:20'),
};

export const sampleWithPartialData: IAutoTagJob = {
  id: 17970,
  documentId: 14863,
  documentSha256: 'after',
  s3Key: 'access often',
  extractedText: '../fake-data/blob/hipster.txt',
  extractedTextSha256: 'despite redevelop',
  detectedLanguage: 'though bec',
  resultCacheKey: 'splash highly',
  isCached: false,
  endDate: dayjs('2025-12-20T09:58'),
  errorMessage: '../fake-data/blob/hipster.txt',
  confidence: 0.86,
  createdDate: dayjs('2025-12-19T22:30'),
};

export const sampleWithFullData: IAutoTagJob = {
  id: 9101,
  documentId: 25866,
  documentSha256: 'fervently',
  s3Key: 'including',
  extractedText: '../fake-data/blob/hipster.txt',
  extractedTextSha256: 'ew mmm traffic',
  detectedLanguage: 'pike',
  languageConfidence: 0.06,
  status: 'FAILED',
  modelVersion: 'pfft',
  resultCacheKey: 'ugh',
  isCached: false,
  startDate: dayjs('2025-12-19T22:23'),
  endDate: dayjs('2025-12-20T14:06'),
  errorMessage: '../fake-data/blob/hipster.txt',
  confidence: 0.79,
  createdDate: dayjs('2025-12-20T06:07'),
};

export const sampleWithNewData: NewAutoTagJob = {
  documentId: 21306,
  documentSha256: 'within afore above',
  s3Key: 'expostulate hornet throughout',
  isCached: false,
  createdDate: dayjs('2025-12-20T13:16'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
