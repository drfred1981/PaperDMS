import dayjs from 'dayjs/esm';
import { IDocument } from 'app/entities/documentService/document/document.model';
import { IMetaTag } from 'app/entities/documentService/meta-tag/meta-tag.model';
import { MetaTagSource } from 'app/entities/enumerations/meta-tag-source.model';

export interface IDocumentTag {
  id: number;
  assignedDate?: dayjs.Dayjs | null;
  assignedBy?: string | null;
  confidence?: number | null;
  isAutoMetaTagged?: boolean | null;
  source?: keyof typeof MetaTagSource | null;
  document?: Pick<IDocument, 'id'> | null;
  metaTag?: Pick<IMetaTag, 'id'> | null;
}

export type NewDocumentTag = Omit<IDocumentTag, 'id'> & { id: null };
