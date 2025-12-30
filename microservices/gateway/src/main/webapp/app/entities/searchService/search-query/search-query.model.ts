import dayjs from 'dayjs/esm';

export interface ISearchQuery {
  id: number;
  query?: string | null;
  filters?: string | null;
  resultCount?: number | null;
  executionTime?: number | null;
  userId?: string | null;
  searchDate?: dayjs.Dayjs | null;
  isRelevant?: boolean | null;
}

export type NewSearchQuery = Omit<ISearchQuery, 'id'> & { id: null };
