import dayjs from 'dayjs/esm';

import { IDocument } from 'app/entities/documentService/document/document.model';
import { ITag } from 'app/entities/documentService/tag/tag.model';
import { TagSource } from 'app/entities/enumerations/tag-source.model';

export interface IDocumentTag {
  id: number;
  assignedDate?: dayjs.Dayjs | null;
  assignedBy?: string | null;
  confidence?: number | null;
  isAutoTagged?: boolean | null;
  source?: keyof typeof TagSource | null;
  document?: Pick<IDocument, 'id'> | null;
  tag?: Pick<ITag, 'id'> | null;
}

export type NewDocumentTag = Omit<IDocumentTag, 'id'> & { id: null };
