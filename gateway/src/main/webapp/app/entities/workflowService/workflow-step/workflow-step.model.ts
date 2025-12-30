import { IWorkflow } from 'app/entities/workflowService/workflow/workflow.model';
import { WorkflowStepType } from 'app/entities/enumerations/workflow-step-type.model';
import { AssigneeType } from 'app/entities/enumerations/assignee-type.model';

export interface IWorkflowStep {
  id: number;
  stepNumber?: number | null;
  name?: string | null;
  description?: string | null;
  stepType?: keyof typeof WorkflowStepType | null;
  assigneeType?: keyof typeof AssigneeType | null;
  assigneeId?: string | null;
  assigneeGroup?: string | null;
  dueInDays?: number | null;
  isRequired?: boolean | null;
  canDelegate?: boolean | null;
  canReject?: boolean | null;
  configuration?: string | null;
  workflow?: Pick<IWorkflow, 'id'> | null;
}

export type NewWorkflowStep = Omit<IWorkflowStep, 'id'> & { id: null };
