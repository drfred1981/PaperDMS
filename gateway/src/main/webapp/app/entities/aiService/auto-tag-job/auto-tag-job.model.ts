import dayjs from 'dayjs/esm';
import { AiJobStatus } from 'app/entities/enumerations/ai-job-status.model';

export interface IAutoTagJob {
  id: number;
  documentId?: number | null;
  documentSha256?: string | null;
  s3Key?: string | null;
  extractedText?: string | null;
  extractedTextSha256?: string | null;
  detectedLanguage?: string | null;
  languageConfidence?: number | null;
  status?: keyof typeof AiJobStatus | null;
  modelVersion?: string | null;
  resultCacheKey?: string | null;
  isCached?: boolean | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  confidence?: number | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewAutoTagJob = Omit<IAutoTagJob, 'id'> & { id: null };
