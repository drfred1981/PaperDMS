import dayjs from 'dayjs/esm';
import { ReportType } from 'app/entities/enumerations/report-type.model';
import { ReportFormat } from 'app/entities/enumerations/report-format.model';

export interface IReportingScheduledReport {
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

export type NewReportingScheduledReport = Omit<IReportingScheduledReport, 'id'> & { id: null };
