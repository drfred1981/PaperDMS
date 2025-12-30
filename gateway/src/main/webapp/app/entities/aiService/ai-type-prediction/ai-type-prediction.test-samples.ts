import dayjs from 'dayjs/esm';

import { IAITypePrediction, NewAITypePrediction } from './ai-type-prediction.model';

export const sampleWithRequiredData: IAITypePrediction = {
  id: 23032,
  documentTypeName: 'achieve depot',
  confidence: 0.04,
  predictionDate: dayjs('2025-12-29T17:22'),
};

export const sampleWithPartialData: IAITypePrediction = {
  id: 18303,
  documentTypeName: 'knavishly gracefully',
  confidence: 0.51,
  isAccepted: false,
  acceptedDate: dayjs('2025-12-29T19:11'),
  predictionDate: dayjs('2025-12-29T23:52'),
};

export const sampleWithFullData: IAITypePrediction = {
  id: 15742,
  documentTypeName: 'bitterly circumnavigate',
  confidence: 0.81,
  reason: 'rural frightfully',
  modelVersion: 'sweet reproach upside-down',
  predictionS3Key: 'sans technician',
  isAccepted: false,
  acceptedBy: 'which reluctantly regarding',
  acceptedDate: dayjs('2025-12-30T05:14'),
  predictionDate: dayjs('2025-12-30T01:38'),
};

export const sampleWithNewData: NewAITypePrediction = {
  documentTypeName: 'than till terribly',
  confidence: 0.24,
  predictionDate: dayjs('2025-12-29T18:48'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
