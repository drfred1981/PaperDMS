import dayjs from 'dayjs/esm';
import { IScheduledReport } from 'app/entities/reportingService/scheduled-report/scheduled-report.model';
import { ReportExecutionStatus } from 'app/entities/enumerations/report-execution-status.model';

export interface IReportExecution {
  id: number;
  scheduledReportId?: number | null;
  status?: keyof typeof ReportExecutionStatus | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  recordsProcessed?: number | null;
  outputS3Key?: string | null;
  outputSize?: number | null;
  errorMessage?: string | null;
  scheduledReport?: Pick<IScheduledReport, 'id'> | null;
}

export type NewReportExecution = Omit<IReportExecution, 'id'> & { id: null };
