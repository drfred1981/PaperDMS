import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ImageQuality } from 'app/entities/enumerations/image-quality.model';
import { ImageFormat } from 'app/entities/enumerations/image-format.model';
import { ConversionType } from 'app/entities/enumerations/conversion-type.model';
import { ImageConversionConfigService } from '../service/image-conversion-config.service';
import { IImageConversionConfig } from '../image-conversion-config.model';
import { ImageConversionConfigFormGroup, ImageConversionConfigFormService } from './image-conversion-config-form.service';

@Component({
  selector: 'jhi-image-conversion-config-update',
  templateUrl: './image-conversion-config-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ImageConversionConfigUpdateComponent implements OnInit {
  isSaving = false;
  imageConversionConfig: IImageConversionConfig | null = null;
  imageQualityValues = Object.keys(ImageQuality);
  imageFormatValues = Object.keys(ImageFormat);
  conversionTypeValues = Object.keys(ConversionType);

  protected imageConversionConfigService = inject(ImageConversionConfigService);
  protected imageConversionConfigFormService = inject(ImageConversionConfigFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ImageConversionConfigFormGroup = this.imageConversionConfigFormService.createImageConversionConfigFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imageConversionConfig }) => {
      this.imageConversionConfig = imageConversionConfig;
      if (imageConversionConfig) {
        this.updateForm(imageConversionConfig);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const imageConversionConfig = this.imageConversionConfigFormService.getImageConversionConfig(this.editForm);
    if (imageConversionConfig.id !== null) {
      this.subscribeToSaveResponse(this.imageConversionConfigService.update(imageConversionConfig));
    } else {
      this.subscribeToSaveResponse(this.imageConversionConfigService.create(imageConversionConfig));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImageConversionConfig>>): void {
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

  protected updateForm(imageConversionConfig: IImageConversionConfig): void {
    this.imageConversionConfig = imageConversionConfig;
    this.imageConversionConfigFormService.resetForm(this.editForm, imageConversionConfig);
  }
}
