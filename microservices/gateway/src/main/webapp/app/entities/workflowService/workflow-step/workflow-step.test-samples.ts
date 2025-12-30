import { IWorkflowStep, NewWorkflowStep } from './workflow-step.model';

export const sampleWithRequiredData: IWorkflowStep = {
  id: 25717,
  stepNumber: 30129,
  name: 'valiantly',
  isRequired: true,
  canDelegate: true,
  canReject: true,
};

export const sampleWithPartialData: IWorkflowStep = {
  id: 13693,
  stepNumber: 22159,
  name: 'forsaken worriedly even',
  isRequired: false,
  canDelegate: true,
  canReject: true,
};

export const sampleWithFullData: IWorkflowStep = {
  id: 30709,
  stepNumber: 25547,
  name: 'boo unless',
  description: '../fake-data/blob/hipster.txt',
  stepType: 'NOTIFICATION',
  assigneeType: 'GROUP',
  assigneeId: 'reward',
  assigneeGroup: 'unnecessarily along near',
  dueInDays: 26471,
  isRequired: false,
  canDelegate: true,
  canReject: true,
  configuration: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewWorkflowStep = {
  stepNumber: 2533,
  name: 'throughout nervous beyond',
  isRequired: true,
  canDelegate: true,
  canReject: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
