import dayjs from 'dayjs/esm';

import { ServiceStatusType } from 'app/entities/enumerations/service-status-type.model';

export interface IServiceStatus {
  id: number;
  serviceName?: string | null;
  serviceType?: string | null;
  status?: keyof typeof ServiceStatusType | null;
  endpoint?: string | null;
  port?: number | null;
  version?: string | null;
  lastPing?: dayjs.Dayjs | null;
  isHealthy?: boolean | null;
}

export type NewServiceStatus = Omit<IServiceStatus, 'id'> & { id: null };
