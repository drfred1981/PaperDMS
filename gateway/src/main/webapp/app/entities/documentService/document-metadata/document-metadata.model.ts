import dayjs from 'dayjs/esm';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { MetadataType } from 'app/entities/enumerations/metadata-type.model';

export interface IDocumentMetadata {
  id: number;
  key?: string | null;
  value?: string | null;
  dataType?: keyof typeof MetadataType | null;
  isSearchable?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  document?: Pick<IDocument, 'id'> | null;
}

export type NewDocumentMetadata = Omit<IDocumentMetadata, 'id'> & { id: null };
