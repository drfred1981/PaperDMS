import dayjs from 'dayjs/esm';

export interface IDocumentType {
  id: number;
  name?: string | null;
  code?: string | null;
  icon?: string | null;
  color?: string | null;
  isActive?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
}

export type NewDocumentType = Omit<IDocumentType, 'id'> & { id: null };
