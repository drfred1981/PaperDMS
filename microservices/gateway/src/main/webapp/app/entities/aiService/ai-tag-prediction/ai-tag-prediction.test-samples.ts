import dayjs from 'dayjs/esm';

import { IAITagPrediction, NewAITagPrediction } from './ai-tag-prediction.model';

export const sampleWithRequiredData: IAITagPrediction = {
  id: 26698,
  tagName: 'ha provider vice',
  confidence: 0.02,
  predictionDate: dayjs('2025-12-30T06:55'),
};

export const sampleWithPartialData: IAITagPrediction = {
  id: 31072,
  tagName: 'gee grimy',
  confidence: 0.2,
  predictionS3Key: 'minister depute aw',
  isAccepted: false,
  acceptedDate: dayjs('2025-12-29T15:19'),
  predictionDate: dayjs('2025-12-29T11:25'),
};

export const sampleWithFullData: IAITagPrediction = {
  id: 7470,
  tagName: 'duh',
  confidence: 0.95,
  reason: 'angelic',
  modelVersion: 'longboat eek',
  predictionS3Key: 'via jubilantly cork',
  isAccepted: false,
  acceptedBy: 'unless interchange zen',
  acceptedDate: dayjs('2025-12-29T16:43'),
  predictionDate: dayjs('2025-12-29T12:23'),
};

export const sampleWithNewData: NewAITagPrediction = {
  tagName: 'cricket worthwhile orange',
  confidence: 0.26,
  predictionDate: dayjs('2025-12-29T14:19'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
