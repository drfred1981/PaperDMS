import dayjs from 'dayjs/esm';
import { MonitoringAlertType } from 'app/entities/enumerations/monitoring-alert-type.model';
import { Severity } from 'app/entities/enumerations/severity.model';

export interface IMonitoringAlertRule {
  id: number;
  name?: string | null;
  description?: string | null;
  alertType?: keyof typeof MonitoringAlertType | null;
  conditions?: string | null;
  severity?: keyof typeof Severity | null;
  recipients?: string | null;
  isActive?: boolean | null;
  triggerCount?: number | null;
  lastTriggered?: dayjs.Dayjs | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewMonitoringAlertRule = Omit<IMonitoringAlertRule, 'id'> & { id: null };
