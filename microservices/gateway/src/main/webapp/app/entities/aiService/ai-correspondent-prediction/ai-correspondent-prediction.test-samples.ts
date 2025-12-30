import dayjs from 'dayjs/esm';

import { IAICorrespondentPrediction, NewAICorrespondentPrediction } from './ai-correspondent-prediction.model';

export const sampleWithRequiredData: IAICorrespondentPrediction = {
  id: 17738,
  correspondentName: 'fluff',
  name: 'worth',
  confidence: 0.08,
  predictionDate: dayjs('2025-12-30T06:42'),
};

export const sampleWithPartialData: IAICorrespondentPrediction = {
  id: 20119,
  correspondentName: 'sheathe and calculating',
  name: 'geez paintwork',
  company: 'form mindless inquisitively',
  confidence: 0.27,
  reason: 'inside',
  modelVersion: 'other unkempt however',
  predictionS3Key: 'however hm near',
  predictionDate: dayjs('2025-12-30T01:45'),
};

export const sampleWithFullData: IAICorrespondentPrediction = {
  id: 6515,
  correspondentName: 'not against',
  name: 'shipper warped smoothly',
  email: 'Fleta_Nolan90@hotmail.com',
  phone: '919-640-3964 x580',
  address: '../fake-data/blob/hipster.txt',
  company: 'scented crushing yowza',
  type: 'UNKNOWN',
  role: 'CC',
  confidence: 0.02,
  reason: 'swine vacation within',
  modelVersion: 'question whenever cleave',
  predictionS3Key: 'that stir-fry',
  isAccepted: true,
  acceptedBy: 'overheard absent reach',
  acceptedDate: dayjs('2025-12-29T11:53'),
  predictionDate: dayjs('2025-12-30T05:37'),
};

export const sampleWithNewData: NewAICorrespondentPrediction = {
  correspondentName: 'wherever pastel',
  name: 'throughout restfully aha',
  confidence: 0.49,
  predictionDate: dayjs('2025-12-29T23:12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
