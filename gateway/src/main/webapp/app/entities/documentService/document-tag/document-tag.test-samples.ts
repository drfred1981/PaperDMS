import dayjs from 'dayjs/esm';

import { IDocumentTag, NewDocumentTag } from './document-tag.model';

export const sampleWithRequiredData: IDocumentTag = {
  id: 29592,
  assignedDate: dayjs('2025-12-20T04:22'),
  assignedBy: 'forswear instead',
  isAutoTagged: true,
};

export const sampleWithPartialData: IDocumentTag = {
  id: 7457,
  assignedDate: dayjs('2025-12-20T01:51'),
  assignedBy: 'which until',
  isAutoTagged: false,
  source: 'IMPORTED',
};

export const sampleWithFullData: IDocumentTag = {
  id: 16763,
  assignedDate: dayjs('2025-12-19T19:31'),
  assignedBy: 'dead whoever',
  confidence: 0.58,
  isAutoTagged: true,
  source: 'AI_SUGGESTED',
};

export const sampleWithNewData: NewDocumentTag = {
  assignedDate: dayjs('2025-12-19T23:18'),
  assignedBy: 'notwithstanding',
  isAutoTagged: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
