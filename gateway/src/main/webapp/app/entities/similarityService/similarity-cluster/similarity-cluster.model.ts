import dayjs from 'dayjs/esm';
import { SimilarityAlgorithm } from 'app/entities/enumerations/similarity-algorithm.model';

export interface ISimilarityCluster {
  id: number;
  name?: string | null;
  description?: string | null;
  algorithm?: keyof typeof SimilarityAlgorithm | null;
  centroid?: string | null;
  documentCount?: number | null;
  avgSimilarity?: number | null;
  createdDate?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
}

export type NewSimilarityCluster = Omit<ISimilarityCluster, 'id'> & { id: null };
