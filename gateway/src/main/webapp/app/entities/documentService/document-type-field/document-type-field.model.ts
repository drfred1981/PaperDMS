import dayjs from 'dayjs/esm';
import { IDocumentType } from 'app/entities/documentService/document-type/document-type.model';
import { MetadataType } from 'app/entities/enumerations/metadata-type.model';

export interface IDocumentTypeField {
  id: number;
  fieldKey?: string | null;
  fieldLabel?: string | null;
  dataType?: keyof typeof MetadataType | null;
  isRequired?: boolean | null;
  isSearchable?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  documentType?: Pick<IDocumentType, 'id'> | null;
}

export type NewDocumentTypeField = Omit<IDocumentTypeField, 'id'> & { id: null };
