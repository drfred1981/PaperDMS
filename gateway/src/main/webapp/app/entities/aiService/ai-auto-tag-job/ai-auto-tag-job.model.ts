import dayjs from 'dayjs/esm';
import { IAITypePrediction } from 'app/entities/aiService/ai-type-prediction/ai-type-prediction.model';
import { IAILanguageDetection } from 'app/entities/aiService/ai-language-detection/ai-language-detection.model';
import { AiJobStatus } from 'app/entities/enumerations/ai-job-status.model';

export interface IAIAutoTagJob {
  id: number;
  documentSha256?: string | null;
  s3Key?: string | null;
  extractedText?: string | null;
  extractedTextSha256?: string | null;
  status?: keyof typeof AiJobStatus | null;
  modelVersion?: string | null;
  resultCacheKey?: string | null;
  isCached?: boolean | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  createdDate?: dayjs.Dayjs | null;
  aITypePrediction?: Pick<IAITypePrediction, 'id'> | null;
  languagePrediction?: Pick<IAILanguageDetection, 'id'> | null;
}

export type NewAIAutoTagJob = Omit<IAIAutoTagJob, 'id'> & { id: null };
