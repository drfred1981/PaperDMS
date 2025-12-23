import dayjs from 'dayjs/esm';

import { RedactionType } from 'app/entities/enumerations/redaction-type.model';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';

export interface IRedactionJob {
  id: number;
  documentId?: number | null;
  redactionAreas?: string | null;
  redactionType?: keyof typeof RedactionType | null;
  redactionColor?: string | null;
  replaceWith?: string | null;
  outputS3Key?: string | null;
  outputDocumentId?: number | null;
  status?: keyof typeof TransformStatus | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewRedactionJob = Omit<IRedactionJob, 'id'> & { id: null };
