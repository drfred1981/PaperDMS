import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../transform-watermark-job.test-samples';

import { TransformWatermarkJobFormService } from './transform-watermark-job-form.service';

describe('TransformWatermarkJob Form Service', () => {
  let service: TransformWatermarkJobFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransformWatermarkJobFormService);
  });

  describe('Service methods', () => {
    describe('createTransformWatermarkJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransformWatermarkJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
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
            outputDocumentSha256: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing ITransformWatermarkJob should create a new form with FormGroup', () => {
        const formGroup = service.createTransformWatermarkJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
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
            outputDocumentSha256: expect.any(Object),
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

    describe('getTransformWatermarkJob', () => {
      it('should return NewTransformWatermarkJob for default TransformWatermarkJob initial value', () => {
        const formGroup = service.createTransformWatermarkJobFormGroup(sampleWithNewData);

        const transformWatermarkJob = service.getTransformWatermarkJob(formGroup) as any;

        expect(transformWatermarkJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransformWatermarkJob for empty TransformWatermarkJob initial value', () => {
        const formGroup = service.createTransformWatermarkJobFormGroup();

        const transformWatermarkJob = service.getTransformWatermarkJob(formGroup) as any;

        expect(transformWatermarkJob).toMatchObject({});
      });

      it('should return ITransformWatermarkJob', () => {
        const formGroup = service.createTransformWatermarkJobFormGroup(sampleWithRequiredData);

        const transformWatermarkJob = service.getTransformWatermarkJob(formGroup) as any;

        expect(transformWatermarkJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransformWatermarkJob should not enable id FormControl', () => {
        const formGroup = service.createTransformWatermarkJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransformWatermarkJob should disable id FormControl', () => {
        const formGroup = service.createTransformWatermarkJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
