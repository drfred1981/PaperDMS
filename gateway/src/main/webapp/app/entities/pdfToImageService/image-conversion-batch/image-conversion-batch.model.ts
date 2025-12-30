import dayjs from 'dayjs/esm';
import { ConversionStatus } from 'app/entities/enumerations/conversion-status.model';

export interface IImageConversionBatch {
  id: number;
  batchName?: string | null;
  description?: string | null;
  createdAt?: dayjs.Dayjs | null;
  status?: keyof typeof ConversionStatus | null;
  totalConversions?: number | null;
  completedConversions?: number | null;
  failedConversions?: number | null;
  startedAt?: dayjs.Dayjs | null;
  completedAt?: dayjs.Dayjs | null;
  totalProcessingDuration?: number | null;
  createdByUserId?: number | null;
}

export type NewImageConversionBatch = Omit<IImageConversionBatch, 'id'> & { id: null };
