import dayjs from 'dayjs/esm';

import { ICorrespondent, NewCorrespondent } from './correspondent.model';

export const sampleWithRequiredData: ICorrespondent = {
  id: 16093,
  name: 'meanwhile',
  isVerified: false,
  extractedDate: dayjs('2025-12-20T13:29'),
};

export const sampleWithPartialData: ICorrespondent = {
  id: 4238,
  name: 'nauseate',
  address: '../fake-data/blob/hipster.txt',
  company: 'frenetically corny attraction',
  type: 'SYSTEM',
  isVerified: true,
  metadata: '../fake-data/blob/hipster.txt',
  extractedDate: dayjs('2025-12-19T18:18'),
};

export const sampleWithFullData: ICorrespondent = {
  id: 8623,
  name: 'alongside lox',
  email: 'Ruth.Roob@hotmail.com',
  phone: '253.293.4438 x246',
  address: '../fake-data/blob/hipster.txt',
  company: 'pro',
  type: 'ORGANIZATION',
  role: 'SENDER',
  confidence: 0.09,
  isVerified: true,
  verifiedBy: 'breakable appropriate dimly',
  verifiedDate: dayjs('2025-12-19T17:41'),
  metadata: '../fake-data/blob/hipster.txt',
  extractedDate: dayjs('2025-12-20T11:02'),
};

export const sampleWithNewData: NewCorrespondent = {
  name: 'consequently',
  isVerified: false,
  extractedDate: dayjs('2025-12-19T20:01'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
