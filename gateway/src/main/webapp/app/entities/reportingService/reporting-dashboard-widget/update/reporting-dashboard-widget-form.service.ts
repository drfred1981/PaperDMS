import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IReportingDashboardWidget, NewReportingDashboardWidget } from '../reporting-dashboard-widget.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportingDashboardWidget for edit and NewReportingDashboardWidgetFormGroupInput for create.
 */
type ReportingDashboardWidgetFormGroupInput = IReportingDashboardWidget | PartialWithRequiredKeyOf<NewReportingDashboardWidget>;

type ReportingDashboardWidgetFormDefaults = Pick<NewReportingDashboardWidget, 'id'>;

type ReportingDashboardWidgetFormGroupContent = {
  id: FormControl<IReportingDashboardWidget['id'] | NewReportingDashboardWidget['id']>;
  widgetType: FormControl<IReportingDashboardWidget['widgetType']>;
  title: FormControl<IReportingDashboardWidget['title']>;
  configuration: FormControl<IReportingDashboardWidget['configuration']>;
  dataSource: FormControl<IReportingDashboardWidget['dataSource']>;
  position: FormControl<IReportingDashboardWidget['position']>;
  sizeX: FormControl<IReportingDashboardWidget['sizeX']>;
  sizeY: FormControl<IReportingDashboardWidget['sizeY']>;
  dashboar: FormControl<IReportingDashboardWidget['dashboar']>;
};

export type ReportingDashboardWidgetFormGroup = FormGroup<ReportingDashboardWidgetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportingDashboardWidgetFormService {
  createReportingDashboardWidgetFormGroup(
    reportingDashboardWidget: ReportingDashboardWidgetFormGroupInput = { id: null },
  ): ReportingDashboardWidgetFormGroup {
    const reportingDashboardWidgetRawValue = {
      ...this.getFormDefaults(),
      ...reportingDashboardWidget,
    };
    return new FormGroup<ReportingDashboardWidgetFormGroupContent>({
      id: new FormControl(
        { value: reportingDashboardWidgetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      widgetType: new FormControl(reportingDashboardWidgetRawValue.widgetType, {
        validators: [Validators.required],
      }),
      title: new FormControl(reportingDashboardWidgetRawValue.title, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      configuration: new FormControl(reportingDashboardWidgetRawValue.configuration, {
        validators: [Validators.required],
      }),
      dataSource: new FormControl(reportingDashboardWidgetRawValue.dataSource, {
        validators: [Validators.maxLength(255)],
      }),
      position: new FormControl(reportingDashboardWidgetRawValue.position, {
        validators: [Validators.required],
      }),
      sizeX: new FormControl(reportingDashboardWidgetRawValue.sizeX, {
        validators: [Validators.required],
      }),
      sizeY: new FormControl(reportingDashboardWidgetRawValue.sizeY, {
        validators: [Validators.required],
      }),
      dashboar: new FormControl(reportingDashboardWidgetRawValue.dashboar),
    });
  }

  getReportingDashboardWidget(form: ReportingDashboardWidgetFormGroup): IReportingDashboardWidget | NewReportingDashboardWidget {
    return form.getRawValue() as IReportingDashboardWidget | NewReportingDashboardWidget;
  }

  resetForm(form: ReportingDashboardWidgetFormGroup, reportingDashboardWidget: ReportingDashboardWidgetFormGroupInput): void {
    const reportingDashboardWidgetRawValue = { ...this.getFormDefaults(), ...reportingDashboardWidget };
    form.reset(
      {
        ...reportingDashboardWidgetRawValue,
        id: { value: reportingDashboardWidgetRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportingDashboardWidgetFormDefaults {
    return {
      id: null,
    };
  }
}
