import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAILanguageDetection, NewAILanguageDetection } from '../ai-language-detection.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAILanguageDetection for edit and NewAILanguageDetectionFormGroupInput for create.
 */
type AILanguageDetectionFormGroupInput = IAILanguageDetection | PartialWithRequiredKeyOf<NewAILanguageDetection>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAILanguageDetection | NewAILanguageDetection> = Omit<T, 'detectedDate'> & {
  detectedDate?: string | null;
};

type AILanguageDetectionFormRawValue = FormValueOf<IAILanguageDetection>;

type NewAILanguageDetectionFormRawValue = FormValueOf<NewAILanguageDetection>;

type AILanguageDetectionFormDefaults = Pick<NewAILanguageDetection, 'id' | 'isCached' | 'detectedDate'>;

type AILanguageDetectionFormGroupContent = {
  id: FormControl<AILanguageDetectionFormRawValue['id'] | NewAILanguageDetection['id']>;
  documentSha256: FormControl<AILanguageDetectionFormRawValue['documentSha256']>;
  detectedLanguage: FormControl<AILanguageDetectionFormRawValue['detectedLanguage']>;
  confidence: FormControl<AILanguageDetectionFormRawValue['confidence']>;
  detectionMethod: FormControl<AILanguageDetectionFormRawValue['detectionMethod']>;
  alternativeLanguages: FormControl<AILanguageDetectionFormRawValue['alternativeLanguages']>;
  textSample: FormControl<AILanguageDetectionFormRawValue['textSample']>;
  resultCacheKey: FormControl<AILanguageDetectionFormRawValue['resultCacheKey']>;
  isCached: FormControl<AILanguageDetectionFormRawValue['isCached']>;
  detectedDate: FormControl<AILanguageDetectionFormRawValue['detectedDate']>;
  modelVersion: FormControl<AILanguageDetectionFormRawValue['modelVersion']>;
};

export type AILanguageDetectionFormGroup = FormGroup<AILanguageDetectionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AILanguageDetectionFormService {
  createAILanguageDetectionFormGroup(aILanguageDetection: AILanguageDetectionFormGroupInput = { id: null }): AILanguageDetectionFormGroup {
    const aILanguageDetectionRawValue = this.convertAILanguageDetectionToAILanguageDetectionRawValue({
      ...this.getFormDefaults(),
      ...aILanguageDetection,
    });
    return new FormGroup<AILanguageDetectionFormGroupContent>({
      id: new FormControl(
        { value: aILanguageDetectionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentSha256: new FormControl(aILanguageDetectionRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      detectedLanguage: new FormControl(aILanguageDetectionRawValue.detectedLanguage, {
        validators: [Validators.required, Validators.maxLength(10)],
      }),
      confidence: new FormControl(aILanguageDetectionRawValue.confidence, {
        validators: [Validators.required, Validators.min(0), Validators.max(1)],
      }),
      detectionMethod: new FormControl(aILanguageDetectionRawValue.detectionMethod),
      alternativeLanguages: new FormControl(aILanguageDetectionRawValue.alternativeLanguages),
      textSample: new FormControl(aILanguageDetectionRawValue.textSample),
      resultCacheKey: new FormControl(aILanguageDetectionRawValue.resultCacheKey, {
        validators: [Validators.maxLength(128)],
      }),
      isCached: new FormControl(aILanguageDetectionRawValue.isCached, {
        validators: [Validators.required],
      }),
      detectedDate: new FormControl(aILanguageDetectionRawValue.detectedDate, {
        validators: [Validators.required],
      }),
      modelVersion: new FormControl(aILanguageDetectionRawValue.modelVersion, {
        validators: [Validators.maxLength(50)],
      }),
    });
  }

  getAILanguageDetection(form: AILanguageDetectionFormGroup): IAILanguageDetection | NewAILanguageDetection {
    return this.convertAILanguageDetectionRawValueToAILanguageDetection(
      form.getRawValue() as AILanguageDetectionFormRawValue | NewAILanguageDetectionFormRawValue,
    );
  }

  resetForm(form: AILanguageDetectionFormGroup, aILanguageDetection: AILanguageDetectionFormGroupInput): void {
    const aILanguageDetectionRawValue = this.convertAILanguageDetectionToAILanguageDetectionRawValue({
      ...this.getFormDefaults(),
      ...aILanguageDetection,
    });
    form.reset(
      {
        ...aILanguageDetectionRawValue,
        id: { value: aILanguageDetectionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AILanguageDetectionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isCached: false,
      detectedDate: currentTime,
    };
  }

  private convertAILanguageDetectionRawValueToAILanguageDetection(
    rawAILanguageDetection: AILanguageDetectionFormRawValue | NewAILanguageDetectionFormRawValue,
  ): IAILanguageDetection | NewAILanguageDetection {
    return {
      ...rawAILanguageDetection,
      detectedDate: dayjs(rawAILanguageDetection.detectedDate, DATE_TIME_FORMAT),
    };
  }

  private convertAILanguageDetectionToAILanguageDetectionRawValue(
    aILanguageDetection: IAILanguageDetection | (Partial<NewAILanguageDetection> & AILanguageDetectionFormDefaults),
  ): AILanguageDetectionFormRawValue | PartialWithRequiredKeyOf<NewAILanguageDetectionFormRawValue> {
    return {
      ...aILanguageDetection,
      detectedDate: aILanguageDetection.detectedDate ? aILanguageDetection.detectedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
