import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILanguageDetection, NewLanguageDetection } from '../language-detection.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILanguageDetection for edit and NewLanguageDetectionFormGroupInput for create.
 */
type LanguageDetectionFormGroupInput = ILanguageDetection | PartialWithRequiredKeyOf<NewLanguageDetection>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILanguageDetection | NewLanguageDetection> = Omit<T, 'detectedDate'> & {
  detectedDate?: string | null;
};

type LanguageDetectionFormRawValue = FormValueOf<ILanguageDetection>;

type NewLanguageDetectionFormRawValue = FormValueOf<NewLanguageDetection>;

type LanguageDetectionFormDefaults = Pick<NewLanguageDetection, 'id' | 'isCached' | 'detectedDate'>;

type LanguageDetectionFormGroupContent = {
  id: FormControl<LanguageDetectionFormRawValue['id'] | NewLanguageDetection['id']>;
  documentId: FormControl<LanguageDetectionFormRawValue['documentId']>;
  documentSha256: FormControl<LanguageDetectionFormRawValue['documentSha256']>;
  detectedLanguage: FormControl<LanguageDetectionFormRawValue['detectedLanguage']>;
  confidence: FormControl<LanguageDetectionFormRawValue['confidence']>;
  detectionMethod: FormControl<LanguageDetectionFormRawValue['detectionMethod']>;
  alternativeLanguages: FormControl<LanguageDetectionFormRawValue['alternativeLanguages']>;
  textSample: FormControl<LanguageDetectionFormRawValue['textSample']>;
  resultCacheKey: FormControl<LanguageDetectionFormRawValue['resultCacheKey']>;
  isCached: FormControl<LanguageDetectionFormRawValue['isCached']>;
  detectedDate: FormControl<LanguageDetectionFormRawValue['detectedDate']>;
  modelVersion: FormControl<LanguageDetectionFormRawValue['modelVersion']>;
};

export type LanguageDetectionFormGroup = FormGroup<LanguageDetectionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LanguageDetectionFormService {
  createLanguageDetectionFormGroup(languageDetection: LanguageDetectionFormGroupInput = { id: null }): LanguageDetectionFormGroup {
    const languageDetectionRawValue = this.convertLanguageDetectionToLanguageDetectionRawValue({
      ...this.getFormDefaults(),
      ...languageDetection,
    });
    return new FormGroup<LanguageDetectionFormGroupContent>({
      id: new FormControl(
        { value: languageDetectionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(languageDetectionRawValue.documentId, {
        validators: [Validators.required],
      }),
      documentSha256: new FormControl(languageDetectionRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      detectedLanguage: new FormControl(languageDetectionRawValue.detectedLanguage, {
        validators: [Validators.required, Validators.maxLength(10)],
      }),
      confidence: new FormControl(languageDetectionRawValue.confidence, {
        validators: [Validators.required, Validators.min(0), Validators.max(1)],
      }),
      detectionMethod: new FormControl(languageDetectionRawValue.detectionMethod),
      alternativeLanguages: new FormControl(languageDetectionRawValue.alternativeLanguages),
      textSample: new FormControl(languageDetectionRawValue.textSample),
      resultCacheKey: new FormControl(languageDetectionRawValue.resultCacheKey, {
        validators: [Validators.maxLength(128)],
      }),
      isCached: new FormControl(languageDetectionRawValue.isCached, {
        validators: [Validators.required],
      }),
      detectedDate: new FormControl(languageDetectionRawValue.detectedDate, {
        validators: [Validators.required],
      }),
      modelVersion: new FormControl(languageDetectionRawValue.modelVersion, {
        validators: [Validators.maxLength(50)],
      }),
    });
  }

  getLanguageDetection(form: LanguageDetectionFormGroup): ILanguageDetection | NewLanguageDetection {
    return this.convertLanguageDetectionRawValueToLanguageDetection(
      form.getRawValue() as LanguageDetectionFormRawValue | NewLanguageDetectionFormRawValue,
    );
  }

  resetForm(form: LanguageDetectionFormGroup, languageDetection: LanguageDetectionFormGroupInput): void {
    const languageDetectionRawValue = this.convertLanguageDetectionToLanguageDetectionRawValue({
      ...this.getFormDefaults(),
      ...languageDetection,
    });
    form.reset(
      {
        ...languageDetectionRawValue,
        id: { value: languageDetectionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LanguageDetectionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isCached: false,
      detectedDate: currentTime,
    };
  }

  private convertLanguageDetectionRawValueToLanguageDetection(
    rawLanguageDetection: LanguageDetectionFormRawValue | NewLanguageDetectionFormRawValue,
  ): ILanguageDetection | NewLanguageDetection {
    return {
      ...rawLanguageDetection,
      detectedDate: dayjs(rawLanguageDetection.detectedDate, DATE_TIME_FORMAT),
    };
  }

  private convertLanguageDetectionToLanguageDetectionRawValue(
    languageDetection: ILanguageDetection | (Partial<NewLanguageDetection> & LanguageDetectionFormDefaults),
  ): LanguageDetectionFormRawValue | PartialWithRequiredKeyOf<NewLanguageDetectionFormRawValue> {
    return {
      ...languageDetection,
      detectedDate: languageDetection.detectedDate ? languageDetection.detectedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
