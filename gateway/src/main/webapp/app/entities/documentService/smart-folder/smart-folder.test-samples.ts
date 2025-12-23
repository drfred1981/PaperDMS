import dayjs from 'dayjs/esm';

import { ISmartFolder, NewSmartFolder } from './smart-folder.model';

export const sampleWithRequiredData: ISmartFolder = {
  id: 5620,
  name: 'boohoo brr instead',
  queryJson: '../fake-data/blob/hipster.txt',
  autoRefresh: true,
  isPublic: false,
  createdBy: 'editor',
  createdDate: dayjs('2025-12-20T14:35'),
};

export const sampleWithPartialData: ISmartFolder = {
  id: 27081,
  name: 'throughout sans midwife',
  queryJson: '../fake-data/blob/hipster.txt',
  autoRefresh: true,
  isPublic: false,
  createdBy: 'uh-huh gloat',
  createdDate: dayjs('2025-12-19T21:53'),
};

export const sampleWithFullData: ISmartFolder = {
  id: 5403,
  name: 'pish',
  queryJson: '../fake-data/blob/hipster.txt',
  autoRefresh: true,
  isPublic: true,
  createdBy: 'creative',
  createdDate: dayjs('2025-12-20T08:19'),
};

export const sampleWithNewData: NewSmartFolder = {
  name: 'sticky',
  queryJson: '../fake-data/blob/hipster.txt',
  autoRefresh: true,
  isPublic: false,
  createdBy: 'disposer fooey',
  createdDate: dayjs('2025-12-20T08:28'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
