import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../image-pdf-conversion-request.test-samples';

import { ImagePdfConversionRequestFormService } from './image-pdf-conversion-request-form.service';

describe('ImagePdfConversionRequest Form Service', () => {
  let service: ImagePdfConversionRequestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImagePdfConversionRequestFormService);
  });

  describe('Service methods', () => {
    describe('createImagePdfConversionRequestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImagePdfConversionRequestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sourceDocumentId: expect.any(Object),
            sourceFileName: expect.any(Object),
            sourcePdfS3Key: expect.any(Object),
            imageQuality: expect.any(Object),
            imageFormat: expect.any(Object),
            conversionType: expect.any(Object),
            startPage: expect.any(Object),
            endPage: expect.any(Object),
            totalPages: expect.any(Object),
            status: expect.any(Object),
            errorMessage: expect.any(Object),
            requestedAt: expect.any(Object),
            startedAt: expect.any(Object),
            completedAt: expect.any(Object),
            processingDuration: expect.any(Object),
            totalImagesSize: expect.any(Object),
            imagesGenerated: expect.any(Object),
            dpi: expect.any(Object),
            requestedByUserId: expect.any(Object),
            priority: expect.any(Object),
            additionalOptions: expect.any(Object),
            batch: expect.any(Object),
          }),
        );
      });

      it('passing IImagePdfConversionRequest should create a new form with FormGroup', () => {
        const formGroup = service.createImagePdfConversionRequestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sourceDocumentId: expect.any(Object),
            sourceFileName: expect.any(Object),
            sourcePdfS3Key: expect.any(Object),
            imageQuality: expect.any(Object),
            imageFormat: expect.any(Object),
            conversionType: expect.any(Object),
            startPage: expect.any(Object),
            endPage: expect.any(Object),
            totalPages: expect.any(Object),
            status: expect.any(Object),
            errorMessage: expect.any(Object),
            requestedAt: expect.any(Object),
            startedAt: expect.any(Object),
            completedAt: expect.any(Object),
            processingDuration: expect.any(Object),
            totalImagesSize: expect.any(Object),
            imagesGenerated: expect.any(Object),
            dpi: expect.any(Object),
            requestedByUserId: expect.any(Object),
            priority: expect.any(Object),
            additionalOptions: expect.any(Object),
            batch: expect.any(Object),
          }),
        );
      });
    });

    describe('getImagePdfConversionRequest', () => {
      it('should return NewImagePdfConversionRequest for default ImagePdfConversionRequest initial value', () => {
        const formGroup = service.createImagePdfConversionRequestFormGroup(sampleWithNewData);

        const imagePdfConversionRequest = service.getImagePdfConversionRequest(formGroup) as any;

        expect(imagePdfConversionRequest).toMatchObject(sampleWithNewData);
      });

      it('should return NewImagePdfConversionRequest for empty ImagePdfConversionRequest initial value', () => {
        const formGroup = service.createImagePdfConversionRequestFormGroup();

        const imagePdfConversionRequest = service.getImagePdfConversionRequest(formGroup) as any;

        expect(imagePdfConversionRequest).toMatchObject({});
      });

      it('should return IImagePdfConversionRequest', () => {
        const formGroup = service.createImagePdfConversionRequestFormGroup(sampleWithRequiredData);

        const imagePdfConversionRequest = service.getImagePdfConversionRequest(formGroup) as any;

        expect(imagePdfConversionRequest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImagePdfConversionRequest should not enable id FormControl', () => {
        const formGroup = service.createImagePdfConversionRequestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImagePdfConversionRequest should disable id FormControl', () => {
        const formGroup = service.createImagePdfConversionRequestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
