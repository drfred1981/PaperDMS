import dayjs from 'dayjs/esm';

import { IMetaBookmark, NewMetaBookmark } from './meta-bookmark.model';

export const sampleWithRequiredData: IMetaBookmark = {
  id: 11217,
  userId: 'as',
  entityType: 'DOCUMENT',
  entityName: 'unless',
  createdDate: dayjs('2025-12-29T10:03'),
};

export const sampleWithPartialData: IMetaBookmark = {
  id: 25071,
  userId: 'meanwhile until',
  entityType: 'WORKFLOW',
  entityName: 'where cornet',
  createdDate: dayjs('2025-12-30T01:46'),
};

export const sampleWithFullData: IMetaBookmark = {
  id: 10533,
  userId: 'stiff wheel bravely',
  entityType: 'DASHBOARD',
  entityName: 'per triumphantly',
  createdDate: dayjs('2025-12-29T07:21'),
};

export const sampleWithNewData: NewMetaBookmark = {
  userId: 'likewise er boohoo',
  entityType: 'DOCUMENT',
  entityName: 'advertisement although',
  createdDate: dayjs('2025-12-30T03:57'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
