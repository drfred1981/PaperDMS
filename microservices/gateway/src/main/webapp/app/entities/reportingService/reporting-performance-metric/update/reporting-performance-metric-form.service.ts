import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReportingPerformanceMetric, NewReportingPerformanceMetric } from '../reporting-performance-metric.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportingPerformanceMetric for edit and NewReportingPerformanceMetricFormGroupInput for create.
 */
type ReportingPerformanceMetricFormGroupInput = IReportingPerformanceMetric | PartialWithRequiredKeyOf<NewReportingPerformanceMetric>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReportingPerformanceMetric | NewReportingPerformanceMetric> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type ReportingPerformanceMetricFormRawValue = FormValueOf<IReportingPerformanceMetric>;

type NewReportingPerformanceMetricFormRawValue = FormValueOf<NewReportingPerformanceMetric>;

type ReportingPerformanceMetricFormDefaults = Pick<NewReportingPerformanceMetric, 'id' | 'timestamp'>;

type ReportingPerformanceMetricFormGroupContent = {
  id: FormControl<ReportingPerformanceMetricFormRawValue['id'] | NewReportingPerformanceMetric['id']>;
  metricName: FormControl<ReportingPerformanceMetricFormRawValue['metricName']>;
  metricType: FormControl<ReportingPerformanceMetricFormRawValue['metricType']>;
  value: FormControl<ReportingPerformanceMetricFormRawValue['value']>;
  unit: FormControl<ReportingPerformanceMetricFormRawValue['unit']>;
  serviceName: FormControl<ReportingPerformanceMetricFormRawValue['serviceName']>;
  timestamp: FormControl<ReportingPerformanceMetricFormRawValue['timestamp']>;
};

export type ReportingPerformanceMetricFormGroup = FormGroup<ReportingPerformanceMetricFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportingPerformanceMetricFormService {
  createReportingPerformanceMetricFormGroup(
    reportingPerformanceMetric: ReportingPerformanceMetricFormGroupInput = { id: null },
  ): ReportingPerformanceMetricFormGroup {
    const reportingPerformanceMetricRawValue = this.convertReportingPerformanceMetricToReportingPerformanceMetricRawValue({
      ...this.getFormDefaults(),
      ...reportingPerformanceMetric,
    });
    return new FormGroup<ReportingPerformanceMetricFormGroupContent>({
      id: new FormControl(
        { value: reportingPerformanceMetricRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      metricName: new FormControl(reportingPerformanceMetricRawValue.metricName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      metricType: new FormControl(reportingPerformanceMetricRawValue.metricType, {
        validators: [Validators.required],
      }),
      value: new FormControl(reportingPerformanceMetricRawValue.value, {
        validators: [Validators.required],
      }),
      unit: new FormControl(reportingPerformanceMetricRawValue.unit, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      serviceName: new FormControl(reportingPerformanceMetricRawValue.serviceName, {
        validators: [Validators.maxLength(100)],
      }),
      timestamp: new FormControl(reportingPerformanceMetricRawValue.timestamp, {
        validators: [Validators.required],
      }),
    });
  }

  getReportingPerformanceMetric(form: ReportingPerformanceMetricFormGroup): IReportingPerformanceMetric | NewReportingPerformanceMetric {
    return this.convertReportingPerformanceMetricRawValueToReportingPerformanceMetric(
      form.getRawValue() as ReportingPerformanceMetricFormRawValue | NewReportingPerformanceMetricFormRawValue,
    );
  }

  resetForm(form: ReportingPerformanceMetricFormGroup, reportingPerformanceMetric: ReportingPerformanceMetricFormGroupInput): void {
    const reportingPerformanceMetricRawValue = this.convertReportingPerformanceMetricToReportingPerformanceMetricRawValue({
      ...this.getFormDefaults(),
      ...reportingPerformanceMetric,
    });
    form.reset(
      {
        ...reportingPerformanceMetricRawValue,
        id: { value: reportingPerformanceMetricRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportingPerformanceMetricFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertReportingPerformanceMetricRawValueToReportingPerformanceMetric(
    rawReportingPerformanceMetric: ReportingPerformanceMetricFormRawValue | NewReportingPerformanceMetricFormRawValue,
  ): IReportingPerformanceMetric | NewReportingPerformanceMetric {
    return {
      ...rawReportingPerformanceMetric,
      timestamp: dayjs(rawReportingPerformanceMetric.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertReportingPerformanceMetricToReportingPerformanceMetricRawValue(
    reportingPerformanceMetric:
      | IReportingPerformanceMetric
      | (Partial<NewReportingPerformanceMetric> & ReportingPerformanceMetricFormDefaults),
  ): ReportingPerformanceMetricFormRawValue | PartialWithRequiredKeyOf<NewReportingPerformanceMetricFormRawValue> {
    return {
      ...reportingPerformanceMetric,
      timestamp: reportingPerformanceMetric.timestamp ? reportingPerformanceMetric.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
