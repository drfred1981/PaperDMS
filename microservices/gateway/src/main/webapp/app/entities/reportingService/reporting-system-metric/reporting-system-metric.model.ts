import dayjs from 'dayjs/esm';

export interface IReportingSystemMetric {
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

export type NewReportingSystemMetric = Omit<IReportingSystemMetric, 'id'> & { id: null };
