import dayjs from 'dayjs/esm';
import { AlertFrequency } from 'app/entities/enumerations/alert-frequency.model';

export interface IMetaSavedSearch {
  id: number;
  name?: string | null;
  query?: string | null;
  isPublic?: boolean | null;
  isAlert?: boolean | null;
  alertFrequency?: keyof typeof AlertFrequency | null;
  userId?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewMetaSavedSearch = Omit<IMetaSavedSearch, 'id'> & { id: null };
