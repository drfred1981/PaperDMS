import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMonitoringDocumentWatch, NewMonitoringDocumentWatch } from '../monitoring-document-watch.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMonitoringDocumentWatch for edit and NewMonitoringDocumentWatchFormGroupInput for create.
 */
type MonitoringDocumentWatchFormGroupInput = IMonitoringDocumentWatch | PartialWithRequiredKeyOf<NewMonitoringDocumentWatch>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMonitoringDocumentWatch | NewMonitoringDocumentWatch> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type MonitoringDocumentWatchFormRawValue = FormValueOf<IMonitoringDocumentWatch>;

type NewMonitoringDocumentWatchFormRawValue = FormValueOf<NewMonitoringDocumentWatch>;

type MonitoringDocumentWatchFormDefaults = Pick<
  NewMonitoringDocumentWatch,
  'id' | 'notifyOnView' | 'notifyOnDownload' | 'notifyOnModify' | 'notifyOnShare' | 'notifyOnDelete' | 'createdDate'
>;

type MonitoringDocumentWatchFormGroupContent = {
  id: FormControl<MonitoringDocumentWatchFormRawValue['id'] | NewMonitoringDocumentWatch['id']>;
  documentSha256: FormControl<MonitoringDocumentWatchFormRawValue['documentSha256']>;
  userId: FormControl<MonitoringDocumentWatchFormRawValue['userId']>;
  watchType: FormControl<MonitoringDocumentWatchFormRawValue['watchType']>;
  notifyOnView: FormControl<MonitoringDocumentWatchFormRawValue['notifyOnView']>;
  notifyOnDownload: FormControl<MonitoringDocumentWatchFormRawValue['notifyOnDownload']>;
  notifyOnModify: FormControl<MonitoringDocumentWatchFormRawValue['notifyOnModify']>;
  notifyOnShare: FormControl<MonitoringDocumentWatchFormRawValue['notifyOnShare']>;
  notifyOnDelete: FormControl<MonitoringDocumentWatchFormRawValue['notifyOnDelete']>;
  createdDate: FormControl<MonitoringDocumentWatchFormRawValue['createdDate']>;
};

export type MonitoringDocumentWatchFormGroup = FormGroup<MonitoringDocumentWatchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MonitoringDocumentWatchFormService {
  createMonitoringDocumentWatchFormGroup(
    monitoringDocumentWatch: MonitoringDocumentWatchFormGroupInput = { id: null },
  ): MonitoringDocumentWatchFormGroup {
    const monitoringDocumentWatchRawValue = this.convertMonitoringDocumentWatchToMonitoringDocumentWatchRawValue({
      ...this.getFormDefaults(),
      ...monitoringDocumentWatch,
    });
    return new FormGroup<MonitoringDocumentWatchFormGroupContent>({
      id: new FormControl(
        { value: monitoringDocumentWatchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentSha256: new FormControl(monitoringDocumentWatchRawValue.documentSha256, {
        validators: [Validators.maxLength(64)],
      }),
      userId: new FormControl(monitoringDocumentWatchRawValue.userId, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      watchType: new FormControl(monitoringDocumentWatchRawValue.watchType, {
        validators: [Validators.required],
      }),
      notifyOnView: new FormControl(monitoringDocumentWatchRawValue.notifyOnView, {
        validators: [Validators.required],
      }),
      notifyOnDownload: new FormControl(monitoringDocumentWatchRawValue.notifyOnDownload, {
        validators: [Validators.required],
      }),
      notifyOnModify: new FormControl(monitoringDocumentWatchRawValue.notifyOnModify, {
        validators: [Validators.required],
      }),
      notifyOnShare: new FormControl(monitoringDocumentWatchRawValue.notifyOnShare, {
        validators: [Validators.required],
      }),
      notifyOnDelete: new FormControl(monitoringDocumentWatchRawValue.notifyOnDelete, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(monitoringDocumentWatchRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getMonitoringDocumentWatch(form: MonitoringDocumentWatchFormGroup): IMonitoringDocumentWatch | NewMonitoringDocumentWatch {
    return this.convertMonitoringDocumentWatchRawValueToMonitoringDocumentWatch(
      form.getRawValue() as MonitoringDocumentWatchFormRawValue | NewMonitoringDocumentWatchFormRawValue,
    );
  }

  resetForm(form: MonitoringDocumentWatchFormGroup, monitoringDocumentWatch: MonitoringDocumentWatchFormGroupInput): void {
    const monitoringDocumentWatchRawValue = this.convertMonitoringDocumentWatchToMonitoringDocumentWatchRawValue({
      ...this.getFormDefaults(),
      ...monitoringDocumentWatch,
    });
    form.reset(
      {
        ...monitoringDocumentWatchRawValue,
        id: { value: monitoringDocumentWatchRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MonitoringDocumentWatchFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      notifyOnView: false,
      notifyOnDownload: false,
      notifyOnModify: false,
      notifyOnShare: false,
      notifyOnDelete: false,
      createdDate: currentTime,
    };
  }

  private convertMonitoringDocumentWatchRawValueToMonitoringDocumentWatch(
    rawMonitoringDocumentWatch: MonitoringDocumentWatchFormRawValue | NewMonitoringDocumentWatchFormRawValue,
  ): IMonitoringDocumentWatch | NewMonitoringDocumentWatch {
    return {
      ...rawMonitoringDocumentWatch,
      createdDate: dayjs(rawMonitoringDocumentWatch.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertMonitoringDocumentWatchToMonitoringDocumentWatchRawValue(
    monitoringDocumentWatch: IMonitoringDocumentWatch | (Partial<NewMonitoringDocumentWatch> & MonitoringDocumentWatchFormDefaults),
  ): MonitoringDocumentWatchFormRawValue | PartialWithRequiredKeyOf<NewMonitoringDocumentWatchFormRawValue> {
    return {
      ...monitoringDocumentWatch,
      createdDate: monitoringDocumentWatch.createdDate ? monitoringDocumentWatch.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
