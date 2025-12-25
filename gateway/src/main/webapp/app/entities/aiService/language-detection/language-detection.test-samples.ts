import dayjs from 'dayjs/esm';

import { ILanguageDetection, NewLanguageDetection } from './language-detection.model';

export const sampleWithRequiredData: ILanguageDetection = {
  id: 11495,
  documentId: 14784,
  documentSha256: 'excellent wetly',
  detectedLanguage: 'which orie',
  confidence: 0.16,
  isCached: true,
  detectedDate: dayjs('2025-12-24T20:12'),
};

export const sampleWithPartialData: ILanguageDetection = {
  id: 21744,
  documentId: 21992,
  documentSha256: 'redevelop',
  detectedLanguage: 'forenenst',
  confidence: 0.56,
  detectionMethod: 'BERT_MULTILINGUAL',
  alternativeLanguages: '../fake-data/blob/hipster.txt',
  textSample: '../fake-data/blob/hipster.txt',
  isCached: true,
  detectedDate: dayjs('2025-12-24T15:06'),
};

export const sampleWithFullData: ILanguageDetection = {
  id: 16633,
  documentId: 31469,
  documentSha256: 'duster',
  detectedLanguage: 'weekly far',
  confidence: 0.79,
  detectionMethod: 'MANUAL',
  alternativeLanguages: '../fake-data/blob/hipster.txt',
  textSample: '../fake-data/blob/hipster.txt',
  resultCacheKey: 'downright hm for',
  isCached: false,
  detectedDate: dayjs('2025-12-24T17:57'),
  modelVersion: 'ape chairperson',
};

export const sampleWithNewData: NewLanguageDetection = {
  documentId: 32403,
  documentSha256: 'ouch',
  detectedLanguage: 'think edge',
  confidence: 0.2,
  isCached: false,
  detectedDate: dayjs('2025-12-24T22:48'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
