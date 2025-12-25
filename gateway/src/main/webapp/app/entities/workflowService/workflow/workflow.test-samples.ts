import dayjs from 'dayjs/esm';

import { IWorkflow, NewWorkflow } from './workflow.model';

export const sampleWithRequiredData: IWorkflow = {
  id: 5550,
  name: 'riser',
  version: 26052,
  isActive: false,
  isParallel: false,
  autoStart: true,
  createdDate: dayjs('2025-12-24T19:35'),
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
  createdDate: dayjs('2025-12-24T18:43'),
  lastModifiedDate: dayjs('2025-12-24T14:27'),
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
  createdDate: dayjs('2025-12-25T01:22'),
  lastModifiedDate: dayjs('2025-12-25T10:57'),
  createdBy: 'so horde lashes',
};

export const sampleWithNewData: NewWorkflow = {
  name: 'muffled beyond',
  version: 27536,
  isActive: true,
  isParallel: true,
  autoStart: false,
  createdDate: dayjs('2025-12-25T03:13'),
  createdBy: 'break',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
