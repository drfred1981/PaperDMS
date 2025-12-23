import { IContract } from 'app/entities/businessDocService/contract/contract.model';
import { ClauseType } from 'app/entities/enumerations/clause-type.model';

export interface IContractClause {
  id: number;
  contractId?: number | null;
  clauseNumber?: string | null;
  title?: string | null;
  content?: string | null;
  clauseType?: keyof typeof ClauseType | null;
  isMandatory?: boolean | null;
  contract?: Pick<IContract, 'id'> | null;
}

export type NewContractClause = Omit<IContractClause, 'id'> & { id: null };
