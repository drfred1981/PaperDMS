import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../image-generated-image.test-samples';

import { ImageGeneratedImageFormService } from './image-generated-image-form.service';

describe('ImageGeneratedImage Form Service', () => {
  let service: ImageGeneratedImageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImageGeneratedImageFormService);
  });

  describe('Service methods', () => {
    describe('createImageGeneratedImageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImageGeneratedImageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pageNumber: expect.any(Object),
            fileName: expect.any(Object),
            s3Key: expect.any(Object),
            preSignedUrl: expect.any(Object),
            urlExpiresAt: expect.any(Object),
            format: expect.any(Object),
            quality: expect.any(Object),
            width: expect.any(Object),
            height: expect.any(Object),
            fileSize: expect.any(Object),
            dpi: expect.any(Object),
            sha256Hash: expect.any(Object),
            generatedAt: expect.any(Object),
            metadata: expect.any(Object),
            conversionRequest: expect.any(Object),
          }),
        );
      });

      it('passing IImageGeneratedImage should create a new form with FormGroup', () => {
        const formGroup = service.createImageGeneratedImageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pageNumber: expect.any(Object),
            fileName: expect.any(Object),
            s3Key: expect.any(Object),
            preSignedUrl: expect.any(Object),
            urlExpiresAt: expect.any(Object),
            format: expect.any(Object),
            quality: expect.any(Object),
            width: expect.any(Object),
            height: expect.any(Object),
            fileSize: expect.any(Object),
            dpi: expect.any(Object),
            sha256Hash: expect.any(Object),
            generatedAt: expect.any(Object),
            metadata: expect.any(Object),
            conversionRequest: expect.any(Object),
          }),
        );
      });
    });

    describe('getImageGeneratedImage', () => {
      it('should return NewImageGeneratedImage for default ImageGeneratedImage initial value', () => {
        const formGroup = service.createImageGeneratedImageFormGroup(sampleWithNewData);

        const imageGeneratedImage = service.getImageGeneratedImage(formGroup) as any;

        expect(imageGeneratedImage).toMatchObject(sampleWithNewData);
      });

      it('should return NewImageGeneratedImage for empty ImageGeneratedImage initial value', () => {
        const formGroup = service.createImageGeneratedImageFormGroup();

        const imageGeneratedImage = service.getImageGeneratedImage(formGroup) as any;

        expect(imageGeneratedImage).toMatchObject({});
      });

      it('should return IImageGeneratedImage', () => {
        const formGroup = service.createImageGeneratedImageFormGroup(sampleWithRequiredData);

        const imageGeneratedImage = service.getImageGeneratedImage(formGroup) as any;

        expect(imageGeneratedImage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImageGeneratedImage should not enable id FormControl', () => {
        const formGroup = service.createImageGeneratedImageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImageGeneratedImage should disable id FormControl', () => {
        const formGroup = service.createImageGeneratedImageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
