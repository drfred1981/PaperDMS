import dayjs from 'dayjs/esm';

import { IMaintenanceTask, NewMaintenanceTask } from './maintenance-task.model';

export const sampleWithRequiredData: IMaintenanceTask = {
  id: 629,
  name: 'intensely even',
  taskType: 'BACKUP_DATABASE',
  schedule: 'an cappelletti',
  status: 'PENDING',
  isActive: true,
  createdBy: 'embarrassment headline majestic',
  createdDate: dayjs('2025-12-25T08:23'),
};

export const sampleWithPartialData: IMaintenanceTask = {
  id: 6636,
  name: 'steak',
  taskType: 'ROTATE_LOGS',
  schedule: 'nectarine helpfully',
  status: 'FAILED',
  isActive: false,
  lastRun: dayjs('2025-12-25T06:21'),
  duration: 9731,
  recordsProcessed: 15907,
  createdBy: 'norm',
  createdDate: dayjs('2025-12-24T12:09'),
};

export const sampleWithFullData: IMaintenanceTask = {
  id: 30685,
  name: 'though smoothly yuck',
  description: '../fake-data/blob/hipster.txt',
  taskType: 'EXPIRE_TEMP_FILES',
  schedule: 'tremendously',
  status: 'FAILED',
  isActive: false,
  lastRun: dayjs('2025-12-24T12:31'),
  nextRun: dayjs('2025-12-25T05:25'),
  duration: 31861,
  recordsProcessed: 25555,
  createdBy: 'if damaged happily',
  createdDate: dayjs('2025-12-25T10:25'),
};

export const sampleWithNewData: NewMaintenanceTask = {
  name: 'that always',
  taskType: 'VACUUM_DATABASE',
  schedule: 'eek',
  status: 'PENDING',
  isActive: true,
  createdBy: 'meatloaf ectoderm',
  createdDate: dayjs('2025-12-25T06:18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
