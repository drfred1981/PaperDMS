import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDashboardWidget, NewDashboardWidget } from '../dashboard-widget.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDashboardWidget for edit and NewDashboardWidgetFormGroupInput for create.
 */
type DashboardWidgetFormGroupInput = IDashboardWidget | PartialWithRequiredKeyOf<NewDashboardWidget>;

type DashboardWidgetFormDefaults = Pick<NewDashboardWidget, 'id'>;

type DashboardWidgetFormGroupContent = {
  id: FormControl<IDashboardWidget['id'] | NewDashboardWidget['id']>;
  dashboardId: FormControl<IDashboardWidget['dashboardId']>;
  widgetType: FormControl<IDashboardWidget['widgetType']>;
  title: FormControl<IDashboardWidget['title']>;
  configuration: FormControl<IDashboardWidget['configuration']>;
  dataSource: FormControl<IDashboardWidget['dataSource']>;
  position: FormControl<IDashboardWidget['position']>;
  sizeX: FormControl<IDashboardWidget['sizeX']>;
  sizeY: FormControl<IDashboardWidget['sizeY']>;
  dashboard: FormControl<IDashboardWidget['dashboard']>;
};

export type DashboardWidgetFormGroup = FormGroup<DashboardWidgetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DashboardWidgetFormService {
  createDashboardWidgetFormGroup(dashboardWidget: DashboardWidgetFormGroupInput = { id: null }): DashboardWidgetFormGroup {
    const dashboardWidgetRawValue = {
      ...this.getFormDefaults(),
      ...dashboardWidget,
    };
    return new FormGroup<DashboardWidgetFormGroupContent>({
      id: new FormControl(
        { value: dashboardWidgetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dashboardId: new FormControl(dashboardWidgetRawValue.dashboardId, {
        validators: [Validators.required],
      }),
      widgetType: new FormControl(dashboardWidgetRawValue.widgetType, {
        validators: [Validators.required],
      }),
      title: new FormControl(dashboardWidgetRawValue.title, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      configuration: new FormControl(dashboardWidgetRawValue.configuration, {
        validators: [Validators.required],
      }),
      dataSource: new FormControl(dashboardWidgetRawValue.dataSource, {
        validators: [Validators.maxLength(255)],
      }),
      position: new FormControl(dashboardWidgetRawValue.position, {
        validators: [Validators.required],
      }),
      sizeX: new FormControl(dashboardWidgetRawValue.sizeX, {
        validators: [Validators.required],
      }),
      sizeY: new FormControl(dashboardWidgetRawValue.sizeY, {
        validators: [Validators.required],
      }),
      dashboard: new FormControl(dashboardWidgetRawValue.dashboard, {
        validators: [Validators.required],
      }),
    });
  }

  getDashboardWidget(form: DashboardWidgetFormGroup): IDashboardWidget | NewDashboardWidget {
    return form.getRawValue() as IDashboardWidget | NewDashboardWidget;
  }

  resetForm(form: DashboardWidgetFormGroup, dashboardWidget: DashboardWidgetFormGroupInput): void {
    const dashboardWidgetRawValue = { ...this.getFormDefaults(), ...dashboardWidget };
    form.reset(
      {
        ...dashboardWidgetRawValue,
        id: { value: dashboardWidgetRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DashboardWidgetFormDefaults {
    return {
      id: null,
    };
  }
}
