import dayjs from 'dayjs/esm';

import { IImageConversionStatistics, NewImageConversionStatistics } from './image-conversion-statistics.model';

export const sampleWithRequiredData: IImageConversionStatistics = {
  id: 31791,
  statisticsDate: dayjs('2025-12-29'),
  calculatedAt: dayjs('2025-12-30T01:39'),
};

export const sampleWithPartialData: IImageConversionStatistics = {
  id: 23519,
  statisticsDate: dayjs('2025-12-29'),
  failedConversions: 31263,
  totalPagesConverted: 10066,
  totalImagesGenerated: 14101,
  averageProcessingDuration: 31355,
  minProcessingDuration: 2800,
  calculatedAt: dayjs('2025-12-29T15:13'),
};

export const sampleWithFullData: IImageConversionStatistics = {
  id: 7116,
  statisticsDate: dayjs('2025-12-29'),
  totalConversions: 6350,
  successfulConversions: 1834,
  failedConversions: 28676,
  totalPagesConverted: 17369,
  totalImagesGenerated: 14003,
  totalImagesSize: 23795,
  averageProcessingDuration: 7191,
  maxProcessingDuration: 805,
  minProcessingDuration: 9038,
  calculatedAt: dayjs('2025-12-29T19:28'),
};

export const sampleWithNewData: NewImageConversionStatistics = {
  statisticsDate: dayjs('2025-12-30'),
  calculatedAt: dayjs('2025-12-29T21:49'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
