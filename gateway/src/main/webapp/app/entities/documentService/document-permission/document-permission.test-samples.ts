import dayjs from 'dayjs/esm';

import { IDocumentPermission, NewDocumentPermission } from './document-permission.model';

export const sampleWithRequiredData: IDocumentPermission = {
  id: 11149,
  documentId: 14054,
  principalType: 'PUBLIC',
  principalId: 'lyre whirlwind',
  permission: 'DOWNLOAD',
  canDelegate: false,
  grantedBy: 'amid off fax',
  grantedDate: dayjs('2025-12-19T20:06'),
};

export const sampleWithPartialData: IDocumentPermission = {
  id: 2610,
  documentId: 22337,
  principalType: 'GROUP',
  principalId: 'deceivingly tasty',
  permission: 'DOWNLOAD',
  canDelegate: true,
  grantedBy: 'drat physically oh',
  grantedDate: dayjs('2025-12-20T12:55'),
};

export const sampleWithFullData: IDocumentPermission = {
  id: 22089,
  documentId: 31082,
  principalType: 'DEPARTMENT',
  principalId: 'furthermore smuggle ouch',
  permission: 'MANAGE_PERMISSIONS',
  canDelegate: true,
  grantedBy: 'repeatedly',
  grantedDate: dayjs('2025-12-20T07:12'),
};

export const sampleWithNewData: NewDocumentPermission = {
  documentId: 22117,
  principalType: 'DEPARTMENT',
  principalId: 'cod digestive collectivization',
  permission: 'APPROVE',
  canDelegate: false,
  grantedBy: 'self-reliant pivot effector',
  grantedDate: dayjs('2025-12-19T18:37'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
