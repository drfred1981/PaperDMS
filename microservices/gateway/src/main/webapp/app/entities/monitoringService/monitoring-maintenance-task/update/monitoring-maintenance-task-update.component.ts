import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { MaintenanceType } from 'app/entities/enumerations/maintenance-type.model';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';
import { MonitoringMaintenanceTaskService } from '../service/monitoring-maintenance-task.service';
import { IMonitoringMaintenanceTask } from '../monitoring-maintenance-task.model';
import { MonitoringMaintenanceTaskFormGroup, MonitoringMaintenanceTaskFormService } from './monitoring-maintenance-task-form.service';

@Component({
  selector: 'jhi-monitoring-maintenance-task-update',
  templateUrl: './monitoring-maintenance-task-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MonitoringMaintenanceTaskUpdateComponent implements OnInit {
  isSaving = false;
  monitoringMaintenanceTask: IMonitoringMaintenanceTask | null = null;
  maintenanceTypeValues = Object.keys(MaintenanceType);
  transformStatusValues = Object.keys(TransformStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected monitoringMaintenanceTaskService = inject(MonitoringMaintenanceTaskService);
  protected monitoringMaintenanceTaskFormService = inject(MonitoringMaintenanceTaskFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MonitoringMaintenanceTaskFormGroup = this.monitoringMaintenanceTaskFormService.createMonitoringMaintenanceTaskFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ monitoringMaintenanceTask }) => {
      this.monitoringMaintenanceTask = monitoringMaintenanceTask;
      if (monitoringMaintenanceTask) {
        this.updateForm(monitoringMaintenanceTask);
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
        this.eventManager.broadcast(new EventWithContent<AlertError>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const monitoringMaintenanceTask = this.monitoringMaintenanceTaskFormService.getMonitoringMaintenanceTask(this.editForm);
    if (monitoringMaintenanceTask.id !== null) {
      this.subscribeToSaveResponse(this.monitoringMaintenanceTaskService.update(monitoringMaintenanceTask));
    } else {
      this.subscribeToSaveResponse(this.monitoringMaintenanceTaskService.create(monitoringMaintenanceTask));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMonitoringMaintenanceTask>>): void {
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

  protected updateForm(monitoringMaintenanceTask: IMonitoringMaintenanceTask): void {
    this.monitoringMaintenanceTask = monitoringMaintenanceTask;
    this.monitoringMaintenanceTaskFormService.resetForm(this.editForm, monitoringMaintenanceTask);
  }
}
