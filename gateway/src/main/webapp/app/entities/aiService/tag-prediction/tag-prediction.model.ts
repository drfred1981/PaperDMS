import dayjs from 'dayjs/esm';

import { IAutoTagJob } from 'app/entities/aiService/auto-tag-job/auto-tag-job.model';

export interface ITagPrediction {
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
  job?: Pick<IAutoTagJob, 'id'> | null;
}

export type NewTagPrediction = Omit<ITagPrediction, 'id'> & { id: null };
