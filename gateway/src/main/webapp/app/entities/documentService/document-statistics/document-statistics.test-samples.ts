import dayjs from 'dayjs/esm';

import { IDocumentStatistics, NewDocumentStatistics } from './document-statistics.model';

export const sampleWithRequiredData: IDocumentStatistics = {
  id: 14163,
  documentId: 28356,
  lastUpdated: dayjs('2025-12-24T23:39'),
};

export const sampleWithPartialData: IDocumentStatistics = {
  id: 24158,
  documentId: 14778,
  lastUpdated: dayjs('2025-12-25T02:54'),
};

export const sampleWithFullData: IDocumentStatistics = {
  id: 24674,
  documentId: 12139,
  viewsTotal: 4165,
  downloadsTotal: 17584,
  uniqueViewers: 27859,
  lastUpdated: dayjs('2025-12-24T18:16'),
};

export const sampleWithNewData: NewDocumentStatistics = {
  documentId: 12801,
  lastUpdated: dayjs('2025-12-24T14:19'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
