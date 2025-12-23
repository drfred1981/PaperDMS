import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IScanBatch, NewScanBatch } from '../scan-batch.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IScanBatch for edit and NewScanBatchFormGroupInput for create.
 */
type ScanBatchFormGroupInput = IScanBatch | PartialWithRequiredKeyOf<NewScanBatch>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IScanBatch | NewScanBatch> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type ScanBatchFormRawValue = FormValueOf<IScanBatch>;

type NewScanBatchFormRawValue = FormValueOf<NewScanBatch>;

type ScanBatchFormDefaults = Pick<NewScanBatch, 'id' | 'createdDate'>;

type ScanBatchFormGroupContent = {
  id: FormControl<ScanBatchFormRawValue['id'] | NewScanBatch['id']>;
  name: FormControl<ScanBatchFormRawValue['name']>;
  description: FormControl<ScanBatchFormRawValue['description']>;
  totalJobs: FormControl<ScanBatchFormRawValue['totalJobs']>;
  completedJobs: FormControl<ScanBatchFormRawValue['completedJobs']>;
  totalPages: FormControl<ScanBatchFormRawValue['totalPages']>;
  status: FormControl<ScanBatchFormRawValue['status']>;
  createdBy: FormControl<ScanBatchFormRawValue['createdBy']>;
  createdDate: FormControl<ScanBatchFormRawValue['createdDate']>;
};

export type ScanBatchFormGroup = FormGroup<ScanBatchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ScanBatchFormService {
  createScanBatchFormGroup(scanBatch?: ScanBatchFormGroupInput): ScanBatchFormGroup {
    const scanBatchRawValue = this.convertScanBatchToScanBatchRawValue({
      ...this.getFormDefaults(),
      ...(scanBatch ?? { id: null }),
    });
    return new FormGroup<ScanBatchFormGroupContent>({
      id: new FormControl(
        { value: scanBatchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(scanBatchRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(scanBatchRawValue.description),
      totalJobs: new FormControl(scanBatchRawValue.totalJobs),
      completedJobs: new FormControl(scanBatchRawValue.completedJobs),
      totalPages: new FormControl(scanBatchRawValue.totalPages),
      status: new FormControl(scanBatchRawValue.status, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(scanBatchRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(scanBatchRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getScanBatch(form: ScanBatchFormGroup): IScanBatch | NewScanBatch {
    return this.convertScanBatchRawValueToScanBatch(form.getRawValue() as ScanBatchFormRawValue | NewScanBatchFormRawValue);
  }

  resetForm(form: ScanBatchFormGroup, scanBatch: ScanBatchFormGroupInput): void {
    const scanBatchRawValue = this.convertScanBatchToScanBatchRawValue({ ...this.getFormDefaults(), ...scanBatch });
    form.reset({
      ...scanBatchRawValue,
      id: { value: scanBatchRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ScanBatchFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertScanBatchRawValueToScanBatch(rawScanBatch: ScanBatchFormRawValue | NewScanBatchFormRawValue): IScanBatch | NewScanBatch {
    return {
      ...rawScanBatch,
      createdDate: dayjs(rawScanBatch.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertScanBatchToScanBatchRawValue(
    scanBatch: IScanBatch | (Partial<NewScanBatch> & ScanBatchFormDefaults),
  ): ScanBatchFormRawValue | PartialWithRequiredKeyOf<NewScanBatchFormRawValue> {
    return {
      ...scanBatch,
      createdDate: scanBatch.createdDate ? scanBatch.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
