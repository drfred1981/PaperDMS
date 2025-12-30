import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../image-conversion-statistics.test-samples';

import { ImageConversionStatisticsFormService } from './image-conversion-statistics-form.service';

describe('ImageConversionStatistics Form Service', () => {
  let service: ImageConversionStatisticsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImageConversionStatisticsFormService);
  });

  describe('Service methods', () => {
    describe('createImageConversionStatisticsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImageConversionStatisticsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statisticsDate: expect.any(Object),
            totalConversions: expect.any(Object),
            successfulConversions: expect.any(Object),
            failedConversions: expect.any(Object),
            totalPagesConverted: expect.any(Object),
            totalImagesGenerated: expect.any(Object),
            totalImagesSize: expect.any(Object),
            averageProcessingDuration: expect.any(Object),
            maxProcessingDuration: expect.any(Object),
            minProcessingDuration: expect.any(Object),
            calculatedAt: expect.any(Object),
          }),
        );
      });

      it('passing IImageConversionStatistics should create a new form with FormGroup', () => {
        const formGroup = service.createImageConversionStatisticsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statisticsDate: expect.any(Object),
            totalConversions: expect.any(Object),
            successfulConversions: expect.any(Object),
            failedConversions: expect.any(Object),
            totalPagesConverted: expect.any(Object),
            totalImagesGenerated: expect.any(Object),
            totalImagesSize: expect.any(Object),
            averageProcessingDuration: expect.any(Object),
            maxProcessingDuration: expect.any(Object),
            minProcessingDuration: expect.any(Object),
            calculatedAt: expect.any(Object),
          }),
        );
      });
    });

    describe('getImageConversionStatistics', () => {
      it('should return NewImageConversionStatistics for default ImageConversionStatistics initial value', () => {
        const formGroup = service.createImageConversionStatisticsFormGroup(sampleWithNewData);

        const imageConversionStatistics = service.getImageConversionStatistics(formGroup) as any;

        expect(imageConversionStatistics).toMatchObject(sampleWithNewData);
      });

      it('should return NewImageConversionStatistics for empty ImageConversionStatistics initial value', () => {
        const formGroup = service.createImageConversionStatisticsFormGroup();

        const imageConversionStatistics = service.getImageConversionStatistics(formGroup) as any;

        expect(imageConversionStatistics).toMatchObject({});
      });

      it('should return IImageConversionStatistics', () => {
        const formGroup = service.createImageConversionStatisticsFormGroup(sampleWithRequiredData);

        const imageConversionStatistics = service.getImageConversionStatistics(formGroup) as any;

        expect(imageConversionStatistics).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImageConversionStatistics should not enable id FormControl', () => {
        const formGroup = service.createImageConversionStatisticsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImageConversionStatistics should disable id FormControl', () => {
        const formGroup = service.createImageConversionStatisticsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
