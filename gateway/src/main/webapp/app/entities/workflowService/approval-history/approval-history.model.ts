import dayjs from 'dayjs/esm';

import { TaskAction } from 'app/entities/enumerations/task-action.model';

export interface IApprovalHistory {
  id: number;
  documentId?: number | null;
  workflowInstanceId?: number | null;
  stepNumber?: number | null;
  action?: keyof typeof TaskAction | null;
  comment?: string | null;
  actionDate?: dayjs.Dayjs | null;
  actionBy?: string | null;
  previousAssignee?: string | null;
  timeTaken?: number | null;
}

export type NewApprovalHistory = Omit<IApprovalHistory, 'id'> & { id: null };
