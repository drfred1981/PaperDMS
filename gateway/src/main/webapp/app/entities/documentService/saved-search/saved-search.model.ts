import dayjs from 'dayjs/esm';
import { AlertFrequency } from 'app/entities/enumerations/alert-frequency.model';

export interface ISavedSearch {
  id: number;
  name?: string | null;
  query?: string | null;
  isPublic?: boolean | null;
  isAlert?: boolean | null;
  alertFrequency?: keyof typeof AlertFrequency | null;
  userId?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewSavedSearch = Omit<ISavedSearch, 'id'> & { id: null };
