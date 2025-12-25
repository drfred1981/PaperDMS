import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAiCache, NewAiCache } from '../ai-cache.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAiCache for edit and NewAiCacheFormGroupInput for create.
 */
type AiCacheFormGroupInput = IAiCache | PartialWithRequiredKeyOf<NewAiCache>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAiCache | NewAiCache> = Omit<T, 'lastAccessDate' | 'createdDate' | 'expirationDate'> & {
  lastAccessDate?: string | null;
  createdDate?: string | null;
  expirationDate?: string | null;
};

type AiCacheFormRawValue = FormValueOf<IAiCache>;

type NewAiCacheFormRawValue = FormValueOf<NewAiCache>;

type AiCacheFormDefaults = Pick<NewAiCache, 'id' | 'lastAccessDate' | 'createdDate' | 'expirationDate'>;

type AiCacheFormGroupContent = {
  id: FormControl<AiCacheFormRawValue['id'] | NewAiCache['id']>;
  cacheKey: FormControl<AiCacheFormRawValue['cacheKey']>;
  inputSha256: FormControl<AiCacheFormRawValue['inputSha256']>;
  aiProvider: FormControl<AiCacheFormRawValue['aiProvider']>;
  aiModel: FormControl<AiCacheFormRawValue['aiModel']>;
  operation: FormControl<AiCacheFormRawValue['operation']>;
  inputData: FormControl<AiCacheFormRawValue['inputData']>;
  resultData: FormControl<AiCacheFormRawValue['resultData']>;
  s3ResultKey: FormControl<AiCacheFormRawValue['s3ResultKey']>;
  confidence: FormControl<AiCacheFormRawValue['confidence']>;
  metadata: FormControl<AiCacheFormRawValue['metadata']>;
  hits: FormControl<AiCacheFormRawValue['hits']>;
  cost: FormControl<AiCacheFormRawValue['cost']>;
  lastAccessDate: FormControl<AiCacheFormRawValue['lastAccessDate']>;
  createdDate: FormControl<AiCacheFormRawValue['createdDate']>;
  expirationDate: FormControl<AiCacheFormRawValue['expirationDate']>;
};

export type AiCacheFormGroup = FormGroup<AiCacheFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AiCacheFormService {
  createAiCacheFormGroup(aiCache: AiCacheFormGroupInput = { id: null }): AiCacheFormGroup {
    const aiCacheRawValue = this.convertAiCacheToAiCacheRawValue({
      ...this.getFormDefaults(),
      ...aiCache,
    });
    return new FormGroup<AiCacheFormGroupContent>({
      id: new FormControl(
        { value: aiCacheRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      cacheKey: new FormControl(aiCacheRawValue.cacheKey, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      inputSha256: new FormControl(aiCacheRawValue.inputSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      aiProvider: new FormControl(aiCacheRawValue.aiProvider, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      aiModel: new FormControl(aiCacheRawValue.aiModel, {
        validators: [Validators.maxLength(100)],
      }),
      operation: new FormControl(aiCacheRawValue.operation, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      inputData: new FormControl(aiCacheRawValue.inputData),
      resultData: new FormControl(aiCacheRawValue.resultData),
      s3ResultKey: new FormControl(aiCacheRawValue.s3ResultKey, {
        validators: [Validators.maxLength(1000)],
      }),
      confidence: new FormControl(aiCacheRawValue.confidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      metadata: new FormControl(aiCacheRawValue.metadata),
      hits: new FormControl(aiCacheRawValue.hits),
      cost: new FormControl(aiCacheRawValue.cost),
      lastAccessDate: new FormControl(aiCacheRawValue.lastAccessDate),
      createdDate: new FormControl(aiCacheRawValue.createdDate, {
        validators: [Validators.required],
      }),
      expirationDate: new FormControl(aiCacheRawValue.expirationDate),
    });
  }

  getAiCache(form: AiCacheFormGroup): IAiCache | NewAiCache {
    return this.convertAiCacheRawValueToAiCache(form.getRawValue() as AiCacheFormRawValue | NewAiCacheFormRawValue);
  }

  resetForm(form: AiCacheFormGroup, aiCache: AiCacheFormGroupInput): void {
    const aiCacheRawValue = this.convertAiCacheToAiCacheRawValue({ ...this.getFormDefaults(), ...aiCache });
    form.reset(
      {
        ...aiCacheRawValue,
        id: { value: aiCacheRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AiCacheFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastAccessDate: currentTime,
      createdDate: currentTime,
      expirationDate: currentTime,
    };
  }

  private convertAiCacheRawValueToAiCache(rawAiCache: AiCacheFormRawValue | NewAiCacheFormRawValue): IAiCache | NewAiCache {
    return {
      ...rawAiCache,
      lastAccessDate: dayjs(rawAiCache.lastAccessDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawAiCache.createdDate, DATE_TIME_FORMAT),
      expirationDate: dayjs(rawAiCache.expirationDate, DATE_TIME_FORMAT),
    };
  }

  private convertAiCacheToAiCacheRawValue(
    aiCache: IAiCache | (Partial<NewAiCache> & AiCacheFormDefaults),
  ): AiCacheFormRawValue | PartialWithRequiredKeyOf<NewAiCacheFormRawValue> {
    return {
      ...aiCache,
      lastAccessDate: aiCache.lastAccessDate ? aiCache.lastAccessDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: aiCache.createdDate ? aiCache.createdDate.format(DATE_TIME_FORMAT) : undefined,
      expirationDate: aiCache.expirationDate ? aiCache.expirationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
