import dayjs from 'dayjs/esm';

export interface ISearchIndex {
  id: number;
  documentSha256?: string | null;
  indexedContent?: string | null;
  metadata?: string | null;
  tags?: string | null;
  correspondents?: string | null;
  extractedEntities?: string | null;
  indexedDate?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
}

export type NewSearchIndex = Omit<ISearchIndex, 'id'> & { id: null };
