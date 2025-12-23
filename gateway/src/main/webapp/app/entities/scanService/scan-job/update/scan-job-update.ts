import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { ColorMode } from 'app/entities/enumerations/color-mode.model';
import { ScanFormat } from 'app/entities/enumerations/scan-format.model';
import { ScanStatus } from 'app/entities/enumerations/scan-status.model';
import { IScanBatch } from 'app/entities/scanService/scan-batch/scan-batch.model';
import { ScanBatchService } from 'app/entities/scanService/scan-batch/service/scan-batch.service';
import { IScannerConfiguration } from 'app/entities/scanService/scanner-configuration/scanner-configuration.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';

import { IScanJob } from '../scan-job.model';
import { ScanJobFormService, ScanJobFormGroup } from './scan-job-form.service';
import { ScanJobService } from '../service/scan-job.service';
import { ScannerConfigurationService } from 'app/entities/scanService/scanner-configuration/service/scanner-configuration.service';

@Component({
  selector: 'jhi-scan-job-update',
  templateUrl: './scan-job-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ScanJobUpdate implements OnInit {
  isSaving = false;
  scanJob: IScanJob | null = null;
  scanStatusValues = Object.keys(ScanStatus);
  colorModeValues = Object.keys(ColorMode);
  scanFormatValues = Object.keys(ScanFormat);

  scannerConfigurationsSharedCollection = signal<IScannerConfiguration[]>([]);
  scanBatchesSharedCollection = signal<IScanBatch[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected scanJobService = inject(ScanJobService);
  protected scanJobFormService = inject(ScanJobFormService);
  protected scannerConfigurationService = inject(ScannerConfigurationService);
  protected scanBatchService = inject(ScanBatchService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ScanJobFormGroup = this.scanJobFormService.createScanJobFormGroup();

  compareScannerConfiguration = (o1: IScannerConfiguration | null, o2: IScannerConfiguration | null): boolean =>
    this.scannerConfigurationService.compareScannerConfiguration(o1, o2);

  compareScanBatch = (o1: IScanBatch | null, o2: IScanBatch | null): boolean => this.scanBatchService.compareScanBatch(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scanJob }) => {
      this.scanJob = scanJob;
      if (scanJob) {
        this.updateForm(scanJob);
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
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const scanJob = this.scanJobFormService.getScanJob(this.editForm);
    if (scanJob.id === null) {
      this.subscribeToSaveResponse(this.scanJobService.create(scanJob));
    } else {
      this.subscribeToSaveResponse(this.scanJobService.update(scanJob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IScanJob>>): void {
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

  protected updateForm(scanJob: IScanJob): void {
    this.scanJob = scanJob;
    this.scanJobFormService.resetForm(this.editForm, scanJob);

    this.scannerConfigurationsSharedCollection.set(
      this.scannerConfigurationService.addScannerConfigurationToCollectionIfMissing<IScannerConfiguration>(
        this.scannerConfigurationsSharedCollection(),
        scanJob.scannerConfig,
      ),
    );
    this.scanBatchesSharedCollection.set(
      this.scanBatchService.addScanBatchToCollectionIfMissing<IScanBatch>(this.scanBatchesSharedCollection(), scanJob.batch),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.scannerConfigurationService
      .query()
      .pipe(map((res: HttpResponse<IScannerConfiguration[]>) => res.body ?? []))
      .pipe(
        map((scannerConfigurations: IScannerConfiguration[]) =>
          this.scannerConfigurationService.addScannerConfigurationToCollectionIfMissing<IScannerConfiguration>(
            scannerConfigurations,
            this.scanJob?.scannerConfig,
          ),
        ),
      )
      .subscribe((scannerConfigurations: IScannerConfiguration[]) => this.scannerConfigurationsSharedCollection.set(scannerConfigurations));

    this.scanBatchService
      .query()
      .pipe(map((res: HttpResponse<IScanBatch[]>) => res.body ?? []))
      .pipe(
        map((scanBatches: IScanBatch[]) =>
          this.scanBatchService.addScanBatchToCollectionIfMissing<IScanBatch>(scanBatches, this.scanJob?.batch),
        ),
      )
      .subscribe((scanBatches: IScanBatch[]) => this.scanBatchesSharedCollection.set(scanBatches));
  }
}
