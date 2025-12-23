import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { WebhookSubscriptionService } from 'app/entities/notificationService/webhook-subscription/service/webhook-subscription.service';
import { IWebhookSubscription } from 'app/entities/notificationService/webhook-subscription/webhook-subscription.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { WebhookLogService } from '../service/webhook-log.service';
import { IWebhookLog } from '../webhook-log.model';

import { WebhookLogFormGroup, WebhookLogFormService } from './webhook-log-form.service';

@Component({
  selector: 'jhi-webhook-log-update',
  templateUrl: './webhook-log-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class WebhookLogUpdate implements OnInit {
  isSaving = false;
  webhookLog: IWebhookLog | null = null;

  webhookSubscriptionsSharedCollection = signal<IWebhookSubscription[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected webhookLogService = inject(WebhookLogService);
  protected webhookLogFormService = inject(WebhookLogFormService);
  protected webhookSubscriptionService = inject(WebhookSubscriptionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WebhookLogFormGroup = this.webhookLogFormService.createWebhookLogFormGroup();

  compareWebhookSubscription = (o1: IWebhookSubscription | null, o2: IWebhookSubscription | null): boolean =>
    this.webhookSubscriptionService.compareWebhookSubscription(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ webhookLog }) => {
      this.webhookLog = webhookLog;
      if (webhookLog) {
        this.updateForm(webhookLog);
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
    const webhookLog = this.webhookLogFormService.getWebhookLog(this.editForm);
    if (webhookLog.id === null) {
      this.subscribeToSaveResponse(this.webhookLogService.create(webhookLog));
    } else {
      this.subscribeToSaveResponse(this.webhookLogService.update(webhookLog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWebhookLog>>): void {
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

  protected updateForm(webhookLog: IWebhookLog): void {
    this.webhookLog = webhookLog;
    this.webhookLogFormService.resetForm(this.editForm, webhookLog);

    this.webhookSubscriptionsSharedCollection.set(
      this.webhookSubscriptionService.addWebhookSubscriptionToCollectionIfMissing<IWebhookSubscription>(
        this.webhookSubscriptionsSharedCollection(),
        webhookLog.subscription,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.webhookSubscriptionService
      .query()
      .pipe(map((res: HttpResponse<IWebhookSubscription[]>) => res.body ?? []))
      .pipe(
        map((webhookSubscriptions: IWebhookSubscription[]) =>
          this.webhookSubscriptionService.addWebhookSubscriptionToCollectionIfMissing<IWebhookSubscription>(
            webhookSubscriptions,
            this.webhookLog?.subscription,
          ),
        ),
      )
      .subscribe((webhookSubscriptions: IWebhookSubscription[]) => this.webhookSubscriptionsSharedCollection.set(webhookSubscriptions));
  }
}
