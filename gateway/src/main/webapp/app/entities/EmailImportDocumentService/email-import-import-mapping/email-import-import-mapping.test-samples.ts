import { IEmailImportImportMapping, NewEmailImportImportMapping } from './email-import-import-mapping.model';

export const sampleWithRequiredData: IEmailImportImportMapping = {
  id: 2510,
  emailField: 'DATE',
  documentField: 'eek duh',
  isRequired: false,
};

export const sampleWithPartialData: IEmailImportImportMapping = {
  id: 3932,
  emailField: 'BODY',
  documentField: 'abnormally pish',
  transformation: 'REPLACE',
  transformationConfig: '../fake-data/blob/hipster.txt',
  isRequired: true,
};

export const sampleWithFullData: IEmailImportImportMapping = {
  id: 12008,
  emailField: 'BODY_HTML',
  documentField: 'mostly',
  transformation: 'CONCAT',
  transformationConfig: '../fake-data/blob/hipster.txt',
  isRequired: false,
  defaultValue: 'merrily wide worse',
  validationRegex: 'spherical of crossly',
};

export const sampleWithNewData: NewEmailImportImportMapping = {
  emailField: 'MESSAGE_ID',
  documentField: 'geez toward',
  isRequired: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
