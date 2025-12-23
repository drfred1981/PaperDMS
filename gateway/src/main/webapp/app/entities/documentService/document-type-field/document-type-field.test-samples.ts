import dayjs from 'dayjs/esm';

import { IDocumentTypeField, NewDocumentTypeField } from './document-type-field.model';

export const sampleWithRequiredData: IDocumentTypeField = {
  id: 27156,
  fieldKey: 'sans',
  fieldLabel: 'now',
  isRequired: false,
  isSearchable: true,
  createdDate: dayjs('2025-12-20T14:56'),
};

export const sampleWithPartialData: IDocumentTypeField = {
  id: 16086,
  fieldKey: 'why even',
  fieldLabel: 'qua',
  isRequired: true,
  isSearchable: false,
  createdDate: dayjs('2025-12-20T10:01'),
};

export const sampleWithFullData: IDocumentTypeField = {
  id: 17576,
  fieldKey: 'however',
  fieldLabel: 'idealistic daily yum',
  dataType: 'ARRAY',
  isRequired: true,
  isSearchable: true,
  createdDate: dayjs('2025-12-20T13:19'),
};

export const sampleWithNewData: NewDocumentTypeField = {
  fieldKey: 'provided interestingly',
  fieldLabel: 'swiftly zowie',
  isRequired: true,
  isSearchable: true,
  createdDate: dayjs('2025-12-19T17:48'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
