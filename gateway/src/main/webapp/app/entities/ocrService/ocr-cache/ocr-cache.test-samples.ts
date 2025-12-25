import dayjs from 'dayjs/esm';

import { IOcrCache, NewOcrCache } from './ocr-cache.model';

export const sampleWithRequiredData: IOcrCache = {
  id: 18819,
  documentSha256: 'weekly',
  pageCount: 5482,
  s3ResultKey: 'buttery yet',
  s3Bucket: 'jot',
  createdDate: dayjs('2025-12-25T05:36'),
};

export const sampleWithPartialData: IOcrCache = {
  id: 13011,
  documentSha256: 'ew whether',
  language: 'innocently',
  pageCount: 29881,
  totalConfidence: 0.6,
  s3ResultKey: 'lieu bah',
  s3Bucket: 'mammoth',
  extractedTextS3Key: 'ha gadzooks',
  metadata: '../fake-data/blob/hipster.txt',
  hits: 12714,
  createdDate: dayjs('2025-12-25T05:18'),
  expirationDate: dayjs('2025-12-24T21:20'),
};

export const sampleWithFullData: IOcrCache = {
  id: 27303,
  documentSha256: 'now yet hopelessly',
  ocrEngine: 'TIKA_TESSERACT',
  language: 'versus cre',
  pageCount: 288,
  totalConfidence: 0.52,
  s3ResultKey: 'arcade and',
  s3Bucket: 'hence',
  extractedTextS3Key: 'parsnip baa defiantly',
  metadata: '../fake-data/blob/hipster.txt',
  hits: 4488,
  lastAccessDate: dayjs('2025-12-25T09:20'),
  createdDate: dayjs('2025-12-24T12:47'),
  expirationDate: dayjs('2025-12-25T10:41'),
};

export const sampleWithNewData: NewOcrCache = {
  documentSha256: 'minus um',
  pageCount: 30618,
  s3ResultKey: 'why uh-huh',
  s3Bucket: 'pivot underneath rebuke',
  createdDate: dayjs('2025-12-24T23:41'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
