import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOrcExtractedText, NewOrcExtractedText } from '../orc-extracted-text.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrcExtractedText for edit and NewOrcExtractedTextFormGroupInput for create.
 */
type OrcExtractedTextFormGroupInput = IOrcExtractedText | PartialWithRequiredKeyOf<NewOrcExtractedText>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrcExtractedText | NewOrcExtractedText> = Omit<T, 'extractedDate'> & {
  extractedDate?: string | null;
};

type OrcExtractedTextFormRawValue = FormValueOf<IOrcExtractedText>;

type NewOrcExtractedTextFormRawValue = FormValueOf<NewOrcExtractedText>;

type OrcExtractedTextFormDefaults = Pick<NewOrcExtractedText, 'id' | 'hasStructuredData' | 'extractedDate'>;

type OrcExtractedTextFormGroupContent = {
  id: FormControl<OrcExtractedTextFormRawValue['id'] | NewOrcExtractedText['id']>;
  content: FormControl<OrcExtractedTextFormRawValue['content']>;
  contentSha256: FormControl<OrcExtractedTextFormRawValue['contentSha256']>;
  s3ContentKey: FormControl<OrcExtractedTextFormRawValue['s3ContentKey']>;
  s3Bucket: FormControl<OrcExtractedTextFormRawValue['s3Bucket']>;
  pageNumber: FormControl<OrcExtractedTextFormRawValue['pageNumber']>;
  language: FormControl<OrcExtractedTextFormRawValue['language']>;
  wordCount: FormControl<OrcExtractedTextFormRawValue['wordCount']>;
  hasStructuredData: FormControl<OrcExtractedTextFormRawValue['hasStructuredData']>;
  structuredData: FormControl<OrcExtractedTextFormRawValue['structuredData']>;
  structuredDataS3Key: FormControl<OrcExtractedTextFormRawValue['structuredDataS3Key']>;
  extractedDate: FormControl<OrcExtractedTextFormRawValue['extractedDate']>;
  job: FormControl<OrcExtractedTextFormRawValue['job']>;
};

export type OrcExtractedTextFormGroup = FormGroup<OrcExtractedTextFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrcExtractedTextFormService {
  createOrcExtractedTextFormGroup(orcExtractedText: OrcExtractedTextFormGroupInput = { id: null }): OrcExtractedTextFormGroup {
    const orcExtractedTextRawValue = this.convertOrcExtractedTextToOrcExtractedTextRawValue({
      ...this.getFormDefaults(),
      ...orcExtractedText,
    });
    return new FormGroup<OrcExtractedTextFormGroupContent>({
      id: new FormControl(
        { value: orcExtractedTextRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      content: new FormControl(orcExtractedTextRawValue.content, {
        validators: [Validators.required],
      }),
      contentSha256: new FormControl(orcExtractedTextRawValue.contentSha256, {
        validators: [Validators.maxLength(64)],
      }),
      s3ContentKey: new FormControl(orcExtractedTextRawValue.s3ContentKey, {
        validators: [Validators.maxLength(1000)],
      }),
      s3Bucket: new FormControl(orcExtractedTextRawValue.s3Bucket, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      pageNumber: new FormControl(orcExtractedTextRawValue.pageNumber, {
        validators: [Validators.required],
      }),
      language: new FormControl(orcExtractedTextRawValue.language, {
        validators: [Validators.maxLength(10)],
      }),
      wordCount: new FormControl(orcExtractedTextRawValue.wordCount),
      hasStructuredData: new FormControl(orcExtractedTextRawValue.hasStructuredData),
      structuredData: new FormControl(orcExtractedTextRawValue.structuredData),
      structuredDataS3Key: new FormControl(orcExtractedTextRawValue.structuredDataS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      extractedDate: new FormControl(orcExtractedTextRawValue.extractedDate, {
        validators: [Validators.required],
      }),
      job: new FormControl(orcExtractedTextRawValue.job),
    });
  }

  getOrcExtractedText(form: OrcExtractedTextFormGroup): IOrcExtractedText | NewOrcExtractedText {
    return this.convertOrcExtractedTextRawValueToOrcExtractedText(
      form.getRawValue() as OrcExtractedTextFormRawValue | NewOrcExtractedTextFormRawValue,
    );
  }

  resetForm(form: OrcExtractedTextFormGroup, orcExtractedText: OrcExtractedTextFormGroupInput): void {
    const orcExtractedTextRawValue = this.convertOrcExtractedTextToOrcExtractedTextRawValue({
      ...this.getFormDefaults(),
      ...orcExtractedText,
    });
    form.reset(
      {
        ...orcExtractedTextRawValue,
        id: { value: orcExtractedTextRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrcExtractedTextFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      hasStructuredData: false,
      extractedDate: currentTime,
    };
  }

  private convertOrcExtractedTextRawValueToOrcExtractedText(
    rawOrcExtractedText: OrcExtractedTextFormRawValue | NewOrcExtractedTextFormRawValue,
  ): IOrcExtractedText | NewOrcExtractedText {
    return {
      ...rawOrcExtractedText,
      extractedDate: dayjs(rawOrcExtractedText.extractedDate, DATE_TIME_FORMAT),
    };
  }

  private convertOrcExtractedTextToOrcExtractedTextRawValue(
    orcExtractedText: IOrcExtractedText | (Partial<NewOrcExtractedText> & OrcExtractedTextFormDefaults),
  ): OrcExtractedTextFormRawValue | PartialWithRequiredKeyOf<NewOrcExtractedTextFormRawValue> {
    return {
      ...orcExtractedText,
      extractedDate: orcExtractedText.extractedDate ? orcExtractedText.extractedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
