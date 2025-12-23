import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { NotificationChannel } from 'app/entities/enumerations/notification-channel.model';
import { NotificationType } from 'app/entities/enumerations/notification-type.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { INotificationTemplate } from '../notification-template.model';
import { NotificationTemplateService } from '../service/notification-template.service';

import { NotificationTemplateFormGroup, NotificationTemplateFormService } from './notification-template-form.service';

@Component({
  selector: 'jhi-notification-template-update',
  templateUrl: './notification-template-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class NotificationTemplateUpdate implements OnInit {
  isSaving = false;
  notificationTemplate: INotificationTemplate | null = null;
  notificationTypeValues = Object.keys(NotificationType);
  notificationChannelValues = Object.keys(NotificationChannel);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected notificationTemplateService = inject(NotificationTemplateService);
  protected notificationTemplateFormService = inject(NotificationTemplateFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: NotificationTemplateFormGroup = this.notificationTemplateFormService.createNotificationTemplateFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificationTemplate }) => {
      this.notificationTemplate = notificationTemplate;
      if (notificationTemplate) {
        this.updateForm(notificationTemplate);
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
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notificationTemplate = this.notificationTemplateFormService.getNotificationTemplate(this.editForm);
    if (notificationTemplate.id === null) {
      this.subscribeToSaveResponse(this.notificationTemplateService.create(notificationTemplate));
    } else {
      this.subscribeToSaveResponse(this.notificationTemplateService.update(notificationTemplate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotificationTemplate>>): void {
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

  protected updateForm(notificationTemplate: INotificationTemplate): void {
    this.notificationTemplate = notificationTemplate;
    this.notificationTemplateFormService.resetForm(this.editForm, notificationTemplate);
  }
}
