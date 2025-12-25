import dayjs from 'dayjs/esm';
import { IScanJob } from 'app/entities/scanService/scan-job/scan-job.model';

export interface IScannedPage {
  id: number;
  scanJobId?: number | null;
  pageNumber?: number | null;
  sha256?: string | null;
  s3Key?: string | null;
  s3PreviewKey?: string | null;
  fileSize?: number | null;
  width?: number | null;
  height?: number | null;
  dpi?: number | null;
  documentId?: number | null;
  scannedDate?: dayjs.Dayjs | null;
  scanJob?: Pick<IScanJob, 'id'> | null;
}

export type NewScannedPage = Omit<IScannedPage, 'id'> & { id: null };
