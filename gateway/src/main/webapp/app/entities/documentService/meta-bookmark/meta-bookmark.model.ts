import dayjs from 'dayjs/esm';
import { MetaBookmarkType } from 'app/entities/enumerations/meta-bookmark-type.model';

export interface IMetaBookmark {
  id: number;
  userId?: string | null;
  entityType?: keyof typeof MetaBookmarkType | null;
  entityName?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewMetaBookmark = Omit<IMetaBookmark, 'id'> & { id: null };
