import dayjs from 'dayjs/esm';

import { IDocumentTemplate, NewDocumentTemplate } from './document-template.model';

export const sampleWithRequiredData: IDocumentTemplate = {
  id: 3827,
  name: 'because radiant',
  templateSha256: 'utterly',
  templateS3Key: 'custody',
  isActive: true,
  createdBy: 'ack bidet stratify',
  createdDate: dayjs('2025-12-29T08:44'),
};

export const sampleWithPartialData: IDocumentTemplate = {
  id: 5061,
  name: 'planula er',
  templateSha256: 'if',
  templateS3Key: 'indeed upon pushy',
  isActive: false,
  createdBy: 'whereas afford',
  createdDate: dayjs('2025-12-29T23:43'),
};

export const sampleWithFullData: IDocumentTemplate = {
  id: 4967,
  name: 'any',
  templateSha256: 'selfishly blond an',
  templateS3Key: 'able',
  isActive: false,
  createdBy: 'viciously metallic beret',
  createdDate: dayjs('2025-12-29T23:16'),
};

export const sampleWithNewData: NewDocumentTemplate = {
  name: 'circulate till shark',
  templateSha256: 'meh bouncy railway',
  templateS3Key: 'accountability reproachfully supposing',
  isActive: true,
  createdBy: 'hydrolyze',
  createdDate: dayjs('2025-12-29T15:05'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
