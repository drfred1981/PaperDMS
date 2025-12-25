import dayjs from 'dayjs/esm';

import { ISmartFolder, NewSmartFolder } from './smart-folder.model';

export const sampleWithRequiredData: ISmartFolder = {
  id: 5620,
  name: 'boohoo brr instead',
  queryJson: '../fake-data/blob/hipster.txt',
  autoRefresh: true,
  isPublic: false,
  createdBy: 'editor',
  createdDate: dayjs('2025-12-25T09:34'),
};

export const sampleWithPartialData: ISmartFolder = {
  id: 27081,
  name: 'throughout sans midwife',
  queryJson: '../fake-data/blob/hipster.txt',
  autoRefresh: true,
  isPublic: false,
  createdBy: 'uh-huh gloat',
  createdDate: dayjs('2025-12-24T16:52'),
};

export const sampleWithFullData: ISmartFolder = {
  id: 5403,
  name: 'pish',
  queryJson: '../fake-data/blob/hipster.txt',
  autoRefresh: true,
  isPublic: true,
  createdBy: 'creative',
  createdDate: dayjs('2025-12-25T03:18'),
};

export const sampleWithNewData: NewSmartFolder = {
  name: 'sticky',
  queryJson: '../fake-data/blob/hipster.txt',
  autoRefresh: true,
  isPublic: false,
  createdBy: 'disposer fooey',
  createdDate: dayjs('2025-12-25T03:27'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
