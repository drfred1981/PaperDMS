import dayjs from 'dayjs/esm';

import { AiJobStatus } from 'app/entities/enumerations/ai-job-status.model';
import { SimilarityAlgorithm } from 'app/entities/enumerations/similarity-algorithm.model';
import { SimilarityScope } from 'app/entities/enumerations/similarity-scope.model';

export interface ISimilarityJob {
  id: number;
  documentId?: number | null;
  documentSha256?: string | null;
  status?: keyof typeof AiJobStatus | null;
  algorithm?: keyof typeof SimilarityAlgorithm | null;
  scope?: keyof typeof SimilarityScope | null;
  minSimilarityThreshold?: number | null;
  matchesFound?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
}

export type NewSimilarityJob = Omit<ISimilarityJob, 'id'> & { id: null };
