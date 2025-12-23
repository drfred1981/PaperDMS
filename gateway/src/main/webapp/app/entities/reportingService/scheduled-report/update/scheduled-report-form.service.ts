import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IScheduledReport, NewScheduledReport } from '../scheduled-report.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IScheduledReport for edit and NewScheduledReportFormGroupInput for create.
 */
type ScheduledReportFormGroupInput = IScheduledReport | PartialWithRequiredKeyOf<NewScheduledReport>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IScheduledReport | NewScheduledReport> = Omit<T, 'lastRun' | 'nextRun' | 'createdDate'> & {
  lastRun?: string | null;
  nextRun?: string | null;
  createdDate?: string | null;
};

type ScheduledReportFormRawValue = FormValueOf<IScheduledReport>;

type NewScheduledReportFormRawValue = FormValueOf<NewScheduledReport>;

type ScheduledReportFormDefaults = Pick<NewScheduledReport, 'id' | 'isActive' | 'lastRun' | 'nextRun' | 'createdDate'>;

type ScheduledReportFormGroupContent = {
  id: FormControl<ScheduledReportFormRawValue['id'] | NewScheduledReport['id']>;
  name: FormControl<ScheduledReportFormRawValue['name']>;
  description: FormControl<ScheduledReportFormRawValue['description']>;
  reportType: FormControl<ScheduledReportFormRawValue['reportType']>;
  query: FormControl<ScheduledReportFormRawValue['query']>;
  schedule: FormControl<ScheduledReportFormRawValue['schedule']>;
  format: FormControl<ScheduledReportFormRawValue['format']>;
  recipients: FormControl<ScheduledReportFormRawValue['recipients']>;
  isActive: FormControl<ScheduledReportFormRawValue['isActive']>;
  lastRun: FormControl<ScheduledReportFormRawValue['lastRun']>;
  nextRun: FormControl<ScheduledReportFormRawValue['nextRun']>;
  createdBy: FormControl<ScheduledReportFormRawValue['createdBy']>;
  createdDate: FormControl<ScheduledReportFormRawValue['createdDate']>;
};

export type ScheduledReportFormGroup = FormGroup<ScheduledReportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ScheduledReportFormService {
  createScheduledReportFormGroup(scheduledReport?: ScheduledReportFormGroupInput): ScheduledReportFormGroup {
    const scheduledReportRawValue = this.convertScheduledReportToScheduledReportRawValue({
      ...this.getFormDefaults(),
      ...(scheduledReport ?? { id: null }),
    });
    return new FormGroup<ScheduledReportFormGroupContent>({
      id: new FormControl(
        { value: scheduledReportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(scheduledReportRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(scheduledReportRawValue.description),
      reportType: new FormControl(scheduledReportRawValue.reportType, {
        validators: [Validators.required],
      }),
      query: new FormControl(scheduledReportRawValue.query),
      schedule: new FormControl(scheduledReportRawValue.schedule, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      format: new FormControl(scheduledReportRawValue.format, {
        validators: [Validators.required],
      }),
      recipients: new FormControl(scheduledReportRawValue.recipients, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(scheduledReportRawValue.isActive, {
        validators: [Validators.required],
      }),
      lastRun: new FormControl(scheduledReportRawValue.lastRun),
      nextRun: new FormControl(scheduledReportRawValue.nextRun),
      createdBy: new FormControl(scheduledReportRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(scheduledReportRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getScheduledReport(form: ScheduledReportFormGroup): IScheduledReport | NewScheduledReport {
    return this.convertScheduledReportRawValueToScheduledReport(
      form.getRawValue() as ScheduledReportFormRawValue | NewScheduledReportFormRawValue,
    );
  }

  resetForm(form: ScheduledReportFormGroup, scheduledReport: ScheduledReportFormGroupInput): void {
    const scheduledReportRawValue = this.convertScheduledReportToScheduledReportRawValue({ ...this.getFormDefaults(), ...scheduledReport });
    form.reset({
      ...scheduledReportRawValue,
      id: { value: scheduledReportRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ScheduledReportFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isActive: false,
      lastRun: currentTime,
      nextRun: currentTime,
      createdDate: currentTime,
    };
  }

  private convertScheduledReportRawValueToScheduledReport(
    rawScheduledReport: ScheduledReportFormRawValue | NewScheduledReportFormRawValue,
  ): IScheduledReport | NewScheduledReport {
    return {
      ...rawScheduledReport,
      lastRun: dayjs(rawScheduledReport.lastRun, DATE_TIME_FORMAT),
      nextRun: dayjs(rawScheduledReport.nextRun, DATE_TIME_FORMAT),
      createdDate: dayjs(rawScheduledReport.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertScheduledReportToScheduledReportRawValue(
    scheduledReport: IScheduledReport | (Partial<NewScheduledReport> & ScheduledReportFormDefaults),
  ): ScheduledReportFormRawValue | PartialWithRequiredKeyOf<NewScheduledReportFormRawValue> {
    return {
      ...scheduledReport,
      lastRun: scheduledReport.lastRun ? scheduledReport.lastRun.format(DATE_TIME_FORMAT) : undefined,
      nextRun: scheduledReport.nextRun ? scheduledReport.nextRun.format(DATE_TIME_FORMAT) : undefined,
      createdDate: scheduledReport.createdDate ? scheduledReport.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
