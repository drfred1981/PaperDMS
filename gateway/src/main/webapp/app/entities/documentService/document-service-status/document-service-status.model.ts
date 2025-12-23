import dayjs from 'dayjs/esm';

import { ServiceStatus } from 'app/entities/enumerations/service-status.model';
import { ServiceType } from 'app/entities/enumerations/service-type.model';

export interface IDocumentServiceStatus {
  id: number;
  documentId?: number | null;
  serviceType?: keyof typeof ServiceType | null;
  status?: keyof typeof ServiceStatus | null;
  statusDetails?: string | null;
  errorMessage?: string | null;
  retryCount?: number | null;
  lastProcessedDate?: dayjs.Dayjs | null;
  processingStartDate?: dayjs.Dayjs | null;
  processingEndDate?: dayjs.Dayjs | null;
  processingDuration?: number | null;
  jobId?: string | null;
  priority?: number | null;
  updatedBy?: string | null;
  updatedDate?: dayjs.Dayjs | null;
}

export type NewDocumentServiceStatus = Omit<IDocumentServiceStatus, 'id'> & { id: null };
