import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReportingSystemMetric, NewReportingSystemMetric } from '../reporting-system-metric.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportingSystemMetric for edit and NewReportingSystemMetricFormGroupInput for create.
 */
type ReportingSystemMetricFormGroupInput = IReportingSystemMetric | PartialWithRequiredKeyOf<NewReportingSystemMetric>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReportingSystemMetric | NewReportingSystemMetric> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type ReportingSystemMetricFormRawValue = FormValueOf<IReportingSystemMetric>;

type NewReportingSystemMetricFormRawValue = FormValueOf<NewReportingSystemMetric>;

type ReportingSystemMetricFormDefaults = Pick<NewReportingSystemMetric, 'id' | 'timestamp'>;

type ReportingSystemMetricFormGroupContent = {
  id: FormControl<ReportingSystemMetricFormRawValue['id'] | NewReportingSystemMetric['id']>;
  metricName: FormControl<ReportingSystemMetricFormRawValue['metricName']>;
  cpuUsage: FormControl<ReportingSystemMetricFormRawValue['cpuUsage']>;
  memoryUsage: FormControl<ReportingSystemMetricFormRawValue['memoryUsage']>;
  diskUsage: FormControl<ReportingSystemMetricFormRawValue['diskUsage']>;
  networkIn: FormControl<ReportingSystemMetricFormRawValue['networkIn']>;
  networkOut: FormControl<ReportingSystemMetricFormRawValue['networkOut']>;
  activeConnections: FormControl<ReportingSystemMetricFormRawValue['activeConnections']>;
  timestamp: FormControl<ReportingSystemMetricFormRawValue['timestamp']>;
};

export type ReportingSystemMetricFormGroup = FormGroup<ReportingSystemMetricFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportingSystemMetricFormService {
  createReportingSystemMetricFormGroup(
    reportingSystemMetric: ReportingSystemMetricFormGroupInput = { id: null },
  ): ReportingSystemMetricFormGroup {
    const reportingSystemMetricRawValue = this.convertReportingSystemMetricToReportingSystemMetricRawValue({
      ...this.getFormDefaults(),
      ...reportingSystemMetric,
    });
    return new FormGroup<ReportingSystemMetricFormGroupContent>({
      id: new FormControl(
        { value: reportingSystemMetricRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      metricName: new FormControl(reportingSystemMetricRawValue.metricName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      cpuUsage: new FormControl(reportingSystemMetricRawValue.cpuUsage),
      memoryUsage: new FormControl(reportingSystemMetricRawValue.memoryUsage),
      diskUsage: new FormControl(reportingSystemMetricRawValue.diskUsage),
      networkIn: new FormControl(reportingSystemMetricRawValue.networkIn),
      networkOut: new FormControl(reportingSystemMetricRawValue.networkOut),
      activeConnections: new FormControl(reportingSystemMetricRawValue.activeConnections),
      timestamp: new FormControl(reportingSystemMetricRawValue.timestamp, {
        validators: [Validators.required],
      }),
    });
  }

  getReportingSystemMetric(form: ReportingSystemMetricFormGroup): IReportingSystemMetric | NewReportingSystemMetric {
    return this.convertReportingSystemMetricRawValueToReportingSystemMetric(
      form.getRawValue() as ReportingSystemMetricFormRawValue | NewReportingSystemMetricFormRawValue,
    );
  }

  resetForm(form: ReportingSystemMetricFormGroup, reportingSystemMetric: ReportingSystemMetricFormGroupInput): void {
    const reportingSystemMetricRawValue = this.convertReportingSystemMetricToReportingSystemMetricRawValue({
      ...this.getFormDefaults(),
      ...reportingSystemMetric,
    });
    form.reset(
      {
        ...reportingSystemMetricRawValue,
        id: { value: reportingSystemMetricRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportingSystemMetricFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertReportingSystemMetricRawValueToReportingSystemMetric(
    rawReportingSystemMetric: ReportingSystemMetricFormRawValue | NewReportingSystemMetricFormRawValue,
  ): IReportingSystemMetric | NewReportingSystemMetric {
    return {
      ...rawReportingSystemMetric,
      timestamp: dayjs(rawReportingSystemMetric.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertReportingSystemMetricToReportingSystemMetricRawValue(
    reportingSystemMetric: IReportingSystemMetric | (Partial<NewReportingSystemMetric> & ReportingSystemMetricFormDefaults),
  ): ReportingSystemMetricFormRawValue | PartialWithRequiredKeyOf<NewReportingSystemMetricFormRawValue> {
    return {
      ...reportingSystemMetric,
      timestamp: reportingSystemMetric.timestamp ? reportingSystemMetric.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
