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
import { IMonitoringAlertRule } from 'app/entities/monitoringService/monitoring-alert-rule/monitoring-alert-rule.model';
import { MonitoringAlertRuleService } from 'app/entities/monitoringService/monitoring-alert-rule/service/monitoring-alert-rule.service';
import { Severity } from 'app/entities/enumerations/severity.model';
import { MonitoringAlertStatus } from 'app/entities/enumerations/monitoring-alert-status.model';
import { MonitoringAlertService } from '../service/monitoring-alert.service';
import { IMonitoringAlert } from '../monitoring-alert.model';
import { MonitoringAlertFormGroup, MonitoringAlertFormService } from './monitoring-alert-form.service';

@Component({
  selector: 'jhi-monitoring-alert-update',
  templateUrl: './monitoring-alert-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MonitoringAlertUpdateComponent implements OnInit {
  isSaving = false;
  monitoringAlert: IMonitoringAlert | null = null;
  severityValues = Object.keys(Severity);
  monitoringAlertStatusValues = Object.keys(MonitoringAlertStatus);

  monitoringAlertRulesSharedCollection: IMonitoringAlertRule[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected monitoringAlertService = inject(MonitoringAlertService);
  protected monitoringAlertFormService = inject(MonitoringAlertFormService);
  protected monitoringAlertRuleService = inject(MonitoringAlertRuleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MonitoringAlertFormGroup = this.monitoringAlertFormService.createMonitoringAlertFormGroup();

  compareMonitoringAlertRule = (o1: IMonitoringAlertRule | null, o2: IMonitoringAlertRule | null): boolean =>
    this.monitoringAlertRuleService.compareMonitoringAlertRule(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ monitoringAlert }) => {
      this.monitoringAlert = monitoringAlert;
      if (monitoringAlert) {
        this.updateForm(monitoringAlert);
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
    const monitoringAlert = this.monitoringAlertFormService.getMonitoringAlert(this.editForm);
    if (monitoringAlert.id !== null) {
      this.subscribeToSaveResponse(this.monitoringAlertService.update(monitoringAlert));
    } else {
      this.subscribeToSaveResponse(this.monitoringAlertService.create(monitoringAlert));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMonitoringAlert>>): void {
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

  protected updateForm(monitoringAlert: IMonitoringAlert): void {
    this.monitoringAlert = monitoringAlert;
    this.monitoringAlertFormService.resetForm(this.editForm, monitoringAlert);

    this.monitoringAlertRulesSharedCollection =
      this.monitoringAlertRuleService.addMonitoringAlertRuleToCollectionIfMissing<IMonitoringAlertRule>(
        this.monitoringAlertRulesSharedCollection,
        monitoringAlert.alertRule,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.monitoringAlertRuleService
      .query()
      .pipe(map((res: HttpResponse<IMonitoringAlertRule[]>) => res.body ?? []))
      .pipe(
        map((monitoringAlertRules: IMonitoringAlertRule[]) =>
          this.monitoringAlertRuleService.addMonitoringAlertRuleToCollectionIfMissing<IMonitoringAlertRule>(
            monitoringAlertRules,
            this.monitoringAlert?.alertRule,
          ),
        ),
      )
      .subscribe((monitoringAlertRules: IMonitoringAlertRule[]) => (this.monitoringAlertRulesSharedCollection = monitoringAlertRules));
  }
}
