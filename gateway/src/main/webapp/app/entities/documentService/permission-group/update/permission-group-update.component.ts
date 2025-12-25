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
import { PermissionGroupService } from '../service/permission-group.service';
import { IPermissionGroup } from '../permission-group.model';
import { PermissionGroupFormGroup, PermissionGroupFormService } from './permission-group-form.service';

@Component({
  selector: 'jhi-permission-group-update',
  templateUrl: './permission-group-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PermissionGroupUpdateComponent implements OnInit {
  isSaving = false;
  permissionGroup: IPermissionGroup | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected permissionGroupService = inject(PermissionGroupService);
  protected permissionGroupFormService = inject(PermissionGroupFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PermissionGroupFormGroup = this.permissionGroupFormService.createPermissionGroupFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permissionGroup }) => {
      this.permissionGroup = permissionGroup;
      if (permissionGroup) {
        this.updateForm(permissionGroup);
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
    const permissionGroup = this.permissionGroupFormService.getPermissionGroup(this.editForm);
    if (permissionGroup.id !== null) {
      this.subscribeToSaveResponse(this.permissionGroupService.update(permissionGroup));
    } else {
      this.subscribeToSaveResponse(this.permissionGroupService.create(permissionGroup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPermissionGroup>>): void {
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

  protected updateForm(permissionGroup: IPermissionGroup): void {
    this.permissionGroup = permissionGroup;
    this.permissionGroupFormService.resetForm(this.editForm, permissionGroup);
  }
}
