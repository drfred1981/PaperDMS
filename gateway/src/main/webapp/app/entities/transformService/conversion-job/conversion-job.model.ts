import dayjs from 'dayjs/esm';

import { TransformStatus } from 'app/entities/enumerations/transform-status.model';

export interface IConversionJob {
  id: number;
  documentId?: number | null;
  documentSha256?: string | null;
  sourceFormat?: string | null;
  targetFormat?: string | null;
  conversionEngine?: string | null;
  options?: string | null;
  outputS3Key?: string | null;
  outputDocumentId?: number | null;
  status?: keyof typeof TransformStatus | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewConversionJob = Omit<IConversionJob, 'id'> & { id: null };
