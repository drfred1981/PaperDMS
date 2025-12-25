import dayjs from 'dayjs/esm';
import { IWorkflow } from 'app/entities/workflowService/workflow/workflow.model';
import { WorkflowInstanceStatus } from 'app/entities/enumerations/workflow-instance-status.model';
import { WorkflowPriority } from 'app/entities/enumerations/workflow-priority.model';

export interface IWorkflowInstance {
  id: number;
  documentId?: number | null;
  status?: keyof typeof WorkflowInstanceStatus | null;
  currentStepNumber?: number | null;
  startDate?: dayjs.Dayjs | null;
  dueDate?: dayjs.Dayjs | null;
  completedDate?: dayjs.Dayjs | null;
  cancelledDate?: dayjs.Dayjs | null;
  cancellationReason?: string | null;
  priority?: keyof typeof WorkflowPriority | null;
  metadata?: string | null;
  createdBy?: string | null;
  workflow?: Pick<IWorkflow, 'id'> | null;
}

export type NewWorkflowInstance = Omit<IWorkflowInstance, 'id'> & { id: null };
