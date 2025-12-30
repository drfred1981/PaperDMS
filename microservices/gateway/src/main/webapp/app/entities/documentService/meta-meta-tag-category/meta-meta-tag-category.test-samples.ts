import dayjs from 'dayjs/esm';

import { IMetaMetaTagCategory, NewMetaMetaTagCategory } from './meta-meta-tag-category.model';

export const sampleWithRequiredData: IMetaMetaTagCategory = {
  id: 4172,
  name: 'beneath',
  isSystem: false,
  createdDate: dayjs('2025-12-29T14:27'),
  createdBy: 'unrealistic tank',
};

export const sampleWithPartialData: IMetaMetaTagCategory = {
  id: 10125,
  name: 'fibre dilate validity',
  color: 'pink',
  isSystem: false,
  createdDate: dayjs('2025-12-29T19:09'),
  createdBy: 'wherever ironclad',
};

export const sampleWithFullData: IMetaMetaTagCategory = {
  id: 29579,
  name: 'pull',
  color: 'red',
  displayOrder: 1777,
  isSystem: false,
  createdDate: dayjs('2025-12-29T18:17'),
  createdBy: 'sleepily',
};

export const sampleWithNewData: NewMetaMetaTagCategory = {
  name: 'cheerfully',
  isSystem: true,
  createdDate: dayjs('2025-12-29T15:37'),
  createdBy: 'joshingly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
