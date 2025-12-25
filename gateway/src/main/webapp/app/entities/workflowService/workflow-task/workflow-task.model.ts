import dayjs from 'dayjs/esm';
import { IWorkflowInstance } from 'app/entities/workflowService/workflow-instance/workflow-instance.model';
import { IWorkflowStep } from 'app/entities/workflowService/workflow-step/workflow-step.model';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';
import { TaskAction } from 'app/entities/enumerations/task-action.model';

export interface IWorkflowTask {
  id: number;
  assigneeId?: string | null;
  status?: keyof typeof TaskStatus | null;
  action?: keyof typeof TaskAction | null;
  comment?: string | null;
  assignedDate?: dayjs.Dayjs | null;
  dueDate?: dayjs.Dayjs | null;
  completedDate?: dayjs.Dayjs | null;
  reminderSent?: boolean | null;
  delegatedTo?: string | null;
  delegatedDate?: dayjs.Dayjs | null;
  instance?: Pick<IWorkflowInstance, 'id'> | null;
  step?: Pick<IWorkflowStep, 'id'> | null;
}

export type NewWorkflowTask = Omit<IWorkflowTask, 'id'> & { id: null };
