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
import { IImagePdfConversionRequest } from 'app/entities/pdfToImageService/image-pdf-conversion-request/image-pdf-conversion-request.model';
import { ImagePdfConversionRequestService } from 'app/entities/pdfToImageService/image-pdf-conversion-request/service/image-pdf-conversion-request.service';
import { ImageFormat } from 'app/entities/enumerations/image-format.model';
import { ImageQuality } from 'app/entities/enumerations/image-quality.model';
import { ImageGeneratedImageService } from '../service/image-generated-image.service';
import { IImageGeneratedImage } from '../image-generated-image.model';
import { ImageGeneratedImageFormGroup, ImageGeneratedImageFormService } from './image-generated-image-form.service';

@Component({
  selector: 'jhi-image-generated-image-update',
  templateUrl: './image-generated-image-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ImageGeneratedImageUpdateComponent implements OnInit {
  isSaving = false;
  imageGeneratedImage: IImageGeneratedImage | null = null;
  imageFormatValues = Object.keys(ImageFormat);
  imageQualityValues = Object.keys(ImageQuality);

  imagePdfConversionRequestsSharedCollection: IImagePdfConversionRequest[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected imageGeneratedImageService = inject(ImageGeneratedImageService);
  protected imageGeneratedImageFormService = inject(ImageGeneratedImageFormService);
  protected imagePdfConversionRequestService = inject(ImagePdfConversionRequestService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ImageGeneratedImageFormGroup = this.imageGeneratedImageFormService.createImageGeneratedImageFormGroup();

  compareImagePdfConversionRequest = (o1: IImagePdfConversionRequest | null, o2: IImagePdfConversionRequest | null): boolean =>
    this.imagePdfConversionRequestService.compareImagePdfConversionRequest(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imageGeneratedImage }) => {
      this.imageGeneratedImage = imageGeneratedImage;
      if (imageGeneratedImage) {
        this.updateForm(imageGeneratedImage);
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
    const imageGeneratedImage = this.imageGeneratedImageFormService.getImageGeneratedImage(this.editForm);
    if (imageGeneratedImage.id !== null) {
      this.subscribeToSaveResponse(this.imageGeneratedImageService.update(imageGeneratedImage));
    } else {
      this.subscribeToSaveResponse(this.imageGeneratedImageService.create(imageGeneratedImage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImageGeneratedImage>>): void {
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

  protected updateForm(imageGeneratedImage: IImageGeneratedImage): void {
    this.imageGeneratedImage = imageGeneratedImage;
    this.imageGeneratedImageFormService.resetForm(this.editForm, imageGeneratedImage);

    this.imagePdfConversionRequestsSharedCollection =
      this.imagePdfConversionRequestService.addImagePdfConversionRequestToCollectionIfMissing<IImagePdfConversionRequest>(
        this.imagePdfConversionRequestsSharedCollection,
        imageGeneratedImage.conversionRequest,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.imagePdfConversionRequestService
      .query()
      .pipe(map((res: HttpResponse<IImagePdfConversionRequest[]>) => res.body ?? []))
      .pipe(
        map((imagePdfConversionRequests: IImagePdfConversionRequest[]) =>
          this.imagePdfConversionRequestService.addImagePdfConversionRequestToCollectionIfMissing<IImagePdfConversionRequest>(
            imagePdfConversionRequests,
            this.imageGeneratedImage?.conversionRequest,
          ),
        ),
      )
      .subscribe(
        (imagePdfConversionRequests: IImagePdfConversionRequest[]) =>
          (this.imagePdfConversionRequestsSharedCollection = imagePdfConversionRequests),
      );
  }
}
