import dayjs from 'dayjs/esm';

import { IAILanguageDetection, NewAILanguageDetection } from './ai-language-detection.model';

export const sampleWithRequiredData: IAILanguageDetection = {
  id: 25843,
  documentSha256: 'satirize',
  detectedLanguage: 'triumphant',
  confidence: 0.97,
  isCached: false,
  detectedDate: dayjs('2025-12-29T22:22'),
};

export const sampleWithPartialData: IAILanguageDetection = {
  id: 2393,
  documentSha256: 'silently chime decode',
  detectedLanguage: 'jaggedly',
  confidence: 0.07,
  isCached: false,
  detectedDate: dayjs('2025-12-29T15:34'),
  modelVersion: 'shore bell or',
};

export const sampleWithFullData: IAILanguageDetection = {
  id: 18582,
  documentSha256: 'ick',
  detectedLanguage: 'until',
  confidence: 0.07,
  detectionMethod: 'MANUAL',
  alternativeLanguages: '../fake-data/blob/hipster.txt',
  textSample: '../fake-data/blob/hipster.txt',
  resultCacheKey: 'blindly',
  isCached: false,
  detectedDate: dayjs('2025-12-30T05:39'),
  modelVersion: 'nippy',
};

export const sampleWithNewData: NewAILanguageDetection = {
  documentSha256: 'lecture',
  detectedLanguage: 'qua succes',
  confidence: 0.45,
  isCached: true,
  detectedDate: dayjs('2025-12-30T04:37'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
