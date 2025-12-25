import dayjs from 'dayjs/esm';

import { IBookmark, NewBookmark } from './bookmark.model';

export const sampleWithRequiredData: IBookmark = {
  id: 29972,
  userId: 'failing',
  entityType: 'FOLDER',
  entityId: 23354,
  createdDate: dayjs('2025-12-25T05:28'),
};

export const sampleWithPartialData: IBookmark = {
  id: 10266,
  userId: 'ew outside',
  entityType: 'WORKFLOW',
  entityId: 19275,
  createdDate: dayjs('2025-12-24T17:48'),
};

export const sampleWithFullData: IBookmark = {
  id: 13947,
  userId: 'antique',
  entityType: 'DOCUMENT',
  entityId: 25438,
  createdDate: dayjs('2025-12-24T14:36'),
};

export const sampleWithNewData: NewBookmark = {
  userId: 'bah sans excellent',
  entityType: 'SEARCH',
  entityId: 28954,
  createdDate: dayjs('2025-12-25T00:51'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
