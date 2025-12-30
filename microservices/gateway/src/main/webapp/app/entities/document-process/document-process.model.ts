import { WorkflowInstanceStatus } from 'app/entities/enumerations/workflow-instance-status.model';

export interface IDocumentProcess {
  id: number;
  status?: keyof typeof WorkflowInstanceStatus | null;
  documentSha256?: string | null;
}

export type NewDocumentProcess = Omit<IDocumentProcess, 'id'> & { id: null };
