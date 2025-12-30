import dayjs from 'dayjs/esm';
import { IImagePdfConversionRequest } from 'app/entities/pdfToImageService/image-pdf-conversion-request/image-pdf-conversion-request.model';
import { ImageFormat } from 'app/entities/enumerations/image-format.model';
import { ImageQuality } from 'app/entities/enumerations/image-quality.model';

export interface IImageGeneratedImage {
  id: number;
  pageNumber?: number | null;
  fileName?: string | null;
  s3Key?: string | null;
  preSignedUrl?: string | null;
  urlExpiresAt?: dayjs.Dayjs | null;
  format?: keyof typeof ImageFormat | null;
  quality?: keyof typeof ImageQuality | null;
  width?: number | null;
  height?: number | null;
  fileSize?: number | null;
  dpi?: number | null;
  sha256Hash?: string | null;
  generatedAt?: dayjs.Dayjs | null;
  metadata?: string | null;
  conversionRequest?: Pick<IImagePdfConversionRequest, 'id'> | null;
}

export type NewImageGeneratedImage = Omit<IImageGeneratedImage, 'id'> & { id: null };
