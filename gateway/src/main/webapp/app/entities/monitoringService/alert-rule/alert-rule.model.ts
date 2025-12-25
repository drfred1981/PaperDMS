import dayjs from 'dayjs/esm';
import { AlertType } from 'app/entities/enumerations/alert-type.model';
import { Severity } from 'app/entities/enumerations/severity.model';

export interface IAlertRule {
  id: number;
  name?: string | null;
  description?: string | null;
  alertType?: keyof typeof AlertType | null;
  conditions?: string | null;
  severity?: keyof typeof Severity | null;
  recipients?: string | null;
  isActive?: boolean | null;
  triggerCount?: number | null;
  lastTriggered?: dayjs.Dayjs | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewAlertRule = Omit<IAlertRule, 'id'> & { id: null };
