import dayjs from 'dayjs/esm';

import { ISimilarityDocumentFingerprint, NewSimilarityDocumentFingerprint } from './similarity-document-fingerprint.model';

export const sampleWithRequiredData: ISimilarityDocumentFingerprint = {
  id: 25137,
  fingerprint: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-29T12:59'),
};

export const sampleWithPartialData: ISimilarityDocumentFingerprint = {
  id: 25208,
  fingerprintType: 'HASH',
  fingerprint: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-29T14:36'),
};

export const sampleWithFullData: ISimilarityDocumentFingerprint = {
  id: 22800,
  fingerprintType: 'PERCEPTUAL',
  fingerprint: '../fake-data/blob/hipster.txt',
  vectorEmbedding: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-29T11:58'),
  lastUpdated: dayjs('2025-12-29T19:12'),
};

export const sampleWithNewData: NewSimilarityDocumentFingerprint = {
  fingerprint: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-30T02:10'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
