import dayjs from 'dayjs/esm';
import { AILanguageDetectionMethod } from 'app/entities/enumerations/ai-language-detection-method.model';

export interface IAILanguageDetection {
  id: number;
  documentSha256?: string | null;
  detectedLanguage?: string | null;
  confidence?: number | null;
  detectionMethod?: keyof typeof AILanguageDetectionMethod | null;
  alternativeLanguages?: string | null;
  textSample?: string | null;
  resultCacheKey?: string | null;
  isCached?: boolean | null;
  detectedDate?: dayjs.Dayjs | null;
  modelVersion?: string | null;
}

export type NewAILanguageDetection = Omit<IAILanguageDetection, 'id'> & { id: null };
