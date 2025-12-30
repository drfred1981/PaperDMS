import dayjs from 'dayjs/esm';
import { IMetaMetaTagCategory } from 'app/entities/documentService/meta-meta-tag-category/meta-meta-tag-category.model';

export interface IMetaTag {
  id: number;
  name?: string | null;
  color?: string | null;
  description?: string | null;
  usageCount?: number | null;
  isSystem?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  metaMetaTagCategory?: Pick<IMetaMetaTagCategory, 'id'> | null;
}

export type NewMetaTag = Omit<IMetaTag, 'id'> & { id: null };
