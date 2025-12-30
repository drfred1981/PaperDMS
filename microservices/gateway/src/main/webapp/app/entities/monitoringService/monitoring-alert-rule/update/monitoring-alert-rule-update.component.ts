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
import { MonitoringAlertType } from 'app/entities/enumerations/monitoring-alert-type.model';
import { Severity } from 'app/entities/enumerations/severity.model';
import { MonitoringAlertRuleService } from '../service/monitoring-alert-rule.service';
import { IMonitoringAlertRule } from '../monitoring-alert-rule.model';
import { MonitoringAlertRuleFormGroup, MonitoringAlertRuleFormService } from './monitoring-alert-rule-form.service';

@Component({
  selector: 'jhi-monitoring-alert-rule-update',
  templateUrl: './monitoring-alert-rule-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MonitoringAlertRuleUpdateComponent implements OnInit {
  isSaving = false;
  monitoringAlertRule: IMonitoringAlertRule | null = null;
  monitoringAlertTypeValues = Object.keys(MonitoringAlertType);
  severityValues = Object.keys(Severity);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected monitoringAlertRuleService = inject(MonitoringAlertRuleService);
  protected monitoringAlertRuleFormService = inject(MonitoringAlertRuleFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MonitoringAlertRuleFormGroup = this.monitoringAlertRuleFormService.createMonitoringAlertRuleFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ monitoringAlertRule }) => {
      this.monitoringAlertRule = monitoringAlertRule;
      if (monitoringAlertRule) {
        this.updateForm(monitoringAlertRule);
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
    const monitoringAlertRule = this.monitoringAlertRuleFormService.getMonitoringAlertRule(this.editForm);
    if (monitoringAlertRule.id !== null) {
      this.subscribeToSaveResponse(this.monitoringAlertRuleService.update(monitoringAlertRule));
    } else {
      this.subscribeToSaveResponse(this.monitoringAlertRuleService.create(monitoringAlertRule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMonitoringAlertRule>>): void {
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

  protected updateForm(monitoringAlertRule: IMonitoringAlertRule): void {
    this.monitoringAlertRule = monitoringAlertRule;
    this.monitoringAlertRuleFormService.resetForm(this.editForm, monitoringAlertRule);
  }
}
