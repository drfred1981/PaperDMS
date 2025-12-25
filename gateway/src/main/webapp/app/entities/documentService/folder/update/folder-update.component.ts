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
import { FolderService } from '../service/folder.service';
import { IFolder } from '../folder.model';
import { FolderFormGroup, FolderFormService } from './folder-form.service';

@Component({
  selector: 'jhi-folder-update',
  templateUrl: './folder-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FolderUpdateComponent implements OnInit {
  isSaving = false;
  folder: IFolder | null = null;

  foldersSharedCollection: IFolder[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected folderService = inject(FolderService);
  protected folderFormService = inject(FolderFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FolderFormGroup = this.folderFormService.createFolderFormGroup();

  compareFolder = (o1: IFolder | null, o2: IFolder | null): boolean => this.folderService.compareFolder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ folder }) => {
      this.folder = folder;
      if (folder) {
        this.updateForm(folder);
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
    const folder = this.folderFormService.getFolder(this.editForm);
    if (folder.id !== null) {
      this.subscribeToSaveResponse(this.folderService.update(folder));
    } else {
      this.subscribeToSaveResponse(this.folderService.create(folder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFolder>>): void {
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

  protected updateForm(folder: IFolder): void {
    this.folder = folder;
    this.folderFormService.resetForm(this.editForm, folder);

    this.foldersSharedCollection = this.folderService.addFolderToCollectionIfMissing<IFolder>(this.foldersSharedCollection, folder.parent);
  }

  protected loadRelationshipsOptions(): void {
    this.folderService
      .query()
      .pipe(map((res: HttpResponse<IFolder[]>) => res.body ?? []))
      .pipe(map((folders: IFolder[]) => this.folderService.addFolderToCollectionIfMissing<IFolder>(folders, this.folder?.parent)))
      .subscribe((folders: IFolder[]) => (this.foldersSharedCollection = folders));
  }
}
