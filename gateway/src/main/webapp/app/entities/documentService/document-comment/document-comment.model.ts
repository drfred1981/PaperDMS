import dayjs from 'dayjs/esm';

export interface IDocumentComment {
  id: number;
  documentId?: number | null;
  content?: string | null;
  pageNumber?: number | null;
  isResolved?: boolean | null;
  authorId?: string | null;
  createdDate?: dayjs.Dayjs | null;
  parentComment?: Pick<IDocumentComment, 'id'> | null;
}

export type NewDocumentComment = Omit<IDocumentComment, 'id'> & { id: null };
