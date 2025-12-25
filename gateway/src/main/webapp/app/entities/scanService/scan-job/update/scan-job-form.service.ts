import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IScanJob, NewScanJob } from '../scan-job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IScanJob for edit and NewScanJobFormGroupInput for create.
 */
type ScanJobFormGroupInput = IScanJob | PartialWithRequiredKeyOf<NewScanJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IScanJob | NewScanJob> = Omit<T, 'startDate' | 'endDate' | 'createdDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
};

type ScanJobFormRawValue = FormValueOf<IScanJob>;

type NewScanJobFormRawValue = FormValueOf<NewScanJob>;

type ScanJobFormDefaults = Pick<NewScanJob, 'id' | 'startDate' | 'endDate' | 'createdDate'>;

type ScanJobFormGroupContent = {
  id: FormControl<ScanJobFormRawValue['id'] | NewScanJob['id']>;
  name: FormControl<ScanJobFormRawValue['name']>;
  description: FormControl<ScanJobFormRawValue['description']>;
  scannerConfigId: FormControl<ScanJobFormRawValue['scannerConfigId']>;
  batchId: FormControl<ScanJobFormRawValue['batchId']>;
  documentTypeId: FormControl<ScanJobFormRawValue['documentTypeId']>;
  folderId: FormControl<ScanJobFormRawValue['folderId']>;
  pageCount: FormControl<ScanJobFormRawValue['pageCount']>;
  status: FormControl<ScanJobFormRawValue['status']>;
  colorMode: FormControl<ScanJobFormRawValue['colorMode']>;
  resolution: FormControl<ScanJobFormRawValue['resolution']>;
  fileFormat: FormControl<ScanJobFormRawValue['fileFormat']>;
  startDate: FormControl<ScanJobFormRawValue['startDate']>;
  endDate: FormControl<ScanJobFormRawValue['endDate']>;
  errorMessage: FormControl<ScanJobFormRawValue['errorMessage']>;
  createdBy: FormControl<ScanJobFormRawValue['createdBy']>;
  createdDate: FormControl<ScanJobFormRawValue['createdDate']>;
  scannerConfig: FormControl<ScanJobFormRawValue['scannerConfig']>;
  batch: FormControl<ScanJobFormRawValue['batch']>;
};

export type ScanJobFormGroup = FormGroup<ScanJobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ScanJobFormService {
  createScanJobFormGroup(scanJob: ScanJobFormGroupInput = { id: null }): ScanJobFormGroup {
    const scanJobRawValue = this.convertScanJobToScanJobRawValue({
      ...this.getFormDefaults(),
      ...scanJob,
    });
    return new FormGroup<ScanJobFormGroupContent>({
      id: new FormControl(
        { value: scanJobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(scanJobRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(scanJobRawValue.description),
      scannerConfigId: new FormControl(scanJobRawValue.scannerConfigId, {
        validators: [Validators.required],
      }),
      batchId: new FormControl(scanJobRawValue.batchId),
      documentTypeId: new FormControl(scanJobRawValue.documentTypeId),
      folderId: new FormControl(scanJobRawValue.folderId),
      pageCount: new FormControl(scanJobRawValue.pageCount),
      status: new FormControl(scanJobRawValue.status, {
        validators: [Validators.required],
      }),
      colorMode: new FormControl(scanJobRawValue.colorMode),
      resolution: new FormControl(scanJobRawValue.resolution),
      fileFormat: new FormControl(scanJobRawValue.fileFormat),
      startDate: new FormControl(scanJobRawValue.startDate),
      endDate: new FormControl(scanJobRawValue.endDate),
      errorMessage: new FormControl(scanJobRawValue.errorMessage),
      createdBy: new FormControl(scanJobRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(scanJobRawValue.createdDate, {
        validators: [Validators.required],
      }),
      scannerConfig: new FormControl(scanJobRawValue.scannerConfig, {
        validators: [Validators.required],
      }),
      batch: new FormControl(scanJobRawValue.batch),
    });
  }

  getScanJob(form: ScanJobFormGroup): IScanJob | NewScanJob {
    return this.convertScanJobRawValueToScanJob(form.getRawValue() as ScanJobFormRawValue | NewScanJobFormRawValue);
  }

  resetForm(form: ScanJobFormGroup, scanJob: ScanJobFormGroupInput): void {
    const scanJobRawValue = this.convertScanJobToScanJobRawValue({ ...this.getFormDefaults(), ...scanJob });
    form.reset(
      {
        ...scanJobRawValue,
        id: { value: scanJobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ScanJobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertScanJobRawValueToScanJob(rawScanJob: ScanJobFormRawValue | NewScanJobFormRawValue): IScanJob | NewScanJob {
    return {
      ...rawScanJob,
      startDate: dayjs(rawScanJob.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawScanJob.endDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawScanJob.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertScanJobToScanJobRawValue(
    scanJob: IScanJob | (Partial<NewScanJob> & ScanJobFormDefaults),
  ): ScanJobFormRawValue | PartialWithRequiredKeyOf<NewScanJobFormRawValue> {
    return {
      ...scanJob,
      startDate: scanJob.startDate ? scanJob.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: scanJob.endDate ? scanJob.endDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: scanJob.createdDate ? scanJob.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
