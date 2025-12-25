import dayjs from 'dayjs/esm';

import { ITag, NewTag } from './tag.model';

export const sampleWithRequiredData: ITag = {
  id: 17364,
  name: 'perp loosely into',
  isSystem: false,
  createdDate: dayjs('2025-12-25T10:22'),
  createdBy: 'quarrelsomely merrily avalanche',
};

export const sampleWithPartialData: ITag = {
  id: 26165,
  name: 'private times',
  isSystem: false,
  createdDate: dayjs('2025-12-25T07:43'),
  createdBy: 'barring',
};

export const sampleWithFullData: ITag = {
  id: 28385,
  name: 'unhappy yummy beyond',
  color: 'maroon',
  description: 'aw however',
  usageCount: 877,
  isSystem: false,
  createdDate: dayjs('2025-12-25T01:02'),
  createdBy: 'whoa',
};

export const sampleWithNewData: NewTag = {
  name: 'silk eek so',
  isSystem: false,
  createdDate: dayjs('2025-12-24T13:33'),
  createdBy: 'where warm',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
