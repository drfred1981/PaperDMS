import dayjs from 'dayjs/esm';
import { ConversionStatus } from 'app/entities/enumerations/conversion-status.model';

export interface IImageConversionHistory {
  id: number;
  originalRequestId?: number | null;
  archivedAt?: dayjs.Dayjs | null;
  conversionData?: string | null;
  imagesCount?: number | null;
  totalSize?: number | null;
  finalStatus?: keyof typeof ConversionStatus | null;
  processingDuration?: number | null;
}

export type NewImageConversionHistory = Omit<IImageConversionHistory, 'id'> & { id: null };
