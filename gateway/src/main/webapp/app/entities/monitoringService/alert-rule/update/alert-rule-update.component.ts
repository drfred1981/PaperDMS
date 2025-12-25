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
import { AlertType } from 'app/entities/enumerations/alert-type.model';
import { Severity } from 'app/entities/enumerations/severity.model';
import { AlertRuleService } from '../service/alert-rule.service';
import { IAlertRule } from '../alert-rule.model';
import { AlertRuleFormGroup, AlertRuleFormService } from './alert-rule-form.service';

@Component({
  selector: 'jhi-alert-rule-update',
  templateUrl: './alert-rule-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AlertRuleUpdateComponent implements OnInit {
  isSaving = false;
  alertRule: IAlertRule | null = null;
  alertTypeValues = Object.keys(AlertType);
  severityValues = Object.keys(Severity);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected alertRuleService = inject(AlertRuleService);
  protected alertRuleFormService = inject(AlertRuleFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AlertRuleFormGroup = this.alertRuleFormService.createAlertRuleFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alertRule }) => {
      this.alertRule = alertRule;
      if (alertRule) {
        this.updateForm(alertRule);
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
    const alertRule = this.alertRuleFormService.getAlertRule(this.editForm);
    if (alertRule.id !== null) {
      this.subscribeToSaveResponse(this.alertRuleService.update(alertRule));
    } else {
      this.subscribeToSaveResponse(this.alertRuleService.create(alertRule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlertRule>>): void {
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

  protected updateForm(alertRule: IAlertRule): void {
    this.alertRule = alertRule;
    this.alertRuleFormService.resetForm(this.editForm, alertRule);
  }
}
