import dayjs from 'dayjs/esm';

import { ContractStatus } from 'app/entities/enumerations/contract-status.model';
import { ContractType } from 'app/entities/enumerations/contract-type.model';

export interface IContract {
  id: number;
  documentId?: number | null;
  contractNumber?: string | null;
  contractType?: keyof typeof ContractType | null;
  title?: string | null;
  partyA?: string | null;
  partyB?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  autoRenew?: boolean | null;
  contractValue?: number | null;
  currency?: string | null;
  status?: keyof typeof ContractStatus | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewContract = Omit<IContract, 'id'> & { id: null };
