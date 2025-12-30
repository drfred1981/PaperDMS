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
import { INotificationWebhookSubscription } from 'app/entities/notificationService/notification-webhook-subscription/notification-webhook-subscription.model';
import { NotificationWebhookSubscriptionService } from 'app/entities/notificationService/notification-webhook-subscription/service/notification-webhook-subscription.service';
import { NotificationWebhookLogService } from '../service/notification-webhook-log.service';
import { INotificationWebhookLog } from '../notification-webhook-log.model';
import { NotificationWebhookLogFormGroup, NotificationWebhookLogFormService } from './notification-webhook-log-form.service';

@Component({
  selector: 'jhi-notification-webhook-log-update',
  templateUrl: './notification-webhook-log-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class NotificationWebhookLogUpdateComponent implements OnInit {
  isSaving = false;
  notificationWebhookLog: INotificationWebhookLog | null = null;

  notificationWebhookSubscriptionsSharedCollection: INotificationWebhookSubscription[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected notificationWebhookLogService = inject(NotificationWebhookLogService);
  protected notificationWebhookLogFormService = inject(NotificationWebhookLogFormService);
  protected notificationWebhookSubscriptionService = inject(NotificationWebhookSubscriptionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: NotificationWebhookLogFormGroup = this.notificationWebhookLogFormService.createNotificationWebhookLogFormGroup();

  compareNotificationWebhookSubscription = (
    o1: INotificationWebhookSubscription | null,
    o2: INotificationWebhookSubscription | null,
  ): boolean => this.notificationWebhookSubscriptionService.compareNotificationWebhookSubscription(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificationWebhookLog }) => {
      this.notificationWebhookLog = notificationWebhookLog;
      if (notificationWebhookLog) {
        this.updateForm(notificationWebhookLog);
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
    const notificationWebhookLog = this.notificationWebhookLogFormService.getNotificationWebhookLog(this.editForm);
    if (notificationWebhookLog.id !== null) {
      this.subscribeToSaveResponse(this.notificationWebhookLogService.update(notificationWebhookLog));
    } else {
      this.subscribeToSaveResponse(this.notificationWebhookLogService.create(notificationWebhookLog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotificationWebhookLog>>): void {
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

  protected updateForm(notificationWebhookLog: INotificationWebhookLog): void {
    this.notificationWebhookLog = notificationWebhookLog;
    this.notificationWebhookLogFormService.resetForm(this.editForm, notificationWebhookLog);

    this.notificationWebhookSubscriptionsSharedCollection =
      this.notificationWebhookSubscriptionService.addNotificationWebhookSubscriptionToCollectionIfMissing<INotificationWebhookSubscription>(
        this.notificationWebhookSubscriptionsSharedCollection,
        notificationWebhookLog.subscription,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.notificationWebhookSubscriptionService
      .query()
      .pipe(map((res: HttpResponse<INotificationWebhookSubscription[]>) => res.body ?? []))
      .pipe(
        map((notificationWebhookSubscriptions: INotificationWebhookSubscription[]) =>
          this.notificationWebhookSubscriptionService.addNotificationWebhookSubscriptionToCollectionIfMissing<INotificationWebhookSubscription>(
            notificationWebhookSubscriptions,
            this.notificationWebhookLog?.subscription,
          ),
        ),
      )
      .subscribe(
        (notificationWebhookSubscriptions: INotificationWebhookSubscription[]) =>
          (this.notificationWebhookSubscriptionsSharedCollection = notificationWebhookSubscriptions),
      );
  }
}
