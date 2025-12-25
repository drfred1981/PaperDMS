import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExtractedText, NewExtractedText } from '../extracted-text.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExtractedText for edit and NewExtractedTextFormGroupInput for create.
 */
type ExtractedTextFormGroupInput = IExtractedText | PartialWithRequiredKeyOf<NewExtractedText>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExtractedText | NewExtractedText> = Omit<T, 'extractedDate'> & {
  extractedDate?: string | null;
};

type ExtractedTextFormRawValue = FormValueOf<IExtractedText>;

type NewExtractedTextFormRawValue = FormValueOf<NewExtractedText>;

type ExtractedTextFormDefaults = Pick<NewExtractedText, 'id' | 'hasStructuredData' | 'extractedDate'>;

type ExtractedTextFormGroupContent = {
  id: FormControl<ExtractedTextFormRawValue['id'] | NewExtractedText['id']>;
  content: FormControl<ExtractedTextFormRawValue['content']>;
  contentSha256: FormControl<ExtractedTextFormRawValue['contentSha256']>;
  s3ContentKey: FormControl<ExtractedTextFormRawValue['s3ContentKey']>;
  s3Bucket: FormControl<ExtractedTextFormRawValue['s3Bucket']>;
  pageNumber: FormControl<ExtractedTextFormRawValue['pageNumber']>;
  language: FormControl<ExtractedTextFormRawValue['language']>;
  wordCount: FormControl<ExtractedTextFormRawValue['wordCount']>;
  hasStructuredData: FormControl<ExtractedTextFormRawValue['hasStructuredData']>;
  structuredData: FormControl<ExtractedTextFormRawValue['structuredData']>;
  structuredDataS3Key: FormControl<ExtractedTextFormRawValue['structuredDataS3Key']>;
  extractedDate: FormControl<ExtractedTextFormRawValue['extractedDate']>;
  job: FormControl<ExtractedTextFormRawValue['job']>;
};

export type ExtractedTextFormGroup = FormGroup<ExtractedTextFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExtractedTextFormService {
  createExtractedTextFormGroup(extractedText: ExtractedTextFormGroupInput = { id: null }): ExtractedTextFormGroup {
    const extractedTextRawValue = this.convertExtractedTextToExtractedTextRawValue({
      ...this.getFormDefaults(),
      ...extractedText,
    });
    return new FormGroup<ExtractedTextFormGroupContent>({
      id: new FormControl(
        { value: extractedTextRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      content: new FormControl(extractedTextRawValue.content, {
        validators: [Validators.required],
      }),
      contentSha256: new FormControl(extractedTextRawValue.contentSha256, {
        validators: [Validators.maxLength(64)],
      }),
      s3ContentKey: new FormControl(extractedTextRawValue.s3ContentKey, {
        validators: [Validators.maxLength(1000)],
      }),
      s3Bucket: new FormControl(extractedTextRawValue.s3Bucket, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      pageNumber: new FormControl(extractedTextRawValue.pageNumber, {
        validators: [Validators.required],
      }),
      language: new FormControl(extractedTextRawValue.language, {
        validators: [Validators.maxLength(10)],
      }),
      wordCount: new FormControl(extractedTextRawValue.wordCount),
      hasStructuredData: new FormControl(extractedTextRawValue.hasStructuredData),
      structuredData: new FormControl(extractedTextRawValue.structuredData),
      structuredDataS3Key: new FormControl(extractedTextRawValue.structuredDataS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      extractedDate: new FormControl(extractedTextRawValue.extractedDate, {
        validators: [Validators.required],
      }),
      job: new FormControl(extractedTextRawValue.job, {
        validators: [Validators.required],
      }),
    });
  }

  getExtractedText(form: ExtractedTextFormGroup): IExtractedText | NewExtractedText {
    return this.convertExtractedTextRawValueToExtractedText(form.getRawValue() as ExtractedTextFormRawValue | NewExtractedTextFormRawValue);
  }

  resetForm(form: ExtractedTextFormGroup, extractedText: ExtractedTextFormGroupInput): void {
    const extractedTextRawValue = this.convertExtractedTextToExtractedTextRawValue({ ...this.getFormDefaults(), ...extractedText });
    form.reset(
      {
        ...extractedTextRawValue,
        id: { value: extractedTextRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExtractedTextFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      hasStructuredData: false,
      extractedDate: currentTime,
    };
  }

  private convertExtractedTextRawValueToExtractedText(
    rawExtractedText: ExtractedTextFormRawValue | NewExtractedTextFormRawValue,
  ): IExtractedText | NewExtractedText {
    return {
      ...rawExtractedText,
      extractedDate: dayjs(rawExtractedText.extractedDate, DATE_TIME_FORMAT),
    };
  }

  private convertExtractedTextToExtractedTextRawValue(
    extractedText: IExtractedText | (Partial<NewExtractedText> & ExtractedTextFormDefaults),
  ): ExtractedTextFormRawValue | PartialWithRequiredKeyOf<NewExtractedTextFormRawValue> {
    return {
      ...extractedText,
      extractedDate: extractedText.extractedDate ? extractedText.extractedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
