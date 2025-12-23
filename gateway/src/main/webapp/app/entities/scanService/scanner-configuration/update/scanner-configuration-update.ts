import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { ColorMode } from 'app/entities/enumerations/color-mode.model';
import { ScanFormat } from 'app/entities/enumerations/scan-format.model';
import { ScannerType } from 'app/entities/enumerations/scanner-type.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IScannerConfiguration } from '../scanner-configuration.model';
import { ScannerConfigurationService } from '../service/scanner-configuration.service';

import { ScannerConfigurationFormGroup, ScannerConfigurationFormService } from './scanner-configuration-form.service';

@Component({
  selector: 'jhi-scanner-configuration-update',
  templateUrl: './scanner-configuration-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ScannerConfigurationUpdate implements OnInit {
  isSaving = false;
  scannerConfiguration: IScannerConfiguration | null = null;
  scannerTypeValues = Object.keys(ScannerType);
  colorModeValues = Object.keys(ColorMode);
  scanFormatValues = Object.keys(ScanFormat);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected scannerConfigurationService = inject(ScannerConfigurationService);
  protected scannerConfigurationFormService = inject(ScannerConfigurationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ScannerConfigurationFormGroup = this.scannerConfigurationFormService.createScannerConfigurationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scannerConfiguration }) => {
      this.scannerConfiguration = scannerConfiguration;
      if (scannerConfiguration) {
        this.updateForm(scannerConfiguration);
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
    const scannerConfiguration = this.scannerConfigurationFormService.getScannerConfiguration(this.editForm);
    if (scannerConfiguration.id === null) {
      this.subscribeToSaveResponse(this.scannerConfigurationService.create(scannerConfiguration));
    } else {
      this.subscribeToSaveResponse(this.scannerConfigurationService.update(scannerConfiguration));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IScannerConfiguration>>): void {
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

  protected updateForm(scannerConfiguration: IScannerConfiguration): void {
    this.scannerConfiguration = scannerConfiguration;
    this.scannerConfigurationFormService.resetForm(this.editForm, scannerConfiguration);
  }
}
