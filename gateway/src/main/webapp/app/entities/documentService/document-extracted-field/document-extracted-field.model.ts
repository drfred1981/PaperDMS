import dayjs from 'dayjs/esm';
import { IDocument } from 'app/entities/documentService/document/document.model';
import { ExtractionMethod } from 'app/entities/enumerations/extraction-method.model';

export interface IDocumentExtractedField {
  id: number;
  fieldKey?: string | null;
  fieldValue?: string | null;
  confidence?: number | null;
  extractionMethod?: keyof typeof ExtractionMethod | null;
  isVerified?: boolean | null;
  extractedDate?: dayjs.Dayjs | null;
  document?: Pick<IDocument, 'id'> | null;
}

export type NewDocumentExtractedField = Omit<IDocumentExtractedField, 'id'> & { id: null };
