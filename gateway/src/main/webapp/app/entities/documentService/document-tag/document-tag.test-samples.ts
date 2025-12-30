import dayjs from 'dayjs/esm';

import { IDocumentTag, NewDocumentTag } from './document-tag.model';

export const sampleWithRequiredData: IDocumentTag = {
  id: 29592,
  assignedDate: dayjs('2025-12-29T19:27'),
  assignedBy: 'forswear instead',
  isAutoMetaTagged: true,
};

export const sampleWithPartialData: IDocumentTag = {
  id: 7457,
  assignedDate: dayjs('2025-12-29T16:56'),
  assignedBy: 'which until',
  isAutoMetaTagged: false,
  source: 'IMPORTED',
};

export const sampleWithFullData: IDocumentTag = {
  id: 16763,
  assignedDate: dayjs('2025-12-29T10:36'),
  assignedBy: 'dead whoever',
  confidence: 0.58,
  isAutoMetaTagged: true,
  source: 'AI_SUGGESTED',
};

export const sampleWithNewData: NewDocumentTag = {
  assignedDate: dayjs('2025-12-29T14:23'),
  assignedBy: 'notwithstanding',
  isAutoMetaTagged: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
