import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { ArchiveFormat } from 'app/entities/enumerations/archive-format.model';
import { ArchiveStatus } from 'app/entities/enumerations/archive-status.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IArchiveJob } from '../archive-job.model';
import { ArchiveJobService } from '../service/archive-job.service';

import { ArchiveJobFormGroup, ArchiveJobFormService } from './archive-job-form.service';

@Component({
  selector: 'jhi-archive-job-update',
  templateUrl: './archive-job-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ArchiveJobUpdate implements OnInit {
  isSaving = false;
  archiveJob: IArchiveJob | null = null;
  archiveFormatValues = Object.keys(ArchiveFormat);
  archiveStatusValues = Object.keys(ArchiveStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected archiveJobService = inject(ArchiveJobService);
  protected archiveJobFormService = inject(ArchiveJobFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ArchiveJobFormGroup = this.archiveJobFormService.createArchiveJobFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ archiveJob }) => {
      this.archiveJob = archiveJob;
      if (archiveJob) {
        this.updateForm(archiveJob);
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
    const archiveJob = this.archiveJobFormService.getArchiveJob(this.editForm);
    if (archiveJob.id === null) {
      this.subscribeToSaveResponse(this.archiveJobService.create(archiveJob));
    } else {
      this.subscribeToSaveResponse(this.archiveJobService.update(archiveJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArchiveJob>>): void {
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

  protected updateForm(archiveJob: IArchiveJob): void {
    this.archiveJob = archiveJob;
    this.archiveJobFormService.resetForm(this.editForm, archiveJob);
  }
}
