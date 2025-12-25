import dayjs from 'dayjs/esm';
import { MetricType } from 'app/entities/enumerations/metric-type.model';

export interface IPerformanceMetric {
  id: number;
  metricName?: string | null;
  metricType?: keyof typeof MetricType | null;
  value?: number | null;
  unit?: string | null;
  serviceName?: string | null;
  timestamp?: dayjs.Dayjs | null;
}

export type NewPerformanceMetric = Omit<IPerformanceMetric, 'id'> & { id: null };
