import dayjs from 'dayjs/esm';
import { ComparisonType } from 'app/entities/enumerations/comparison-type.model';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';

export interface IComparisonJob {
  id: number;
  documentId1?: number | null;
  documentId2?: number | null;
  comparisonType?: keyof typeof ComparisonType | null;
  differences?: string | null;
  differenceCount?: number | null;
  similarityPercentage?: number | null;
  diffReportS3Key?: string | null;
  diffVisualS3Key?: string | null;
  status?: keyof typeof TransformStatus | null;
  comparedDate?: dayjs.Dayjs | null;
  comparedBy?: string | null;
}

export type NewComparisonJob = Omit<IComparisonJob, 'id'> & { id: null };
