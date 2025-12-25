import dayjs from 'dayjs/esm';

import { IOcrResult, NewOcrResult } from './ocr-result.model';

export const sampleWithRequiredData: IOcrResult = {
  id: 8149,
  pageNumber: 1363,
  s3Bucket: 'till',
  processedDate: dayjs('2025-12-24T23:24'),
};

export const sampleWithPartialData: IOcrResult = {
  id: 6119,
  pageNumber: 4794,
  confidence: 0.65,
  s3ResultKey: 'label yawningly mortally',
  s3Bucket: 'conceptualize piglet quietly',
  s3BoundingBoxKey: 'cope international',
  metadata: '../fake-data/blob/hipster.txt',
  rawResponseS3Key: 'confused',
  processedDate: dayjs('2025-12-25T04:58'),
};

export const sampleWithFullData: IOcrResult = {
  id: 6849,
  pageNumber: 24162,
  pageSha256: 'around than',
  confidence: 0.8,
  s3ResultKey: 'famously yippee',
  s3Bucket: 'off',
  s3BoundingBoxKey: 'innovation judicious yippee',
  boundingBoxes: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  language: 'low amongs',
  wordCount: 2800,
  ocrEngine: 'ANTHROPIC_CLAUDE',
  processingTime: 13606,
  rawResponse: '../fake-data/blob/hipster.txt',
  rawResponseS3Key: 'ah off among',
  processedDate: dayjs('2025-12-24T13:13'),
};

export const sampleWithNewData: NewOcrResult = {
  pageNumber: 4233,
  s3Bucket: 'developmental mature instead',
  processedDate: dayjs('2025-12-24T18:33'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
