import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MonitoringServiceStatusType } from 'app/entities/enumerations/monitoring-service-status-type.model';
import { IMonitoringServiceStatus } from '../monitoring-service-status.model';
import { MonitoringServiceStatusService } from '../service/monitoring-service-status.service';
import { MonitoringServiceStatusFormGroup, MonitoringServiceStatusFormService } from './monitoring-service-status-form.service';

@Component({
  selector: 'jhi-monitoring-service-status-update',
  templateUrl: './monitoring-service-status-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MonitoringServiceStatusUpdateComponent implements OnInit {
  isSaving = false;
  monitoringServiceStatus: IMonitoringServiceStatus | null = null;
  monitoringServiceStatusTypeValues = Object.keys(MonitoringServiceStatusType);

  protected monitoringServiceStatusService = inject(MonitoringServiceStatusService);
  protected monitoringServiceStatusFormService = inject(MonitoringServiceStatusFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MonitoringServiceStatusFormGroup = this.monitoringServiceStatusFormService.createMonitoringServiceStatusFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ monitoringServiceStatus }) => {
      this.monitoringServiceStatus = monitoringServiceStatus;
      if (monitoringServiceStatus) {
        this.updateForm(monitoringServiceStatus);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const monitoringServiceStatus = this.monitoringServiceStatusFormService.getMonitoringServiceStatus(this.editForm);
    if (monitoringServiceStatus.id !== null) {
      this.subscribeToSaveResponse(this.monitoringServiceStatusService.update(monitoringServiceStatus));
    } else {
      this.subscribeToSaveResponse(this.monitoringServiceStatusService.create(monitoringServiceStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMonitoringServiceStatus>>): void {
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

  protected updateForm(monitoringServiceStatus: IMonitoringServiceStatus): void {
    this.monitoringServiceStatus = monitoringServiceStatus;
    this.monitoringServiceStatusFormService.resetForm(this.editForm, monitoringServiceStatus);
  }
}
