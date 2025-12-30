import dayjs from 'dayjs/esm';
import { IAIAutoTagJob } from 'app/entities/aiService/ai-auto-tag-job/ai-auto-tag-job.model';
import { CorrespondentType } from 'app/entities/enumerations/correspondent-type.model';
import { CorrespondentRole } from 'app/entities/enumerations/correspondent-role.model';

export interface IAICorrespondentPrediction {
  id: number;
  correspondentName?: string | null;
  name?: string | null;
  email?: string | null;
  phone?: string | null;
  address?: string | null;
  company?: string | null;
  type?: keyof typeof CorrespondentType | null;
  role?: keyof typeof CorrespondentRole | null;
  confidence?: number | null;
  reason?: string | null;
  modelVersion?: string | null;
  predictionS3Key?: string | null;
  isAccepted?: boolean | null;
  acceptedBy?: string | null;
  acceptedDate?: dayjs.Dayjs | null;
  predictionDate?: dayjs.Dayjs | null;
  job?: Pick<IAIAutoTagJob, 'id'> | null;
}

export type NewAICorrespondentPrediction = Omit<IAICorrespondentPrediction, 'id'> & { id: null };
