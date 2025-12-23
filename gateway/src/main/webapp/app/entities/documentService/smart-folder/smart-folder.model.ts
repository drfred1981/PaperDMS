import dayjs from 'dayjs/esm';

export interface ISmartFolder {
  id: number;
  name?: string | null;
  queryJson?: string | null;
  autoRefresh?: boolean | null;
  isPublic?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewSmartFolder = Omit<ISmartFolder, 'id'> & { id: null };
