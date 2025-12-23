import dayjs from 'dayjs/esm';

import { ReportFormat } from 'app/entities/enumerations/report-format.model';
import { ReportType } from 'app/entities/enumerations/report-type.model';

export interface IScheduledReport {
  id: number;
  name?: string | null;
  description?: string | null;
  reportType?: keyof typeof ReportType | null;
  query?: string | null;
  schedule?: string | null;
  format?: keyof typeof ReportFormat | null;
  recipients?: string | null;
  isActive?: boolean | null;
  lastRun?: dayjs.Dayjs | null;
  nextRun?: dayjs.Dayjs | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewScheduledReport = Omit<IScheduledReport, 'id'> & { id: null };
