import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPerformanceMetric, NewPerformanceMetric } from '../performance-metric.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPerformanceMetric for edit and NewPerformanceMetricFormGroupInput for create.
 */
type PerformanceMetricFormGroupInput = IPerformanceMetric | PartialWithRequiredKeyOf<NewPerformanceMetric>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPerformanceMetric | NewPerformanceMetric> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type PerformanceMetricFormRawValue = FormValueOf<IPerformanceMetric>;

type NewPerformanceMetricFormRawValue = FormValueOf<NewPerformanceMetric>;

type PerformanceMetricFormDefaults = Pick<NewPerformanceMetric, 'id' | 'timestamp'>;

type PerformanceMetricFormGroupContent = {
  id: FormControl<PerformanceMetricFormRawValue['id'] | NewPerformanceMetric['id']>;
  metricName: FormControl<PerformanceMetricFormRawValue['metricName']>;
  metricType: FormControl<PerformanceMetricFormRawValue['metricType']>;
  value: FormControl<PerformanceMetricFormRawValue['value']>;
  unit: FormControl<PerformanceMetricFormRawValue['unit']>;
  serviceName: FormControl<PerformanceMetricFormRawValue['serviceName']>;
  timestamp: FormControl<PerformanceMetricFormRawValue['timestamp']>;
};

export type PerformanceMetricFormGroup = FormGroup<PerformanceMetricFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PerformanceMetricFormService {
  createPerformanceMetricFormGroup(performanceMetric?: PerformanceMetricFormGroupInput): PerformanceMetricFormGroup {
    const performanceMetricRawValue = this.convertPerformanceMetricToPerformanceMetricRawValue({
      ...this.getFormDefaults(),
      ...(performanceMetric ?? { id: null }),
    });
    return new FormGroup<PerformanceMetricFormGroupContent>({
      id: new FormControl(
        { value: performanceMetricRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      metricName: new FormControl(performanceMetricRawValue.metricName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      metricType: new FormControl(performanceMetricRawValue.metricType, {
        validators: [Validators.required],
      }),
      value: new FormControl(performanceMetricRawValue.value, {
        validators: [Validators.required],
      }),
      unit: new FormControl(performanceMetricRawValue.unit, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      serviceName: new FormControl(performanceMetricRawValue.serviceName, {
        validators: [Validators.maxLength(100)],
      }),
      timestamp: new FormControl(performanceMetricRawValue.timestamp, {
        validators: [Validators.required],
      }),
    });
  }

  getPerformanceMetric(form: PerformanceMetricFormGroup): IPerformanceMetric | NewPerformanceMetric {
    return this.convertPerformanceMetricRawValueToPerformanceMetric(
      form.getRawValue() as PerformanceMetricFormRawValue | NewPerformanceMetricFormRawValue,
    );
  }

  resetForm(form: PerformanceMetricFormGroup, performanceMetric: PerformanceMetricFormGroupInput): void {
    const performanceMetricRawValue = this.convertPerformanceMetricToPerformanceMetricRawValue({
      ...this.getFormDefaults(),
      ...performanceMetric,
    });
    form.reset({
      ...performanceMetricRawValue,
      id: { value: performanceMetricRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PerformanceMetricFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertPerformanceMetricRawValueToPerformanceMetric(
    rawPerformanceMetric: PerformanceMetricFormRawValue | NewPerformanceMetricFormRawValue,
  ): IPerformanceMetric | NewPerformanceMetric {
    return {
      ...rawPerformanceMetric,
      timestamp: dayjs(rawPerformanceMetric.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertPerformanceMetricToPerformanceMetricRawValue(
    performanceMetric: IPerformanceMetric | (Partial<NewPerformanceMetric> & PerformanceMetricFormDefaults),
  ): PerformanceMetricFormRawValue | PartialWithRequiredKeyOf<NewPerformanceMetricFormRawValue> {
    return {
      ...performanceMetric,
      timestamp: performanceMetric.timestamp ? performanceMetric.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
