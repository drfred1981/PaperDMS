import dayjs from 'dayjs/esm';

import { IServiceStatus, NewServiceStatus } from './service-status.model';

export const sampleWithRequiredData: IServiceStatus = {
  id: 7772,
  serviceName: 'although unlike',
  status: 'RUNNING',
  isHealthy: false,
};

export const sampleWithPartialData: IServiceStatus = {
  id: 4953,
  serviceName: 'ouch ouch',
  status: 'STOPPED',
  port: 31911,
  lastPing: dayjs('2025-12-20T13:57'),
  isHealthy: false,
};

export const sampleWithFullData: IServiceStatus = {
  id: 16553,
  serviceName: 'about until taxicab',
  serviceType: 'kiddingly',
  status: 'MAINTENANCE',
  endpoint: 'aw hmph',
  port: 23453,
  version: 'worldly behold',
  lastPing: dayjs('2025-12-20T01:07'),
  isHealthy: false,
};

export const sampleWithNewData: NewServiceStatus = {
  serviceName: 'gown bravely',
  status: 'RUNNING',
  isHealthy: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
