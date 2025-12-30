import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAITagPrediction, NewAITagPrediction } from '../ai-tag-prediction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAITagPrediction for edit and NewAITagPredictionFormGroupInput for create.
 */
type AITagPredictionFormGroupInput = IAITagPrediction | PartialWithRequiredKeyOf<NewAITagPrediction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAITagPrediction | NewAITagPrediction> = Omit<T, 'acceptedDate' | 'predictionDate'> & {
  acceptedDate?: string | null;
  predictionDate?: string | null;
};

type AITagPredictionFormRawValue = FormValueOf<IAITagPrediction>;

type NewAITagPredictionFormRawValue = FormValueOf<NewAITagPrediction>;

type AITagPredictionFormDefaults = Pick<NewAITagPrediction, 'id' | 'isAccepted' | 'acceptedDate' | 'predictionDate'>;

type AITagPredictionFormGroupContent = {
  id: FormControl<AITagPredictionFormRawValue['id'] | NewAITagPrediction['id']>;
  tagName: FormControl<AITagPredictionFormRawValue['tagName']>;
  confidence: FormControl<AITagPredictionFormRawValue['confidence']>;
  reason: FormControl<AITagPredictionFormRawValue['reason']>;
  modelVersion: FormControl<AITagPredictionFormRawValue['modelVersion']>;
  predictionS3Key: FormControl<AITagPredictionFormRawValue['predictionS3Key']>;
  isAccepted: FormControl<AITagPredictionFormRawValue['isAccepted']>;
  acceptedBy: FormControl<AITagPredictionFormRawValue['acceptedBy']>;
  acceptedDate: FormControl<AITagPredictionFormRawValue['acceptedDate']>;
  predictionDate: FormControl<AITagPredictionFormRawValue['predictionDate']>;
  job: FormControl<AITagPredictionFormRawValue['job']>;
};

export type AITagPredictionFormGroup = FormGroup<AITagPredictionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AITagPredictionFormService {
  createAITagPredictionFormGroup(aITagPrediction: AITagPredictionFormGroupInput = { id: null }): AITagPredictionFormGroup {
    const aITagPredictionRawValue = this.convertAITagPredictionToAITagPredictionRawValue({
      ...this.getFormDefaults(),
      ...aITagPrediction,
    });
    return new FormGroup<AITagPredictionFormGroupContent>({
      id: new FormControl(
        { value: aITagPredictionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tagName: new FormControl(aITagPredictionRawValue.tagName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      confidence: new FormControl(aITagPredictionRawValue.confidence, {
        validators: [Validators.required, Validators.min(0), Validators.max(1)],
      }),
      reason: new FormControl(aITagPredictionRawValue.reason, {
        validators: [Validators.maxLength(500)],
      }),
      modelVersion: new FormControl(aITagPredictionRawValue.modelVersion, {
        validators: [Validators.maxLength(50)],
      }),
      predictionS3Key: new FormControl(aITagPredictionRawValue.predictionS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      isAccepted: new FormControl(aITagPredictionRawValue.isAccepted),
      acceptedBy: new FormControl(aITagPredictionRawValue.acceptedBy, {
        validators: [Validators.maxLength(50)],
      }),
      acceptedDate: new FormControl(aITagPredictionRawValue.acceptedDate),
      predictionDate: new FormControl(aITagPredictionRawValue.predictionDate, {
        validators: [Validators.required],
      }),
      job: new FormControl(aITagPredictionRawValue.job),
    });
  }

  getAITagPrediction(form: AITagPredictionFormGroup): IAITagPrediction | NewAITagPrediction {
    return this.convertAITagPredictionRawValueToAITagPrediction(
      form.getRawValue() as AITagPredictionFormRawValue | NewAITagPredictionFormRawValue,
    );
  }

  resetForm(form: AITagPredictionFormGroup, aITagPrediction: AITagPredictionFormGroupInput): void {
    const aITagPredictionRawValue = this.convertAITagPredictionToAITagPredictionRawValue({ ...this.getFormDefaults(), ...aITagPrediction });
    form.reset(
      {
        ...aITagPredictionRawValue,
        id: { value: aITagPredictionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AITagPredictionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isAccepted: false,
      acceptedDate: currentTime,
      predictionDate: currentTime,
    };
  }

  private convertAITagPredictionRawValueToAITagPrediction(
    rawAITagPrediction: AITagPredictionFormRawValue | NewAITagPredictionFormRawValue,
  ): IAITagPrediction | NewAITagPrediction {
    return {
      ...rawAITagPrediction,
      acceptedDate: dayjs(rawAITagPrediction.acceptedDate, DATE_TIME_FORMAT),
      predictionDate: dayjs(rawAITagPrediction.predictionDate, DATE_TIME_FORMAT),
    };
  }

  private convertAITagPredictionToAITagPredictionRawValue(
    aITagPrediction: IAITagPrediction | (Partial<NewAITagPrediction> & AITagPredictionFormDefaults),
  ): AITagPredictionFormRawValue | PartialWithRequiredKeyOf<NewAITagPredictionFormRawValue> {
    return {
      ...aITagPrediction,
      acceptedDate: aITagPrediction.acceptedDate ? aITagPrediction.acceptedDate.format(DATE_TIME_FORMAT) : undefined,
      predictionDate: aITagPrediction.predictionDate ? aITagPrediction.predictionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
