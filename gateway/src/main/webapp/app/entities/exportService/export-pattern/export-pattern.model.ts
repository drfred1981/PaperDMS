import dayjs from 'dayjs/esm';

export interface IExportPattern {
  id: number;
  name?: string | null;
  description?: string | null;
  pathTemplate?: string | null;
  fileNameTemplate?: string | null;
  variables?: string | null;
  examples?: string | null;
  isSystem?: boolean | null;
  isActive?: boolean | null;
  usageCount?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewExportPattern = Omit<IExportPattern, 'id'> & { id: null };
