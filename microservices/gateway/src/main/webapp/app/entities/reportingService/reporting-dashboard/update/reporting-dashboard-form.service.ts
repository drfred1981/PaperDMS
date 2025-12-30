import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReportingDashboard, NewReportingDashboard } from '../reporting-dashboard.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportingDashboard for edit and NewReportingDashboardFormGroupInput for create.
 */
type ReportingDashboardFormGroupInput = IReportingDashboard | PartialWithRequiredKeyOf<NewReportingDashboard>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReportingDashboard | NewReportingDashboard> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type ReportingDashboardFormRawValue = FormValueOf<IReportingDashboard>;

type NewReportingDashboardFormRawValue = FormValueOf<NewReportingDashboard>;

type ReportingDashboardFormDefaults = Pick<NewReportingDashboard, 'id' | 'isPublic' | 'isDefault' | 'createdDate'>;

type ReportingDashboardFormGroupContent = {
  id: FormControl<ReportingDashboardFormRawValue['id'] | NewReportingDashboard['id']>;
  name: FormControl<ReportingDashboardFormRawValue['name']>;
  description: FormControl<ReportingDashboardFormRawValue['description']>;
  userId: FormControl<ReportingDashboardFormRawValue['userId']>;
  isPublic: FormControl<ReportingDashboardFormRawValue['isPublic']>;
  layout: FormControl<ReportingDashboardFormRawValue['layout']>;
  refreshInterval: FormControl<ReportingDashboardFormRawValue['refreshInterval']>;
  isDefault: FormControl<ReportingDashboardFormRawValue['isDefault']>;
  createdDate: FormControl<ReportingDashboardFormRawValue['createdDate']>;
};

export type ReportingDashboardFormGroup = FormGroup<ReportingDashboardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportingDashboardFormService {
  createReportingDashboardFormGroup(reportingDashboard: ReportingDashboardFormGroupInput = { id: null }): ReportingDashboardFormGroup {
    const reportingDashboardRawValue = this.convertReportingDashboardToReportingDashboardRawValue({
      ...this.getFormDefaults(),
      ...reportingDashboard,
    });
    return new FormGroup<ReportingDashboardFormGroupContent>({
      id: new FormControl(
        { value: reportingDashboardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(reportingDashboardRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(reportingDashboardRawValue.description),
      userId: new FormControl(reportingDashboardRawValue.userId, {
        validators: [Validators.maxLength(50)],
      }),
      isPublic: new FormControl(reportingDashboardRawValue.isPublic, {
        validators: [Validators.required],
      }),
      layout: new FormControl(reportingDashboardRawValue.layout, {
        validators: [Validators.required],
      }),
      refreshInterval: new FormControl(reportingDashboardRawValue.refreshInterval),
      isDefault: new FormControl(reportingDashboardRawValue.isDefault),
      createdDate: new FormControl(reportingDashboardRawValue.createdDate, {
        validators: [Validators.required],
      }),
    });
  }

  getReportingDashboard(form: ReportingDashboardFormGroup): IReportingDashboard | NewReportingDashboard {
    return this.convertReportingDashboardRawValueToReportingDashboard(
      form.getRawValue() as ReportingDashboardFormRawValue | NewReportingDashboardFormRawValue,
    );
  }

  resetForm(form: ReportingDashboardFormGroup, reportingDashboard: ReportingDashboardFormGroupInput): void {
    const reportingDashboardRawValue = this.convertReportingDashboardToReportingDashboardRawValue({
      ...this.getFormDefaults(),
      ...reportingDashboard,
    });
    form.reset(
      {
        ...reportingDashboardRawValue,
        id: { value: reportingDashboardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportingDashboardFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isPublic: false,
      isDefault: false,
      createdDate: currentTime,
    };
  }

  private convertReportingDashboardRawValueToReportingDashboard(
    rawReportingDashboard: ReportingDashboardFormRawValue | NewReportingDashboardFormRawValue,
  ): IReportingDashboard | NewReportingDashboard {
    return {
      ...rawReportingDashboard,
      createdDate: dayjs(rawReportingDashboard.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertReportingDashboardToReportingDashboardRawValue(
    reportingDashboard: IReportingDashboard | (Partial<NewReportingDashboard> & ReportingDashboardFormDefaults),
  ): ReportingDashboardFormRawValue | PartialWithRequiredKeyOf<NewReportingDashboardFormRawValue> {
    return {
      ...reportingDashboard,
      createdDate: reportingDashboard.createdDate ? reportingDashboard.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
