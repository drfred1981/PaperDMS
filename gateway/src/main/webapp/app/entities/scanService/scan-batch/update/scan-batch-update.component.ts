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
import { BatchStatus } from 'app/entities/enumerations/batch-status.model';
import { ScanBatchService } from '../service/scan-batch.service';
import { IScanBatch } from '../scan-batch.model';
import { ScanBatchFormGroup, ScanBatchFormService } from './scan-batch-form.service';

@Component({
  selector: 'jhi-scan-batch-update',
  templateUrl: './scan-batch-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ScanBatchUpdateComponent implements OnInit {
  isSaving = false;
  scanBatch: IScanBatch | null = null;
  batchStatusValues = Object.keys(BatchStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected scanBatchService = inject(ScanBatchService);
  protected scanBatchFormService = inject(ScanBatchFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ScanBatchFormGroup = this.scanBatchFormService.createScanBatchFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scanBatch }) => {
      this.scanBatch = scanBatch;
      if (scanBatch) {
        this.updateForm(scanBatch);
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
    const scanBatch = this.scanBatchFormService.getScanBatch(this.editForm);
    if (scanBatch.id !== null) {
      this.subscribeToSaveResponse(this.scanBatchService.update(scanBatch));
    } else {
      this.subscribeToSaveResponse(this.scanBatchService.create(scanBatch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IScanBatch>>): void {
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

  protected updateForm(scanBatch: IScanBatch): void {
    this.scanBatch = scanBatch;
    this.scanBatchFormService.resetForm(this.editForm, scanBatch);
  }
}
