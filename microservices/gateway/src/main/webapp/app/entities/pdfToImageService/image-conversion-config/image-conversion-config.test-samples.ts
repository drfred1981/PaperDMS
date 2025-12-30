import dayjs from 'dayjs/esm';

import { IImageConversionConfig, NewImageConversionConfig } from './image-conversion-config.model';

export const sampleWithRequiredData: IImageConversionConfig = {
  id: 23969,
  configName: 'questioningly freckle zebra',
  defaultQuality: 'ULTRA',
  defaultFormat: 'JPEG',
  defaultDpi: 890,
  defaultConversionType: 'PAGE_RANGE',
  isActive: false,
  isDefault: true,
  createdAt: dayjs('2025-12-30T02:53'),
};

export const sampleWithPartialData: IImageConversionConfig = {
  id: 8330,
  configName: 'camouflage assail',
  defaultQuality: 'ULTRA',
  defaultFormat: 'WEBP',
  defaultDpi: 751,
  defaultConversionType: 'SINGLE_PAGE',
  isActive: false,
  isDefault: false,
  createdAt: dayjs('2025-12-30T05:06'),
};

export const sampleWithFullData: IImageConversionConfig = {
  id: 27164,
  configName: 'smug miscalculate',
  description: 'mid acidic next',
  defaultQuality: 'HIGH',
  defaultFormat: 'PNG',
  defaultDpi: 158,
  defaultConversionType: 'FIRST_PAGE_ONLY',
  defaultPriority: 5,
  isActive: false,
  isDefault: false,
  createdAt: dayjs('2025-12-30T00:57'),
  updatedAt: dayjs('2025-12-29T19:54'),
};

export const sampleWithNewData: NewImageConversionConfig = {
  configName: 'er',
  defaultQuality: 'ULTRA',
  defaultFormat: 'JPEG',
  defaultDpi: 224,
  defaultConversionType: 'ALL_PAGES',
  isActive: false,
  isDefault: false,
  createdAt: dayjs('2025-12-30T00:37'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
