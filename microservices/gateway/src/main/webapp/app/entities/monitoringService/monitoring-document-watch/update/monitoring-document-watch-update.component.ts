import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { WatchType } from 'app/entities/enumerations/watch-type.model';
import { IMonitoringDocumentWatch } from '../monitoring-document-watch.model';
import { MonitoringDocumentWatchService } from '../service/monitoring-document-watch.service';
import { MonitoringDocumentWatchFormGroup, MonitoringDocumentWatchFormService } from './monitoring-document-watch-form.service';

@Component({
  selector: 'jhi-monitoring-document-watch-update',
  templateUrl: './monitoring-document-watch-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MonitoringDocumentWatchUpdateComponent implements OnInit {
  isSaving = false;
  monitoringDocumentWatch: IMonitoringDocumentWatch | null = null;
  watchTypeValues = Object.keys(WatchType);

  protected monitoringDocumentWatchService = inject(MonitoringDocumentWatchService);
  protected monitoringDocumentWatchFormService = inject(MonitoringDocumentWatchFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MonitoringDocumentWatchFormGroup = this.monitoringDocumentWatchFormService.createMonitoringDocumentWatchFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ monitoringDocumentWatch }) => {
      this.monitoringDocumentWatch = monitoringDocumentWatch;
      if (monitoringDocumentWatch) {
        this.updateForm(monitoringDocumentWatch);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const monitoringDocumentWatch = this.monitoringDocumentWatchFormService.getMonitoringDocumentWatch(this.editForm);
    if (monitoringDocumentWatch.id !== null) {
      this.subscribeToSaveResponse(this.monitoringDocumentWatchService.update(monitoringDocumentWatch));
    } else {
      this.subscribeToSaveResponse(this.monitoringDocumentWatchService.create(monitoringDocumentWatch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMonitoringDocumentWatch>>): void {
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

  protected updateForm(monitoringDocumentWatch: IMonitoringDocumentWatch): void {
    this.monitoringDocumentWatch = monitoringDocumentWatch;
    this.monitoringDocumentWatchFormService.resetForm(this.editForm, monitoringDocumentWatch);
  }
}
