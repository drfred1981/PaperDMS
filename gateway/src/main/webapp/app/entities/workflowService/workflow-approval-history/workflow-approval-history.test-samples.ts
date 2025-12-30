import dayjs from 'dayjs/esm';

import { IWorkflowApprovalHistory, NewWorkflowApprovalHistory } from './workflow-approval-history.model';

export const sampleWithRequiredData: IWorkflowApprovalHistory = {
  id: 17162,
  documentSha256: 'impanel silt insolence',
  stepNumber: 24566,
  actionDate: dayjs('2025-12-29T08:38'),
  actionBy: 'silky unless',
};

export const sampleWithPartialData: IWorkflowApprovalHistory = {
  id: 10026,
  documentSha256: 'when',
  stepNumber: 29587,
  comment: '../fake-data/blob/hipster.txt',
  actionDate: dayjs('2025-12-30T02:18'),
  actionBy: 'but ajar huzzah',
  timeTaken: 4913,
};

export const sampleWithFullData: IWorkflowApprovalHistory = {
  id: 2729,
  documentSha256: 'except glossy polyester',
  stepNumber: 13629,
  action: 'COMMENT',
  comment: '../fake-data/blob/hipster.txt',
  actionDate: dayjs('2025-12-29T20:58'),
  actionBy: 'bashfully per outside',
  previousAssignee: 'fine',
  timeTaken: 10891,
};

export const sampleWithNewData: NewWorkflowApprovalHistory = {
  documentSha256: 'bookend midst',
  stepNumber: 25987,
  actionDate: dayjs('2025-12-29T15:46'),
  actionBy: 'overspend',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
