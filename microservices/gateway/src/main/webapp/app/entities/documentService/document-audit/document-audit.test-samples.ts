import dayjs from 'dayjs/esm';

import { IDocumentAudit, NewDocumentAudit } from './document-audit.model';

export const sampleWithRequiredData: IDocumentAudit = {
  id: 10299,
  documentSha256: 'gape',
  action: 'WORKFLOW_COMPLETED',
  userId: 'where faithfully import',
  actionDate: dayjs('2025-12-29T14:48'),
};

export const sampleWithPartialData: IDocumentAudit = {
  id: 23236,
  documentSha256: 'lazy which',
  action: 'ARCHIVED',
  userId: 'ugh unnecessarily ack',
  userIp: 'programme when',
  actionDate: dayjs('2025-12-29T19:07'),
};

export const sampleWithFullData: IDocumentAudit = {
  id: 17238,
  documentSha256: 'fooey meanwhile pish',
  action: 'WORKFLOW_STARTED',
  userId: 'however peony',
  userIp: 'rewrite how',
  actionDate: dayjs('2025-12-29T11:22'),
  additionalInfo: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewDocumentAudit = {
  documentSha256: 'without decisive impolite',
  action: 'MODIFIED',
  userId: 'minor blindly tenant',
  actionDate: dayjs('2025-12-29T22:44'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
