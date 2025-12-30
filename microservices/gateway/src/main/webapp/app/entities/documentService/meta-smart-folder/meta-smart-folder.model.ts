import dayjs from 'dayjs/esm';

export interface IMetaSmartFolder {
  id: number;
  name?: string | null;
  queryJson?: string | null;
  autoRefresh?: boolean | null;
  isPublic?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewMetaSmartFolder = Omit<IMetaSmartFolder, 'id'> & { id: null };
