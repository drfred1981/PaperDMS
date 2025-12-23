import dayjs from 'dayjs/esm';

export interface ISystemMetric {
  id: number;
  metricName?: string | null;
  cpuUsage?: number | null;
  memoryUsage?: number | null;
  diskUsage?: number | null;
  networkIn?: number | null;
  networkOut?: number | null;
  activeConnections?: number | null;
  timestamp?: dayjs.Dayjs | null;
}

export type NewSystemMetric = Omit<ISystemMetric, 'id'> & { id: null };
