import dayjs from 'dayjs/esm';

import { IWorkflow, NewWorkflow } from './workflow.model';

export const sampleWithRequiredData: IWorkflow = {
  id: 5550,
  name: 'riser',
  version: 26052,
  isActive: false,
  isParallel: false,
  autoStart: true,
  createdDate: dayjs('2025-12-29T15:41'),
  createdBy: 'first',
};

export const sampleWithPartialData: IWorkflow = {
  id: 21530,
  name: 'fowl',
  description: '../fake-data/blob/hipster.txt',
  version: 27671,
  isActive: true,
  isParallel: false,
  autoStart: true,
  triggerEvent: 'orange filthy exaggerate',
  createdDate: dayjs('2025-12-29T14:49'),
  lastModifiedDate: dayjs('2025-12-29T10:33'),
  createdBy: 'clonk harangue',
};

export const sampleWithFullData: IWorkflow = {
  id: 19598,
  name: 'boohoo jaggedly polite',
  description: '../fake-data/blob/hipster.txt',
  version: 7373,
  isActive: false,
  isParallel: false,
  autoStart: true,
  triggerEvent: 'custom divert hospitable',
  configuration: '../fake-data/blob/hipster.txt',
  createdDate: dayjs('2025-12-29T21:28'),
  lastModifiedDate: dayjs('2025-12-30T07:03'),
  createdBy: 'so horde lashes',
};

export const sampleWithNewData: NewWorkflow = {
  name: 'muffled beyond',
  version: 27536,
  isActive: true,
  isParallel: true,
  autoStart: false,
  createdDate: dayjs('2025-12-29T23:19'),
  createdBy: 'break',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
