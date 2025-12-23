import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IConversionJob } from '../conversion-job.model';
import { ConversionJobService } from '../service/conversion-job.service';

import { ConversionJobFormGroup, ConversionJobFormService } from './conversion-job-form.service';

@Component({
  selector: 'jhi-conversion-job-update',
  templateUrl: './conversion-job-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ConversionJobUpdate implements OnInit {
  isSaving = false;
  conversionJob: IConversionJob | null = null;
  transformStatusValues = Object.keys(TransformStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected conversionJobService = inject(ConversionJobService);
  protected conversionJobFormService = inject(ConversionJobFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConversionJobFormGroup = this.conversionJobFormService.createConversionJobFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conversionJob }) => {
      this.conversionJob = conversionJob;
      if (conversionJob) {
        this.updateForm(conversionJob);
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
    const conversionJob = this.conversionJobFormService.getConversionJob(this.editForm);
    if (conversionJob.id === null) {
      this.subscribeToSaveResponse(this.conversionJobService.create(conversionJob));
    } else {
      this.subscribeToSaveResponse(this.conversionJobService.update(conversionJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConversionJob>>): void {
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

  protected updateForm(conversionJob: IConversionJob): void {
    this.conversionJob = conversionJob;
    this.conversionJobFormService.resetForm(this.editForm, conversionJob);
  }
}
