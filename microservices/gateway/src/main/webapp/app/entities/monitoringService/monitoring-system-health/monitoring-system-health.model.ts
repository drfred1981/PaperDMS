import dayjs from 'dayjs/esm';
import { HealthStatus } from 'app/entities/enumerations/health-status.model';

export interface IMonitoringSystemHealth {
  id: number;
  serviceName?: string | null;
  status?: keyof typeof HealthStatus | null;
  version?: string | null;
  uptime?: number | null;
  cpuUsage?: number | null;
  memoryUsage?: number | null;
  errorRate?: number | null;
  lastCheck?: dayjs.Dayjs | null;
}

export type NewMonitoringSystemHealth = Omit<IMonitoringSystemHealth, 'id'> & { id: null };
