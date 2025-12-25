import dayjs from 'dayjs/esm';
import { ExtractionMethod } from 'app/entities/enumerations/extraction-method.model';

export interface IExtractedField {
  id: number;
  documentId?: number | null;
  fieldKey?: string | null;
  fieldValue?: string | null;
  confidence?: number | null;
  extractionMethod?: keyof typeof ExtractionMethod | null;
  isVerified?: boolean | null;
  extractedDate?: dayjs.Dayjs | null;
}

export type NewExtractedField = Omit<IExtractedField, 'id'> & { id: null };
