import dayjs from 'dayjs/esm';

import { IScannerConfiguration, NewScannerConfiguration } from './scanner-configuration.model';

export const sampleWithRequiredData: IScannerConfiguration = {
  id: 17511,
  name: 'despite knowingly or',
  scannerType: 'LOCAL',
  isActive: true,
  createdDate: dayjs('2025-12-25T03:31'),
};

export const sampleWithPartialData: IScannerConfiguration = {
  id: 25102,
  name: 'backbone spectacles',
  scannerType: 'NETWORK',
  protocol: 'immediately uh-huh',
  model: 'hmph hence wrong',
  defaultResolution: 32646,
  capabilities: '../fake-data/blob/hipster.txt',
  isActive: true,
  createdDate: dayjs('2025-12-25T02:30'),
  lastModifiedDate: dayjs('2025-12-24T15:48'),
};

export const sampleWithFullData: IScannerConfiguration = {
  id: 19692,
  name: 'yieldingly considering',
  scannerType: 'LOCAL',
  ipAddress: 'incidentally',
  port: 399,
  protocol: 'all',
  manufacturer: 'thin ethyl',
  model: 'whoa second weary',
  defaultColorMode: 'AUTO',
  defaultResolution: 18549,
  defaultFormat: 'TIFF',
  capabilities: '../fake-data/blob/hipster.txt',
  isActive: true,
  createdDate: dayjs('2025-12-25T03:42'),
  lastModifiedDate: dayjs('2025-12-24T20:32'),
};

export const sampleWithNewData: NewScannerConfiguration = {
  name: 'uh-huh role',
  scannerType: 'MOBILE',
  isActive: false,
  createdDate: dayjs('2025-12-25T04:32'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
