import dayjs from 'dayjs/esm';

import { ICorrespondentExtraction, NewCorrespondentExtraction } from './correspondent-extraction.model';

export const sampleWithRequiredData: ICorrespondentExtraction = {
  id: 22240,
  documentId: 9840,
  documentSha256: 'replacement hourly',
  extractedText: '../fake-data/blob/hipster.txt',
  extractedTextSha256: 'properly woot',
  isCached: true,
  createdDate: dayjs('2025-12-25T07:53'),
};

export const sampleWithPartialData: ICorrespondentExtraction = {
  id: 15256,
  documentId: 24575,
  documentSha256: 'er',
  extractedText: '../fake-data/blob/hipster.txt',
  extractedTextSha256: 'vice',
  status: 'FAILED',
  isCached: true,
  startDate: dayjs('2025-12-25T07:33'),
  sendersCount: 9444,
  recipientsCount: 32733,
  createdDate: dayjs('2025-12-24T13:02'),
};

export const sampleWithFullData: ICorrespondentExtraction = {
  id: 32337,
  documentId: 460,
  documentSha256: 'worthy verbally',
  extractedText: '../fake-data/blob/hipster.txt',
  extractedTextSha256: 'throughout humor commemorate',
  detectedLanguage: 'woot',
  languageConfidence: 0.79,
  status: 'COMPLETED',
  resultCacheKey: 'density',
  isCached: true,
  resultS3Key: 'dwell ah lazily',
  startDate: dayjs('2025-12-24T12:14'),
  endDate: dayjs('2025-12-24T18:45'),
  errorMessage: '../fake-data/blob/hipster.txt',
  sendersCount: 18238,
  recipientsCount: 29655,
  createdDate: dayjs('2025-12-24T20:36'),
};

export const sampleWithNewData: NewCorrespondentExtraction = {
  documentId: 30351,
  documentSha256: 'boo ack whoa',
  extractedText: '../fake-data/blob/hipster.txt',
  extractedTextSha256: 'dimly duh',
  isCached: false,
  createdDate: dayjs('2025-12-24T23:33'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
