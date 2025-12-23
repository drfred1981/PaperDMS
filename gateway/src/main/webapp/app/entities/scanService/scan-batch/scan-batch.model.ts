import dayjs from 'dayjs/esm';

import { BatchStatus } from 'app/entities/enumerations/batch-status.model';

export interface IScanBatch {
  id: number;
  name?: string | null;
  description?: string | null;
  totalJobs?: number | null;
  completedJobs?: number | null;
  totalPages?: number | null;
  status?: keyof typeof BatchStatus | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewScanBatch = Omit<IScanBatch, 'id'> & { id: null };
