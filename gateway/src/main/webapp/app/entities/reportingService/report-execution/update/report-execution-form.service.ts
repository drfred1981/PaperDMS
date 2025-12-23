import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReportExecution, NewReportExecution } from '../report-execution.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportExecution for edit and NewReportExecutionFormGroupInput for create.
 */
type ReportExecutionFormGroupInput = IReportExecution | PartialWithRequiredKeyOf<NewReportExecution>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReportExecution | NewReportExecution> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type ReportExecutionFormRawValue = FormValueOf<IReportExecution>;

type NewReportExecutionFormRawValue = FormValueOf<NewReportExecution>;

type ReportExecutionFormDefaults = Pick<NewReportExecution, 'id' | 'startDate' | 'endDate'>;

type ReportExecutionFormGroupContent = {
  id: FormControl<ReportExecutionFormRawValue['id'] | NewReportExecution['id']>;
  scheduledReportId: FormControl<ReportExecutionFormRawValue['scheduledReportId']>;
  status: FormControl<ReportExecutionFormRawValue['status']>;
  startDate: FormControl<ReportExecutionFormRawValue['startDate']>;
  endDate: FormControl<ReportExecutionFormRawValue['endDate']>;
  recordsProcessed: FormControl<ReportExecutionFormRawValue['recordsProcessed']>;
  outputS3Key: FormControl<ReportExecutionFormRawValue['outputS3Key']>;
  outputSize: FormControl<ReportExecutionFormRawValue['outputSize']>;
  errorMessage: FormControl<ReportExecutionFormRawValue['errorMessage']>;
  scheduledReport: FormControl<ReportExecutionFormRawValue['scheduledReport']>;
};

export type ReportExecutionFormGroup = FormGroup<ReportExecutionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportExecutionFormService {
  createReportExecutionFormGroup(reportExecution?: ReportExecutionFormGroupInput): ReportExecutionFormGroup {
    const reportExecutionRawValue = this.convertReportExecutionToReportExecutionRawValue({
      ...this.getFormDefaults(),
      ...(reportExecution ?? { id: null }),
    });
    return new FormGroup<ReportExecutionFormGroupContent>({
      id: new FormControl(
        { value: reportExecutionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      scheduledReportId: new FormControl(reportExecutionRawValue.scheduledReportId, {
        validators: [Validators.required],
      }),
      status: new FormControl(reportExecutionRawValue.status, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(reportExecutionRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(reportExecutionRawValue.endDate),
      recordsProcessed: new FormControl(reportExecutionRawValue.recordsProcessed),
      outputS3Key: new FormControl(reportExecutionRawValue.outputS3Key, {
        validators: [Validators.maxLength(1000)],
      }),
      outputSize: new FormControl(reportExecutionRawValue.outputSize),
      errorMessage: new FormControl(reportExecutionRawValue.errorMessage),
      scheduledReport: new FormControl(reportExecutionRawValue.scheduledReport, {
        validators: [Validators.required],
      }),
    });
  }

  getReportExecution(form: ReportExecutionFormGroup): IReportExecution | NewReportExecution {
    return this.convertReportExecutionRawValueToReportExecution(
      form.getRawValue() as ReportExecutionFormRawValue | NewReportExecutionFormRawValue,
    );
  }

  resetForm(form: ReportExecutionFormGroup, reportExecution: ReportExecutionFormGroupInput): void {
    const reportExecutionRawValue = this.convertReportExecutionToReportExecutionRawValue({ ...this.getFormDefaults(), ...reportExecution });
    form.reset({
      ...reportExecutionRawValue,
      id: { value: reportExecutionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ReportExecutionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertReportExecutionRawValueToReportExecution(
    rawReportExecution: ReportExecutionFormRawValue | NewReportExecutionFormRawValue,
  ): IReportExecution | NewReportExecution {
    return {
      ...rawReportExecution,
      startDate: dayjs(rawReportExecution.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawReportExecution.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertReportExecutionToReportExecutionRawValue(
    reportExecution: IReportExecution | (Partial<NewReportExecution> & ReportExecutionFormDefaults),
  ): ReportExecutionFormRawValue | PartialWithRequiredKeyOf<NewReportExecutionFormRawValue> {
    return {
      ...reportExecution,
      startDate: reportExecution.startDate ? reportExecution.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: reportExecution.endDate ? reportExecution.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
