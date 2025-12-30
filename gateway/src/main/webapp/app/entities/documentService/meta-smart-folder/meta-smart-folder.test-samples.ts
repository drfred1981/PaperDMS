import dayjs from 'dayjs/esm';

import { IMetaSmartFolder, NewMetaSmartFolder } from './meta-smart-folder.model';

export const sampleWithRequiredData: IMetaSmartFolder = {
  id: 9112,
  name: 'freezing whenever',
  queryJson: '../fake-data/blob/hipster.txt',
  autoRefresh: true,
  isPublic: true,
  createdBy: 'yin than',
  createdDate: dayjs('2025-12-29T15:20'),
};

export const sampleWithPartialData: IMetaSmartFolder = {
  id: 5800,
  name: 'desecrate classic',
  queryJson: '../fake-data/blob/hipster.txt',
  autoRefresh: false,
  isPublic: true,
  createdBy: 'foretell where',
  createdDate: dayjs('2025-12-29T11:11'),
};

export const sampleWithFullData: IMetaSmartFolder = {
  id: 6938,
  name: 'now out',
  queryJson: '../fake-data/blob/hipster.txt',
  autoRefresh: false,
  isPublic: false,
  createdBy: 'slide why',
  createdDate: dayjs('2025-12-29T07:57'),
};

export const sampleWithNewData: NewMetaSmartFolder = {
  name: 'who colorfully throughout',
  queryJson: '../fake-data/blob/hipster.txt',
  autoRefresh: true,
  isPublic: false,
  createdBy: 'upside-down reassuringly',
  createdDate: dayjs('2025-12-30T03:52'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
