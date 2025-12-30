import dayjs from 'dayjs/esm';

import { INotificationPreference, NewNotificationPreference } from './notification-preference.model';

export const sampleWithRequiredData: INotificationPreference = {
  id: 22673,
  userId: 'distorted',
  emailEnabled: true,
  pushEnabled: true,
  inAppEnabled: true,
};

export const sampleWithPartialData: INotificationPreference = {
  id: 30436,
  userId: 'phooey but cuckoo',
  emailEnabled: true,
  pushEnabled: false,
  inAppEnabled: false,
  notificationTypes: '../fake-data/blob/hipster.txt',
  quietHoursStart: 'gosh ',
  quietHoursEnd: 'over',
  frequency: 'HOURLY',
};

export const sampleWithFullData: INotificationPreference = {
  id: 15812,
  userId: 'hence uh-huh',
  emailEnabled: true,
  pushEnabled: false,
  inAppEnabled: false,
  notificationTypes: '../fake-data/blob/hipster.txt',
  quietHoursStart: 'in an',
  quietHoursEnd: 'aside',
  frequency: 'IMMEDIATELY',
  lastModifiedDate: dayjs('2025-12-29T11:18'),
};

export const sampleWithNewData: NewNotificationPreference = {
  userId: 'tenderly amend hippodrome',
  emailEnabled: true,
  pushEnabled: true,
  inAppEnabled: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
