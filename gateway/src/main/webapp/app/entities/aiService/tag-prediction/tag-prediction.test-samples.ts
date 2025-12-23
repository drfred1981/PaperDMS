import dayjs from 'dayjs/esm';

import { ITagPrediction, NewTagPrediction } from './tag-prediction.model';

export const sampleWithRequiredData: ITagPrediction = {
  id: 14165,
  tagName: 'awkwardly though',
  confidence: 0.41,
  predictionDate: dayjs('2025-12-19T19:24'),
};

export const sampleWithPartialData: ITagPrediction = {
  id: 23223,
  tagName: 'which',
  confidence: 0.33,
  acceptedBy: 'impostor assail gown',
  acceptedDate: dayjs('2025-12-20T11:14'),
  predictionDate: dayjs('2025-12-20T06:39'),
};

export const sampleWithFullData: ITagPrediction = {
  id: 17672,
  tagName: 'validity like opposite',
  confidence: 0.07,
  reason: 'verify',
  modelVersion: 'hm',
  predictionS3Key: 'healthily',
  isAccepted: false,
  acceptedBy: 'vice towards smoothly',
  acceptedDate: dayjs('2025-12-20T02:01'),
  predictionDate: dayjs('2025-12-20T07:38'),
};

export const sampleWithNewData: NewTagPrediction = {
  tagName: 'throughout er',
  confidence: 0.06,
  predictionDate: dayjs('2025-12-20T14:58'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
