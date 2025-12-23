import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExportResult, NewExportResult } from '../export-result.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExportResult for edit and NewExportResultFormGroupInput for create.
 */
type ExportResultFormGroupInput = IExportResult | PartialWithRequiredKeyOf<NewExportResult>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExportResult | NewExportResult> = Omit<T, 'exportedDate'> & {
  exportedDate?: string | null;
};

type ExportResultFormRawValue = FormValueOf<IExportResult>;

type NewExportResultFormRawValue = FormValueOf<NewExportResult>;

type ExportResultFormDefaults = Pick<NewExportResult, 'id' | 'exportedDate'>;

type ExportResultFormGroupContent = {
  id: FormControl<ExportResultFormRawValue['id'] | NewExportResult['id']>;
  exportJobId: FormControl<ExportResultFormRawValue['exportJobId']>;
  documentId: FormControl<ExportResultFormRawValue['documentId']>;
  documentSha256: FormControl<ExportResultFormRawValue['documentSha256']>;
  originalFileName: FormControl<ExportResultFormRawValue['originalFileName']>;
  exportedPath: FormControl<ExportResultFormRawValue['exportedPath']>;
  exportedFileName: FormControl<ExportResultFormRawValue['exportedFileName']>;
  s3ExportKey: FormControl<ExportResultFormRawValue['s3ExportKey']>;
  fileSize: FormControl<ExportResultFormRawValue['fileSize']>;
  status: FormControl<ExportResultFormRawValue['status']>;
  errorMessage: FormControl<ExportResultFormRawValue['errorMessage']>;
  exportedDate: FormControl<ExportResultFormRawValue['exportedDate']>;
  exportJob: FormControl<ExportResultFormRawValue['exportJob']>;
};

export type ExportResultFormGroup = FormGroup<ExportResultFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExportResultFormService {
  createExportResultFormGroup(exportResult?: ExportResultFormGroupInput): ExportResultFormGroup {
    const exportResultRawValue = this.convertExportResultToExportResultRawValue({
      ...this.getFormDefaults(),
      ...(exportResult ?? { id: null }),
    });
    return new FormGroup<ExportResultFormGroupContent>({
      id: new FormControl(
        { value: exportResultRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      exportJobId: new FormControl(exportResultRawValue.exportJobId, {
        validators: [Validators.required],
      }),
      documentId: new FormControl(exportResultRawValue.documentId, {
        validators: [Validators.required],
      }),
      documentSha256: new FormControl(exportResultRawValue.documentSha256, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      originalFileName: new FormControl(exportResultRawValue.originalFileName, {
        validators: [Validators.maxLength(500)],
      }),
      exportedPath: new FormControl(exportResultRawValue.exportedPath, {
        validators: [Validators.maxLength(1000)],
      }),
      exportedFileName: new FormControl(exportResultRawValue.exportedFileName, {
        validators: [Validators.maxLength(500)],
      }),
      s3ExportKey: new FormControl(exportResultRawValue.s3ExportKey, {
        validators: [Validators.maxLength(1000)],
      }),
      fileSize: new FormControl(exportResultRawValue.fileSize),
      status: new FormControl(exportResultRawValue.status),
      errorMessage: new FormControl(exportResultRawValue.errorMessage),
      exportedDate: new FormControl(exportResultRawValue.exportedDate, {
        validators: [Validators.required],
      }),
      exportJob: new FormControl(exportResultRawValue.exportJob, {
        validators: [Validators.required],
      }),
    });
  }

  getExportResult(form: ExportResultFormGroup): IExportResult | NewExportResult {
    return this.convertExportResultRawValueToExportResult(form.getRawValue() as ExportResultFormRawValue | NewExportResultFormRawValue);
  }

  resetForm(form: ExportResultFormGroup, exportResult: ExportResultFormGroupInput): void {
    const exportResultRawValue = this.convertExportResultToExportResultRawValue({ ...this.getFormDefaults(), ...exportResult });
    form.reset({
      ...exportResultRawValue,
      id: { value: exportResultRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ExportResultFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      exportedDate: currentTime,
    };
  }

  private convertExportResultRawValueToExportResult(
    rawExportResult: ExportResultFormRawValue | NewExportResultFormRawValue,
  ): IExportResult | NewExportResult {
    return {
      ...rawExportResult,
      exportedDate: dayjs(rawExportResult.exportedDate, DATE_TIME_FORMAT),
    };
  }

  private convertExportResultToExportResultRawValue(
    exportResult: IExportResult | (Partial<NewExportResult> & ExportResultFormDefaults),
  ): ExportResultFormRawValue | PartialWithRequiredKeyOf<NewExportResultFormRawValue> {
    return {
      ...exportResult,
      exportedDate: exportResult.exportedDate ? exportResult.exportedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
