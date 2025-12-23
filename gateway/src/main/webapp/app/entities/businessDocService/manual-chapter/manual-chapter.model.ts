import { IManual } from 'app/entities/businessDocService/manual/manual.model';

export interface IManualChapter {
  id: number;
  manualId?: number | null;
  chapterNumber?: string | null;
  title?: string | null;
  content?: string | null;
  pageStart?: number | null;
  pageEnd?: number | null;
  level?: number | null;
  displayOrder?: number | null;
  manual?: Pick<IManual, 'id'> | null;
  parentChapter?: Pick<IManualChapter, 'id'> | null;
}

export type NewManualChapter = Omit<IManualChapter, 'id'> & { id: null };
