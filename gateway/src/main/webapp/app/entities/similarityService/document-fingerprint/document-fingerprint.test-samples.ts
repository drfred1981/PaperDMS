import dayjs from 'dayjs/esm';

import { IDocumentFingerprint, NewDocumentFingerprint } from './document-fingerprint.model';

export const sampleWithRequiredData: IDocumentFingerprint = {
  id: 10584,
  documentId: 8360,
  fingerprint: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-20T04:10'),
};

export const sampleWithPartialData: IDocumentFingerprint = {
  id: 27042,
  documentId: 25474,
  fingerprint: '../fake-data/blob/hipster.txt',
  vectorEmbedding: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-19T19:36'),
  lastUpdated: dayjs('2025-12-20T15:06'),
};

export const sampleWithFullData: IDocumentFingerprint = {
  id: 10830,
  documentId: 32731,
  fingerprintType: 'HASH',
  fingerprint: '../fake-data/blob/hipster.txt',
  vectorEmbedding: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-20T03:16'),
  lastUpdated: dayjs('2025-12-19T20:33'),
};

export const sampleWithNewData: NewDocumentFingerprint = {
  documentId: 16819,
  fingerprint: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-20T07:39'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
