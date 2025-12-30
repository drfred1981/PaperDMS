import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ConversionStatus } from 'app/entities/enumerations/conversion-status.model';
import { IImageConversionBatch } from '../image-conversion-batch.model';
import { ImageConversionBatchService } from '../service/image-conversion-batch.service';
import { ImageConversionBatchFormGroup, ImageConversionBatchFormService } from './image-conversion-batch-form.service';

@Component({
  selector: 'jhi-image-conversion-batch-update',
  templateUrl: './image-conversion-batch-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ImageConversionBatchUpdateComponent implements OnInit {
  isSaving = false;
  imageConversionBatch: IImageConversionBatch | null = null;
  conversionStatusValues = Object.keys(ConversionStatus);

  protected imageConversionBatchService = inject(ImageConversionBatchService);
  protected imageConversionBatchFormService = inject(ImageConversionBatchFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ImageConversionBatchFormGroup = this.imageConversionBatchFormService.createImageConversionBatchFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imageConversionBatch }) => {
      this.imageConversionBatch = imageConversionBatch;
      if (imageConversionBatch) {
        this.updateForm(imageConversionBatch);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const imageConversionBatch = this.imageConversionBatchFormService.getImageConversionBatch(this.editForm);
    if (imageConversionBatch.id !== null) {
      this.subscribeToSaveResponse(this.imageConversionBatchService.update(imageConversionBatch));
    } else {
      this.subscribeToSaveResponse(this.imageConversionBatchService.create(imageConversionBatch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImageConversionBatch>>): void {
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

  protected updateForm(imageConversionBatch: IImageConversionBatch): void {
    this.imageConversionBatch = imageConversionBatch;
    this.imageConversionBatchFormService.resetForm(this.editForm, imageConversionBatch);
  }
}
