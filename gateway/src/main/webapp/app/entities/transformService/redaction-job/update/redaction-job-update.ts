import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { RedactionType } from 'app/entities/enumerations/redaction-type.model';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IRedactionJob } from '../redaction-job.model';
import { RedactionJobService } from '../service/redaction-job.service';

import { RedactionJobFormGroup, RedactionJobFormService } from './redaction-job-form.service';

@Component({
  selector: 'jhi-redaction-job-update',
  templateUrl: './redaction-job-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class RedactionJobUpdate implements OnInit {
  isSaving = false;
  redactionJob: IRedactionJob | null = null;
  redactionTypeValues = Object.keys(RedactionType);
  transformStatusValues = Object.keys(TransformStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected redactionJobService = inject(RedactionJobService);
  protected redactionJobFormService = inject(RedactionJobFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RedactionJobFormGroup = this.redactionJobFormService.createRedactionJobFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ redactionJob }) => {
      this.redactionJob = redactionJob;
      if (redactionJob) {
        this.updateForm(redactionJob);
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
    const redactionJob = this.redactionJobFormService.getRedactionJob(this.editForm);
    if (redactionJob.id === null) {
      this.subscribeToSaveResponse(this.redactionJobService.create(redactionJob));
    } else {
      this.subscribeToSaveResponse(this.redactionJobService.update(redactionJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRedactionJob>>): void {
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

  protected updateForm(redactionJob: IRedactionJob): void {
    this.redactionJob = redactionJob;
    this.redactionJobFormService.resetForm(this.editForm, redactionJob);
  }
}
