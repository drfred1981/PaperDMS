import dayjs from 'dayjs/esm';
import { IImageConversionBatch } from 'app/entities/pdfToImageService/image-conversion-batch/image-conversion-batch.model';
import { ImageQuality } from 'app/entities/enumerations/image-quality.model';
import { ImageFormat } from 'app/entities/enumerations/image-format.model';
import { ConversionType } from 'app/entities/enumerations/conversion-type.model';
import { ConversionStatus } from 'app/entities/enumerations/conversion-status.model';

export interface IImagePdfConversionRequest {
  id: number;
  sourceDocumentId?: number | null;
  sourceFileName?: string | null;
  sourcePdfS3Key?: string | null;
  imageQuality?: keyof typeof ImageQuality | null;
  imageFormat?: keyof typeof ImageFormat | null;
  conversionType?: keyof typeof ConversionType | null;
  startPage?: number | null;
  endPage?: number | null;
  totalPages?: number | null;
  status?: keyof typeof ConversionStatus | null;
  errorMessage?: string | null;
  requestedAt?: dayjs.Dayjs | null;
  startedAt?: dayjs.Dayjs | null;
  completedAt?: dayjs.Dayjs | null;
  processingDuration?: number | null;
  totalImagesSize?: number | null;
  imagesGenerated?: number | null;
  dpi?: number | null;
  requestedByUserId?: number | null;
  priority?: number | null;
  additionalOptions?: string | null;
  batch?: Pick<IImageConversionBatch, 'id'> | null;
}

export type NewImagePdfConversionRequest = Omit<IImagePdfConversionRequest, 'id'> & { id: null };
