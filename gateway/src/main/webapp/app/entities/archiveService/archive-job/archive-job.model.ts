import dayjs from 'dayjs/esm';

import { ArchiveFormat } from 'app/entities/enumerations/archive-format.model';
import { ArchiveStatus } from 'app/entities/enumerations/archive-status.model';

export interface IArchiveJob {
  id: number;
  name?: string | null;
  description?: string | null;
  documentQuery?: string | null;
  archiveFormat?: keyof typeof ArchiveFormat | null;
  compressionLevel?: number | null;
  encryptionEnabled?: boolean | null;
  encryptionAlgorithm?: string | null;
  password?: string | null;
  s3ArchiveKey?: string | null;
  archiveSha256?: string | null;
  archiveSize?: number | null;
  documentCount?: number | null;
  status?: keyof typeof ArchiveStatus | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewArchiveJob = Omit<IArchiveJob, 'id'> & { id: null };
