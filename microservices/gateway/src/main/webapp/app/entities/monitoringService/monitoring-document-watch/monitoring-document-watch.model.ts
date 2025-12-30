import dayjs from 'dayjs/esm';
import { WatchType } from 'app/entities/enumerations/watch-type.model';

export interface IMonitoringDocumentWatch {
  id: number;
  documentSha256?: string | null;
  userId?: string | null;
  watchType?: keyof typeof WatchType | null;
  notifyOnView?: boolean | null;
  notifyOnDownload?: boolean | null;
  notifyOnModify?: boolean | null;
  notifyOnShare?: boolean | null;
  notifyOnDelete?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewMonitoringDocumentWatch = Omit<IMonitoringDocumentWatch, 'id'> & { id: null };
