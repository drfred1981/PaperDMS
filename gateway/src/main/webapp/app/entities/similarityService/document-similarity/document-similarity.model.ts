import dayjs from 'dayjs/esm';

import { SimilarityAlgorithm } from 'app/entities/enumerations/similarity-algorithm.model';
import { ISimilarityJob } from 'app/entities/similarityService/similarity-job/similarity-job.model';

export interface IDocumentSimilarity {
  id: number;
  documentId1?: number | null;
  documentId2?: number | null;
  similarityScore?: number | null;
  algorithm?: keyof typeof SimilarityAlgorithm | null;
  features?: string | null;
  computedDate?: dayjs.Dayjs | null;
  isRelevant?: boolean | null;
  reviewedBy?: string | null;
  reviewedDate?: dayjs.Dayjs | null;
  job?: Pick<ISimilarityJob, 'id'> | null;
}

export type NewDocumentSimilarity = Omit<IDocumentSimilarity, 'id'> & { id: null };
