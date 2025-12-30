import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAICorrespondentPrediction, NewAICorrespondentPrediction } from '../ai-correspondent-prediction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAICorrespondentPrediction for edit and NewAICorrespondentPredictionFormGroupInput for create.
 */
type AICorrespondentPredictionFormGroupInput = IAICorrespondentPrediction | PartialWithRequiredKeyOf<NewAICorrespondentPrediction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAICorrespondentPrediction | NewAICorrespondentPrediction> = Omit<T, 'acceptedDate' | 'predictionDate'> & {
  acceptedDate?: string | null;
  predictionDate?: string | null;
};

type AICorrespondentPredictionFormRawValue = FormValueOf<IAICorrespondentPrediction>;

type NewAICorrespondentPredictionFormRawValue = FormValueOf<NewAICorrespondentPrediction>;

type AICorrespondentPredictionFormDefaults = Pick<NewAICorrespondentPrediction, 'id' | 'isAccepted' | 'acceptedDate' | 'predictionDate'>;

type AICorrespondentPredictionFormGroupContent = {
  id: FormControl<AICorrespondentPredictionFormRawValue['id'] | NewAICorrespondentPrediction['id']>;
  correspondentName: FormControl<AICorrespondentPredictionFormRawValue['correspondentName']>;
  name: FormControl<AICorrespondentPredictionFormRawValue['name']>;
  email: FormControl<AICorrespondentPredictionFormRawValue['email']>;
  phone: FormControl<AICorrespondentPredictionFormRawValue['phone']>;
  address: FormControl<AICorrespondentPredictionFormRawValue['address']>;
  company: FormControl<AICorrespondentPredictionFormRawValue['company']>;
  type: FormControl<AICorrespondentPredictionFormRawValue['type']>;
  role: FormControl<AICorrespondentPredictionFormRawValue['role']>;
  confidence: FormControl<AICorrespondentPredictionFormRawValue['confidence']>;
  reason: FormControl<AICorrespondentPredictionFormRawValue['reason']>;
  modelVersion: FormControl<AICorrespondentPredictionFormRawValue['modelVersion']>;
  predictionS3Key: FormControl<AICorrespondentPredictionFormRawValue['predictionS3Key']>;
  isAccepted: FormControl<AICorrespondentPredictionFormRawValue['isAccepted']>;
  acceptedBy: FormControl<AICorrespondentPredictionFormRawValue['acceptedBy']>;
  acceptedDate: FormControl<AICorrespondentPredictionFormRawValue['acceptedDate']>;
  predictionDate: FormControl<AICorrespondentPredictionFormRawValue['predictionDate']>;
  job: FormControl<AICorrespondentPredictionFormRawValue['job']>;
};

export type AICorrespondentPredictionFormGroup = FormGroup<AICorrespondentPredictionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AICorrespondentPredictionFormService {
  createAICorrespondentPredictionFormGroup(
    aICorrespondentPrediction: AICorrespondentPredictionFormGroupInput = { id: null },
  ): AICorrespondentPredictionFormGroup {
    const aICorrespondentPredictionRawValue = this.convertAICorrespondentPredictionToAICorrespondentPredictionRawValue({
      ...this.getFormDefaults(),
      ...aICorrespondentPrediction,
    });
    return new FormGroup<AICorrespondentPredictionFormGroupContent>({
      id: new FormControl(
        { value: aICorrespondentPredictionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      correspondentName: new FormControl(aICorrespondentPredictionRawValue.correspondentName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      name: new FormControl(aICorrespondentPredictionRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      email: new FormControl(aICorrespondentPredictionRawValue.email, {
        validators: [Validators.maxLength(255)],
      }),
      phone: new FormControl(aICorrespondentPredictionRawValue.phone, {
        validators: [Validators.maxLength(50)],
      }),
      address: new FormControl(aICorrespondentPredictionRawValue.address),
      company: new FormControl(aICorrespondentPredictionRawValue.company, {
        validators: [Validators.maxLength(255)],
      }),
      type: new FormControl(aICorrespondentPredictionRawValue.type),
      role: new FormControl(aICorrespondentPredictionRawValue.role),
      confidence: new FormControl(aICorrespondentPredictionRawValue.confidence, {
        validators: [Validators.required, Validators.min(0), Validators.max(1)],
      }),
      reason: new FormControl(aICorrespondentPredictionRawValue.reason, {
        validators: [Validators.maxLength(500)],
      }),
      modelVersion: new FormControl(aICorrespondentPredictionRawValue.modelVersion, {
        validators: [Validators.maxLength(50)],
      }),
      predictionS3Key: new FormControl(aICorrespondentPredictionRawValue.predictionS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      isAccepted: new FormControl(aICorrespondentPredictionRawValue.isAccepted),
      acceptedBy: new FormControl(aICorrespondentPredictionRawValue.acceptedBy, {
        validators: [Validators.maxLength(50)],
      }),
      acceptedDate: new FormControl(aICorrespondentPredictionRawValue.acceptedDate),
      predictionDate: new FormControl(aICorrespondentPredictionRawValue.predictionDate, {
        validators: [Validators.required],
      }),
      job: new FormControl(aICorrespondentPredictionRawValue.job),
    });
  }

  getAICorrespondentPrediction(form: AICorrespondentPredictionFormGroup): IAICorrespondentPrediction | NewAICorrespondentPrediction {
    return this.convertAICorrespondentPredictionRawValueToAICorrespondentPrediction(
      form.getRawValue() as AICorrespondentPredictionFormRawValue | NewAICorrespondentPredictionFormRawValue,
    );
  }

  resetForm(form: AICorrespondentPredictionFormGroup, aICorrespondentPrediction: AICorrespondentPredictionFormGroupInput): void {
    const aICorrespondentPredictionRawValue = this.convertAICorrespondentPredictionToAICorrespondentPredictionRawValue({
      ...this.getFormDefaults(),
      ...aICorrespondentPrediction,
    });
    form.reset(
      {
        ...aICorrespondentPredictionRawValue,
        id: { value: aICorrespondentPredictionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AICorrespondentPredictionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isAccepted: false,
      acceptedDate: currentTime,
      predictionDate: currentTime,
    };
  }

  private convertAICorrespondentPredictionRawValueToAICorrespondentPrediction(
    rawAICorrespondentPrediction: AICorrespondentPredictionFormRawValue | NewAICorrespondentPredictionFormRawValue,
  ): IAICorrespondentPrediction | NewAICorrespondentPrediction {
    return {
      ...rawAICorrespondentPrediction,
      acceptedDate: dayjs(rawAICorrespondentPrediction.acceptedDate, DATE_TIME_FORMAT),
      predictionDate: dayjs(rawAICorrespondentPrediction.predictionDate, DATE_TIME_FORMAT),
    };
  }

  private convertAICorrespondentPredictionToAICorrespondentPredictionRawValue(
    aICorrespondentPrediction: IAICorrespondentPrediction | (Partial<NewAICorrespondentPrediction> & AICorrespondentPredictionFormDefaults),
  ): AICorrespondentPredictionFormRawValue | PartialWithRequiredKeyOf<NewAICorrespondentPredictionFormRawValue> {
    return {
      ...aICorrespondentPrediction,
      acceptedDate: aICorrespondentPrediction.acceptedDate ? aICorrespondentPrediction.acceptedDate.format(DATE_TIME_FORMAT) : undefined,
      predictionDate: aICorrespondentPrediction.predictionDate
        ? aICorrespondentPrediction.predictionDate.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
