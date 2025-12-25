import dayjs from 'dayjs/esm';

import { IExtractedText, NewExtractedText } from './extracted-text.model';

export const sampleWithRequiredData: IExtractedText = {
  id: 26984,
  content: '../fake-data/blob/hipster.txt',
  s3Bucket: 'boo',
  pageNumber: 25784,
  extractedDate: dayjs('2025-12-25T00:32'),
};

export const sampleWithPartialData: IExtractedText = {
  id: 14682,
  content: '../fake-data/blob/hipster.txt',
  s3ContentKey: 'replacement phew schnitzel',
  s3Bucket: 'hose in',
  pageNumber: 23121,
  language: 'whereas yu',
  structuredData: '../fake-data/blob/hipster.txt',
  structuredDataS3Key: 'cease flimsy rear',
  extractedDate: dayjs('2025-12-24T23:22'),
};

export const sampleWithFullData: IExtractedText = {
  id: 23246,
  content: '../fake-data/blob/hipster.txt',
  contentSha256: 'scotch honorable maroon',
  s3ContentKey: 'inasmuch meh',
  s3Bucket: 'among coaxingly',
  pageNumber: 19212,
  language: 'ingratiate',
  wordCount: 30900,
  hasStructuredData: false,
  structuredData: '../fake-data/blob/hipster.txt',
  structuredDataS3Key: 'whose amid although',
  extractedDate: dayjs('2025-12-25T05:50'),
};

export const sampleWithNewData: NewExtractedText = {
  content: '../fake-data/blob/hipster.txt',
  s3Bucket: 'behest aw factorise',
  pageNumber: 23300,
  extractedDate: dayjs('2025-12-24T21:03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
