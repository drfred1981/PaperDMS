import dayjs from 'dayjs/esm';

import { ExportFormat } from 'app/entities/enumerations/export-format.model';
import { ExportStatus } from 'app/entities/enumerations/export-status.model';
import { IExportPattern } from 'app/entities/exportService/export-pattern/export-pattern.model';

export interface IExportJob {
  id: number;
  name?: string | null;
  description?: string | null;
  documentQuery?: string | null;
  exportPatternId?: number | null;
  exportFormat?: keyof typeof ExportFormat | null;
  includeMetadata?: boolean | null;
  includeVersions?: boolean | null;
  includeComments?: boolean | null;
  includeAuditTrail?: boolean | null;
  s3ExportKey?: string | null;
  exportSize?: number | null;
  documentCount?: number | null;
  filesGenerated?: number | null;
  status?: keyof typeof ExportStatus | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  exportPattern?: Pick<IExportPattern, 'id'> | null;
}

export type NewExportJob = Omit<IExportJob, 'id'> & { id: null };
