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
import { SmartFolderService } from '../service/smart-folder.service';
import { ISmartFolder } from '../smart-folder.model';

import { SmartFolderFormGroup, SmartFolderFormService } from './smart-folder-form.service';

@Component({
  selector: 'jhi-smart-folder-update',
  templateUrl: './smart-folder-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class SmartFolderUpdate implements OnInit {
  isSaving = false;
  smartFolder: ISmartFolder | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected smartFolderService = inject(SmartFolderService);
  protected smartFolderFormService = inject(SmartFolderFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SmartFolderFormGroup = this.smartFolderFormService.createSmartFolderFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ smartFolder }) => {
      this.smartFolder = smartFolder;
      if (smartFolder) {
        this.updateForm(smartFolder);
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
    const smartFolder = this.smartFolderFormService.getSmartFolder(this.editForm);
    if (smartFolder.id === null) {
      this.subscribeToSaveResponse(this.smartFolderService.create(smartFolder));
    } else {
      this.subscribeToSaveResponse(this.smartFolderService.update(smartFolder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmartFolder>>): void {
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

  protected updateForm(smartFolder: ISmartFolder): void {
    this.smartFolder = smartFolder;
    this.smartFolderFormService.resetForm(this.editForm, smartFolder);
  }
}
