import dayjs from 'dayjs/esm';

import { IEmailImportImportRule, NewEmailImportImportRule } from './email-import-import-rule.model';

export const sampleWithRequiredData: IEmailImportImportRule = {
  id: 3343,
  name: 'whoever apud incidentally',
  priority: 5418,
  isActive: false,
  conditions: '../fake-data/blob/hipster.txt',
  actions: '../fake-data/blob/hipster.txt',
  createdBy: 'inasmuch ha blindly',
  createdDate: dayjs('2025-12-29T10:27'),
};

export const sampleWithPartialData: IEmailImportImportRule = {
  id: 13923,
  name: 'marksman incidentally',
  priority: 2210,
  isActive: true,
  conditions: '../fake-data/blob/hipster.txt',
  actions: '../fake-data/blob/hipster.txt',
  matchCount: 8044,
  lastMatchDate: dayjs('2025-12-29T16:40'),
  createdBy: 'demonstrate or unique',
  createdDate: dayjs('2025-12-29T20:22'),
};

export const sampleWithFullData: IEmailImportImportRule = {
  id: 992,
  name: 'masterpiece yuck instead',
  description: '../fake-data/blob/hipster.txt',
  priority: 3908,
  isActive: true,
  conditions: '../fake-data/blob/hipster.txt',
  actions: '../fake-data/blob/hipster.txt',
  notifyUsers: '../fake-data/blob/hipster.txt',
  matchCount: 22438,
  lastMatchDate: dayjs('2025-12-29T13:04'),
  createdBy: 'rebuild splash',
  createdDate: dayjs('2025-12-29T17:03'),
  lastModifiedDate: dayjs('2025-12-29T23:24'),
};

export const sampleWithNewData: NewEmailImportImportRule = {
  name: 'scamper ouch',
  priority: 6357,
  isActive: false,
  conditions: '../fake-data/blob/hipster.txt',
  actions: '../fake-data/blob/hipster.txt',
  createdBy: 'lustrous huzzah along',
  createdDate: dayjs('2025-12-30T05:35'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
