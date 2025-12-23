import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IDashboard } from '../dashboard.model';
import { DashboardService } from '../service/dashboard.service';

import { DashboardFormGroup, DashboardFormService } from './dashboard-form.service';

@Component({
  selector: 'jhi-dashboard-update',
  templateUrl: './dashboard-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class DashboardUpdate implements OnInit {
  isSaving = false;
  dashboard: IDashboard | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected dashboardService = inject(DashboardService);
  protected dashboardFormService = inject(DashboardFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DashboardFormGroup = this.dashboardFormService.createDashboardFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dashboard }) => {
      this.dashboard = dashboard;
      if (dashboard) {
        this.updateForm(dashboard);
      }
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
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dashboard = this.dashboardFormService.getDashboard(this.editForm);
    if (dashboard.id === null) {
      this.subscribeToSaveResponse(this.dashboardService.create(dashboard));
    } else {
      this.subscribeToSaveResponse(this.dashboardService.update(dashboard));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDashboard>>): void {
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

  protected updateForm(dashboard: IDashboard): void {
    this.dashboard = dashboard;
    this.dashboardFormService.resetForm(this.editForm, dashboard);
  }
}
