import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { HealthStatus } from 'app/entities/enumerations/health-status.model';
import { IMonitoringSystemHealth } from '../monitoring-system-health.model';
import { MonitoringSystemHealthService } from '../service/monitoring-system-health.service';
import { MonitoringSystemHealthFormGroup, MonitoringSystemHealthFormService } from './monitoring-system-health-form.service';

@Component({
  selector: 'jhi-monitoring-system-health-update',
  templateUrl: './monitoring-system-health-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MonitoringSystemHealthUpdateComponent implements OnInit {
  isSaving = false;
  monitoringSystemHealth: IMonitoringSystemHealth | null = null;
  healthStatusValues = Object.keys(HealthStatus);

  protected monitoringSystemHealthService = inject(MonitoringSystemHealthService);
  protected monitoringSystemHealthFormService = inject(MonitoringSystemHealthFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MonitoringSystemHealthFormGroup = this.monitoringSystemHealthFormService.createMonitoringSystemHealthFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ monitoringSystemHealth }) => {
      this.monitoringSystemHealth = monitoringSystemHealth;
      if (monitoringSystemHealth) {
        this.updateForm(monitoringSystemHealth);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const monitoringSystemHealth = this.monitoringSystemHealthFormService.getMonitoringSystemHealth(this.editForm);
    if (monitoringSystemHealth.id !== null) {
      this.subscribeToSaveResponse(this.monitoringSystemHealthService.update(monitoringSystemHealth));
    } else {
      this.subscribeToSaveResponse(this.monitoringSystemHealthService.create(monitoringSystemHealth));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMonitoringSystemHealth>>): void {
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

  protected updateForm(monitoringSystemHealth: IMonitoringSystemHealth): void {
    this.monitoringSystemHealth = monitoringSystemHealth;
    this.monitoringSystemHealthFormService.resetForm(this.editForm, monitoringSystemHealth);
  }
}
