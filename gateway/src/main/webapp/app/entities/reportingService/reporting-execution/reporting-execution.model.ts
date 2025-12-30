import dayjs from 'dayjs/esm';
import { IReportingScheduledReport } from 'app/entities/reportingService/reporting-scheduled-report/reporting-scheduled-report.model';
import { ReportingExecutionStatus } from 'app/entities/enumerations/reporting-execution-status.model';

export interface IReportingExecution {
  id: number;
  status?: keyof typeof ReportingExecutionStatus | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  recordsProcessed?: number | null;
  outputS3Key?: string | null;
  outputSize?: number | null;
  errorMessage?: string | null;
  scheduledReport?: Pick<IReportingScheduledReport, 'id'> | null;
}

export type NewReportingExecution = Omit<IReportingExecution, 'id'> & { id: null };
