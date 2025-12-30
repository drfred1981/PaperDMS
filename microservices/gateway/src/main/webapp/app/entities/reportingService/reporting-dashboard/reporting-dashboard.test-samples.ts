import dayjs from 'dayjs/esm';

import { IReportingDashboard, NewReportingDashboard } from './reporting-dashboard.model';

export const sampleWithRequiredData: IReportingDashboard = {
  id: 12856,
  name: 'harmful sunder mmm',
  isPublic: true,
  layout: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2025-12-30T06:33'),
};

export const sampleWithPartialData: IReportingDashboard = {
  id: 24904,
  name: 'flimsy till for',
  description: '../fake-data/blob/hipster.txt',
  userId: 'pish furthermore',
  isPublic: false,
  layout: '../fake-data/blob/hipster.txt',
  isDefault: true,
  createdDate: dayjs('2025-12-29T08:25'),
};

export const sampleWithFullData: IReportingDashboard = {
  id: 25402,
  name: 'underpants clinking amidst',
  description: '../fake-data/blob/hipster.txt',
  userId: 'warlike foolhardy',
  isPublic: true,
  layout: '../fake-data/blob/hipster.txt',
  refreshInterval: 22239,
  isDefault: true,
  createdDate: dayjs('2025-12-30T04:04'),
};

export const sampleWithNewData: NewReportingDashboard = {
  name: 'ha hmph',
  isPublic: true,
  layout: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2025-12-29T21:17'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
