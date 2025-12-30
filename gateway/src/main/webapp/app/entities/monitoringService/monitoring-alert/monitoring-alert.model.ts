import dayjs from 'dayjs/esm';
import { IMonitoringAlertRule } from 'app/entities/monitoringService/monitoring-alert-rule/monitoring-alert-rule.model';
import { Severity } from 'app/entities/enumerations/severity.model';
import { MonitoringAlertStatus } from 'app/entities/enumerations/monitoring-alert-status.model';

export interface IMonitoringAlert {
  id: number;
  severity?: keyof typeof Severity | null;
  title?: string | null;
  message?: string | null;
  entityType?: string | null;
  entityName?: string | null;
  status?: keyof typeof MonitoringAlertStatus | null;
  triggeredDate?: dayjs.Dayjs | null;
  acknowledgedBy?: string | null;
  acknowledgedDate?: dayjs.Dayjs | null;
  resolvedBy?: string | null;
  resolvedDate?: dayjs.Dayjs | null;
  alertRule?: Pick<IMonitoringAlertRule, 'id'> | null;
}

export type NewMonitoringAlert = Omit<IMonitoringAlert, 'id'> & { id: null };
