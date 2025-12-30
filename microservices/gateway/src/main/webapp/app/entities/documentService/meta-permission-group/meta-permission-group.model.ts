import dayjs from 'dayjs/esm';

export interface IMetaPermissionGroup {
  id: number;
  name?: string | null;
  permissions?: string | null;
  isSystem?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
}

export type NewMetaPermissionGroup = Omit<IMetaPermissionGroup, 'id'> & { id: null };
