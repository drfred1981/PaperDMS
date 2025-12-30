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
import { MetaFolderService } from '../service/meta-folder.service';
import { IMetaFolder } from '../meta-folder.model';
import { MetaFolderFormGroup, MetaFolderFormService } from './meta-folder-form.service';

@Component({
  selector: 'jhi-meta-folder-update',
  templateUrl: './meta-folder-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MetaFolderUpdateComponent implements OnInit {
  isSaving = false;
  metaFolder: IMetaFolder | null = null;

  metaFoldersSharedCollection: IMetaFolder[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected metaFolderService = inject(MetaFolderService);
  protected metaFolderFormService = inject(MetaFolderFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MetaFolderFormGroup = this.metaFolderFormService.createMetaFolderFormGroup();

  compareMetaFolder = (o1: IMetaFolder | null, o2: IMetaFolder | null): boolean => this.metaFolderService.compareMetaFolder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ metaFolder }) => {
      this.metaFolder = metaFolder;
      if (metaFolder) {
        this.updateForm(metaFolder);
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
    const metaFolder = this.metaFolderFormService.getMetaFolder(this.editForm);
    if (metaFolder.id !== null) {
      this.subscribeToSaveResponse(this.metaFolderService.update(metaFolder));
    } else {
      this.subscribeToSaveResponse(this.metaFolderService.create(metaFolder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMetaFolder>>): void {
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

  protected updateForm(metaFolder: IMetaFolder): void {
    this.metaFolder = metaFolder;
    this.metaFolderFormService.resetForm(this.editForm, metaFolder);

    this.metaFoldersSharedCollection = this.metaFolderService.addMetaFolderToCollectionIfMissing<IMetaFolder>(
      this.metaFoldersSharedCollection,
      metaFolder.parent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.metaFolderService
      .query()
      .pipe(map((res: HttpResponse<IMetaFolder[]>) => res.body ?? []))
      .pipe(
        map((metaFolders: IMetaFolder[]) =>
          this.metaFolderService.addMetaFolderToCollectionIfMissing<IMetaFolder>(metaFolders, this.metaFolder?.parent),
        ),
      )
      .subscribe((metaFolders: IMetaFolder[]) => (this.metaFoldersSharedCollection = metaFolders));
  }
}
