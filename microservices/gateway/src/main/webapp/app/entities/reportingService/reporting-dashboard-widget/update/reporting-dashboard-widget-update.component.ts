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
import { IReportingDashboard } from 'app/entities/reportingService/reporting-dashboard/reporting-dashboard.model';
import { ReportingDashboardService } from 'app/entities/reportingService/reporting-dashboard/service/reporting-dashboard.service';
import { WidgetType } from 'app/entities/enumerations/widget-type.model';
import { ReportingDashboardWidgetService } from '../service/reporting-dashboard-widget.service';
import { IReportingDashboardWidget } from '../reporting-dashboard-widget.model';
import { ReportingDashboardWidgetFormGroup, ReportingDashboardWidgetFormService } from './reporting-dashboard-widget-form.service';

@Component({
  selector: 'jhi-reporting-dashboard-widget-update',
  templateUrl: './reporting-dashboard-widget-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportingDashboardWidgetUpdateComponent implements OnInit {
  isSaving = false;
  reportingDashboardWidget: IReportingDashboardWidget | null = null;
  widgetTypeValues = Object.keys(WidgetType);

  reportingDashboardsSharedCollection: IReportingDashboard[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected reportingDashboardWidgetService = inject(ReportingDashboardWidgetService);
  protected reportingDashboardWidgetFormService = inject(ReportingDashboardWidgetFormService);
  protected reportingDashboardService = inject(ReportingDashboardService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportingDashboardWidgetFormGroup = this.reportingDashboardWidgetFormService.createReportingDashboardWidgetFormGroup();

  compareReportingDashboard = (o1: IReportingDashboard | null, o2: IReportingDashboard | null): boolean =>
    this.reportingDashboardService.compareReportingDashboard(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportingDashboardWidget }) => {
      this.reportingDashboardWidget = reportingDashboardWidget;
      if (reportingDashboardWidget) {
        this.updateForm(reportingDashboardWidget);
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
    const reportingDashboardWidget = this.reportingDashboardWidgetFormService.getReportingDashboardWidget(this.editForm);
    if (reportingDashboardWidget.id !== null) {
      this.subscribeToSaveResponse(this.reportingDashboardWidgetService.update(reportingDashboardWidget));
    } else {
      this.subscribeToSaveResponse(this.reportingDashboardWidgetService.create(reportingDashboardWidget));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportingDashboardWidget>>): void {
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

  protected updateForm(reportingDashboardWidget: IReportingDashboardWidget): void {
    this.reportingDashboardWidget = reportingDashboardWidget;
    this.reportingDashboardWidgetFormService.resetForm(this.editForm, reportingDashboardWidget);

    this.reportingDashboardsSharedCollection =
      this.reportingDashboardService.addReportingDashboardToCollectionIfMissing<IReportingDashboard>(
        this.reportingDashboardsSharedCollection,
        reportingDashboardWidget.dashboar,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.reportingDashboardService
      .query()
      .pipe(map((res: HttpResponse<IReportingDashboard[]>) => res.body ?? []))
      .pipe(
        map((reportingDashboards: IReportingDashboard[]) =>
          this.reportingDashboardService.addReportingDashboardToCollectionIfMissing<IReportingDashboard>(
            reportingDashboards,
            this.reportingDashboardWidget?.dashboar,
          ),
        ),
      )
      .subscribe((reportingDashboards: IReportingDashboard[]) => (this.reportingDashboardsSharedCollection = reportingDashboards));
  }
}
