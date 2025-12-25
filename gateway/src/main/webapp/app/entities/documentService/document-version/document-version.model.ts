import dayjs from 'dayjs/esm';
import { IDocument } from 'app/entities/documentService/document/document.model';

export interface IDocumentVersion {
  id: number;
  versionNumber?: number | null;
  sha256?: string | null;
  s3Key?: string | null;
  fileSize?: number | null;
  uploadDate?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  createdBy?: string | null;
  document?: Pick<IDocument, 'id'> | null;
}

export type NewDocumentVersion = Omit<IDocumentVersion, 'id'> & { id: null };
