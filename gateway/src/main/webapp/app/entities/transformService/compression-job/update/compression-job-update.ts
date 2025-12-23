import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { CompressionType } from 'app/entities/enumerations/compression-type.model';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { ICompressionJob } from '../compression-job.model';
import { CompressionJobService } from '../service/compression-job.service';

import { CompressionJobFormGroup, CompressionJobFormService } from './compression-job-form.service';

@Component({
  selector: 'jhi-compression-job-update',
  templateUrl: './compression-job-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class CompressionJobUpdate implements OnInit {
  isSaving = false;
  compressionJob: ICompressionJob | null = null;
  compressionTypeValues = Object.keys(CompressionType);
  transformStatusValues = Object.keys(TransformStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected compressionJobService = inject(CompressionJobService);
  protected compressionJobFormService = inject(CompressionJobFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CompressionJobFormGroup = this.compressionJobFormService.createCompressionJobFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compressionJob }) => {
      this.compressionJob = compressionJob;
      if (compressionJob) {
        this.updateForm(compressionJob);
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
    const compressionJob = this.compressionJobFormService.getCompressionJob(this.editForm);
    if (compressionJob.id === null) {
      this.subscribeToSaveResponse(this.compressionJobService.create(compressionJob));
    } else {
      this.subscribeToSaveResponse(this.compressionJobService.update(compressionJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompressionJob>>): void {
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

  protected updateForm(compressionJob: ICompressionJob): void {
    this.compressionJob = compressionJob;
    this.compressionJobFormService.resetForm(this.editForm, compressionJob);
  }
}
