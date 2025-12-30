import dayjs from 'dayjs/esm';

import { IMetaPermissionGroup, NewMetaPermissionGroup } from './meta-permission-group.model';

export const sampleWithRequiredData: IMetaPermissionGroup = {
  id: 20144,
  name: 'hence',
  permissions: '../fake-data/blob/hipster.txt',
  isSystem: true,
  createdDate: dayjs('2025-12-29T14:57'),
  createdBy: 'horn',
};

export const sampleWithPartialData: IMetaPermissionGroup = {
  id: 17623,
  name: 'beneath miserably',
  permissions: '../fake-data/blob/hipster.txt',
  isSystem: false,
  createdDate: dayjs('2025-12-29T19:45'),
  createdBy: 'utilization throughout',
};

export const sampleWithFullData: IMetaPermissionGroup = {
  id: 13074,
  name: 'unripe',
  permissions: '../fake-data/blob/hipster.txt',
  isSystem: true,
  createdDate: dayjs('2025-12-29T12:35'),
  createdBy: 'goodwill once',
};

export const sampleWithNewData: NewMetaPermissionGroup = {
  name: 'indeed ugh',
  permissions: '../fake-data/blob/hipster.txt',
  isSystem: false,
  createdDate: dayjs('2025-12-29T21:57'),
  createdBy: 'courtroom',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
