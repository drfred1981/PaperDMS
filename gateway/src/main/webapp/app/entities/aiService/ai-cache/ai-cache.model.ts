import dayjs from 'dayjs/esm';

export interface IAiCache {
  id: number;
  cacheKey?: string | null;
  inputSha256?: string | null;
  aiProvider?: string | null;
  aiModel?: string | null;
  operation?: string | null;
  inputData?: string | null;
  resultData?: string | null;
  s3ResultKey?: string | null;
  confidence?: number | null;
  metadata?: string | null;
  hits?: number | null;
  cost?: number | null;
  lastAccessDate?: dayjs.Dayjs | null;
  createdDate?: dayjs.Dayjs | null;
  expirationDate?: dayjs.Dayjs | null;
}

export type NewAiCache = Omit<IAiCache, 'id'> & { id: null };
