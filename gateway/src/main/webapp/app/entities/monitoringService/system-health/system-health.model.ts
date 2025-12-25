import dayjs from 'dayjs/esm';
import { HealthStatus } from 'app/entities/enumerations/health-status.model';

export interface ISystemHealth {
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

export type NewSystemHealth = Omit<ISystemHealth, 'id'> & { id: null };
