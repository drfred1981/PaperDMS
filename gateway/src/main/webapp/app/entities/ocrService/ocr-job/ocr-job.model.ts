import dayjs from 'dayjs/esm';
import { OcrStatus } from 'app/entities/enumerations/ocr-status.model';
import { OcrEngine } from 'app/entities/enumerations/ocr-engine.model';

export interface IOcrJob {
  id: number;
  status?: keyof typeof OcrStatus | null;
  documentId?: number | null;
  documentSha256?: string | null;
  s3Key?: string | null;
  s3Bucket?: string | null;
  requestedLanguage?: string | null;
  detectedLanguage?: string | null;
  languageConfidence?: number | null;
  ocrEngine?: keyof typeof OcrEngine | null;
  tikaEndpoint?: string | null;
  aiProvider?: string | null;
  aiModel?: string | null;
  resultCacheKey?: string | null;
  isCached?: boolean | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  pageCount?: number | null;
  progress?: number | null;
  retryCount?: number | null;
  priority?: number | null;
  processingTime?: number | null;
  costEstimate?: number | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
}

export type NewOcrJob = Omit<IOcrJob, 'id'> & { id: null };
