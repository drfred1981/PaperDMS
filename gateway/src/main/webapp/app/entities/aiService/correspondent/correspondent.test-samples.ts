import dayjs from 'dayjs/esm';

import { ICorrespondent, NewCorrespondent } from './correspondent.model';

export const sampleWithRequiredData: ICorrespondent = {
  id: 16093,
  name: 'meanwhile',
  isVerified: false,
  extractedDate: dayjs('2025-12-25T08:28'),
};

export const sampleWithPartialData: ICorrespondent = {
  id: 4238,
  name: 'nauseate',
  address: '../fake-data/blob/hipster.txt',
  company: 'frenetically corny attraction',
  type: 'SYSTEM',
  isVerified: true,
  metadata: '../fake-data/blob/hipster.txt',
  extractedDate: dayjs('2025-12-24T13:17'),
};

export const sampleWithFullData: ICorrespondent = {
  id: 8623,
  name: 'alongside lox',
  email: 'Ruth.Roob@yahoo.com',
  phone: '253.293.4438 x246',
  address: '../fake-data/blob/hipster.txt',
  company: 'pro',
  type: 'ORGANIZATION',
  role: 'SENDER',
  confidence: 0.09,
  isVerified: true,
  verifiedBy: 'breakable appropriate dimly',
  verifiedDate: dayjs('2025-12-24T12:40'),
  metadata: '../fake-data/blob/hipster.txt',
  extractedDate: dayjs('2025-12-25T06:01'),
};

export const sampleWithNewData: NewCorrespondent = {
  name: 'consequently',
  isVerified: false,
  extractedDate: dayjs('2025-12-24T15:00'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
