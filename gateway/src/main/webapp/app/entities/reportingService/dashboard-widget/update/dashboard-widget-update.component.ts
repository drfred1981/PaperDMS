import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IDashboard } from 'app/entities/reportingService/dashboard/dashboard.model';
import { DashboardService } from 'app/entities/reportingService/dashboard/service/dashboard.service';
import { WidgetType } from 'app/entities/enumerations/widget-type.model';
import { DashboardWidgetService } from '../service/dashboard-widget.service';
import { IDashboardWidget } from '../dashboard-widget.model';
import { DashboardWidgetFormGroup, DashboardWidgetFormService } from './dashboard-widget-form.service';

@Component({
  selector: 'jhi-dashboard-widget-update',
  templateUrl: './dashboard-widget-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DashboardWidgetUpdateComponent implements OnInit {
  isSaving = false;
  dashboardWidget: IDashboardWidget | null = null;
  widgetTypeValues = Object.keys(WidgetType);

  dashboardsSharedCollection: IDashboard[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected dashboardWidgetService = inject(DashboardWidgetService);
  protected dashboardWidgetFormService = inject(DashboardWidgetFormService);
  protected dashboardService = inject(DashboardService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DashboardWidgetFormGroup = this.dashboardWidgetFormService.createDashboardWidgetFormGroup();

  compareDashboard = (o1: IDashboard | null, o2: IDashboard | null): boolean => this.dashboardService.compareDashboard(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dashboardWidget }) => {
      this.dashboardWidget = dashboardWidget;
      if (dashboardWidget) {
        this.updateForm(dashboardWidget);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dashboardWidget = this.dashboardWidgetFormService.getDashboardWidget(this.editForm);
    if (dashboardWidget.id !== null) {
      this.subscribeToSaveResponse(this.dashboardWidgetService.update(dashboardWidget));
    } else {
      this.subscribeToSaveResponse(this.dashboardWidgetService.create(dashboardWidget));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDashboardWidget>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(dashboardWidget: IDashboardWidget): void {
    this.dashboardWidget = dashboardWidget;
    this.dashboardWidgetFormService.resetForm(this.editForm, dashboardWidget);

    this.dashboardsSharedCollection = this.dashboardService.addDashboardToCollectionIfMissing<IDashboard>(
      this.dashboardsSharedCollection,
      dashboardWidget.dashboard,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dashboardService
      .query()
      .pipe(map((res: HttpResponse<IDashboard[]>) => res.body ?? []))
      .pipe(
        map((dashboards: IDashboard[]) =>
          this.dashboardService.addDashboardToCollectionIfMissing<IDashboard>(dashboards, this.dashboardWidget?.dashboard),
        ),
      )
      .subscribe((dashboards: IDashboard[]) => (this.dashboardsSharedCollection = dashboards));
  }
}
