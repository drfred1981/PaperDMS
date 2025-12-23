import dayjs from 'dayjs/esm';

export interface IImportRule {
  id: number;
  name?: string | null;
  description?: string | null;
  priority?: number | null;
  isActive?: boolean | null;
  conditions?: string | null;
  actions?: string | null;
  folderId?: number | null;
  documentTypeId?: number | null;
  applyTags?: string | null;
  notifyUsers?: string | null;
  matchCount?: number | null;
  lastMatchDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewImportRule = Omit<IImportRule, 'id'> & { id: null };
