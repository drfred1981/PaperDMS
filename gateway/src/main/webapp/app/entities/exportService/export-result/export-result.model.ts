import dayjs from 'dayjs/esm';

import { ExportResultStatus } from 'app/entities/enumerations/export-result-status.model';
import { IExportJob } from 'app/entities/exportService/export-job/export-job.model';

export interface IExportResult {
  id: number;
  exportJobId?: number | null;
  documentId?: number | null;
  documentSha256?: string | null;
  originalFileName?: string | null;
  exportedPath?: string | null;
  exportedFileName?: string | null;
  s3ExportKey?: string | null;
  fileSize?: number | null;
  status?: keyof typeof ExportResultStatus | null;
  errorMessage?: string | null;
  exportedDate?: dayjs.Dayjs | null;
  exportJob?: Pick<IExportJob, 'id'> | null;
}

export type NewExportResult = Omit<IExportResult, 'id'> & { id: null };
