import dayjs from 'dayjs/esm';
import { IDocument } from 'app/entities/documentService/document/document.model';

export interface IDocumentStatistics {
  id: number;
  viewsTotal?: number | null;
  downloadsTotal?: number | null;
  uniqueViewers?: number | null;
  lastUpdated?: dayjs.Dayjs | null;
  document?: Pick<IDocument, 'id'> | null;
}

export type NewDocumentStatistics = Omit<IDocumentStatistics, 'id'> & { id: null };
