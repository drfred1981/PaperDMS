import dayjs from 'dayjs/esm';
import { IDocumentType } from 'app/entities/documentService/document-type/document-type.model';

export interface IDocumentTemplate {
  id: number;
  name?: string | null;
  templateSha256?: string | null;
  templateS3Key?: string | null;
  isActive?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  documentType?: Pick<IDocumentType, 'id'> | null;
}

export type NewDocumentTemplate = Omit<IDocumentTemplate, 'id'> & { id: null };
