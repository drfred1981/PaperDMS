import dayjs from 'dayjs/esm';

import { IMergeJob, NewMergeJob } from './merge-job.model';

export const sampleWithRequiredData: IMergeJob = {
  id: 18385,
  name: 'furthermore physical',
  sourceDocumentIds: '../fake-data/blob/hipster.txt',
  mergeOrder: '../fake-data/blob/hipster.txt',
  status: 'CANCELLED',
  createdBy: 'whether absolve inasmuch',
  createdDate: dayjs('2025-12-20T02:16'),
};

export const sampleWithPartialData: IMergeJob = {
  id: 10016,
  name: 'coast beep',
  sourceDocumentIds: '../fake-data/blob/hipster.txt',
  mergeOrder: '../fake-data/blob/hipster.txt',
  includeBookmarks: false,
  addPageNumbers: true,
  outputS3Key: 'amidst',
  outputDocumentId: 6077,
  status: 'PENDING',
  startDate: dayjs('2025-12-20T03:35'),
  endDate: dayjs('2025-12-20T08:37'),
  createdBy: 'gosh terrorise now',
  createdDate: dayjs('2025-12-19T23:41'),
};

export const sampleWithFullData: IMergeJob = {
  id: 20002,
  name: 'wonderfully',
  sourceDocumentIds: '../fake-data/blob/hipster.txt',
  mergeOrder: '../fake-data/blob/hipster.txt',
  includeBookmarks: true,
  includeToc: true,
  addPageNumbers: true,
  outputS3Key: 'huzzah mobilise',
  outputDocumentId: 14639,
  status: 'PROCESSING',
  startDate: dayjs('2025-12-19T20:10'),
  endDate: dayjs('2025-12-19T19:19'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'very quip',
  createdDate: dayjs('2025-12-20T10:24'),
};

export const sampleWithNewData: NewMergeJob = {
  name: 'surprise ack fooey',
  sourceDocumentIds: '../fake-data/blob/hipster.txt',
  mergeOrder: '../fake-data/blob/hipster.txt',
  status: 'FAILED',
  createdBy: 'thoroughly',
  createdDate: dayjs('2025-12-20T13:30'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
