import dayjs from 'dayjs/esm';
import { MonitoringServiceStatusType } from 'app/entities/enumerations/monitoring-service-status-type.model';

export interface IMonitoringServiceStatus {
  id: number;
  serviceName?: string | null;
  serviceType?: string | null;
  status?: keyof typeof MonitoringServiceStatusType | null;
  endpoint?: string | null;
  port?: number | null;
  version?: string | null;
  lastPing?: dayjs.Dayjs | null;
  isHealthy?: boolean | null;
}

export type NewMonitoringServiceStatus = Omit<IMonitoringServiceStatus, 'id'> & { id: null };
