import dayjs from 'dayjs/esm';
import { ManualType } from 'app/entities/enumerations/manual-type.model';
import { ManualStatus } from 'app/entities/enumerations/manual-status.model';

export interface IManual {
  id: number;
  documentId?: number | null;
  title?: string | null;
  manualType?: keyof typeof ManualType | null;
  version?: string | null;
  language?: string | null;
  publicationDate?: dayjs.Dayjs | null;
  pageCount?: number | null;
  status?: keyof typeof ManualStatus | null;
  isPublic?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewManual = Omit<IManual, 'id'> & { id: null };
