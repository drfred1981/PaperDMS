import dayjs from 'dayjs/esm';

import { IWorkflowInstance, NewWorkflowInstance } from './workflow-instance.model';

export const sampleWithRequiredData: IWorkflowInstance = {
  id: 29309,
  documentId: 21351,
  startDate: dayjs('2025-12-24T19:48'),
  createdBy: 'unwelcome gastropod',
};

export const sampleWithPartialData: IWorkflowInstance = {
  id: 20587,
  documentId: 17685,
  currentStepNumber: 5976,
  startDate: dayjs('2025-12-24T20:25'),
  dueDate: dayjs('2025-12-24T18:22'),
  cancelledDate: dayjs('2025-12-25T10:10'),
  cancellationReason: '../fake-data/blob/hipster.txt',
  metadata: '../fake-data/blob/hipster.txt',
  createdBy: 'demonstrate deliberately brown',
};

export const sampleWithFullData: IWorkflowInstance = {
  id: 21005,
  documentId: 11488,
  status: 'COMPLETED',
  currentStepNumber: 14536,
  startDate: dayjs('2025-12-25T07:55'),
  dueDate: dayjs('2025-12-24T15:03'),
  completedDate: dayjs('2025-12-25T06:58'),
  cancelledDate: dayjs('2025-12-24T19:54'),
  cancellationReason: '../fake-data/blob/hipster.txt',
  priority: 'HIGH',
  metadata: '../fake-data/blob/hipster.txt',
  createdBy: 'whenever skyscraper psst',
};

export const sampleWithNewData: NewWorkflowInstance = {
  documentId: 27202,
  startDate: dayjs('2025-12-24T17:33'),
  createdBy: 'straw esteemed',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
