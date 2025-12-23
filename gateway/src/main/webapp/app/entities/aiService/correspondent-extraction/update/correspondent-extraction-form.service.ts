import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICorrespondentExtraction, NewCorrespondentExtraction } from '../correspondent-extraction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICorrespondentExtraction for edit and NewCorrespondentExtractionFormGroupInput for create.
 */
type CorrespondentExtractionFormGroupInput = ICorrespondentExtraction | PartialWithRequiredKeyOf<NewCorrespondentExtraction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICorrespondentExtraction | NewCorrespondentExtraction> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type CorrespondentExtractionFormRawValue = FormValueOf<ICorrespondentExtraction>;

type NewCorrespondentExtractionFormRawValue = FormValueOf<NewCorrespondentExtraction>;

type CorrespondentExtractionFormDefaults = Pick<NewCorrespondentExtraction, 'id' | 'isCached' | 'startDate' | 'endDate' | 'createdDate'>;

type CorrespondentExtractionFormGroupContent = {
  id: FormControl<CorrespondentExtractionFormRawValue['id'] | NewCorrespondentExtraction['id']>;
  documentId: FormControl<CorrespondentExtractionFormRawValue['documentId']>;
  documentSha256: FormControl<CorrespondentExtractionFormRawValue['documentSha256']>;
  extractedText: FormControl<CorrespondentExtractionFormRawValue['extractedText']>;
  extractedTextSha256: FormControl<CorrespondentExtractionFormRawValue['extractedTextSha256']>;
  detectedLanguage: FormControl<CorrespondentExtractionFormRawValue['detectedLanguage']>;
  languageConfidence: FormControl<CorrespondentExtractionFormRawValue['languageConfidence']>;
  status: FormControl<CorrespondentExtractionFormRawValue['status']>;
  resultCacheKey: FormControl<CorrespondentExtractionFormRawValue['resultCacheKey']>;
  isCached: FormControl<CorrespondentExtractionFormRawValue['isCached']>;
  resultS3Key: FormControl<CorrespondentExtractionFormRawValue['resultS3Key']>;
  startDate: FormControl<CorrespondentExtractionFormRawValue['startDate']>;
  endDate: FormControl<CorrespondentExtractionFormRawValue['endDate']>;
  errorMessage: FormControl<CorrespondentExtractionFormRawValue['errorMessage']>;
  sendersCount: FormControl<CorrespondentExtractionFormRawValue['sendersCount']>;
  recipientsCount: FormControl<CorrespondentExtractionFormRawValue['recipientsCount']>;
  createdDate: FormControl<CorrespondentExtractionFormRawValue['createdDate']>;
};

export type CorrespondentExtractionFormGroup = FormGroup<CorrespondentExtractionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CorrespondentExtractionFormService {
  createCorrespondentExtractionFormGroup(
    correspondentExtraction?: CorrespondentExtractionFormGroupInput,
  ): CorrespondentExtractionFormGroup {
    const correspondentExtractionRawValue = this.convertCorrespondentExtractionToCorrespondentExtractionRawValue({
      ...this.getFormDefaults(),
      ...(correspondentExtraction ?? { id: null }),
    });
    return new FormGroup<CorrespondentExtractionFormGroupContent>({
      id: new FormControl(
        { value: correspondentExtractionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(correspondentExtractionRawValue.documentId, {
        validators: [Validators.required],
      }),
      documentSha256: new FormControl(correspondentExtractionRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      extractedText: new FormControl(correspondentExtractionRawValue.extractedText, {
        validators: [Validators.required],
      }),
      extractedTextSha256: new FormControl(correspondentExtractionRawValue.extractedTextSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      detectedLanguage: new FormControl(correspondentExtractionRawValue.detectedLanguage, {
        validators: [Validators.maxLength(10)],
      }),
      languageConfidence: new FormControl(correspondentExtractionRawValue.languageConfidence, {
        validators: [Validators.min(0), Validators.max(1)],
      }),
      status: new FormControl(correspondentExtractionRawValue.status),
      resultCacheKey: new FormControl(correspondentExtractionRawValue.resultCacheKey, {
        validators: [Validators.maxLength(128)],
      }),
      isCached: new FormControl(correspondentExtractionRawValue.isCached, {
        validators: [Validators.required],
      }),
      resultS3Key: new FormControl(correspondentExtractionRawValue.resultS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      startDate: new FormControl(correspondentExtractionRawValue.startDate),
      endDate: new FormControl(correspondentExtractionRawValue.endDate),
      errorMessage: new FormControl(correspondentExtractionRawValue.errorMessage),
      sendersCount: new FormControl(correspondentExtractionRawValue.sendersCount),
      recipientsCount: new FormControl(correspondentExtractionRawValue.recipientsCount),
      createdDate: new FormControl(correspondentExtractionRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getCorrespondentExtraction(form: CorrespondentExtractionFormGroup): ICorrespondentExtraction | NewCorrespondentExtraction {
    return this.convertCorrespondentExtractionRawValueToCorrespondentExtraction(
      form.getRawValue() as CorrespondentExtractionFormRawValue | NewCorrespondentExtractionFormRawValue,
    );
  }

  resetForm(form: CorrespondentExtractionFormGroup, correspondentExtraction: CorrespondentExtractionFormGroupInput): void {
    const correspondentExtractionRawValue = this.convertCorrespondentExtractionToCorrespondentExtractionRawValue({
      ...this.getFormDefaults(),
      ...correspondentExtraction,
    });
    form.reset({
      ...correspondentExtractionRawValue,
      id: { value: correspondentExtractionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CorrespondentExtractionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isCached: false,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertCorrespondentExtractionRawValueToCorrespondentExtraction(
    rawCorrespondentExtraction: CorrespondentExtractionFormRawValue | NewCorrespondentExtractionFormRawValue,
  ): ICorrespondentExtraction | NewCorrespondentExtraction {
    return {
      ...rawCorrespondentExtraction,
      startDate: dayjs(rawCorrespondentExtraction.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawCorrespondentExtraction.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawCorrespondentExtraction.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertCorrespondentExtractionToCorrespondentExtractionRawValue(
    correspondentExtraction: ICorrespondentExtraction | (Partial<NewCorrespondentExtraction> & CorrespondentExtractionFormDefaults),
  ): CorrespondentExtractionFormRawValue | PartialWithRequiredKeyOf<NewCorrespondentExtractionFormRawValue> {
    return {
      ...correspondentExtraction,
      startDate: correspondentExtraction.startDate ? correspondentExtraction.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: correspondentExtraction.endDate ? correspondentExtraction.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: correspondentExtraction.createdDate ? correspondentExtraction.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
