import dayjs from 'dayjs/esm';

export interface IImageConversionStatistics {
  id: number;
  statisticsDate?: dayjs.Dayjs | null;
  totalConversions?: number | null;
  successfulConversions?: number | null;
  failedConversions?: number | null;
  totalPagesConverted?: number | null;
  totalImagesGenerated?: number | null;
  totalImagesSize?: number | null;
  averageProcessingDuration?: number | null;
  maxProcessingDuration?: number | null;
  minProcessingDuration?: number | null;
  calculatedAt?: dayjs.Dayjs | null;
}

export type NewImageConversionStatistics = Omit<IImageConversionStatistics, 'id'> & { id: null };
