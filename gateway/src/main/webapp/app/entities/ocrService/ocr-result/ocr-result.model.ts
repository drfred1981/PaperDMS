import dayjs from 'dayjs/esm';
import { IOcrJob } from 'app/entities/ocrService/ocr-job/ocr-job.model';
import { OcrEngine } from 'app/entities/enumerations/ocr-engine.model';

export interface IOcrResult {
  id: number;
  pageNumber?: number | null;
  pageSha256?: string | null;
  confidence?: number | null;
  s3ResultKey?: string | null;
  s3Bucket?: string | null;
  s3BoundingBoxKey?: string | null;
  boundingBoxes?: string | null;
  metadata?: string | null;
  language?: string | null;
  wordCount?: number | null;
  ocrEngine?: keyof typeof OcrEngine | null;
  processingTime?: number | null;
  rawResponse?: string | null;
  rawResponseS3Key?: string | null;
  processedDate?: dayjs.Dayjs | null;
  job?: Pick<IOcrJob, 'id'> | null;
}

export type NewOcrResult = Omit<IOcrResult, 'id'> & { id: null };
