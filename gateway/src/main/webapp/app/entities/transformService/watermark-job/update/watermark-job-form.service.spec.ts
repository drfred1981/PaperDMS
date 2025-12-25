import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../watermark-job.test-samples';

import { WatermarkJobFormService } from './watermark-job-form.service';

describe('WatermarkJob Form Service', () => {
  let service: WatermarkJobFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WatermarkJobFormService);
  });

  describe('Service methods', () => {
    describe('createWatermarkJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWatermarkJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            watermarkType: expect.any(Object),
            watermarkText: expect.any(Object),
            watermarkImageS3Key: expect.any(Object),
            position: expect.any(Object),
            opacity: expect.any(Object),
            fontSize: expect.any(Object),
            color: expect.any(Object),
            rotation: expect.any(Object),
            tiled: expect.any(Object),
            outputS3Key: expect.any(Object),
            outputDocumentId: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IWatermarkJob should create a new form with FormGroup', () => {
        const formGroup = service.createWatermarkJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            watermarkType: expect.any(Object),
            watermarkText: expect.any(Object),
            watermarkImageS3Key: expect.any(Object),
            position: expect.any(Object),
            opacity: expect.any(Object),
            fontSize: expect.any(Object),
            color: expect.any(Object),
            rotation: expect.any(Object),
            tiled: expect.any(Object),
            outputS3Key: expect.any(Object),
            outputDocumentId: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getWatermarkJob', () => {
      it('should return NewWatermarkJob for default WatermarkJob initial value', () => {
        const formGroup = service.createWatermarkJobFormGroup(sampleWithNewData);

        const watermarkJob = service.getWatermarkJob(formGroup) as any;

        expect(watermarkJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewWatermarkJob for empty WatermarkJob initial value', () => {
        const formGroup = service.createWatermarkJobFormGroup();

        const watermarkJob = service.getWatermarkJob(formGroup) as any;

        expect(watermarkJob).toMatchObject({});
      });

      it('should return IWatermarkJob', () => {
        const formGroup = service.createWatermarkJobFormGroup(sampleWithRequiredData);

        const watermarkJob = service.getWatermarkJob(formGroup) as any;

        expect(watermarkJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWatermarkJob should not enable id FormControl', () => {
        const formGroup = service.createWatermarkJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWatermarkJob should disable id FormControl', () => {
        const formGroup = service.createWatermarkJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
