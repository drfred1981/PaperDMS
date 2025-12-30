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
import { MetaPermissionGroupService } from '../service/meta-permission-group.service';
import { IMetaPermissionGroup } from '../meta-permission-group.model';
import { MetaPermissionGroupFormGroup, MetaPermissionGroupFormService } from './meta-permission-group-form.service';

@Component({
  selector: 'jhi-meta-permission-group-update',
  templateUrl: './meta-permission-group-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MetaPermissionGroupUpdateComponent implements OnInit {
  isSaving = false;
  metaPermissionGroup: IMetaPermissionGroup | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected metaPermissionGroupService = inject(MetaPermissionGroupService);
  protected metaPermissionGroupFormService = inject(MetaPermissionGroupFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MetaPermissionGroupFormGroup = this.metaPermissionGroupFormService.createMetaPermissionGroupFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ metaPermissionGroup }) => {
      this.metaPermissionGroup = metaPermissionGroup;
      if (metaPermissionGroup) {
        this.updateForm(metaPermissionGroup);
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
    const metaPermissionGroup = this.metaPermissionGroupFormService.getMetaPermissionGroup(this.editForm);
    if (metaPermissionGroup.id !== null) {
      this.subscribeToSaveResponse(this.metaPermissionGroupService.update(metaPermissionGroup));
    } else {
      this.subscribeToSaveResponse(this.metaPermissionGroupService.create(metaPermissionGroup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMetaPermissionGroup>>): void {
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

  protected updateForm(metaPermissionGroup: IMetaPermissionGroup): void {
    this.metaPermissionGroup = metaPermissionGroup;
    this.metaPermissionGroupFormService.resetForm(this.editForm, metaPermissionGroup);
  }
}
