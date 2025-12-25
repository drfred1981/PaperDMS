import dayjs from 'dayjs/esm';
import { AiJobStatus } from 'app/entities/enumerations/ai-job-status.model';

export interface ICorrespondentExtraction {
  id: number;
  documentId?: number | null;
  documentSha256?: string | null;
  extractedText?: string | null;
  extractedTextSha256?: string | null;
  detectedLanguage?: string | null;
  languageConfidence?: number | null;
  status?: keyof typeof AiJobStatus | null;
  resultCacheKey?: string | null;
  isCached?: boolean | null;
  resultS3Key?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  sendersCount?: number | null;
  recipientsCount?: number | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewCorrespondentExtraction = Omit<ICorrespondentExtraction, 'id'> & { id: null };
