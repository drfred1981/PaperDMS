import dayjs from 'dayjs/esm';

export interface IMetaFolder {
  id: number;
  name?: string | null;
  description?: string | null;
  path?: string | null;
  isShared?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  parent?: Pick<IMetaFolder, 'id'> | null;
}

export type NewMetaFolder = Omit<IMetaFolder, 'id'> & { id: null };
