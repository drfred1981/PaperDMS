import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAICache, NewAICache } from '../ai-cache.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAICache for edit and NewAICacheFormGroupInput for create.
 */
type AICacheFormGroupInput = IAICache | PartialWithRequiredKeyOf<NewAICache>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAICache | NewAICache> = Omit<T, 'lastAccessDate' | 'createdDate' | 'expirationDate'> & {
  lastAccessDate?: string | null;
  createdDate?: string | null;
  expirationDate?: string | null;
};

type AICacheFormRawValue = FormValueOf<IAICache>;

type NewAICacheFormRawValue = FormValueOf<NewAICache>;

type AICacheFormDefaults = Pick<NewAICache, 'id' | 'lastAccessDate' | 'createdDate' | 'expirationDate'>;

type AICacheFormGroupContent = {
  id: FormControl<AICacheFormRawValue['id'] | NewAICache['id']>;
  cacheKey: FormControl<AICacheFormRawValue['cacheKey']>;
  inputSha256: FormControl<AICacheFormRawValue['inputSha256']>;
  aiProvider: FormControl<AICacheFormRawValue['aiProvider']>;
  aiModel: FormControl<AICacheFormRawValue['aiModel']>;
  operation: FormControl<AICacheFormRawValue['operation']>;
  inputData: FormControl<AICacheFormRawValue['inputData']>;
  resultData: FormControl<AICacheFormRawValue['resultData']>;
  s3ResultKey: FormControl<AICacheFormRawValue['s3ResultKey']>;
  confidence: FormControl<AICacheFormRawValue['confidence']>;
  metadata: FormControl<AICacheFormRawValue['metadata']>;
  hits: FormControl<AICacheFormRawValue['hits']>;
  cost: FormControl<AICacheFormRawValue['cost']>;
  lastAccessDate: FormControl<AICacheFormRawValue['lastAccessDate']>;
  createdDate: FormControl<AICacheFormRawValue['createdDate']>;
  expirationDate: FormControl<AICacheFormRawValue['expirationDate']>;
};

export type AICacheFormGroup = FormGroup<AICacheFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AICacheFormService {
  createAICacheFormGroup(aICache: AICacheFormGroupInput = { id: null }): AICacheFormGroup {
    const aICacheRawValue = this.convertAICacheToAICacheRawValue({
      ...this.getFormDefaults(),
      ...aICache,
    });
    return new FormGroup<AICacheFormGroupContent>({
      id: new FormControl(
        { value: aICacheRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      cacheKey: new FormControl(aICacheRawValue.cacheKey, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      inputSha256: new FormControl(aICacheRawValue.inputSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      aiProvider: new FormControl(aICacheRawValue.aiProvider, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      aiModel: new FormControl(aICacheRawValue.aiModel, {
        validators: [Validators.maxLength(100)],
      }),
      operation: new FormControl(aICacheRawValue.operation, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      inputData: new FormControl(aICacheRawValue.inputData),
      resultData: new FormControl(aICacheRawValue.resultData),
      s3ResultKey: new FormControl(aICacheRawValue.s3ResultKey, {
        validators: [Validators.maxLength(1000)],
      }),
      confidence: new FormControl(aICacheRawValue.confidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      metadata: new FormControl(aICacheRawValue.metadata),
      hits: new FormControl(aICacheRawValue.hits),
      cost: new FormControl(aICacheRawValue.cost),
      lastAccessDate: new FormControl(aICacheRawValue.lastAccessDate),
      createdDate: new FormControl(aICacheRawValue.createdDate, {
        validators: [Validators.required],
      }),
      expirationDate: new FormControl(aICacheRawValue.expirationDate),
    });
  }

  getAICache(form: AICacheFormGroup): IAICache | NewAICache {
    return this.convertAICacheRawValueToAICache(form.getRawValue() as AICacheFormRawValue | NewAICacheFormRawValue);
  }

  resetForm(form: AICacheFormGroup, aICache: AICacheFormGroupInput): void {
    const aICacheRawValue = this.convertAICacheToAICacheRawValue({ ...this.getFormDefaults(), ...aICache });
    form.reset(
      {
        ...aICacheRawValue,
        id: { value: aICacheRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AICacheFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastAccessDate: currentTime,
      createdDate: currentTime,
      expirationDate: currentTime,
    };
  }

  private convertAICacheRawValueToAICache(rawAICache: AICacheFormRawValue | NewAICacheFormRawValue): IAICache | NewAICache {
    return {
      ...rawAICache,
      lastAccessDate: dayjs(rawAICache.lastAccessDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawAICache.createdDate, DATE_TIME_FORMAT),
      expirationDate: dayjs(rawAICache.expirationDate, DATE_TIME_FORMAT),
    };
  }

  private convertAICacheToAICacheRawValue(
    aICache: IAICache | (Partial<NewAICache> & AICacheFormDefaults),
  ): AICacheFormRawValue | PartialWithRequiredKeyOf<NewAICacheFormRawValue> {
    return {
      ...aICache,
      lastAccessDate: aICache.lastAccessDate ? aICache.lastAccessDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: aICache.createdDate ? aICache.createdDate.format(DATE_TIME_FORMAT) : undefined,
      expirationDate: aICache.expirationDate ? aICache.expirationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
