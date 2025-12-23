import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { NotificationChannel } from 'app/entities/enumerations/notification-channel.model';
import { NotificationPriority } from 'app/entities/enumerations/notification-priority.model';
import { NotificationType } from 'app/entities/enumerations/notification-type.model';
import { INotificationTemplate } from 'app/entities/notificationService/notification-template/notification-template.model';
import { NotificationTemplateService } from 'app/entities/notificationService/notification-template/service/notification-template.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { INotification } from '../notification.model';

import { NotificationService } from '../service/notification.service';
import { NotificationFormService, NotificationFormGroup } from './notification-form.service';

@Component({
  selector: 'jhi-notification-update',
  templateUrl: './notification-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class NotificationUpdate implements OnInit {
  isSaving = false;
  notification: INotification | null = null;
  notificationTypeValues = Object.keys(NotificationType);
  notificationPriorityValues = Object.keys(NotificationPriority);
  notificationChannelValues = Object.keys(NotificationChannel);

  notificationTemplatesSharedCollection = signal<INotificationTemplate[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected notificationService = inject(NotificationService);
  protected notificationFormService = inject(NotificationFormService);
  protected notificationTemplateService = inject(NotificationTemplateService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: NotificationFormGroup = this.notificationFormService.createNotificationFormGroup();

  compareNotificationTemplate = (o1: INotificationTemplate | null, o2: INotificationTemplate | null): boolean =>
    this.notificationTemplateService.compareNotificationTemplate(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notification }) => {
      this.notification = notification;
      if (notification) {
        this.updateForm(notification);
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
    const notification = this.notificationFormService.getNotification(this.editForm);
    if (notification.id === null) {
      this.subscribeToSaveResponse(this.notificationService.create(notification));
    } else {
      this.subscribeToSaveResponse(this.notificationService.update(notification));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotification>>): void {
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

  protected updateForm(notification: INotification): void {
    this.notification = notification;
    this.notificationFormService.resetForm(this.editForm, notification);

    this.notificationTemplatesSharedCollection.set(
      this.notificationTemplateService.addNotificationTemplateToCollectionIfMissing<INotificationTemplate>(
        this.notificationTemplatesSharedCollection(),
        notification.template,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.notificationTemplateService
      .query()
      .pipe(map((res: HttpResponse<INotificationTemplate[]>) => res.body ?? []))
      .pipe(
        map((notificationTemplates: INotificationTemplate[]) =>
          this.notificationTemplateService.addNotificationTemplateToCollectionIfMissing<INotificationTemplate>(
            notificationTemplates,
            this.notification?.template,
          ),
        ),
      )
      .subscribe((notificationTemplates: INotificationTemplate[]) => this.notificationTemplatesSharedCollection.set(notificationTemplates));
  }
}
