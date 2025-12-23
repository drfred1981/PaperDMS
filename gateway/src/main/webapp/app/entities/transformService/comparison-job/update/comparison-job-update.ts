import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { ComparisonType } from 'app/entities/enumerations/comparison-type.model';
import { TransformStatus } from 'app/entities/enumerations/transform-status.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IComparisonJob } from '../comparison-job.model';
import { ComparisonJobService } from '../service/comparison-job.service';

import { ComparisonJobFormGroup, ComparisonJobFormService } from './comparison-job-form.service';

@Component({
  selector: 'jhi-comparison-job-update',
  templateUrl: './comparison-job-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ComparisonJobUpdate implements OnInit {
  isSaving = false;
  comparisonJob: IComparisonJob | null = null;
  comparisonTypeValues = Object.keys(ComparisonType);
  transformStatusValues = Object.keys(TransformStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected comparisonJobService = inject(ComparisonJobService);
  protected comparisonJobFormService = inject(ComparisonJobFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ComparisonJobFormGroup = this.comparisonJobFormService.createComparisonJobFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comparisonJob }) => {
      this.comparisonJob = comparisonJob;
      if (comparisonJob) {
        this.updateForm(comparisonJob);
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
    const comparisonJob = this.comparisonJobFormService.getComparisonJob(this.editForm);
    if (comparisonJob.id === null) {
      this.subscribeToSaveResponse(this.comparisonJobService.create(comparisonJob));
    } else {
      this.subscribeToSaveResponse(this.comparisonJobService.update(comparisonJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComparisonJob>>): void {
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

  protected updateForm(comparisonJob: IComparisonJob): void {
    this.comparisonJob = comparisonJob;
    this.comparisonJobFormService.resetForm(this.editForm, comparisonJob);
  }
}
