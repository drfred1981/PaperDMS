import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { MaintenanceType } from 'app/entities/enumerations/maintenance-type.model';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IMaintenanceTask } from '../maintenance-task.model';
import { MaintenanceTaskService } from '../service/maintenance-task.service';

import { MaintenanceTaskFormGroup, MaintenanceTaskFormService } from './maintenance-task-form.service';

@Component({
  selector: 'jhi-maintenance-task-update',
  templateUrl: './maintenance-task-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class MaintenanceTaskUpdate implements OnInit {
  isSaving = false;
  maintenanceTask: IMaintenanceTask | null = null;
  maintenanceTypeValues = Object.keys(MaintenanceType);
  transformStatusValues = Object.keys(TransformStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected maintenanceTaskService = inject(MaintenanceTaskService);
  protected maintenanceTaskFormService = inject(MaintenanceTaskFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MaintenanceTaskFormGroup = this.maintenanceTaskFormService.createMaintenanceTaskFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maintenanceTask }) => {
      this.maintenanceTask = maintenanceTask;
      if (maintenanceTask) {
        this.updateForm(maintenanceTask);
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
    const maintenanceTask = this.maintenanceTaskFormService.getMaintenanceTask(this.editForm);
    if (maintenanceTask.id === null) {
      this.subscribeToSaveResponse(this.maintenanceTaskService.create(maintenanceTask));
    } else {
      this.subscribeToSaveResponse(this.maintenanceTaskService.update(maintenanceTask));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaintenanceTask>>): void {
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

  protected updateForm(maintenanceTask: IMaintenanceTask): void {
    this.maintenanceTask = maintenanceTask;
    this.maintenanceTaskFormService.resetForm(this.editForm, maintenanceTask);
  }
}
