import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { SystemMetricService } from '../service/system-metric.service';
import { ISystemMetric } from '../system-metric.model';

import { SystemMetricFormGroup, SystemMetricFormService } from './system-metric-form.service';

@Component({
  selector: 'jhi-system-metric-update',
  templateUrl: './system-metric-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class SystemMetricUpdate implements OnInit {
  isSaving = false;
  systemMetric: ISystemMetric | null = null;

  protected systemMetricService = inject(SystemMetricService);
  protected systemMetricFormService = inject(SystemMetricFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SystemMetricFormGroup = this.systemMetricFormService.createSystemMetricFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemMetric }) => {
      this.systemMetric = systemMetric;
      if (systemMetric) {
        this.updateForm(systemMetric);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const systemMetric = this.systemMetricFormService.getSystemMetric(this.editForm);
    if (systemMetric.id === null) {
      this.subscribeToSaveResponse(this.systemMetricService.create(systemMetric));
    } else {
      this.subscribeToSaveResponse(this.systemMetricService.update(systemMetric));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISystemMetric>>): void {
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

  protected updateForm(systemMetric: ISystemMetric): void {
    this.systemMetric = systemMetric;
    this.systemMetricFormService.resetForm(this.editForm, systemMetric);
  }
}
