import dayjs from 'dayjs/esm';

import { IDocumentPermission, NewDocumentPermission } from './document-permission.model';

export const sampleWithRequiredData: IDocumentPermission = {
  id: 11149,
  principalType: 'ROLE',
  principalId: 'boulevard solicit',
  permission: 'FULL_CONTROL',
  canDelegate: true,
  grantedBy: 'luck tabletop beloved',
  grantedDate: dayjs('2025-12-29T14:22'),
};

export const sampleWithPartialData: IDocumentPermission = {
  id: 2610,
  principalType: 'PUBLIC',
  principalId: 'forceful',
  permission: 'DOWNLOAD',
  canDelegate: false,
  grantedBy: 'more',
  grantedDate: dayjs('2025-12-29T23:40'),
};

export const sampleWithFullData: IDocumentPermission = {
  id: 22089,
  principalType: 'DEPARTMENT',
  principalId: 'frenetically midst whoever',
  permission: 'COMMENT',
  canDelegate: false,
  grantedBy: 'irresponsible',
  grantedDate: dayjs('2025-12-29T22:37'),
};

export const sampleWithNewData: NewDocumentPermission = {
  principalType: 'PUBLIC',
  principalId: 'fooey scorn bracelet',
  permission: 'DOWNLOAD',
  canDelegate: true,
  grantedBy: 'which redress doting',
  grantedDate: dayjs('2025-12-29T13:32'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
