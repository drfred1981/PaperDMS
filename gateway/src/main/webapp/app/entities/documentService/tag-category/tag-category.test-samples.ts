import dayjs from 'dayjs/esm';

import { ITagCategory, NewTagCategory } from './tag-category.model';

export const sampleWithRequiredData: ITagCategory = {
  id: 3418,
  name: 'exotic boohoo moralise',
  isSystem: true,
  createdDate: dayjs('2025-12-19T21:43'),
  createdBy: 'guidance phooey',
};

export const sampleWithPartialData: ITagCategory = {
  id: 3238,
  name: 'adventurously instead',
  displayOrder: 25312,
  isSystem: false,
  createdDate: dayjs('2025-12-19T18:42'),
  createdBy: 'fervently red retention',
};

export const sampleWithFullData: ITagCategory = {
  id: 5121,
  name: 'apostrophize',
  color: 'olive',
  displayOrder: 10023,
  isSystem: false,
  createdDate: dayjs('2025-12-19T16:16'),
  createdBy: 'frivolous',
};

export const sampleWithNewData: NewTagCategory = {
  name: 'boo deploy meanwhile',
  isSystem: false,
  createdDate: dayjs('2025-12-19T18:11'),
  createdBy: 'towards',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
