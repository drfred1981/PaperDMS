import dayjs from 'dayjs/esm';

export interface IReportingDashboard {
  id: number;
  name?: string | null;
  description?: string | null;
  userId?: string | null;
  isPublic?: boolean | null;
  layout?: string | null;
  refreshInterval?: number | null;
  isDefault?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewReportingDashboard = Omit<IReportingDashboard, 'id'> & { id: null };
