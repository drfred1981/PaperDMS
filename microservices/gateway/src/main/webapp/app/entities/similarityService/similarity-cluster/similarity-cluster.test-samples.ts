import dayjs from 'dayjs/esm';

import { ISimilarityCluster, NewSimilarityCluster } from './similarity-cluster.model';

export const sampleWithRequiredData: ISimilarityCluster = {
  id: 30282,
  createdDate: dayjs('2025-12-29T21:04'),
};

export const sampleWithPartialData: ISimilarityCluster = {
  id: 13050,
  name: 'boo',
  algorithm: 'COMBINED',
  avgSimilarity: 0.42,
  createdDate: dayjs('2025-12-29T07:24'),
};

export const sampleWithFullData: ISimilarityCluster = {
  id: 23692,
  name: 'about fatally hover',
  description: '../fake-data/blob/hipster.txt',
  algorithm: 'LSH',
  centroid: '../fake-data/blob/hipster.txt',
  documentCount: 17112,
  avgSimilarity: 0.01,
  createdDate: dayjs('2025-12-29T13:28'),
  lastUpdated: dayjs('2025-12-30T07:20'),
};

export const sampleWithNewData: NewSimilarityCluster = {
  createdDate: dayjs('2025-12-29T14:41'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
