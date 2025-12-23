import dayjs from 'dayjs/esm';

import { ISearchIndex, NewSearchIndex } from './search-index.model';

export const sampleWithRequiredData: ISearchIndex = {
  id: 13798,
  documentId: 399,
  indexedContent: '../fake-data/blob/hipster.txt',
  indexedDate: dayjs('2025-12-19T17:22'),
};

export const sampleWithPartialData: ISearchIndex = {
  id: 10755,
  documentId: 3594,
  indexedContent: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  extractedEntities: '../fake-data/blob/hipster.txt',
  indexedDate: dayjs('2025-12-19T16:04'),
};

export const sampleWithFullData: ISearchIndex = {
  id: 15482,
  documentId: 8752,
  indexedContent: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  tags: 'who overreact',
  correspondents: 'hm negligible scrutinise',
  extractedEntities: '../fake-data/blob/hipster.txt',
  indexedDate: dayjs('2025-12-20T04:56'),
  lastUpdated: dayjs('2025-12-19T21:38'),
};

export const sampleWithNewData: NewSearchIndex = {
  documentId: 23109,
  indexedContent: '../fake-data/blob/hipster.txt',
  indexedDate: dayjs('2025-12-19T19:44'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
