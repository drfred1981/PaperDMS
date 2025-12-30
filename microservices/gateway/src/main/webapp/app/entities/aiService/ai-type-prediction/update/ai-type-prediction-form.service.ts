import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAITypePrediction, NewAITypePrediction } from '../ai-type-prediction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAITypePrediction for edit and NewAITypePredictionFormGroupInput for create.
 */
type AITypePredictionFormGroupInput = IAITypePrediction | PartialWithRequiredKeyOf<NewAITypePrediction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAITypePrediction | NewAITypePrediction> = Omit<T, 'acceptedDate' | 'predictionDate'> & {
  acceptedDate?: string | null;
  predictionDate?: string | null;
};

type AITypePredictionFormRawValue = FormValueOf<IAITypePrediction>;

type NewAITypePredictionFormRawValue = FormValueOf<NewAITypePrediction>;

type AITypePredictionFormDefaults = Pick<NewAITypePrediction, 'id' | 'isAccepted' | 'acceptedDate' | 'predictionDate'>;

type AITypePredictionFormGroupContent = {
  id: FormControl<AITypePredictionFormRawValue['id'] | NewAITypePrediction['id']>;
  documentTypeName: FormControl<AITypePredictionFormRawValue['documentTypeName']>;
  confidence: FormControl<AITypePredictionFormRawValue['confidence']>;
  reason: FormControl<AITypePredictionFormRawValue['reason']>;
  modelVersion: FormControl<AITypePredictionFormRawValue['modelVersion']>;
  predictionS3Key: FormControl<AITypePredictionFormRawValue['predictionS3Key']>;
  isAccepted: FormControl<AITypePredictionFormRawValue['isAccepted']>;
  acceptedBy: FormControl<AITypePredictionFormRawValue['acceptedBy']>;
  acceptedDate: FormControl<AITypePredictionFormRawValue['acceptedDate']>;
  predictionDate: FormControl<AITypePredictionFormRawValue['predictionDate']>;
};

export type AITypePredictionFormGroup = FormGroup<AITypePredictionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AITypePredictionFormService {
  createAITypePredictionFormGroup(aITypePrediction: AITypePredictionFormGroupInput = { id: null }): AITypePredictionFormGroup {
    const aITypePredictionRawValue = this.convertAITypePredictionToAITypePredictionRawValue({
      ...this.getFormDefaults(),
      ...aITypePrediction,
    });
    return new FormGroup<AITypePredictionFormGroupContent>({
      id: new FormControl(
        { value: aITypePredictionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentTypeName: new FormControl(aITypePredictionRawValue.documentTypeName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      confidence: new FormControl(aITypePredictionRawValue.confidence, {
        validators: [Validators.required, Validators.min(0), Validators.max(1)],
      }),
      reason: new FormControl(aITypePredictionRawValue.reason, {
        validators: [Validators.maxLength(500)],
      }),
      modelVersion: new FormControl(aITypePredictionRawValue.modelVersion, {
        validators: [Validators.maxLength(50)],
      }),
      predictionS3Key: new FormControl(aITypePredictionRawValue.predictionS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      isAccepted: new FormControl(aITypePredictionRawValue.isAccepted),
      acceptedBy: new FormControl(aITypePredictionRawValue.acceptedBy, {
        validators: [Validators.maxLength(50)],
      }),
      acceptedDate: new FormControl(aITypePredictionRawValue.acceptedDate),
      predictionDate: new FormControl(aITypePredictionRawValue.predictionDate, {
        validators: [Validators.required],
      }),
    });
  }

  getAITypePrediction(form: AITypePredictionFormGroup): IAITypePrediction | NewAITypePrediction {
    return this.convertAITypePredictionRawValueToAITypePrediction(
      form.getRawValue() as AITypePredictionFormRawValue | NewAITypePredictionFormRawValue,
    );
  }

  resetForm(form: AITypePredictionFormGroup, aITypePrediction: AITypePredictionFormGroupInput): void {
    const aITypePredictionRawValue = this.convertAITypePredictionToAITypePredictionRawValue({
      ...this.getFormDefaults(),
      ...aITypePrediction,
    });
    form.reset(
      {
        ...aITypePredictionRawValue,
        id: { value: aITypePredictionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AITypePredictionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isAccepted: false,
      acceptedDate: currentTime,
      predictionDate: currentTime,
    };
  }

  private convertAITypePredictionRawValueToAITypePrediction(
    rawAITypePrediction: AITypePredictionFormRawValue | NewAITypePredictionFormRawValue,
  ): IAITypePrediction | NewAITypePrediction {
    return {
      ...rawAITypePrediction,
      acceptedDate: dayjs(rawAITypePrediction.acceptedDate, DATE_TIME_FORMAT),
      predictionDate: dayjs(rawAITypePrediction.predictionDate, DATE_TIME_FORMAT),
    };
  }

  private convertAITypePredictionToAITypePredictionRawValue(
    aITypePrediction: IAITypePrediction | (Partial<NewAITypePrediction> & AITypePredictionFormDefaults),
  ): AITypePredictionFormRawValue | PartialWithRequiredKeyOf<NewAITypePredictionFormRawValue> {
    return {
      ...aITypePrediction,
      acceptedDate: aITypePrediction.acceptedDate ? aITypePrediction.acceptedDate.format(DATE_TIME_FORMAT) : undefined,
      predictionDate: aITypePrediction.predictionDate ? aITypePrediction.predictionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
