import dayjs from 'dayjs/esm';
import { OcrEngine } from 'app/entities/enumerations/ocr-engine.model';

export interface IOcrCache {
  id: number;
  documentSha256?: string | null;
  ocrEngine?: keyof typeof OcrEngine | null;
  language?: string | null;
  pageCount?: number | null;
  totalConfidence?: number | null;
  s3ResultKey?: string | null;
  s3Bucket?: string | null;
  extractedTextS3Key?: string | null;
  metadata?: string | null;
  hits?: number | null;
  lastAccessDate?: dayjs.Dayjs | null;
  createdDate?: dayjs.Dayjs | null;
  expirationDate?: dayjs.Dayjs | null;
}

export type NewOcrCache = Omit<IOcrCache, 'id'> & { id: null };
