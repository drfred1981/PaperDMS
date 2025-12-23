import dayjs from 'dayjs/esm';

export interface ITagCategory {
  id: number;
  name?: string | null;
  color?: string | null;
  displayOrder?: number | null;
  isSystem?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  parent?: Pick<ITagCategory, 'id'> | null;
}

export type NewTagCategory = Omit<ITagCategory, 'id'> & { id: null };
