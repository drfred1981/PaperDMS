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
import { IImageConversionBatch } from 'app/entities/pdfToImageService/image-conversion-batch/image-conversion-batch.model';
import { ImageConversionBatchService } from 'app/entities/pdfToImageService/image-conversion-batch/service/image-conversion-batch.service';
import { ImageQuality } from 'app/entities/enumerations/image-quality.model';
import { ImageFormat } from 'app/entities/enumerations/image-format.model';
import { ConversionType } from 'app/entities/enumerations/conversion-type.model';
import { ConversionStatus } from 'app/entities/enumerations/conversion-status.model';
import { ImagePdfConversionRequestService } from '../service/image-pdf-conversion-request.service';
import { IImagePdfConversionRequest } from '../image-pdf-conversion-request.model';
import { ImagePdfConversionRequestFormGroup, ImagePdfConversionRequestFormService } from './image-pdf-conversion-request-form.service';

@Component({
  selector: 'jhi-image-pdf-conversion-request-update',
  templateUrl: './image-pdf-conversion-request-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ImagePdfConversionRequestUpdateComponent implements OnInit {
  isSaving = false;
  imagePdfConversionRequest: IImagePdfConversionRequest | null = null;
  imageQualityValues = Object.keys(ImageQuality);
  imageFormatValues = Object.keys(ImageFormat);
  conversionTypeValues = Object.keys(ConversionType);
  conversionStatusValues = Object.keys(ConversionStatus);

  imageConversionBatchesSharedCollection: IImageConversionBatch[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected imagePdfConversionRequestService = inject(ImagePdfConversionRequestService);
  protected imagePdfConversionRequestFormService = inject(ImagePdfConversionRequestFormService);
  protected imageConversionBatchService = inject(ImageConversionBatchService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ImagePdfConversionRequestFormGroup = this.imagePdfConversionRequestFormService.createImagePdfConversionRequestFormGroup();

  compareImageConversionBatch = (o1: IImageConversionBatch | null, o2: IImageConversionBatch | null): boolean =>
    this.imageConversionBatchService.compareImageConversionBatch(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imagePdfConversionRequest }) => {
      this.imagePdfConversionRequest = imagePdfConversionRequest;
      if (imagePdfConversionRequest) {
        this.updateForm(imagePdfConversionRequest);
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
    const imagePdfConversionRequest = this.imagePdfConversionRequestFormService.getImagePdfConversionRequest(this.editForm);
    if (imagePdfConversionRequest.id !== null) {
      this.subscribeToSaveResponse(this.imagePdfConversionRequestService.update(imagePdfConversionRequest));
    } else {
      this.subscribeToSaveResponse(this.imagePdfConversionRequestService.create(imagePdfConversionRequest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImagePdfConversionRequest>>): void {
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

  protected updateForm(imagePdfConversionRequest: IImagePdfConversionRequest): void {
    this.imagePdfConversionRequest = imagePdfConversionRequest;
    this.imagePdfConversionRequestFormService.resetForm(this.editForm, imagePdfConversionRequest);

    this.imageConversionBatchesSharedCollection =
      this.imageConversionBatchService.addImageConversionBatchToCollectionIfMissing<IImageConversionBatch>(
        this.imageConversionBatchesSharedCollection,
        imagePdfConversionRequest.batch,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.imageConversionBatchService
      .query()
      .pipe(map((res: HttpResponse<IImageConversionBatch[]>) => res.body ?? []))
      .pipe(
        map((imageConversionBatches: IImageConversionBatch[]) =>
          this.imageConversionBatchService.addImageConversionBatchToCollectionIfMissing<IImageConversionBatch>(
            imageConversionBatches,
            this.imagePdfConversionRequest?.batch,
          ),
        ),
      )
      .subscribe(
        (imageConversionBatches: IImageConversionBatch[]) => (this.imageConversionBatchesSharedCollection = imageConversionBatches),
      );
  }
}
