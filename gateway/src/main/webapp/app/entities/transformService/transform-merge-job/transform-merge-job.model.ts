import dayjs from 'dayjs/esm';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';

export interface ITransformMergeJob {
  id: number;
  name?: string | null;
  sourceDocumentSha256?: string | null;
  mergeOrder?: string | null;
  includeBookmarks?: boolean | null;
  includeToc?: boolean | null;
  addPageNumbers?: boolean | null;
  outputS3Key?: string | null;
  outputDocumentSha256?: string | null;
  status?: keyof typeof TransformStatus | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewTransformMergeJob = Omit<ITransformMergeJob, 'id'> & { id: null };
