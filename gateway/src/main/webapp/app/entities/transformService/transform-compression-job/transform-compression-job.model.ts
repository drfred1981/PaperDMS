import dayjs from 'dayjs/esm';
import { CompressionType } from 'app/entities/enumerations/compression-type.model';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';

export interface ITransformCompressionJob {
  id: number;
  documentSha256?: string | null;
  compressionType?: keyof typeof CompressionType | null;
  quality?: number | null;
  targetSizeKb?: number | null;
  originalSize?: number | null;
  compressedSize?: number | null;
  compressionRatio?: number | null;
  outputS3Key?: string | null;
  outputDocumentSha256?: string | null;
  status?: keyof typeof TransformStatus | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewTransformCompressionJob = Omit<ITransformCompressionJob, 'id'> & { id: null };
