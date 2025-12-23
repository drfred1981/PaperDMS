import dayjs from 'dayjs/esm';

import { ISemanticSearch, NewSemanticSearch } from './semantic-search.model';

export const sampleWithRequiredData: ISemanticSearch = {
  id: 7130,
  query: 'own inwardly eventually',
  queryEmbedding: '../fake-data/blob/hipster.txt',
  results: '../fake-data/blob/hipster.txt',
  searchDate: dayjs('2025-12-20T13:11'),
};

export const sampleWithPartialData: ISemanticSearch = {
  id: 3866,
  query: 'improbable',
  queryEmbedding: '../fake-data/blob/hipster.txt',
  results: '../fake-data/blob/hipster.txt',
  searchDate: dayjs('2025-12-20T14:09'),
};

export const sampleWithFullData: ISemanticSearch = {
  id: 609,
  query: 'adolescent',
  queryEmbedding: '../fake-data/blob/hipster.txt',
  results: '../fake-data/blob/hipster.txt',
  relevanceScores: '../fake-data/blob/hipster.txt',
  modelUsed: 'penalise whether',
  executionTime: 13518,
  userId: 'meatloaf',
  searchDate: dayjs('2025-12-20T04:05'),
};

export const sampleWithNewData: NewSemanticSearch = {
  query: 'smoothly travel for',
  queryEmbedding: '../fake-data/blob/hipster.txt',
  results: '../fake-data/blob/hipster.txt',
  searchDate: dayjs('2025-12-19T21:14'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
