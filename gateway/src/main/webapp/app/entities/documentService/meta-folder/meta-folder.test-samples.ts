import dayjs from 'dayjs/esm';

import { IMetaFolder, NewMetaFolder } from './meta-folder.model';

export const sampleWithRequiredData: IMetaFolder = {
  id: 29849,
  name: 'phew noon',
  isShared: false,
  createdDate: dayjs('2025-12-29T13:59'),
  createdBy: 'diligently',
};

export const sampleWithPartialData: IMetaFolder = {
  id: 16088,
  name: 'inasmuch',
  path: 'dependent blah pfft',
  isShared: false,
  createdDate: dayjs('2025-12-29T13:04'),
  createdBy: 'sharply',
};

export const sampleWithFullData: IMetaFolder = {
  id: 9443,
  name: 'famously and',
  description: '../fake-data/blob/hipster.txt',
  path: 'insert within',
  isShared: true,
  createdDate: dayjs('2025-12-29T16:53'),
  createdBy: 'zowie supposing that',
};

export const sampleWithNewData: NewMetaFolder = {
  name: 'absent',
  isShared: false,
  createdDate: dayjs('2025-12-30T02:31'),
  createdBy: 'apropos yawn colorfully',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
