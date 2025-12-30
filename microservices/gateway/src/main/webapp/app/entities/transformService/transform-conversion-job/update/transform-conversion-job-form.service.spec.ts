import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../transform-conversion-job.test-samples';

import { TransformConversionJobFormService } from './transform-conversion-job-form.service';

describe('TransformConversionJob Form Service', () => {
  let service: TransformConversionJobFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransformConversionJobFormService);
  });

  describe('Service methods', () => {
    describe('createTransformConversionJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransformConversionJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            sourceFormat: expect.any(Object),
            targetFormat: expect.any(Object),
            conversionEngine: expect.any(Object),
            options: expect.any(Object),
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

      it('passing ITransformConversionJob should create a new form with FormGroup', () => {
        const formGroup = service.createTransformConversionJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            sourceFormat: expect.any(Object),
            targetFormat: expect.any(Object),
            conversionEngine: expect.any(Object),
            options: expect.any(Object),
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

    describe('getTransformConversionJob', () => {
      it('should return NewTransformConversionJob for default TransformConversionJob initial value', () => {
        const formGroup = service.createTransformConversionJobFormGroup(sampleWithNewData);

        const transformConversionJob = service.getTransformConversionJob(formGroup) as any;

        expect(transformConversionJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransformConversionJob for empty TransformConversionJob initial value', () => {
        const formGroup = service.createTransformConversionJobFormGroup();

        const transformConversionJob = service.getTransformConversionJob(formGroup) as any;

        expect(transformConversionJob).toMatchObject({});
      });

      it('should return ITransformConversionJob', () => {
        const formGroup = service.createTransformConversionJobFormGroup(sampleWithRequiredData);

        const transformConversionJob = service.getTransformConversionJob(formGroup) as any;

        expect(transformConversionJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransformConversionJob should not enable id FormControl', () => {
        const formGroup = service.createTransformConversionJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransformConversionJob should disable id FormControl', () => {
        const formGroup = service.createTransformConversionJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
