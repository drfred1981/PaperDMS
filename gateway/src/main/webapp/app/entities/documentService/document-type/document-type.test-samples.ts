import dayjs from 'dayjs/esm';

import { IDocumentType, NewDocumentType } from './document-type.model';

export const sampleWithRequiredData: IDocumentType = {
  id: 3188,
  name: 'personalise',
  code: 'yum meanwhile section',
  isActive: true,
  createdDate: dayjs('2025-12-20T05:01'),
  createdBy: 'before wealthy pixellate',
};

export const sampleWithPartialData: IDocumentType = {
  id: 14763,
  name: 'pish',
  code: 'given',
  icon: 'lest of',
  color: 'maroon',
  isActive: false,
  createdDate: dayjs('2025-12-19T17:40'),
  createdBy: 'roundabout or tenant',
};

export const sampleWithFullData: IDocumentType = {
  id: 1005,
  name: 'bah',
  code: 'hence unfreeze dreamily',
  icon: 'rigidly without why',
  color: 'orchid',
  isActive: false,
  createdDate: dayjs('2025-12-19T17:13'),
  createdBy: 'sting massage the',
};

export const sampleWithNewData: NewDocumentType = {
  name: 'grimy ugh',
  code: 'out',
  isActive: true,
  createdDate: dayjs('2025-12-20T00:57'),
  createdBy: 'drab psst immediately',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
