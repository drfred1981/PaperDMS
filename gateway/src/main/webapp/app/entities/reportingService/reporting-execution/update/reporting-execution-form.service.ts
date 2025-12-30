import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReportingExecution, NewReportingExecution } from '../reporting-execution.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportingExecution for edit and NewReportingExecutionFormGroupInput for create.
 */
type ReportingExecutionFormGroupInput = IReportingExecution | PartialWithRequiredKeyOf<NewReportingExecution>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReportingExecution | NewReportingExecution> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type ReportingExecutionFormRawValue = FormValueOf<IReportingExecution>;

type NewReportingExecutionFormRawValue = FormValueOf<NewReportingExecution>;

type ReportingExecutionFormDefaults = Pick<NewReportingExecution, 'id' | 'startDate' | 'endDate'>;

type ReportingExecutionFormGroupContent = {
  id: FormControl<ReportingExecutionFormRawValue['id'] | NewReportingExecution['id']>;
  status: FormControl<ReportingExecutionFormRawValue['status']>;
  startDate: FormControl<ReportingExecutionFormRawValue['startDate']>;
  endDate: FormControl<ReportingExecutionFormRawValue['endDate']>;
  recordsProcessed: FormControl<ReportingExecutionFormRawValue['recordsProcessed']>;
  outputS3Key: FormControl<ReportingExecutionFormRawValue['outputS3Key']>;
  outputSize: FormControl<ReportingExecutionFormRawValue['outputSize']>;
  errorMessage: FormControl<ReportingExecutionFormRawValue['errorMessage']>;
  scheduledReport: FormControl<ReportingExecutionFormRawValue['scheduledReport']>;
};

export type ReportingExecutionFormGroup = FormGroup<ReportingExecutionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportingExecutionFormService {
  createReportingExecutionFormGroup(reportingExecution: ReportingExecutionFormGroupInput = { id: null }): ReportingExecutionFormGroup {
    const reportingExecutionRawValue = this.convertReportingExecutionToReportingExecutionRawValue({
      ...this.getFormDefaults(),
      ...reportingExecution,
    });
    return new FormGroup<ReportingExecutionFormGroupContent>({
      id: new FormControl(
        { value: reportingExecutionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(reportingExecutionRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(reportingExecutionRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(reportingExecutionRawValue.endDate),
      recordsProcessed: new FormControl(reportingExecutionRawValue.recordsProcessed),
      outputS3Key: new FormControl(reportingExecutionRawValue.outputS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      outputSize: new FormControl(reportingExecutionRawValue.outputSize),
      errorMessage: new FormControl(reportingExecutionRawValue.errorMessage),
      scheduledReport: new FormControl(reportingExecutionRawValue.scheduledReport),
    });
  }

  getReportingExecution(form: ReportingExecutionFormGroup): IReportingExecution | NewReportingExecution {
    return this.convertReportingExecutionRawValueToReportingExecution(
      form.getRawValue() as ReportingExecutionFormRawValue | NewReportingExecutionFormRawValue,
    );
  }

  resetForm(form: ReportingExecutionFormGroup, reportingExecution: ReportingExecutionFormGroupInput): void {
    const reportingExecutionRawValue = this.convertReportingExecutionToReportingExecutionRawValue({
      ...this.getFormDefaults(),
      ...reportingExecution,
    });
    form.reset(
      {
        ...reportingExecutionRawValue,
        id: { value: reportingExecutionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportingExecutionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertReportingExecutionRawValueToReportingExecution(
    rawReportingExecution: ReportingExecutionFormRawValue | NewReportingExecutionFormRawValue,
  ): IReportingExecution | NewReportingExecution {
    return {
      ...rawReportingExecution,
      startDate: dayjs(rawReportingExecution.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawReportingExecution.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertReportingExecutionToReportingExecutionRawValue(
    reportingExecution: IReportingExecution | (Partial<NewReportingExecution> & ReportingExecutionFormDefaults),
  ): ReportingExecutionFormRawValue | PartialWithRequiredKeyOf<NewReportingExecutionFormRawValue> {
    return {
      ...reportingExecution,
      startDate: reportingExecution.startDate ? reportingExecution.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: reportingExecution.endDate ? reportingExecution.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
