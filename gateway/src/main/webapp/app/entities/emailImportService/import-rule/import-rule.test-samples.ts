import dayjs from 'dayjs/esm';

import { IImportRule, NewImportRule } from './import-rule.model';

export const sampleWithRequiredData: IImportRule = {
  id: 8257,
  name: 'king because',
  priority: 25948,
  isActive: false,
  conditions: '../fake-data/blob/hipster.txt',
  actions: '../fake-data/blob/hipster.txt',
  createdBy: 'foolish ah',
  createdDate: dayjs('2025-12-19T20:52'),
};

export const sampleWithPartialData: IImportRule = {
  id: 6555,
  name: 'per',
  priority: 18800,
  isActive: true,
  conditions: '../fake-data/blob/hipster.txt',
  actions: '../fake-data/blob/hipster.txt',
  folderId: 32627,
  documentTypeId: 23093,
  matchCount: 18963,
  lastMatchDate: dayjs('2025-12-20T10:17'),
  createdBy: 'furlough promptly',
  createdDate: dayjs('2025-12-20T01:53'),
};

export const sampleWithFullData: IImportRule = {
  id: 31840,
  name: 'ouch rudely',
  description: '../fake-data/blob/hipster.txt',
  priority: 19038,
  isActive: false,
  conditions: '../fake-data/blob/hipster.txt',
  actions: '../fake-data/blob/hipster.txt',
  folderId: 3980,
  documentTypeId: 4059,
  applyTags: '../fake-data/blob/hipster.txt',
  notifyUsers: '../fake-data/blob/hipster.txt',
  matchCount: 24110,
  lastMatchDate: dayjs('2025-12-19T17:12'),
  createdBy: 'blacken',
  createdDate: dayjs('2025-12-20T13:07'),
  lastModifiedDate: dayjs('2025-12-19T23:20'),
};

export const sampleWithNewData: NewImportRule = {
  name: 'into promptly',
  priority: 28508,
  isActive: true,
  conditions: '../fake-data/blob/hipster.txt',
  actions: '../fake-data/blob/hipster.txt',
  createdBy: 'unruly',
  createdDate: dayjs('2025-12-19T21:53'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
