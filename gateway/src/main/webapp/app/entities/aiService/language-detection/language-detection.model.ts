import dayjs from 'dayjs/esm';
import { LanguageDetectionMethod } from 'app/entities/enumerations/language-detection-method.model';

export interface ILanguageDetection {
  id: number;
  documentId?: number | null;
  documentSha256?: string | null;
  detectedLanguage?: string | null;
  confidence?: number | null;
  detectionMethod?: keyof typeof LanguageDetectionMethod | null;
  alternativeLanguages?: string | null;
  textSample?: string | null;
  resultCacheKey?: string | null;
  isCached?: boolean | null;
  detectedDate?: dayjs.Dayjs | null;
  modelVersion?: string | null;
}

export type NewLanguageDetection = Omit<ILanguageDetection, 'id'> & { id: null };
