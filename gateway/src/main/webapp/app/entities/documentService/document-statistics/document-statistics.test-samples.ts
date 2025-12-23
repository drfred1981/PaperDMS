import dayjs from 'dayjs/esm';

import { IDocumentStatistics, NewDocumentStatistics } from './document-statistics.model';

export const sampleWithRequiredData: IDocumentStatistics = {
  id: 14163,
  documentId: 28356,
  lastUpdated: dayjs('2025-12-20T04:39'),
};

export const sampleWithPartialData: IDocumentStatistics = {
  id: 24158,
  documentId: 14778,
  lastUpdated: dayjs('2025-12-20T07:55'),
};

export const sampleWithFullData: IDocumentStatistics = {
  id: 24674,
  documentId: 12139,
  viewsTotal: 4165,
  downloadsTotal: 17584,
  uniqueViewers: 27859,
  lastUpdated: dayjs('2025-12-19T23:17'),
};

export const sampleWithNewData: NewDocumentStatistics = {
  documentId: 12801,
  lastUpdated: dayjs('2025-12-19T19:20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
