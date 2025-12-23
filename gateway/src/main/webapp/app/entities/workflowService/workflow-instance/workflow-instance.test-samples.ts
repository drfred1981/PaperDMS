import dayjs from 'dayjs/esm';

import { IWorkflowInstance, NewWorkflowInstance } from './workflow-instance.model';

export const sampleWithRequiredData: IWorkflowInstance = {
  id: 29309,
  documentId: 21351,
  startDate: dayjs('2025-12-20T00:49'),
  createdBy: 'unwelcome gastropod',
};

export const sampleWithPartialData: IWorkflowInstance = {
  id: 20587,
  documentId: 17685,
  currentStepNumber: 5976,
  startDate: dayjs('2025-12-20T01:26'),
  dueDate: dayjs('2025-12-19T23:23'),
  cancelledDate: dayjs('2025-12-20T15:11'),
  cancellationReason: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  createdBy: 'demonstrate deliberately brown',
};

export const sampleWithFullData: IWorkflowInstance = {
  id: 21005,
  documentId: 11488,
  status: 'COMPLETED',
  currentStepNumber: 14536,
  startDate: dayjs('2025-12-20T12:56'),
  dueDate: dayjs('2025-12-19T20:04'),
  completedDate: dayjs('2025-12-20T11:59'),
  cancelledDate: dayjs('2025-12-20T00:55'),
  cancellationReason: '../fake-data/blob/hipster.txt',
  priority: 'HIGH',
  metadata: '../fake-data/blob/hipster.txt',
  createdBy: 'whenever skyscraper psst',
};

export const sampleWithNewData: NewWorkflowInstance = {
  documentId: 27202,
  startDate: dayjs('2025-12-19T22:34'),
  createdBy: 'straw esteemed',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
