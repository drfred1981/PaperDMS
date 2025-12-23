import dayjs from 'dayjs/esm';

import { FingerprintType } from 'app/entities/enumerations/fingerprint-type.model';

export interface IDocumentFingerprint {
  id: number;
  documentId?: number | null;
  fingerprintType?: keyof typeof FingerprintType | null;
  fingerprint?: string | null;
  vectorEmbedding?: string | null;
  metadata?: string | null;
  computedDate?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
}

export type NewDocumentFingerprint = Omit<IDocumentFingerprint, 'id'> & { id: null };
