import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { NotificationFrequency } from 'app/entities/enumerations/notification-frequency.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { INotificationPreference } from '../notification-preference.model';
import { NotificationPreferenceService } from '../service/notification-preference.service';

import { NotificationPreferenceFormGroup, NotificationPreferenceFormService } from './notification-preference-form.service';

@Component({
  selector: 'jhi-notification-preference-update',
  templateUrl: './notification-preference-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class NotificationPreferenceUpdate implements OnInit {
  isSaving = false;
  notificationPreference: INotificationPreference | null = null;
  notificationFrequencyValues = Object.keys(NotificationFrequency);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected notificationPreferenceService = inject(NotificationPreferenceService);
  protected notificationPreferenceFormService = inject(NotificationPreferenceFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: NotificationPreferenceFormGroup = this.notificationPreferenceFormService.createNotificationPreferenceFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificationPreference }) => {
      this.notificationPreference = notificationPreference;
      if (notificationPreference) {
        this.updateForm(notificationPreference);
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
    const notificationPreference = this.notificationPreferenceFormService.getNotificationPreference(this.editForm);
    if (notificationPreference.id === null) {
      this.subscribeToSaveResponse(this.notificationPreferenceService.create(notificationPreference));
    } else {
      this.subscribeToSaveResponse(this.notificationPreferenceService.update(notificationPreference));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotificationPreference>>): void {
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

  protected updateForm(notificationPreference: INotificationPreference): void {
    this.notificationPreference = notificationPreference;
    this.notificationPreferenceFormService.resetForm(this.editForm, notificationPreference);
  }
}
