import dayjs from 'dayjs/esm';

import { IMonitoringServiceStatus, NewMonitoringServiceStatus } from './monitoring-service-status.model';

export const sampleWithRequiredData: IMonitoringServiceStatus = {
  id: 27450,
  serviceName: 'narrow',
  status: 'STOPPED',
  isHealthy: true,
};

export const sampleWithPartialData: IMonitoringServiceStatus = {
  id: 28385,
  serviceName: 'including continually analyse',
  serviceType: 'conceal nightlife',
  status: 'STOPPING',
  endpoint: 'why westernise',
  version: 'easily authentic bowed',
  isHealthy: false,
};

export const sampleWithFullData: IMonitoringServiceStatus = {
  id: 7892,
  serviceName: 'profuse beyond upon',
  serviceType: 'softly',
  status: 'RUNNING',
  endpoint: 'times what whose',
  port: 4159,
  version: 'mooch frightfully',
  lastPing: dayjs('2025-12-29T13:25'),
  isHealthy: true,
};

export const sampleWithNewData: NewMonitoringServiceStatus = {
  serviceName: 'hmph slowly',
  status: 'RUNNING',
  isHealthy: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
