import dayjs from 'dayjs/esm';

import { IMetaTag, NewMetaTag } from './meta-tag.model';

export const sampleWithRequiredData: IMetaTag = {
  id: 137,
  name: 'gerbil phew',
  isSystem: false,
  createdDate: dayjs('2025-12-30T04:31'),
  createdBy: 'quadruple',
};

export const sampleWithPartialData: IMetaTag = {
  id: 30195,
  name: 'preheat contractor ugh',
  color: 'plum',
  isSystem: false,
  createdDate: dayjs('2025-12-29T16:49'),
  createdBy: 'wafer',
};

export const sampleWithFullData: IMetaTag = {
  id: 47,
  name: 'an',
  color: 'mint gr',
  description: 'bandwidth besides',
  usageCount: 7739,
  isSystem: true,
  createdDate: dayjs('2025-12-30T05:32'),
  createdBy: 'edible swill shabby',
};

export const sampleWithNewData: NewMetaTag = {
  name: 'anticodon provided cool',
  isSystem: false,
  createdDate: dayjs('2025-12-30T01:55'),
  createdBy: 'coarse',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
