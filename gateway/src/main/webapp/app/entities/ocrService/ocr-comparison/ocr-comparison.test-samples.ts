import dayjs from 'dayjs/esm';

import { IOcrComparison, NewOcrComparison } from './ocr-comparison.model';

export const sampleWithRequiredData: IOcrComparison = {
  id: 27483,
  documentId: 9429,
  documentSha256: 'briefly per',
  pageNumber: 11129,
  comparisonDate: dayjs('2025-12-19T22:51'),
};

export const sampleWithPartialData: IOcrComparison = {
  id: 23244,
  documentId: 26994,
  documentSha256: 'rigidly utterly',
  pageNumber: 17028,
  tikaText: '../fake-data/blob/hipster.txt',
  similarity: 0.46,
  differences: '../fake-data/blob/hipster.txt',
  differencesS3Key: 'besides gracefully glisten',
  selectedBy: 'solemnly',
  comparisonDate: dayjs('2025-12-20T08:08'),
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IOcrComparison = {
  id: 31030,
  documentId: 3629,
  documentSha256: 'misfire yum furthermore',
  pageNumber: 8724,
  tikaText: '../fake-data/blob/hipster.txt',
  tikaConfidence: 0.43,
  aiText: '../fake-data/blob/hipster.txt',
  aiConfidence: 0.78,
  similarity: 0.39,
  differences: '../fake-data/blob/hipster.txt',
  differencesS3Key: 'anesthetize geez',
  selectedEngine: 'CUSTOM',
  selectedBy: 'fold',
  selectedDate: dayjs('2025-12-20T01:30'),
  comparisonDate: dayjs('2025-12-20T01:45'),
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewOcrComparison = {
  documentId: 12097,
  documentSha256: 'up',
  pageNumber: 29783,
  comparisonDate: dayjs('2025-12-19T23:08'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
