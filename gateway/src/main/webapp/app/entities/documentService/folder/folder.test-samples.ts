import dayjs from 'dayjs/esm';

import { IFolder, NewFolder } from './folder.model';

export const sampleWithRequiredData: IFolder = {
  id: 13315,
  name: 'frightfully closely',
  isShared: false,
  createdDate: dayjs('2025-12-24T10:35'),
  createdBy: 'yellow loftily',
};

export const sampleWithPartialData: IFolder = {
  id: 26481,
  name: 'silent explode',
  description: '../fake-data/blob/hipster.txt',
  path: 'drain frenetically definitive',
  isShared: false,
  createdDate: dayjs('2025-12-25T10:01'),
  createdBy: 'jeopardise',
};

export const sampleWithFullData: IFolder = {
  id: 12076,
  name: 'willfully finally',
  description: '../fake-data/blob/hipster.txt',
  path: 'willow however bruised',
  isShared: true,
  createdDate: dayjs('2025-12-25T04:23'),
  createdBy: 'carelessly sans though',
};

export const sampleWithNewData: NewFolder = {
  name: 'where recklessly',
  isShared: true,
  createdDate: dayjs('2025-12-25T06:14'),
  createdBy: 'aw',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
