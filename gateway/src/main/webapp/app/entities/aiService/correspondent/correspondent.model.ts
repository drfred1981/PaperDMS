import dayjs from 'dayjs/esm';
import { ICorrespondentExtraction } from 'app/entities/aiService/correspondent-extraction/correspondent-extraction.model';
import { CorrespondentType } from 'app/entities/enumerations/correspondent-type.model';
import { CorrespondentRole } from 'app/entities/enumerations/correspondent-role.model';

export interface ICorrespondent {
  id: number;
  name?: string | null;
  email?: string | null;
  phone?: string | null;
  address?: string | null;
  company?: string | null;
  type?: keyof typeof CorrespondentType | null;
  role?: keyof typeof CorrespondentRole | null;
  confidence?: number | null;
  isVerified?: boolean | null;
  verifiedBy?: string | null;
  verifiedDate?: dayjs.Dayjs | null;
  metadata?: string | null;
  extractedDate?: dayjs.Dayjs | null;
  extraction?: Pick<ICorrespondentExtraction, 'id'> | null;
}

export type NewCorrespondent = Omit<ICorrespondent, 'id'> & { id: null };
