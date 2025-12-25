import dayjs from 'dayjs/esm';

import { IApprovalHistory, NewApprovalHistory } from './approval-history.model';

export const sampleWithRequiredData: IApprovalHistory = {
  id: 14507,
  documentId: 19862,
  workflowInstanceId: 14512,
  stepNumber: 12560,
  actionDate: dayjs('2025-12-24T20:07'),
  actionBy: 'inasmuch more',
};

export const sampleWithPartialData: IApprovalHistory = {
  id: 1229,
  documentId: 27810,
  workflowInstanceId: 8753,
  stepNumber: 32237,
  actionDate: dayjs('2025-12-25T02:43'),
  actionBy: 'mainstream',
  previousAssignee: 'although nifty well-made',
  timeTaken: 670,
};

export const sampleWithFullData: IApprovalHistory = {
  id: 18068,
  documentId: 8958,
  workflowInstanceId: 12788,
  stepNumber: 26251,
  action: 'REQUEST_CHANGES',
  comment: '../fake-data/blob/hipster.txt',
  actionDate: dayjs('2025-12-25T00:21'),
  actionBy: 'vamoose depute',
  previousAssignee: 'salty linseed',
  timeTaken: 32505,
};

export const sampleWithNewData: NewApprovalHistory = {
  documentId: 14867,
  workflowInstanceId: 21183,
  stepNumber: 21976,
  actionDate: dayjs('2025-12-24T19:41'),
  actionBy: 'sneak immediately',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
