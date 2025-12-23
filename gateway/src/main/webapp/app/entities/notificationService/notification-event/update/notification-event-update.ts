import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { INotificationEvent } from '../notification-event.model';
import { NotificationEventService } from '../service/notification-event.service';

import { NotificationEventFormGroup, NotificationEventFormService } from './notification-event-form.service';

@Component({
  selector: 'jhi-notification-event-update',
  templateUrl: './notification-event-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class NotificationEventUpdate implements OnInit {
  isSaving = false;
  notificationEvent: INotificationEvent | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected notificationEventService = inject(NotificationEventService);
  protected notificationEventFormService = inject(NotificationEventFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: NotificationEventFormGroup = this.notificationEventFormService.createNotificationEventFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificationEvent }) => {
      this.notificationEvent = notificationEvent;
      if (notificationEvent) {
        this.updateForm(notificationEvent);
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
    const notificationEvent = this.notificationEventFormService.getNotificationEvent(this.editForm);
    if (notificationEvent.id === null) {
      this.subscribeToSaveResponse(this.notificationEventService.create(notificationEvent));
    } else {
      this.subscribeToSaveResponse(this.notificationEventService.update(notificationEvent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotificationEvent>>): void {
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

  protected updateForm(notificationEvent: INotificationEvent): void {
    this.notificationEvent = notificationEvent;
    this.notificationEventFormService.resetForm(this.editForm, notificationEvent);
  }
}
