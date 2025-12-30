import dayjs from 'dayjs/esm';

import { IOcrComparison, NewOcrComparison } from './ocr-comparison.model';

export const sampleWithRequiredData: IOcrComparison = {
  id: 27483,
  documentSha256: 'despite',
  pageNumber: 2421,
  comparisonDate: dayjs('2025-12-29T21:50'),
};

export const sampleWithPartialData: IOcrComparison = {
  id: 23244,
  documentSha256: 'kiddingly pfft illustrious',
  pageNumber: 27646,
  tikaText: '../fake-data/blob/hipster.txt',
  similarity: 0.27,
  differences: '../fake-data/blob/hipster.txt',
  differencesS3Key: 'cheese',
  selectedBy: 'glisten',
  comparisonDate: dayjs('2025-12-29T13:12'),
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IOcrComparison = {
  id: 31030,
  documentSha256: 'commandeer',
  pageNumber: 16993,
  tikaText: '../fake-data/blob/hipster.txt',
  tikaConfidence: 0.6,
  aiText: '../fake-data/blob/hipster.txt',
  aiConfidence: 0.41,
  similarity: 0.16,
  differences: '../fake-data/blob/hipster.txt',
  differencesS3Key: 'following',
  selectedEngine: 'TIKA_TESSERACT',
  selectedBy: 'cautious arrogantly',
  selectedDate: dayjs('2025-12-29T15:08'),
  comparisonDate: dayjs('2025-12-29T12:51'),
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewOcrComparison = {
  documentSha256: 'depend um',
  pageNumber: 18417,
  comparisonDate: dayjs('2025-12-29T17:40'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
