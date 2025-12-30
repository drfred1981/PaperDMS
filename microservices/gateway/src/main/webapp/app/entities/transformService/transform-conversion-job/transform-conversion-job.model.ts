import dayjs from 'dayjs/esm';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';

export interface ITransformConversionJob {
  id: number;
  documentSha256?: string | null;
  sourceFormat?: string | null;
  targetFormat?: string | null;
  conversionEngine?: string | null;
  options?: string | null;
  outputS3Key?: string | null;
  outputDocumentSha256?: string | null;
  status?: keyof typeof TransformStatus | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewTransformConversionJob = Omit<ITransformConversionJob, 'id'> & { id: null };
