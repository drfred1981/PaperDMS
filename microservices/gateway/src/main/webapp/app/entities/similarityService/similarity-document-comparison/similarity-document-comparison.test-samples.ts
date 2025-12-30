import dayjs from 'dayjs/esm';

import { ISimilarityDocumentComparison, NewSimilarityDocumentComparison } from './similarity-document-comparison.model';

export const sampleWithRequiredData: ISimilarityDocumentComparison = {
  id: 5972,
  sourceDocumentSha256: 'like though ghost',
  targetDocumentSha256: 'tenderly vibraphone speedy',
  similarityScore: 0.11,
  computedDate: dayjs('2025-12-29T09:15'),
};

export const sampleWithPartialData: ISimilarityDocumentComparison = {
  id: 31500,
  sourceDocumentSha256: 'overtrain boastfully ah',
  targetDocumentSha256: 'almost',
  similarityScore: 0.9,
  computedDate: dayjs('2025-12-30T01:01'),
  isRelevant: true,
  reviewedBy: 'gust over',
  reviewedDate: dayjs('2025-12-29T13:36'),
};

export const sampleWithFullData: ISimilarityDocumentComparison = {
  id: 31589,
  sourceDocumentSha256: 'cleverly',
  targetDocumentSha256: 'daintily',
  similarityScore: 0,
  algorithm: 'DEEP_LEARNING',
  features: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-29T13:52'),
  isRelevant: true,
  reviewedBy: 'brr though',
  reviewedDate: dayjs('2025-12-29T16:57'),
};

export const sampleWithNewData: NewSimilarityDocumentComparison = {
  sourceDocumentSha256: 'hierarchy',
  targetDocumentSha256: 'more',
  similarityScore: 0.89,
  computedDate: dayjs('2025-12-29T12:36'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
