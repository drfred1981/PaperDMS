import dayjs from 'dayjs/esm';

export interface IDocumentStatistics {
  id: number;
  documentId?: number | null;
  viewsTotal?: number | null;
  downloadsTotal?: number | null;
  uniqueViewers?: number | null;
  lastUpdated?: dayjs.Dayjs | null;
}

export type NewDocumentStatistics = Omit<IDocumentStatistics, 'id'> & { id: null };
