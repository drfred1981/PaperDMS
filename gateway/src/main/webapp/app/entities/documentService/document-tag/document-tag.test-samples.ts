import dayjs from 'dayjs/esm';

import { IDocumentTag, NewDocumentTag } from './document-tag.model';

export const sampleWithRequiredData: IDocumentTag = {
  id: 29592,
  assignedDate: dayjs('2025-12-24T23:21'),
  assignedBy: 'forswear instead',
  isAutoTagged: true,
};

export const sampleWithPartialData: IDocumentTag = {
  id: 7457,
  assignedDate: dayjs('2025-12-24T20:50'),
  assignedBy: 'which until',
  isAutoTagged: false,
  source: 'IMPORTED',
};

export const sampleWithFullData: IDocumentTag = {
  id: 16763,
  assignedDate: dayjs('2025-12-24T14:30'),
  assignedBy: 'dead whoever',
  confidence: 0.58,
  isAutoTagged: true,
  source: 'AI_SUGGESTED',
};

export const sampleWithNewData: NewDocumentTag = {
  assignedDate: dayjs('2025-12-24T18:17'),
  assignedBy: 'notwithstanding',
  isAutoTagged: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
