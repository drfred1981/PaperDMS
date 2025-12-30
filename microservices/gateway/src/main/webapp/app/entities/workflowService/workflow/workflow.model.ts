import dayjs from 'dayjs/esm';

export interface IWorkflow {
  id: number;
  name?: string | null;
  description?: string | null;
  version?: number | null;
  isActive?: boolean | null;
  isParallel?: boolean | null;
  autoStart?: boolean | null;
  triggerEvent?: string | null;
  configuration?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
}

export type NewWorkflow = Omit<IWorkflow, 'id'> & { id: null };
