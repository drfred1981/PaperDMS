import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ServiceStatusType } from 'app/entities/enumerations/service-status-type.model';
import SharedModule from 'app/shared/shared.module';
import { ServiceStatusService } from '../service/service-status.service';
import { IServiceStatus } from '../service-status.model';

import { ServiceStatusFormGroup, ServiceStatusFormService } from './service-status-form.service';

@Component({
  selector: 'jhi-service-status-update',
  templateUrl: './service-status-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ServiceStatusUpdate implements OnInit {
  isSaving = false;
  serviceStatus: IServiceStatus | null = null;
  serviceStatusTypeValues = Object.keys(ServiceStatusType);

  protected serviceStatusService = inject(ServiceStatusService);
  protected serviceStatusFormService = inject(ServiceStatusFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServiceStatusFormGroup = this.serviceStatusFormService.createServiceStatusFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceStatus }) => {
      this.serviceStatus = serviceStatus;
      if (serviceStatus) {
        this.updateForm(serviceStatus);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceStatus = this.serviceStatusFormService.getServiceStatus(this.editForm);
    if (serviceStatus.id === null) {
      this.subscribeToSaveResponse(this.serviceStatusService.create(serviceStatus));
    } else {
      this.subscribeToSaveResponse(this.serviceStatusService.update(serviceStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceStatus>>): void {
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

  protected updateForm(serviceStatus: IServiceStatus): void {
    this.serviceStatus = serviceStatus;
    this.serviceStatusFormService.resetForm(this.editForm, serviceStatus);
  }
}
