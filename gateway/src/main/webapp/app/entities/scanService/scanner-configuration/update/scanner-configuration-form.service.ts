import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IScannerConfiguration, NewScannerConfiguration } from '../scanner-configuration.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IScannerConfiguration for edit and NewScannerConfigurationFormGroupInput for create.
 */
type ScannerConfigurationFormGroupInput = IScannerConfiguration | PartialWithRequiredKeyOf<NewScannerConfiguration>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IScannerConfiguration | NewScannerConfiguration> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ScannerConfigurationFormRawValue = FormValueOf<IScannerConfiguration>;

type NewScannerConfigurationFormRawValue = FormValueOf<NewScannerConfiguration>;

type ScannerConfigurationFormDefaults = Pick<NewScannerConfiguration, 'id' | 'isActive' | 'createdDate' | 'lastModifiedDate'>;

type ScannerConfigurationFormGroupContent = {
  id: FormControl<ScannerConfigurationFormRawValue['id'] | NewScannerConfiguration['id']>;
  name: FormControl<ScannerConfigurationFormRawValue['name']>;
  scannerType: FormControl<ScannerConfigurationFormRawValue['scannerType']>;
  ipAddress: FormControl<ScannerConfigurationFormRawValue['ipAddress']>;
  port: FormControl<ScannerConfigurationFormRawValue['port']>;
  protocol: FormControl<ScannerConfigurationFormRawValue['protocol']>;
  manufacturer: FormControl<ScannerConfigurationFormRawValue['manufacturer']>;
  model: FormControl<ScannerConfigurationFormRawValue['model']>;
  defaultColorMode: FormControl<ScannerConfigurationFormRawValue['defaultColorMode']>;
  defaultResolution: FormControl<ScannerConfigurationFormRawValue['defaultResolution']>;
  defaultFormat: FormControl<ScannerConfigurationFormRawValue['defaultFormat']>;
  capabilities: FormControl<ScannerConfigurationFormRawValue['capabilities']>;
  isActive: FormControl<ScannerConfigurationFormRawValue['isActive']>;
  createdDate: FormControl<ScannerConfigurationFormRawValue['createdDate']>;
  lastModifiedDate: FormControl<ScannerConfigurationFormRawValue['lastModifiedDate']>;
};

export type ScannerConfigurationFormGroup = FormGroup<ScannerConfigurationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ScannerConfigurationFormService {
  createScannerConfigurationFormGroup(scannerConfiguration?: ScannerConfigurationFormGroupInput): ScannerConfigurationFormGroup {
    const scannerConfigurationRawValue = this.convertScannerConfigurationToScannerConfigurationRawValue({
      ...this.getFormDefaults(),
      ...(scannerConfiguration ?? { id: null }),
    });
    return new FormGroup<ScannerConfigurationFormGroupContent>({
      id: new FormControl(
        { value: scannerConfigurationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(scannerConfigurationRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      scannerType: new FormControl(scannerConfigurationRawValue.scannerType, {
        validators: [Validators.required],
      }),
      ipAddress: new FormControl(scannerConfigurationRawValue.ipAddress, {
        validators: [Validators.maxLength(45)],
      }),
      port: new FormControl(scannerConfigurationRawValue.port),
      protocol: new FormControl(scannerConfigurationRawValue.protocol, {
        validators: [Validators.maxLength(50)],
      }),
      manufacturer: new FormControl(scannerConfigurationRawValue.manufacturer, {
        validators: [Validators.maxLength(100)],
      }),
      model: new FormControl(scannerConfigurationRawValue.model, {
        validators: [Validators.maxLength(100)],
      }),
      defaultColorMode: new FormControl(scannerConfigurationRawValue.defaultColorMode),
      defaultResolution: new FormControl(scannerConfigurationRawValue.defaultResolution),
      defaultFormat: new FormControl(scannerConfigurationRawValue.defaultFormat),
      capabilities: new FormControl(scannerConfigurationRawValue.capabilities),
      isActive: new FormControl(scannerConfigurationRawValue.isActive, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(scannerConfigurationRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedDate: new FormControl(scannerConfigurationRawValue.lastModifiedDate),
    });
  }

  getScannerConfiguration(form: ScannerConfigurationFormGroup): IScannerConfiguration | NewScannerConfiguration {
    return this.convertScannerConfigurationRawValueToScannerConfiguration(
      form.getRawValue() as ScannerConfigurationFormRawValue | NewScannerConfigurationFormRawValue,
    );
  }

  resetForm(form: ScannerConfigurationFormGroup, scannerConfiguration: ScannerConfigurationFormGroupInput): void {
    const scannerConfigurationRawValue = this.convertScannerConfigurationToScannerConfigurationRawValue({
      ...this.getFormDefaults(),
      ...scannerConfiguration,
    });
    form.reset({
      ...scannerConfigurationRawValue,
      id: { value: scannerConfigurationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ScannerConfigurationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertScannerConfigurationRawValueToScannerConfiguration(
    rawScannerConfiguration: ScannerConfigurationFormRawValue | NewScannerConfigurationFormRawValue,
  ): IScannerConfiguration | NewScannerConfiguration {
    return {
      ...rawScannerConfiguration,
      createdDate: dayjs(rawScannerConfiguration.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawScannerConfiguration.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertScannerConfigurationToScannerConfigurationRawValue(
    scannerConfiguration: IScannerConfiguration | (Partial<NewScannerConfiguration> & ScannerConfigurationFormDefaults),
  ): ScannerConfigurationFormRawValue | PartialWithRequiredKeyOf<NewScannerConfigurationFormRawValue> {
    return {
      ...scannerConfiguration,
      createdDate: scannerConfiguration.createdDate ? scannerConfiguration.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: scannerConfiguration.lastModifiedDate ? scannerConfiguration.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
