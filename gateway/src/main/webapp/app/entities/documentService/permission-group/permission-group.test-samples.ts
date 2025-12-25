import dayjs from 'dayjs/esm';

import { IPermissionGroup, NewPermissionGroup } from './permission-group.model';

export const sampleWithRequiredData: IPermissionGroup = {
  id: 30337,
  name: 'smog',
  permissions: '../fake-data/blob/hipster.txt',
  isSystem: false,
  createdDate: dayjs('2025-12-24T13:15'),
  createdBy: 'intensely ack',
};

export const sampleWithPartialData: IPermissionGroup = {
  id: 9350,
  name: 'institute',
  permissions: '../fake-data/blob/hipster.txt',
  isSystem: false,
  createdDate: dayjs('2025-12-24T22:12'),
  createdBy: 'once mortally',
};

export const sampleWithFullData: IPermissionGroup = {
  id: 27888,
  name: 'till behind',
  permissions: '../fake-data/blob/hipster.txt',
  isSystem: true,
  createdDate: dayjs('2025-12-24T14:35'),
  createdBy: 'unnaturally orientate very',
};

export const sampleWithNewData: NewPermissionGroup = {
  name: 'fluffy certification',
  permissions: '../fake-data/blob/hipster.txt',
  isSystem: true,
  createdDate: dayjs('2025-12-25T06:22'),
  createdBy: 'train reboot',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
