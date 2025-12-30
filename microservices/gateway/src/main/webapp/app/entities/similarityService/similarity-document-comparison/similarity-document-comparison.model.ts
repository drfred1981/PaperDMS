import dayjs from 'dayjs/esm';
import { ISimilarityJob } from 'app/entities/similarityService/similarity-job/similarity-job.model';
import { SimilarityAlgorithm } from 'app/entities/enumerations/similarity-algorithm.model';

export interface ISimilarityDocumentComparison {
  id: number;
  sourceDocumentSha256?: string | null;
  targetDocumentSha256?: string | null;
  similarityScore?: number | null;
  algorithm?: keyof typeof SimilarityAlgorithm | null;
  features?: string | null;
  computedDate?: dayjs.Dayjs | null;
  isRelevant?: boolean | null;
  reviewedBy?: string | null;
  reviewedDate?: dayjs.Dayjs | null;
  job?: Pick<ISimilarityJob, 'id'> | null;
}

export type NewSimilarityDocumentComparison = Omit<ISimilarityDocumentComparison, 'id'> & { id: null };
