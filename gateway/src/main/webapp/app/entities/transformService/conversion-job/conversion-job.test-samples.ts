import dayjs from 'dayjs/esm';

import { IConversionJob, NewConversionJob } from './conversion-job.model';

export const sampleWithRequiredData: IConversionJob = {
  id: 32555,
  documentId: 18397,
  documentSha256: 'feminize outlandish',
  sourceFormat: 'on character funny',
  targetFormat: 'vice boohoo forenenst',
  status: 'PENDING',
  createdBy: 'above energetic quit',
  createdDate: dayjs('2025-12-25T05:58'),
};

export const sampleWithPartialData: IConversionJob = {
  id: 3735,
  documentId: 7324,
  documentSha256: 'qua',
  sourceFormat: 'eek truthfully',
  targetFormat: 'beyond among beyond',
  options: '../fake-data/blob/hipster.txt',
  status: 'FAILED',
  createdBy: 'density',
  createdDate: dayjs('2025-12-25T02:31'),
};

export const sampleWithFullData: IConversionJob = {
  id: 17917,
  documentId: 9100,
  documentSha256: 'bobble',
  sourceFormat: 'rowdy midwife until',
  targetFormat: 'consequently uh-huh determined',
  conversionEngine: 'lest',
  options: '../fake-data/blob/hipster.txt',
  outputS3Key: 'spotless gadzooks depot',
  outputDocumentId: 7600,
  status: 'PROCESSING',
  startDate: dayjs('2025-12-24T23:04'),
  endDate: dayjs('2025-12-25T00:54'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'flawed',
  createdDate: dayjs('2025-12-24T17:13'),
};

export const sampleWithNewData: NewConversionJob = {
  documentId: 25868,
  documentSha256: 'slow',
  sourceFormat: 'huddle courageous because',
  targetFormat: 'admonish spirited',
  status: 'PROCESSING',
  createdBy: 'conclude',
  createdDate: dayjs('2025-12-25T01:27'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
