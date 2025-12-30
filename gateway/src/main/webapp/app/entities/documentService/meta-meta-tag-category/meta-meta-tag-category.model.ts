import dayjs from 'dayjs/esm';

export interface IMetaMetaTagCategory {
  id: number;
  name?: string | null;
  color?: string | null;
  displayOrder?: number | null;
  isSystem?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  parent?: Pick<IMetaMetaTagCategory, 'id'> | null;
}

export type NewMetaMetaTagCategory = Omit<IMetaMetaTagCategory, 'id'> & { id: null };
