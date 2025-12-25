import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { HealthStatus } from 'app/entities/enumerations/health-status.model';
import { ISystemHealth } from '../system-health.model';
import { SystemHealthService } from '../service/system-health.service';
import { SystemHealthFormGroup, SystemHealthFormService } from './system-health-form.service';

@Component({
  selector: 'jhi-system-health-update',
  templateUrl: './system-health-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SystemHealthUpdateComponent implements OnInit {
  isSaving = false;
  systemHealth: ISystemHealth | null = null;
  healthStatusValues = Object.keys(HealthStatus);

  protected systemHealthService = inject(SystemHealthService);
  protected systemHealthFormService = inject(SystemHealthFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SystemHealthFormGroup = this.systemHealthFormService.createSystemHealthFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemHealth }) => {
      this.systemHealth = systemHealth;
      if (systemHealth) {
        this.updateForm(systemHealth);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const systemHealth = this.systemHealthFormService.getSystemHealth(this.editForm);
    if (systemHealth.id !== null) {
      this.subscribeToSaveResponse(this.systemHealthService.update(systemHealth));
    } else {
      this.subscribeToSaveResponse(this.systemHealthService.create(systemHealth));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISystemHealth>>): void {
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

  protected updateForm(systemHealth: ISystemHealth): void {
    this.systemHealth = systemHealth;
    this.systemHealthFormService.resetForm(this.editForm, systemHealth);
  }
}
