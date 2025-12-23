import { IContractClause, NewContractClause } from './contract-clause.model';

export const sampleWithRequiredData: IContractClause = {
  id: 9884,
  contractId: 20696,
  clauseNumber: 'fluffy um',
  title: 'fair produce elegantly',
  content: '../fake-data/blob/hipster.txt',
  isMandatory: false,
};

export const sampleWithPartialData: IContractClause = {
  id: 10401,
  contractId: 21882,
  clauseNumber: 'oxidise sharply scoff',
  title: 'inasmuch exactly',
  content: '../fake-data/blob/hipster.txt',
  isMandatory: false,
};

export const sampleWithFullData: IContractClause = {
  id: 1795,
  contractId: 24950,
  clauseNumber: 'sans',
  title: 'as',
  content: '../fake-data/blob/hipster.txt',
  clauseType: 'PAYMENT',
  isMandatory: true,
};

export const sampleWithNewData: NewContractClause = {
  contractId: 12276,
  clauseNumber: 'contrast past',
  title: 'including unsteady',
  content: '../fake-data/blob/hipster.txt',
  isMandatory: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
