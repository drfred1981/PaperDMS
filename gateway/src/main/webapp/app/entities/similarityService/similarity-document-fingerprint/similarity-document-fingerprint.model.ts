import dayjs from 'dayjs/esm';
import { FingerprintType } from 'app/entities/enumerations/fingerprint-type.model';

export interface ISimilarityDocumentFingerprint {
  id: number;
  fingerprintType?: keyof typeof FingerprintType | null;
  fingerprint?: string | null;
  vectorEmbedding?: string | null;
  metadata?: string | null;
  computedDate?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
}

export type NewSimilarityDocumentFingerprint = Omit<ISimilarityDocumentFingerprint, 'id'> & { id: null };
