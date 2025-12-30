import dayjs from 'dayjs/esm';

import { IWorkflowTask, NewWorkflowTask } from './workflow-task.model';

export const sampleWithRequiredData: IWorkflowTask = {
  id: 7925,
  assigneeId: 'interestingly anenst out',
  assignedDate: dayjs('2025-12-29T08:56'),
  reminderSent: false,
};

export const sampleWithPartialData: IWorkflowTask = {
  id: 1244,
  assigneeId: 'amongst wherever when',
  status: 'CANCELLED',
  action: 'REJECT',
  assignedDate: dayjs('2025-12-29T11:59'),
  dueDate: dayjs('2025-12-29T11:52'),
  reminderSent: false,
  delegatedTo: 'annually',
  delegatedDate: dayjs('2025-12-29T08:21'),
};

export const sampleWithFullData: IWorkflowTask = {
  id: 23245,
  assigneeId: 'masquerade pike',
  status: 'IN_PROGRESS',
  action: 'DELEGATE',
  comment: '../fake-data/blob/hipster.txt',
  assignedDate: dayjs('2025-12-29T14:28'),
  dueDate: dayjs('2025-12-29T10:31'),
  completedDate: dayjs('2025-12-29T23:31'),
  reminderSent: false,
  delegatedTo: 'lamp which baritone',
  delegatedDate: dayjs('2025-12-29T19:06'),
};

export const sampleWithNewData: NewWorkflowTask = {
  assigneeId: 'phooey after',
  assignedDate: dayjs('2025-12-30T06:30'),
  reminderSent: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
