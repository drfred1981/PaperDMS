import dayjs from 'dayjs/esm';
import { IWorkflowInstance } from 'app/entities/workflowService/workflow-instance/workflow-instance.model';
import { TaskAction } from 'app/entities/enumerations/task-action.model';

export interface IWorkflowApprovalHistory {
  id: number;
  documentSha256?: string | null;
  stepNumber?: number | null;
  action?: keyof typeof TaskAction | null;
  comment?: string | null;
  actionDate?: dayjs.Dayjs | null;
  actionBy?: string | null;
  previousAssignee?: string | null;
  timeTaken?: number | null;
  workflowInstance?: Pick<IWorkflowInstance, 'id'> | null;
}

export type NewWorkflowApprovalHistory = Omit<IWorkflowApprovalHistory, 'id'> & { id: null };
