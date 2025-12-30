import dayjs from 'dayjs/esm';

import { IMetaSavedSearch, NewMetaSavedSearch } from './meta-saved-search.model';

export const sampleWithRequiredData: IMetaSavedSearch = {
  id: 16322,
  name: 'busily kookily search',
  query: '../fake-data/blob/hipster.txt',
  isPublic: false,
  isAlert: true,
  userId: 'plumber',
  createdDate: dayjs('2025-12-30T04:44'),
};

export const sampleWithPartialData: IMetaSavedSearch = {
  id: 30008,
  name: 'fast',
  query: '../fake-data/blob/hipster.txt',
  isPublic: true,
  isAlert: true,
  alertFrequency: 'NEVER',
  userId: 'elver singing because',
  createdDate: dayjs('2025-12-29T17:34'),
};

export const sampleWithFullData: IMetaSavedSearch = {
  id: 25693,
  name: 'before alarmed mechanically',
  query: '../fake-data/blob/hipster.txt',
  isPublic: true,
  isAlert: false,
  alertFrequency: 'MONTHLY',
  userId: 'aw lest',
  createdDate: dayjs('2025-12-29T15:49'),
};

export const sampleWithNewData: NewMetaSavedSearch = {
  name: 'roughly an',
  query: '../fake-data/blob/hipster.txt',
  isPublic: false,
  isAlert: true,
  userId: 'but yet',
  createdDate: dayjs('2025-12-29T08:35'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
