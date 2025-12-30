import dayjs from 'dayjs/esm';

export interface ISearchSemantic {
  id: number;
  query?: string | null;
  queryEmbedding?: string | null;
  results?: string | null;
  relevanceScores?: string | null;
  modelUsed?: string | null;
  executionTime?: number | null;
  userId?: string | null;
  searchDate?: dayjs.Dayjs | null;
}

export type NewSearchSemantic = Omit<ISearchSemantic, 'id'> & { id: null };
