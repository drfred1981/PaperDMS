import dayjs from 'dayjs/esm';

import { IExportPattern, NewExportPattern } from './export-pattern.model';

export const sampleWithRequiredData: IExportPattern = {
  id: 5345,
  name: 'grouper',
  pathTemplate: 'fooey',
  fileNameTemplate: 'instantly metabolite',
  isSystem: false,
  isActive: true,
  createdBy: 'onto short',
  createdDate: dayjs('2025-12-30T00:21'),
};

export const sampleWithPartialData: IExportPattern = {
  id: 23463,
  name: 'yum incidentally',
  pathTemplate: 'not lashes',
  fileNameTemplate: 'save',
  variables: '../fake-data/blob/hipster.txt',
  examples: '../fake-data/blob/hipster.txt',
  isSystem: false,
  isActive: true,
  usageCount: 10482,
  createdBy: 'pity',
  createdDate: dayjs('2025-12-30T00:53'),
};

export const sampleWithFullData: IExportPattern = {
  id: 13031,
  name: 'enhance',
  description: '../fake-data/blob/hipster.txt',
  pathTemplate: 'round',
  fileNameTemplate: 'loosely or',
  variables: '../fake-data/blob/hipster.txt',
  examples: '../fake-data/blob/hipster.txt',
  isSystem: false,
  isActive: true,
  usageCount: 4107,
  createdBy: 'following',
  createdDate: dayjs('2025-12-29T21:55'),
  lastModifiedDate: dayjs('2025-12-30T06:21'),
};

export const sampleWithNewData: NewExportPattern = {
  name: 'vicinity expatiate psst',
  pathTemplate: 'misread',
  fileNameTemplate: 'hence thongs needily',
  isSystem: true,
  isActive: true,
  createdBy: 'even since vanadyl',
  createdDate: dayjs('2025-12-29T19:37'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
