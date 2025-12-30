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
import { ConversionStatus } from 'app/entities/enumerations/conversion-status.model';
import { ImageConversionHistoryService } from '../service/image-conversion-history.service';
import { IImageConversionHistory } from '../image-conversion-history.model';
import { ImageConversionHistoryFormGroup, ImageConversionHistoryFormService } from './image-conversion-history-form.service';

@Component({
  selector: 'jhi-image-conversion-history-update',
  templateUrl: './image-conversion-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ImageConversionHistoryUpdateComponent implements OnInit {
  isSaving = false;
  imageConversionHistory: IImageConversionHistory | null = null;
  conversionStatusValues = Object.keys(ConversionStatus);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected imageConversionHistoryService = inject(ImageConversionHistoryService);
  protected imageConversionHistoryFormService = inject(ImageConversionHistoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ImageConversionHistoryFormGroup = this.imageConversionHistoryFormService.createImageConversionHistoryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imageConversionHistory }) => {
      this.imageConversionHistory = imageConversionHistory;
      if (imageConversionHistory) {
        this.updateForm(imageConversionHistory);
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
    const imageConversionHistory = this.imageConversionHistoryFormService.getImageConversionHistory(this.editForm);
    if (imageConversionHistory.id !== null) {
      this.subscribeToSaveResponse(this.imageConversionHistoryService.update(imageConversionHistory));
    } else {
      this.subscribeToSaveResponse(this.imageConversionHistoryService.create(imageConversionHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImageConversionHistory>>): void {
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

  protected updateForm(imageConversionHistory: IImageConversionHistory): void {
    this.imageConversionHistory = imageConversionHistory;
    this.imageConversionHistoryFormService.resetForm(this.editForm, imageConversionHistory);
  }
}
