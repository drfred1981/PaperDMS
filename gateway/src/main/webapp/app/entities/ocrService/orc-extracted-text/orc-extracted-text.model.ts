import dayjs from 'dayjs/esm';
import { IOcrJob } from 'app/entities/ocrService/ocr-job/ocr-job.model';

export interface IOrcExtractedText {
  id: number;
  content?: string | null;
  contentSha256?: string | null;
  s3ContentKey?: string | null;
  s3Bucket?: string | null;
  pageNumber?: number | null;
  language?: string | null;
  wordCount?: number | null;
  hasStructuredData?: boolean | null;
  structuredData?: string | null;
  structuredDataS3Key?: string | null;
  extractedDate?: dayjs.Dayjs | null;
  job?: Pick<IOcrJob, 'id'> | null;
}

export type NewOrcExtractedText = Omit<IOrcExtractedText, 'id'> & { id: null };
