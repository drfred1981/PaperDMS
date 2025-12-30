import dayjs from 'dayjs/esm';

import { IWorkflowInstance, NewWorkflowInstance } from './workflow-instance.model';

export const sampleWithRequiredData: IWorkflowInstance = {
  id: 29309,
  documentSha256: 'rowdy stump',
  startDate: dayjs('2025-12-29T07:20'),
  createdBy: 'electronics lovingly',
};

export const sampleWithPartialData: IWorkflowInstance = {
  id: 20587,
  documentSha256: 'fantastic opposite',
  currentStepNumber: 4799,
  startDate: dayjs('2025-12-29T08:23'),
  dueDate: dayjs('2025-12-29T12:03'),
  cancelledDate: dayjs('2025-12-29T17:28'),
  cancellationReason: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  createdBy: 'brown',
};

export const sampleWithFullData: IWorkflowInstance = {
  id: 21005,
  documentSha256: 'selfishly fixed',
  status: 'REJECTED',
  currentStepNumber: 18719,
  startDate: dayjs('2025-12-29T08:34'),
  dueDate: dayjs('2025-12-29T18:46'),
  completedDate: dayjs('2025-12-30T06:30'),
  cancelledDate: dayjs('2025-12-29T15:50'),
  cancellationReason: '../fake-data/blob/hipster.txt',
  priority: 'NORMAL',
  metadata: '../fake-data/blob/hipster.txt',
  createdBy: 'psst supposing massage',
};

export const sampleWithNewData: NewWorkflowInstance = {
  documentSha256: 'negligible severe ah',
  startDate: dayjs('2025-12-29T19:27'),
  createdBy: 'above tusk',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
