import dayjs from 'dayjs/esm';

import { IDocumentRelation, NewDocumentRelation } from './document-relation.model';

export const sampleWithRequiredData: IDocumentRelation = {
  id: 25311,
  sourceDocumentId: 32107,
  targetDocumentId: 8318,
  relationType: 'HAS_VERSION',
  createdBy: 'poor versus',
  createdDate: dayjs('2025-12-25T08:02'),
};

export const sampleWithPartialData: IDocumentRelation = {
  id: 21887,
  sourceDocumentId: 8150,
  targetDocumentId: 27970,
  relationType: 'ATTACHMENT_OF',
  createdBy: 'bah',
  createdDate: dayjs('2025-12-24T20:45'),
};

export const sampleWithFullData: IDocumentRelation = {
  id: 30994,
  sourceDocumentId: 28928,
  targetDocumentId: 10592,
  relationType: 'SUPPLEMENTED_BY',
  createdBy: 'ew',
  createdDate: dayjs('2025-12-25T08:07'),
};

export const sampleWithNewData: NewDocumentRelation = {
  sourceDocumentId: 21766,
  targetDocumentId: 3594,
  relationType: 'REPLACES',
  createdBy: 'lest whereas',
  createdDate: dayjs('2025-12-24T11:00'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
