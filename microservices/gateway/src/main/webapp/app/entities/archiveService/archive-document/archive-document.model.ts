import dayjs from 'dayjs/esm';
import { IArchiveJob } from 'app/entities/archiveService/archive-job/archive-job.model';

export interface IArchiveDocument {
  id: number;
  documentSha256?: string | null;
  originalPath?: string | null;
  archivePath?: string | null;
  fileSize?: number | null;
  addedDate?: dayjs.Dayjs | null;
  archiveJob?: Pick<IArchiveJob, 'id'> | null;
}

export type NewArchiveDocument = Omit<IArchiveDocument, 'id'> & { id: null };
