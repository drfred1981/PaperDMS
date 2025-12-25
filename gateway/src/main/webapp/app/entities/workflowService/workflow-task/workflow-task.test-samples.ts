import dayjs from 'dayjs/esm';

import { IWorkflowTask, NewWorkflowTask } from './workflow-task.model';

export const sampleWithRequiredData: IWorkflowTask = {
  id: 7925,
  assigneeId: 'interestingly anenst out',
  assignedDate: dayjs('2025-12-24T12:50'),
  reminderSent: false,
};

export const sampleWithPartialData: IWorkflowTask = {
  id: 1244,
  assigneeId: 'amongst wherever when',
  status: 'CANCELLED',
  action: 'REJECT',
  assignedDate: dayjs('2025-12-24T15:53'),
  dueDate: dayjs('2025-12-24T15:46'),
  reminderSent: false,
  delegatedTo: 'annually',
  delegatedDate: dayjs('2025-12-24T12:15'),
};

export const sampleWithFullData: IWorkflowTask = {
  id: 23245,
  assigneeId: 'masquerade pike',
  status: 'IN_PROGRESS',
  action: 'DELEGATE',
  comment: '../fake-data/blob/hipster.txt',
  assignedDate: dayjs('2025-12-24T18:22'),
  dueDate: dayjs('2025-12-24T14:25'),
  completedDate: dayjs('2025-12-25T03:25'),
  reminderSent: false,
  delegatedTo: 'lamp which baritone',
  delegatedDate: dayjs('2025-12-24T23:00'),
};

export const sampleWithNewData: NewWorkflowTask = {
  assigneeId: 'phooey after',
  assignedDate: dayjs('2025-12-25T10:24'),
  reminderSent: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
