import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../image-conversion-batch.test-samples';

import { ImageConversionBatchFormService } from './image-conversion-batch-form.service';

describe('ImageConversionBatch Form Service', () => {
  let service: ImageConversionBatchFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImageConversionBatchFormService);
  });

  describe('Service methods', () => {
    describe('createImageConversionBatchFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImageConversionBatchFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            batchName: expect.any(Object),
            description: expect.any(Object),
            createdAt: expect.any(Object),
            status: expect.any(Object),
            totalConversions: expect.any(Object),
            completedConversions: expect.any(Object),
            failedConversions: expect.any(Object),
            startedAt: expect.any(Object),
            completedAt: expect.any(Object),
            totalProcessingDuration: expect.any(Object),
            createdByUserId: expect.any(Object),
          }),
        );
      });

      it('passing IImageConversionBatch should create a new form with FormGroup', () => {
        const formGroup = service.createImageConversionBatchFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            batchName: expect.any(Object),
            description: expect.any(Object),
            createdAt: expect.any(Object),
            status: expect.any(Object),
            totalConversions: expect.any(Object),
            completedConversions: expect.any(Object),
            failedConversions: expect.any(Object),
            startedAt: expect.any(Object),
            completedAt: expect.any(Object),
            totalProcessingDuration: expect.any(Object),
            createdByUserId: expect.any(Object),
          }),
        );
      });
    });

    describe('getImageConversionBatch', () => {
      it('should return NewImageConversionBatch for default ImageConversionBatch initial value', () => {
        const formGroup = service.createImageConversionBatchFormGroup(sampleWithNewData);

        const imageConversionBatch = service.getImageConversionBatch(formGroup) as any;

        expect(imageConversionBatch).toMatchObject(sampleWithNewData);
      });

      it('should return NewImageConversionBatch for empty ImageConversionBatch initial value', () => {
        const formGroup = service.createImageConversionBatchFormGroup();

        const imageConversionBatch = service.getImageConversionBatch(formGroup) as any;

        expect(imageConversionBatch).toMatchObject({});
      });

      it('should return IImageConversionBatch', () => {
        const formGroup = service.createImageConversionBatchFormGroup(sampleWithRequiredData);

        const imageConversionBatch = service.getImageConversionBatch(formGroup) as any;

        expect(imageConversionBatch).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImageConversionBatch should not enable id FormControl', () => {
        const formGroup = service.createImageConversionBatchFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImageConversionBatch should disable id FormControl', () => {
        const formGroup = service.createImageConversionBatchFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
