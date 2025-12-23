import dayjs from 'dayjs/esm';

import { IContract, NewContract } from './contract.model';

export const sampleWithRequiredData: IContract = {
  id: 1607,
  documentId: 20532,
  contractNumber: 'yippee',
  contractType: 'PURCHASE_CONTRACT',
  title: 'an',
  partyA: 'mosh',
  partyB: 'thoughtfully glossy',
  startDate: dayjs('2025-12-19'),
  autoRenew: true,
  status: 'DRAFT',
  createdDate: dayjs('2025-12-20T12:04'),
};

export const sampleWithPartialData: IContract = {
  id: 20671,
  documentId: 18942,
  contractNumber: 'against bandwidth',
  contractType: 'OTHER',
  title: 'including or',
  partyA: 'excepting and once',
  partyB: 'whose',
  startDate: dayjs('2025-12-20'),
  autoRenew: true,
  currency: 'ouc',
  status: 'ARCHIVED',
  createdDate: dayjs('2025-12-20T15:23'),
};

export const sampleWithFullData: IContract = {
  id: 9674,
  documentId: 30242,
  contractNumber: 'absent phooey',
  contractType: 'OTHER',
  title: 'smug before',
  partyA: 'functional',
  partyB: 'corral',
  startDate: dayjs('2025-12-19'),
  endDate: dayjs('2025-12-20'),
  autoRenew: false,
  contractValue: 29597.6,
  currency: 'gol',
  status: 'DRAFT',
  createdDate: dayjs('2025-12-20T02:38'),
};

export const sampleWithNewData: NewContract = {
  documentId: 7089,
  contractNumber: 'finally lampoon gadzooks',
  contractType: 'LICENSE',
  title: 'bad hospitalization limited',
  partyA: 'meanwhile zowie pish',
  partyB: 'braid yowza regarding',
  startDate: dayjs('2025-12-20'),
  autoRenew: false,
  status: 'RENEWED',
  createdDate: dayjs('2025-12-20T01:54'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
