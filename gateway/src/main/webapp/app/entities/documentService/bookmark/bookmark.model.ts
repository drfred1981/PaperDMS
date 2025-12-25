import dayjs from 'dayjs/esm';
import { BookmarkType } from 'app/entities/enumerations/bookmark-type.model';

export interface IBookmark {
  id: number;
  userId?: string | null;
  entityType?: keyof typeof BookmarkType | null;
  entityId?: number | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewBookmark = Omit<IBookmark, 'id'> & { id: null };
