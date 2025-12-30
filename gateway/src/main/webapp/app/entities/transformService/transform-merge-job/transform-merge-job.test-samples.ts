import dayjs from 'dayjs/esm';

import { ITransformMergeJob, NewTransformMergeJob } from './transform-merge-job.model';

export const sampleWithRequiredData: ITransformMergeJob = {
  id: 32612,
  name: 'questioningly',
  sourceDocumentSha256: '../fake-data/blob/hipster.txt',
  mergeOrder: '../fake-data/blob/hipster.txt',
  status: 'PENDING',
  createdBy: 'greatly',
  createdDate: dayjs('2025-12-30T01:24'),
};

export const sampleWithPartialData: ITransformMergeJob = {
  id: 4645,
  name: 'good partially',
  sourceDocumentSha256: '../fake-data/blob/hipster.txt',
  mergeOrder: '../fake-data/blob/hipster.txt',
  includeBookmarks: true,
  outputDocumentSha256: 'why incidentally',
  status: 'FAILED',
  endDate: dayjs('2025-12-29T12:45'),
  createdBy: 'publicity ouch',
  createdDate: dayjs('2025-12-30T06:13'),
};

export const sampleWithFullData: ITransformMergeJob = {
  id: 25399,
  name: 'slipper expostulate deduction',
  sourceDocumentSha256: '../fake-data/blob/hipster.txt',
  mergeOrder: '../fake-data/blob/hipster.txt',
  includeBookmarks: false,
  includeToc: false,
  addPageNumbers: true,
  outputS3Key: 'sick mockingly commonly',
  outputDocumentSha256: 'impolite so',
  status: 'CANCELLED',
  startDate: dayjs('2025-12-30T02:09'),
  endDate: dayjs('2025-12-29T17:12'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdBy: 'whereas',
  createdDate: dayjs('2025-12-29T19:23'),
};

export const sampleWithNewData: NewTransformMergeJob = {
  name: 'at sweet throbbing',
  sourceDocumentSha256: '../fake-data/blob/hipster.txt',
  mergeOrder: '../fake-data/blob/hipster.txt',
  status: 'COMPLETED',
  createdBy: 'yuck thick',
  createdDate: dayjs('2025-12-29T09:03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
