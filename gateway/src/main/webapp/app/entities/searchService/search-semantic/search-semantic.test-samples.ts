import dayjs from 'dayjs/esm';

import { ISearchSemantic, NewSearchSemantic } from './search-semantic.model';

export const sampleWithRequiredData: ISearchSemantic = {
  id: 5415,
  query: 'huzzah cumbersome scale',
  queryEmbedding: '../fake-data/blob/hipster.txt',
  results: '../fake-data/blob/hipster.txt',
  searchDate: dayjs('2025-12-29T23:54'),
};

export const sampleWithPartialData: ISearchSemantic = {
  id: 17989,
  query: 'than',
  queryEmbedding: '../fake-data/blob/hipster.txt',
  results: '../fake-data/blob/hipster.txt',
  modelUsed: 'than phooey astride',
  executionTime: 13205,
  searchDate: dayjs('2025-12-29T20:08'),
};

export const sampleWithFullData: ISearchSemantic = {
  id: 23167,
  query: 'enormously boyfriend',
  queryEmbedding: '../fake-data/blob/hipster.txt',
  results: '../fake-data/blob/hipster.txt',
  relevanceScores: '../fake-data/blob/hipster.txt',
  modelUsed: 'object',
  executionTime: 8578,
  userId: 'outgoing',
  searchDate: dayjs('2025-12-29T08:31'),
};

export const sampleWithNewData: NewSearchSemantic = {
  query: 'meh uncork after',
  queryEmbedding: '../fake-data/blob/hipster.txt',
  results: '../fake-data/blob/hipster.txt',
  searchDate: dayjs('2025-12-29T08:16'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
