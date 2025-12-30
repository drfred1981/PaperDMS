import dayjs from 'dayjs/esm';
import { IDocument } from 'app/entities/documentService/document/document.model';

export interface IDocumentComment {
  id: number;
  content?: string | null;
  pageNumber?: number | null;
  isResolved?: boolean | null;
  authorId?: string | null;
  createdDate?: dayjs.Dayjs | null;
  document?: Pick<IDocument, 'id'> | null;
  parentComment?: Pick<IDocumentComment, 'id'> | null;
}

export type NewDocumentComment = Omit<IDocumentComment, 'id'> & { id: null };
