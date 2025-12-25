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
  createdDate: dayjs('2025-12-24T15:51'),
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
  lastMatchDate: dayjs('2025-12-25T05:16'),
  createdBy: 'furlough promptly',
  createdDate: dayjs('2025-12-24T20:52'),
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
  lastMatchDate: dayjs('2025-12-24T12:11'),
  createdBy: 'blacken',
  createdDate: dayjs('2025-12-25T08:06'),
  lastModifiedDate: dayjs('2025-12-24T18:19'),
};

export const sampleWithNewData: NewImportRule = {
  name: 'into promptly',
  priority: 28508,
  isActive: true,
  conditions: '../fake-data/blob/hipster.txt',
  actions: '../fake-data/blob/hipster.txt',
  createdBy: 'unruly',
  createdDate: dayjs('2025-12-24T16:52'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
