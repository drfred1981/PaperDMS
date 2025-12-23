import dayjs from 'dayjs/esm';

import { IDashboard, NewDashboard } from './dashboard.model';

export const sampleWithRequiredData: IDashboard = {
  id: 29725,
  name: 'whose apud bouncy',
  isPublic: false,
  layout: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2025-12-19T18:19'),
};

export const sampleWithPartialData: IDashboard = {
  id: 19243,
  name: 'though an',
  userId: 'correctly thick huge',
  isPublic: true,
  layout: '../fake-data/blob/hipster.txt',
  refreshInterval: 20047,
  isDefault: false,
  createdDate: dayjs('2025-12-19T17:15'),
};

export const sampleWithFullData: IDashboard = {
  id: 14801,
  name: 'worth galvanize through',
  description: '../fake-data/blob/hipster.txt',
  userId: 'coolly offensively phew',
  isPublic: true,
  layout: '../fake-data/blob/hipster.txt',
  refreshInterval: 8516,
  isDefault: true,
  createdDate: dayjs('2025-12-20T05:37'),
};

export const sampleWithNewData: NewDashboard = {
  name: 'excluding underneath however',
  isPublic: false,
  layout: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2025-12-20T06:55'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
