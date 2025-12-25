import dayjs from 'dayjs/esm';
import { MaintenanceType } from 'app/entities/enumerations/maintenance-type.model';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';

export interface IMaintenanceTask {
  id: number;
  name?: string | null;
  description?: string | null;
  taskType?: keyof typeof MaintenanceType | null;
  schedule?: string | null;
  status?: keyof typeof TransformStatus | null;
  isActive?: boolean | null;
  lastRun?: dayjs.Dayjs | null;
  nextRun?: dayjs.Dayjs | null;
  duration?: number | null;
  recordsProcessed?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewMaintenanceTask = Omit<IMaintenanceTask, 'id'> & { id: null };
