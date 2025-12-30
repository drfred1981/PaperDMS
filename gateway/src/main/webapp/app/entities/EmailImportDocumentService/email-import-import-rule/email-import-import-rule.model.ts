import dayjs from 'dayjs/esm';

export interface IEmailImportImportRule {
  id: number;
  name?: string | null;
  description?: string | null;
  priority?: number | null;
  isActive?: boolean | null;
  conditions?: string | null;
  actions?: string | null;
  notifyUsers?: string | null;
  matchCount?: number | null;
  lastMatchDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewEmailImportImportRule = Omit<IEmailImportImportRule, 'id'> & { id: null };
