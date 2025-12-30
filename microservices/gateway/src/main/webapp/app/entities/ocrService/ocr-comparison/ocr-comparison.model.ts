import dayjs from 'dayjs/esm';
import { OcrEngine } from 'app/entities/enumerations/ocr-engine.model';

export interface IOcrComparison {
  id: number;
  documentSha256?: string | null;
  pageNumber?: number | null;
  tikaText?: string | null;
  tikaConfidence?: number | null;
  aiText?: string | null;
  aiConfidence?: number | null;
  similarity?: number | null;
  differences?: string | null;
  differencesS3Key?: string | null;
  selectedEngine?: keyof typeof OcrEngine | null;
  selectedBy?: string | null;
  selectedDate?: dayjs.Dayjs | null;
  comparisonDate?: dayjs.Dayjs | null;
  metadata?: string | null;
}

export type NewOcrComparison = Omit<IOcrComparison, 'id'> & { id: null };
