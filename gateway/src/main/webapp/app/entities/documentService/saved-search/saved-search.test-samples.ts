import dayjs from 'dayjs/esm';

import { ISavedSearch, NewSavedSearch } from './saved-search.model';

export const sampleWithRequiredData: ISavedSearch = {
  id: 28585,
  name: 'astride',
  query: '../fake-data/blob/hipster.txt',
  isPublic: false,
  isAlert: false,
  userId: 'drug vein obnoxiously',
  createdDate: dayjs('2025-12-20T01:12'),
};

export const sampleWithPartialData: ISavedSearch = {
  id: 9148,
  name: 'across regarding',
  query: '../fake-data/blob/hipster.txt',
  isPublic: false,
  isAlert: false,
  alertFrequency: 'IMMEDIATELY',
  userId: 'yak frail',
  createdDate: dayjs('2025-12-20T12:16'),
};

export const sampleWithFullData: ISavedSearch = {
  id: 4104,
  name: 'coagulate gah',
  query: '../fake-data/blob/hipster.txt',
  isPublic: false,
  isAlert: false,
  alertFrequency: 'MONTHLY',
  userId: 'fiercely louse',
  createdDate: dayjs('2025-12-19T22:31'),
};

export const sampleWithNewData: NewSavedSearch = {
  name: 'sideboard',
  query: '../fake-data/blob/hipster.txt',
  isPublic: false,
  isAlert: false,
  userId: 'elastic',
  createdDate: dayjs('2025-12-20T04:20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
