import dayjs from 'dayjs/esm';

import { ISimilarityJob, NewSimilarityJob } from './similarity-job.model';

export const sampleWithRequiredData: ISimilarityJob = {
  id: 12676,
  documentId: 12348,
  documentSha256: 'waver quietly',
  createdDate: dayjs('2025-12-20T09:56'),
  createdBy: 'blissfully',
};

export const sampleWithPartialData: ISimilarityJob = {
  id: 30046,
  documentId: 17939,
  documentSha256: 'worst unabashedly',
  status: 'FAILED',
  minSimilarityThreshold: 0.76,
  matchesFound: 11680,
  createdDate: dayjs('2025-12-20T02:08'),
  createdBy: 'boohoo',
};

export const sampleWithFullData: ISimilarityJob = {
  id: 28812,
  documentId: 20181,
  documentSha256: 'unfortunate tributary schlep',
  status: 'IN_PROGRESS',
  algorithm: 'COSINE',
  scope: 'ALL_DOCUMENTS',
  minSimilarityThreshold: 0.19,
  matchesFound: 14698,
  startDate: dayjs('2025-12-19T18:13'),
  endDate: dayjs('2025-12-20T03:15'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2025-12-19T16:46'),
  createdBy: 'majestically until',
};

export const sampleWithNewData: NewSimilarityJob = {
  documentId: 9987,
  documentSha256: 'meanwhile colorful',
  createdDate: dayjs('2025-12-19T21:43'),
  createdBy: 'staid dissemble',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
