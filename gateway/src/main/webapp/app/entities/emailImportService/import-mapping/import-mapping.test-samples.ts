import { IImportMapping, NewImportMapping } from './import-mapping.model';

export const sampleWithRequiredData: IImportMapping = {
  id: 24534,
  ruleId: 21457,
  emailField: 'SUBJECT',
  documentField: 'inasmuch amid seriously',
  isRequired: false,
};

export const sampleWithPartialData: IImportMapping = {
  id: 17346,
  ruleId: 31655,
  emailField: 'BODY_HTML',
  documentField: 'unsung gape suddenly',
  transformation: 'CONCAT',
  isRequired: false,
  validationRegex: 'portly',
};

export const sampleWithFullData: IImportMapping = {
  id: 12820,
  ruleId: 2625,
  emailField: 'CC',
  documentField: 'dishonor which',
  transformation: 'REPLACE',
  transformationConfig: '../fake-data/blob/hipster.txt',
  isRequired: false,
  defaultValue: 'as premeditation rival',
  validationRegex: 'vicinity till searchingly',
};

export const sampleWithNewData: NewImportMapping = {
  ruleId: 30332,
  emailField: 'ATTACHMENT_COUNT',
  documentField: 'where veto',
  isRequired: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
