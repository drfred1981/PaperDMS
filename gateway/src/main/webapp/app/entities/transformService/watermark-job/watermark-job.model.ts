import dayjs from 'dayjs/esm';
import { WatermarkType } from 'app/entities/enumerations/watermark-type.model';
import { WatermarkPosition } from 'app/entities/enumerations/watermark-position.model';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';

export interface IWatermarkJob {
  id: number;
  documentId?: number | null;
  watermarkType?: keyof typeof WatermarkType | null;
  watermarkText?: string | null;
  watermarkImageS3Key?: string | null;
  position?: keyof typeof WatermarkPosition | null;
  opacity?: number | null;
  fontSize?: number | null;
  color?: string | null;
  rotation?: number | null;
  tiled?: boolean | null;
  outputS3Key?: string | null;
  outputDocumentId?: number | null;
  status?: keyof typeof TransformStatus | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewWatermarkJob = Omit<IWatermarkJob, 'id'> & { id: null };
