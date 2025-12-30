import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../image-conversion-history.test-samples';

import { ImageConversionHistoryFormService } from './image-conversion-history-form.service';

describe('ImageConversionHistory Form Service', () => {
  let service: ImageConversionHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImageConversionHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createImageConversionHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImageConversionHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            originalRequestId: expect.any(Object),
            archivedAt: expect.any(Object),
            conversionData: expect.any(Object),
            imagesCount: expect.any(Object),
            totalSize: expect.any(Object),
            finalStatus: expect.any(Object),
            processingDuration: expect.any(Object),
          }),
        );
      });

      it('passing IImageConversionHistory should create a new form with FormGroup', () => {
        const formGroup = service.createImageConversionHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            originalRequestId: expect.any(Object),
            archivedAt: expect.any(Object),
            conversionData: expect.any(Object),
            imagesCount: expect.any(Object),
            totalSize: expect.any(Object),
            finalStatus: expect.any(Object),
            processingDuration: expect.any(Object),
          }),
        );
      });
    });

    describe('getImageConversionHistory', () => {
      it('should return NewImageConversionHistory for default ImageConversionHistory initial value', () => {
        const formGroup = service.createImageConversionHistoryFormGroup(sampleWithNewData);

        const imageConversionHistory = service.getImageConversionHistory(formGroup) as any;

        expect(imageConversionHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewImageConversionHistory for empty ImageConversionHistory initial value', () => {
        const formGroup = service.createImageConversionHistoryFormGroup();

        const imageConversionHistory = service.getImageConversionHistory(formGroup) as any;

        expect(imageConversionHistory).toMatchObject({});
      });

      it('should return IImageConversionHistory', () => {
        const formGroup = service.createImageConversionHistoryFormGroup(sampleWithRequiredData);

        const imageConversionHistory = service.getImageConversionHistory(formGroup) as any;

        expect(imageConversionHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImageConversionHistory should not enable id FormControl', () => {
        const formGroup = service.createImageConversionHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImageConversionHistory should disable id FormControl', () => {
        const formGroup = service.createImageConversionHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
