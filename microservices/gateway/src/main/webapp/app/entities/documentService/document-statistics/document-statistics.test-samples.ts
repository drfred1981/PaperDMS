import dayjs from 'dayjs/esm';

import { IDocumentStatistics, NewDocumentStatistics } from './document-statistics.model';

export const sampleWithRequiredData: IDocumentStatistics = {
  id: 14163,
  lastUpdated: dayjs('2025-12-30T03:31'),
};

export const sampleWithPartialData: IDocumentStatistics = {
  id: 24158,
  lastUpdated: dayjs('2025-12-29T17:34'),
};

export const sampleWithFullData: IDocumentStatistics = {
  id: 24674,
  viewsTotal: 12139,
  downloadsTotal: 4165,
  uniqueViewers: 17584,
  lastUpdated: dayjs('2025-12-30T03:09'),
};

export const sampleWithNewData: NewDocumentStatistics = {
  lastUpdated: dayjs('2025-12-29T16:07'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
