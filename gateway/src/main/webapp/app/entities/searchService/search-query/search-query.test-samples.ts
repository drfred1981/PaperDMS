import dayjs from 'dayjs/esm';

import { ISearchQuery, NewSearchQuery } from './search-query.model';

export const sampleWithRequiredData: ISearchQuery = {
  id: 7284,
  query: 'when kettledrum attest',
  searchDate: dayjs('2025-12-24T17:20'),
};

export const sampleWithPartialData: ISearchQuery = {
  id: 25967,
  query: 'hmph',
  filters: '../fake-data/blob/hipster.txt',
  executionTime: 26633,
  searchDate: dayjs('2025-12-24T11:42'),
  isRelevant: true,
};

export const sampleWithFullData: ISearchQuery = {
  id: 9902,
  query: 'anxiously noxious',
  filters: '../fake-data/blob/hipster.txt',
  resultCount: 29920,
  executionTime: 31733,
  userId: 'atop',
  searchDate: dayjs('2025-12-24T20:34'),
  isRelevant: true,
};

export const sampleWithNewData: NewSearchQuery = {
  query: 'lobster aboard athwart',
  searchDate: dayjs('2025-12-24T13:20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
