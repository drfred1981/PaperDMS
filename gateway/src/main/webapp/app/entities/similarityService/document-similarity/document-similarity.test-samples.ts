import dayjs from 'dayjs/esm';

import { IDocumentSimilarity, NewDocumentSimilarity } from './document-similarity.model';

export const sampleWithRequiredData: IDocumentSimilarity = {
  id: 17206,
  documentId1: 11684,
  documentId2: 7502,
  similarityScore: 0.39,
  computedDate: dayjs('2025-12-25T06:28'),
};

export const sampleWithPartialData: IDocumentSimilarity = {
  id: 29335,
  documentId1: 19472,
  documentId2: 23207,
  similarityScore: 0.64,
  computedDate: dayjs('2025-12-24T17:36'),
  reviewedBy: 'woot',
  reviewedDate: dayjs('2025-12-24T15:53'),
};

export const sampleWithFullData: IDocumentSimilarity = {
  id: 15463,
  documentId1: 27394,
  documentId2: 20526,
  similarityScore: 0.54,
  algorithm: 'JACCARD',
  features: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-24T21:12'),
  isRelevant: true,
  reviewedBy: 'stark',
  reviewedDate: dayjs('2025-12-25T06:44'),
};

export const sampleWithNewData: NewDocumentSimilarity = {
  documentId1: 26229,
  documentId2: 9110,
  similarityScore: 0.28,
  computedDate: dayjs('2025-12-24T12:16'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
