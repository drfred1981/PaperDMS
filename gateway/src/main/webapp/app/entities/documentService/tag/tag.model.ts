import dayjs from 'dayjs/esm';

import { ITagCategory } from 'app/entities/documentService/tag-category/tag-category.model';

export interface ITag {
  id: number;
  name?: string | null;
  color?: string | null;
  description?: string | null;
  usageCount?: number | null;
  isSystem?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  tagCategory?: Pick<ITagCategory, 'id'> | null;
}

export type NewTag = Omit<ITag, 'id'> & { id: null };
