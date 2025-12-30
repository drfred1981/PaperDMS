import dayjs from 'dayjs/esm';

import { ISearchIndex, NewSearchIndex } from './search-index.model';

export const sampleWithRequiredData: ISearchIndex = {
  id: 13798,
  documentSha256: 'once',
  indexedContent: '../fake-data/blob/hipster.txt',
  indexedDate: dayjs('2025-12-29T16:27'),
};

export const sampleWithPartialData: ISearchIndex = {
  id: 10755,
  documentSha256: 'lest',
  indexedContent: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  extractedEntities: '../fake-data/blob/hipster.txt',
  indexedDate: dayjs('2025-12-29T18:39'),
};

export const sampleWithFullData: ISearchIndex = {
  id: 15482,
  documentSha256: 'inveigle',
  indexedContent: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  tags: 'overreact aha mispronounce',
  correspondents: 'scrutinise deflate',
  extractedEntities: '../fake-data/blob/hipster.txt',
  indexedDate: dayjs('2025-12-29T22:47'),
  lastUpdated: dayjs('2025-12-29T22:10'),
};

export const sampleWithNewData: NewSearchIndex = {
  documentSha256: 'secrecy godfather whose',
  indexedContent: '../fake-data/blob/hipster.txt',
  indexedDate: dayjs('2025-12-29T22:30'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
