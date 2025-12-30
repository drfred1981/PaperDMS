import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IImageConversionStatistics } from '../image-conversion-statistics.model';
import { ImageConversionStatisticsService } from '../service/image-conversion-statistics.service';
import { ImageConversionStatisticsFormGroup, ImageConversionStatisticsFormService } from './image-conversion-statistics-form.service';

@Component({
  selector: 'jhi-image-conversion-statistics-update',
  templateUrl: './image-conversion-statistics-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ImageConversionStatisticsUpdateComponent implements OnInit {
  isSaving = false;
  imageConversionStatistics: IImageConversionStatistics | null = null;

  protected imageConversionStatisticsService = inject(ImageConversionStatisticsService);
  protected imageConversionStatisticsFormService = inject(ImageConversionStatisticsFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ImageConversionStatisticsFormGroup = this.imageConversionStatisticsFormService.createImageConversionStatisticsFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imageConversionStatistics }) => {
      this.imageConversionStatistics = imageConversionStatistics;
      if (imageConversionStatistics) {
        this.updateForm(imageConversionStatistics);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const imageConversionStatistics = this.imageConversionStatisticsFormService.getImageConversionStatistics(this.editForm);
    if (imageConversionStatistics.id !== null) {
      this.subscribeToSaveResponse(this.imageConversionStatisticsService.update(imageConversionStatistics));
    } else {
      this.subscribeToSaveResponse(this.imageConversionStatisticsService.create(imageConversionStatistics));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImageConversionStatistics>>): void {
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

  protected updateForm(imageConversionStatistics: IImageConversionStatistics): void {
    this.imageConversionStatistics = imageConversionStatistics;
    this.imageConversionStatisticsFormService.resetForm(this.editForm, imageConversionStatistics);
  }
}
