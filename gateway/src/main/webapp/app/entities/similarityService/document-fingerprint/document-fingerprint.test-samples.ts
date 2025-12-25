import dayjs from 'dayjs/esm';

import { IDocumentFingerprint, NewDocumentFingerprint } from './document-fingerprint.model';

export const sampleWithRequiredData: IDocumentFingerprint = {
  id: 10584,
  documentId: 8360,
  fingerprint: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-24T23:09'),
};

export const sampleWithPartialData: IDocumentFingerprint = {
  id: 27042,
  documentId: 25474,
  fingerprint: '../fake-data/blob/hipster.txt',
  vectorEmbedding: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-24T14:35'),
  lastUpdated: dayjs('2025-12-25T10:05'),
};

export const sampleWithFullData: IDocumentFingerprint = {
  id: 10830,
  documentId: 32731,
  fingerprintType: 'HASH',
  fingerprint: '../fake-data/blob/hipster.txt',
  vectorEmbedding: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-24T22:15'),
  lastUpdated: dayjs('2025-12-24T15:32'),
};

export const sampleWithNewData: NewDocumentFingerprint = {
  documentId: 16819,
  fingerprint: '../fake-data/blob/hipster.txt',
  computedDate: dayjs('2025-12-25T02:38'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
