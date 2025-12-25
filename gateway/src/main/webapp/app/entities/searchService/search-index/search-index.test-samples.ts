import dayjs from 'dayjs/esm';

import { ISearchIndex, NewSearchIndex } from './search-index.model';

export const sampleWithRequiredData: ISearchIndex = {
  id: 13798,
  documentId: 399,
  indexedContent: '../fake-data/blob/hipster.txt',
  indexedDate: dayjs('2025-12-24T12:21'),
};

export const sampleWithPartialData: ISearchIndex = {
  id: 10755,
  documentId: 3594,
  indexedContent: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  extractedEntities: '../fake-data/blob/hipster.txt',
  indexedDate: dayjs('2025-12-24T11:03'),
};

export const sampleWithFullData: ISearchIndex = {
  id: 15482,
  documentId: 8752,
  indexedContent: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  tags: 'who overreact',
  correspondents: 'hm negligible scrutinise',
  extractedEntities: '../fake-data/blob/hipster.txt',
  indexedDate: dayjs('2025-12-24T23:55'),
  lastUpdated: dayjs('2025-12-24T16:37'),
};

export const sampleWithNewData: NewSearchIndex = {
  documentId: 23109,
  indexedContent: '../fake-data/blob/hipster.txt',
  indexedDate: dayjs('2025-12-24T14:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
