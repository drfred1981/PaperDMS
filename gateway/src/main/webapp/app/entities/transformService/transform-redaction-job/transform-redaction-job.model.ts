import dayjs from 'dayjs/esm';
import { RedactionType } from 'app/entities/enumerations/redaction-type.model';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';

export interface ITransformRedactionJob {
  id: number;
  documentSha256?: string | null;
  redactionAreas?: string | null;
  redactionType?: keyof typeof RedactionType | null;
  redactionColor?: string | null;
  replaceWith?: string | null;
  outputS3Key?: string | null;
  outputDocumentSha256?: string | null;
  status?: keyof typeof TransformStatus | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  errorMessage?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewTransformRedactionJob = Omit<ITransformRedactionJob, 'id'> & { id: null };
