import dayjs from 'dayjs/esm';

export interface IFolder {
  id: number;
  name?: string | null;
  description?: string | null;
  path?: string | null;
  isShared?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  parent?: Pick<IFolder, 'id'> | null;
}

export type NewFolder = Omit<IFolder, 'id'> & { id: null };
