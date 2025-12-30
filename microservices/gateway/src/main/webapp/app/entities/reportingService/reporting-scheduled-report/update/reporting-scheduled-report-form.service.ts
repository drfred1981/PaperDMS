import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReportingScheduledReport, NewReportingScheduledReport } from '../reporting-scheduled-report.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportingScheduledReport for edit and NewReportingScheduledReportFormGroupInput for create.
 */
type ReportingScheduledReportFormGroupInput = IReportingScheduledReport | PartialWithRequiredKeyOf<NewReportingScheduledReport>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReportingScheduledReport | NewReportingScheduledReport> = Omit<T, 'lastRun' | 'nextRun' | 'createdDate'> & {
  lastRun?: string | null;
  nextRun?: string | null;
  createdDate?: string | null;
};

type ReportingScheduledReportFormRawValue = FormValueOf<IReportingScheduledReport>;

type NewReportingScheduledReportFormRawValue = FormValueOf<NewReportingScheduledReport>;

type ReportingScheduledReportFormDefaults = Pick<NewReportingScheduledReport, 'id' | 'isActive' | 'lastRun' | 'nextRun' | 'createdDate'>;

type ReportingScheduledReportFormGroupContent = {
  id: FormControl<ReportingScheduledReportFormRawValue['id'] | NewReportingScheduledReport['id']>;
  name: FormControl<ReportingScheduledReportFormRawValue['name']>;
  description: FormControl<ReportingScheduledReportFormRawValue['description']>;
  reportType: FormControl<ReportingScheduledReportFormRawValue['reportType']>;
  query: FormControl<ReportingScheduledReportFormRawValue['query']>;
  schedule: FormControl<ReportingScheduledReportFormRawValue['schedule']>;
  format: FormControl<ReportingScheduledReportFormRawValue['format']>;
  recipients: FormControl<ReportingScheduledReportFormRawValue['recipients']>;
  isActive: FormControl<ReportingScheduledReportFormRawValue['isActive']>;
  lastRun: FormControl<ReportingScheduledReportFormRawValue['lastRun']>;
  nextRun: FormControl<ReportingScheduledReportFormRawValue['nextRun']>;
  createdBy: FormControl<ReportingScheduledReportFormRawValue['createdBy']>;
  createdDate: FormControl<ReportingScheduledReportFormRawValue['createdDate']>;
};

export type ReportingScheduledReportFormGroup = FormGroup<ReportingScheduledReportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportingScheduledReportFormService {
  createReportingScheduledReportFormGroup(
    reportingScheduledReport: ReportingScheduledReportFormGroupInput = { id: null },
  ): ReportingScheduledReportFormGroup {
    const reportingScheduledReportRawValue = this.convertReportingScheduledReportToReportingScheduledReportRawValue({
      ...this.getFormDefaults(),
      ...reportingScheduledReport,
    });
    return new FormGroup<ReportingScheduledReportFormGroupContent>({
      id: new FormControl(
        { value: reportingScheduledReportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(reportingScheduledReportRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(reportingScheduledReportRawValue.description),
      reportType: new FormControl(reportingScheduledReportRawValue.reportType, {
        validators: [Validators.required],
      }),
      query: new FormControl(reportingScheduledReportRawValue.query),
      schedule: new FormControl(reportingScheduledReportRawValue.schedule, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      format: new FormControl(reportingScheduledReportRawValue.format, {
        validators: [Validators.required],
      }),
      recipients: new FormControl(reportingScheduledReportRawValue.recipients, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(reportingScheduledReportRawValue.isActive, {
        validators: [Validators.required],
      }),
      lastRun: new FormControl(reportingScheduledReportRawValue.lastRun),
      nextRun: new FormControl(reportingScheduledReportRawValue.nextRun),
      createdBy: new FormControl(reportingScheduledReportRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(reportingScheduledReportRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getReportingScheduledReport(form: ReportingScheduledReportFormGroup): IReportingScheduledReport | NewReportingScheduledReport {
    return this.convertReportingScheduledReportRawValueToReportingScheduledReport(
      form.getRawValue() as ReportingScheduledReportFormRawValue | NewReportingScheduledReportFormRawValue,
    );
  }

  resetForm(form: ReportingScheduledReportFormGroup, reportingScheduledReport: ReportingScheduledReportFormGroupInput): void {
    const reportingScheduledReportRawValue = this.convertReportingScheduledReportToReportingScheduledReportRawValue({
      ...this.getFormDefaults(),
      ...reportingScheduledReport,
    });
    form.reset(
      {
        ...reportingScheduledReportRawValue,
        id: { value: reportingScheduledReportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportingScheduledReportFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      lastRun: currentTime,
      nextRun: currentTime,
      createdDate: currentTime,
    };
  }

  private convertReportingScheduledReportRawValueToReportingScheduledReport(
    rawReportingScheduledReport: ReportingScheduledReportFormRawValue | NewReportingScheduledReportFormRawValue,
  ): IReportingScheduledReport | NewReportingScheduledReport {
    return {
      ...rawReportingScheduledReport,
      lastRun: dayjs(rawReportingScheduledReport.lastRun, DATE_TIME_FORMAT),
      nextRun: dayjs(rawReportingScheduledReport.nextRun, DATE_TIME_FORMAT),
      createdDate: dayjs(rawReportingScheduledReport.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertReportingScheduledReportToReportingScheduledReportRawValue(
    reportingScheduledReport: IReportingScheduledReport | (Partial<NewReportingScheduledReport> & ReportingScheduledReportFormDefaults),
  ): ReportingScheduledReportFormRawValue | PartialWithRequiredKeyOf<NewReportingScheduledReportFormRawValue> {
    return {
      ...reportingScheduledReport,
      lastRun: reportingScheduledReport.lastRun ? reportingScheduledReport.lastRun.format(DATE_TIME_FORMAT) : undefined,
      nextRun: reportingScheduledReport.nextRun ? reportingScheduledReport.nextRun.format(DATE_TIME_FORMAT) : undefined,
      createdDate: reportingScheduledReport.createdDate ? reportingScheduledReport.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
