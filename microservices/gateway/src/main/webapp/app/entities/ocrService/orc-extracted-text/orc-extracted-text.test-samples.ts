import dayjs from 'dayjs/esm';

import { IOrcExtractedText, NewOrcExtractedText } from './orc-extracted-text.model';

export const sampleWithRequiredData: IOrcExtractedText = {
  id: 25304,
  content: '../fake-data/blob/hipster.txt',
  s3Bucket: 'whether',
  pageNumber: 16903,
  extractedDate: dayjs('2025-12-30T01:25'),
};

export const sampleWithPartialData: IOrcExtractedText = {
  id: 25615,
  content: '../fake-data/blob/hipster.txt',
  s3Bucket: 'ugh candid salty',
  pageNumber: 22936,
  wordCount: 2712,
  hasStructuredData: false,
  structuredData: '../fake-data/blob/hipster.txt',
  structuredDataS3Key: 'pop slimy',
  extractedDate: dayjs('2025-12-29T19:38'),
};

export const sampleWithFullData: IOrcExtractedText = {
  id: 28110,
  content: '../fake-data/blob/hipster.txt',
  contentSha256: 'display utterly depart',
  s3ContentKey: 'lest cinch',
  s3Bucket: 'eek',
  pageNumber: 8675,
  language: 'till thank',
  wordCount: 14801,
  hasStructuredData: false,
  structuredData: '../fake-data/blob/hipster.txt',
  structuredDataS3Key: 'gadzooks supposing gah',
  extractedDate: dayjs('2025-12-29T07:34'),
};

export const sampleWithNewData: NewOrcExtractedText = {
  content: '../fake-data/blob/hipster.txt',
  s3Bucket: 'that',
  pageNumber: 16709,
  extractedDate: dayjs('2025-12-30T06:22'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
