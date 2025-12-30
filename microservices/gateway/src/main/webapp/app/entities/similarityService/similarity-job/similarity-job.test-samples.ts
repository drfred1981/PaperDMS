import dayjs from 'dayjs/esm';

import { ISimilarityJob, NewSimilarityJob } from './similarity-job.model';

export const sampleWithRequiredData: ISimilarityJob = {
  id: 12676,
  documentSha256: 'hole actual',
  createdDate: dayjs('2025-12-29T22:29'),
  createdBy: 'gah unnaturally old',
};

export const sampleWithPartialData: ISimilarityJob = {
  id: 30046,
  documentSha256: 'sour courageously',
  status: 'CANCELLED',
  minSimilarityThreshold: 0.63,
  matchesFound: 24915,
  createdDate: dayjs('2025-12-29T15:52'),
  createdBy: 'planula superficial',
};

export const sampleWithFullData: ISimilarityJob = {
  id: 28812,
  documentSha256: 'while hmph',
  status: 'CANCELLED',
  algorithm: 'COSINE',
  scope: 'CUSTOM',
  minSimilarityThreshold: 0.41,
  matchesFound: 28494,
  startDate: dayjs('2025-12-29T22:51'),
  endDate: dayjs('2025-12-30T02:12'),
  errorMessage: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2025-12-30T01:42'),
  createdBy: 'oh fond',
};

export const sampleWithNewData: NewSimilarityJob = {
  documentSha256: 'pace',
  createdDate: dayjs('2025-12-29T17:42'),
  createdBy: 'deploy under',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
