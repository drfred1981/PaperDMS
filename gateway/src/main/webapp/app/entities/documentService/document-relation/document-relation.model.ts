import dayjs from 'dayjs/esm';
import { RelationType } from 'app/entities/enumerations/relation-type.model';

export interface IDocumentRelation {
  id: number;
  sourceDocumentId?: number | null;
  targetDocumentId?: number | null;
  relationType?: keyof typeof RelationType | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewDocumentRelation = Omit<IDocumentRelation, 'id'> & { id: null };
