import dayjs from 'dayjs/esm';

import { IDocumentTypeField, NewDocumentTypeField } from './document-type-field.model';

export const sampleWithRequiredData: IDocumentTypeField = {
  id: 27156,
  fieldKey: 'sans',
  fieldLabel: 'now',
  isRequired: false,
  isSearchable: true,
  createdDate: dayjs('2025-12-30T06:01'),
};

export const sampleWithPartialData: IDocumentTypeField = {
  id: 16086,
  fieldKey: 'why even',
  fieldLabel: 'qua',
  isRequired: true,
  isSearchable: false,
  createdDate: dayjs('2025-12-30T01:07'),
};

export const sampleWithFullData: IDocumentTypeField = {
  id: 17576,
  fieldKey: 'however',
  fieldLabel: 'idealistic daily yum',
  dataType: 'ARRAY',
  isRequired: true,
  isSearchable: true,
  createdDate: dayjs('2025-12-30T04:24'),
};

export const sampleWithNewData: NewDocumentTypeField = {
  fieldKey: 'provided interestingly',
  fieldLabel: 'swiftly zowie',
  isRequired: true,
  isSearchable: true,
  createdDate: dayjs('2025-12-29T08:53'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
