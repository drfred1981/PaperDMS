import dayjs from 'dayjs/esm';

import { AlertStatus } from 'app/entities/enumerations/alert-status.model';
import { Severity } from 'app/entities/enumerations/severity.model';
import { IAlertRule } from 'app/entities/monitoringService/alert-rule/alert-rule.model';

export interface IAlert {
  id: number;
  alertRuleId?: number | null;
  severity?: keyof typeof Severity | null;
  title?: string | null;
  message?: string | null;
  entityType?: string | null;
  entityId?: number | null;
  status?: keyof typeof AlertStatus | null;
  triggeredDate?: dayjs.Dayjs | null;
  acknowledgedBy?: string | null;
  acknowledgedDate?: dayjs.Dayjs | null;
  resolvedBy?: string | null;
  resolvedDate?: dayjs.Dayjs | null;
  alertRule?: Pick<IAlertRule, 'id'> | null;
}

export type NewAlert = Omit<IAlert, 'id'> & { id: null };
