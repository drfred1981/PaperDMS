import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../transform-redaction-job.test-samples';

import { TransformRedactionJobFormService } from './transform-redaction-job-form.service';

describe('TransformRedactionJob Form Service', () => {
  let service: TransformRedactionJobFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransformRedactionJobFormService);
  });

  describe('Service methods', () => {
    describe('createTransformRedactionJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransformRedactionJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            redactionAreas: expect.any(Object),
            redactionType: expect.any(Object),
            redactionColor: expect.any(Object),
            replaceWith: expect.any(Object),
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

      it('passing ITransformRedactionJob should create a new form with FormGroup', () => {
        const formGroup = service.createTransformRedactionJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            redactionAreas: expect.any(Object),
            redactionType: expect.any(Object),
            redactionColor: expect.any(Object),
            replaceWith: expect.any(Object),
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

    describe('getTransformRedactionJob', () => {
      it('should return NewTransformRedactionJob for default TransformRedactionJob initial value', () => {
        const formGroup = service.createTransformRedactionJobFormGroup(sampleWithNewData);

        const transformRedactionJob = service.getTransformRedactionJob(formGroup) as any;

        expect(transformRedactionJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransformRedactionJob for empty TransformRedactionJob initial value', () => {
        const formGroup = service.createTransformRedactionJobFormGroup();

        const transformRedactionJob = service.getTransformRedactionJob(formGroup) as any;

        expect(transformRedactionJob).toMatchObject({});
      });

      it('should return ITransformRedactionJob', () => {
        const formGroup = service.createTransformRedactionJobFormGroup(sampleWithRequiredData);

        const transformRedactionJob = service.getTransformRedactionJob(formGroup) as any;

        expect(transformRedactionJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransformRedactionJob should not enable id FormControl', () => {
        const formGroup = service.createTransformRedactionJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransformRedactionJob should disable id FormControl', () => {
        const formGroup = service.createTransformRedactionJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
