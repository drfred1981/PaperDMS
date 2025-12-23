import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { AlertStatus } from 'app/entities/enumerations/alert-status.model';
import { Severity } from 'app/entities/enumerations/severity.model';
import { IAlertRule } from 'app/entities/monitoringService/alert-rule/alert-rule.model';
import { AlertRuleService } from 'app/entities/monitoringService/alert-rule/service/alert-rule.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IAlert } from '../alert.model';
import { AlertService } from '../service/alert.service';

import { AlertFormGroup, AlertFormService } from './alert-form.service';

@Component({
  selector: 'jhi-alert-update',
  templateUrl: './alert-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class AlertUpdate implements OnInit {
  isSaving = false;
  alert: IAlert | null = null;
  severityValues = Object.keys(Severity);
  alertStatusValues = Object.keys(AlertStatus);

  alertRulesSharedCollection = signal<IAlertRule[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected alertService = inject(AlertService);
  protected alertFormService = inject(AlertFormService);
  protected alertRuleService = inject(AlertRuleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AlertFormGroup = this.alertFormService.createAlertFormGroup();

  compareAlertRule = (o1: IAlertRule | null, o2: IAlertRule | null): boolean => this.alertRuleService.compareAlertRule(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alert }) => {
      this.alert = alert;
      if (alert) {
        this.updateForm(alert);
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
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alert = this.alertFormService.getAlert(this.editForm);
    if (alert.id === null) {
      this.subscribeToSaveResponse(this.alertService.create(alert));
    } else {
      this.subscribeToSaveResponse(this.alertService.update(alert));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlert>>): void {
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

  protected updateForm(alert: IAlert): void {
    this.alert = alert;
    this.alertFormService.resetForm(this.editForm, alert);

    this.alertRulesSharedCollection.set(
      this.alertRuleService.addAlertRuleToCollectionIfMissing<IAlertRule>(this.alertRulesSharedCollection(), alert.alertRule),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.alertRuleService
      .query()
      .pipe(map((res: HttpResponse<IAlertRule[]>) => res.body ?? []))
      .pipe(
        map((alertRules: IAlertRule[]) =>
          this.alertRuleService.addAlertRuleToCollectionIfMissing<IAlertRule>(alertRules, this.alert?.alertRule),
        ),
      )
      .subscribe((alertRules: IAlertRule[]) => this.alertRulesSharedCollection.set(alertRules));
  }
}
