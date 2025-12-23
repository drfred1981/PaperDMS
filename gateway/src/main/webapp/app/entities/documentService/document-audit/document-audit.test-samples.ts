import dayjs from 'dayjs/esm';

import { IDocumentAudit, NewDocumentAudit } from './document-audit.model';

export const sampleWithRequiredData: IDocumentAudit = {
  id: 10299,
  documentId: 4115,
  documentSha256: 'own',
  action: 'RELATION_REMOVED',
  userId: 'next',
  actionDate: dayjs('2025-12-20T05:19'),
};

export const sampleWithPartialData: IDocumentAudit = {
  id: 23236,
  documentId: 14176,
  documentSha256: 'mainstream gosh nor',
  action: 'WORKFLOW_STARTED',
  userId: 'pish',
  userIp: 'beneath unlike psst',
  actionDate: dayjs('2025-12-20T02:45'),
};

export const sampleWithFullData: IDocumentAudit = {
  id: 17238,
  documentId: 32671,
  documentSha256: 'disappointment',
  action: 'CREATED',
  userId: 'whereas rosin interestingly',
  userIp: 'but palatable',
  actionDate: dayjs('2025-12-20T04:10'),
  additionalInfo: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewDocumentAudit = {
  documentId: 22660,
  documentSha256: 'however',
  action: 'DELETED',
  userId: 'pfft',
  actionDate: dayjs('2025-12-19T22:03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
