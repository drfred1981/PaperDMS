import dayjs from 'dayjs/esm';

import { IWorkflowTask, NewWorkflowTask } from './workflow-task.model';

export const sampleWithRequiredData: IWorkflowTask = {
  id: 7925,
  assigneeId: 'interestingly anenst out',
  assignedDate: dayjs('2025-12-19T17:51'),
  reminderSent: false,
};

export const sampleWithPartialData: IWorkflowTask = {
  id: 1244,
  assigneeId: 'amongst wherever when',
  status: 'CANCELLED',
  action: 'REJECT',
  assignedDate: dayjs('2025-12-19T20:54'),
  dueDate: dayjs('2025-12-19T20:47'),
  reminderSent: false,
  delegatedTo: 'annually',
  delegatedDate: dayjs('2025-12-19T17:16'),
};

export const sampleWithFullData: IWorkflowTask = {
  id: 23245,
  assigneeId: 'masquerade pike',
  status: 'IN_PROGRESS',
  action: 'DELEGATE',
  comment: '../fake-data/blob/hipster.txt',
  assignedDate: dayjs('2025-12-19T23:23'),
  dueDate: dayjs('2025-12-19T19:26'),
  completedDate: dayjs('2025-12-20T08:26'),
  reminderSent: false,
  delegatedTo: 'lamp which baritone',
  delegatedDate: dayjs('2025-12-20T04:01'),
};

export const sampleWithNewData: NewWorkflowTask = {
  assigneeId: 'phooey after',
  assignedDate: dayjs('2025-12-20T15:25'),
  reminderSent: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
