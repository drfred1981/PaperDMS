import dayjs from 'dayjs/esm';

export interface IPermissionGroup {
  id: number;
  name?: string | null;
  permissions?: string | null;
  isSystem?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
}

export type NewPermissionGroup = Omit<IPermissionGroup, 'id'> & { id: null };
