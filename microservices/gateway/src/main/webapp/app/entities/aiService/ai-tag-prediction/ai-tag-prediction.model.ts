import dayjs from 'dayjs/esm';
import { IAIAutoTagJob } from 'app/entities/aiService/ai-auto-tag-job/ai-auto-tag-job.model';

export interface IAITagPrediction {
  id: number;
  tagName?: string | null;
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

export type NewAITagPrediction = Omit<IAITagPrediction, 'id'> & { id: null };
