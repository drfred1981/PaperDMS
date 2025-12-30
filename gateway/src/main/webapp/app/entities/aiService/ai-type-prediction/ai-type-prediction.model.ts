import dayjs from 'dayjs/esm';

export interface IAITypePrediction {
  id: number;
  documentTypeName?: string | null;
  confidence?: number | null;
  reason?: string | null;
  modelVersion?: string | null;
  predictionS3Key?: string | null;
  isAccepted?: boolean | null;
  acceptedBy?: string | null;
  acceptedDate?: dayjs.Dayjs | null;
  predictionDate?: dayjs.Dayjs | null;
}

export type NewAITypePrediction = Omit<IAITypePrediction, 'id'> & { id: null };
