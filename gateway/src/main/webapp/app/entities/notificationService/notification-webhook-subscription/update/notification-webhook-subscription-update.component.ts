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
import { NotificationWebhookSubscriptionService } from '../service/notification-webhook-subscription.service';
import { INotificationWebhookSubscription } from '../notification-webhook-subscription.model';
import {
  NotificationWebhookSubscriptionFormGroup,
  NotificationWebhookSubscriptionFormService,
} from './notification-webhook-subscription-form.service';

@Component({
  selector: 'jhi-notification-webhook-subscription-update',
  templateUrl: './notification-webhook-subscription-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class NotificationWebhookSubscriptionUpdateComponent implements OnInit {
  isSaving = false;
  notificationWebhookSubscription: INotificationWebhookSubscription | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected notificationWebhookSubscriptionService = inject(NotificationWebhookSubscriptionService);
  protected notificationWebhookSubscriptionFormService = inject(NotificationWebhookSubscriptionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: NotificationWebhookSubscriptionFormGroup =
    this.notificationWebhookSubscriptionFormService.createNotificationWebhookSubscriptionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificationWebhookSubscription }) => {
      this.notificationWebhookSubscription = notificationWebhookSubscription;
      if (notificationWebhookSubscription) {
        this.updateForm(notificationWebhookSubscription);
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
    const notificationWebhookSubscription = this.notificationWebhookSubscriptionFormService.getNotificationWebhookSubscription(
      this.editForm,
    );
    if (notificationWebhookSubscription.id !== null) {
      this.subscribeToSaveResponse(this.notificationWebhookSubscriptionService.update(notificationWebhookSubscription));
    } else {
      this.subscribeToSaveResponse(this.notificationWebhookSubscriptionService.create(notificationWebhookSubscription));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotificationWebhookSubscription>>): void {
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

  protected updateForm(notificationWebhookSubscription: INotificationWebhookSubscription): void {
    this.notificationWebhookSubscription = notificationWebhookSubscription;
    this.notificationWebhookSubscriptionFormService.resetForm(this.editForm, notificationWebhookSubscription);
  }
}
