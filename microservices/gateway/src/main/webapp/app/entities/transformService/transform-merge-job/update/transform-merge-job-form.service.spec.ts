import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../transform-merge-job.test-samples';

import { TransformMergeJobFormService } from './transform-merge-job-form.service';

describe('TransformMergeJob Form Service', () => {
  let service: TransformMergeJobFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransformMergeJobFormService);
  });

  describe('Service methods', () => {
    describe('createTransformMergeJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransformMergeJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            sourceDocumentSha256: expect.any(Object),
            mergeOrder: expect.any(Object),
            includeBookmarks: expect.any(Object),
            includeToc: expect.any(Object),
            addPageNumbers: expect.any(Object),
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

      it('passing ITransformMergeJob should create a new form with FormGroup', () => {
        const formGroup = service.createTransformMergeJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            sourceDocumentSha256: expect.any(Object),
            mergeOrder: expect.any(Object),
            includeBookmarks: expect.any(Object),
            includeToc: expect.any(Object),
            addPageNumbers: expect.any(Object),
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

    describe('getTransformMergeJob', () => {
      it('should return NewTransformMergeJob for default TransformMergeJob initial value', () => {
        const formGroup = service.createTransformMergeJobFormGroup(sampleWithNewData);

        const transformMergeJob = service.getTransformMergeJob(formGroup) as any;

        expect(transformMergeJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransformMergeJob for empty TransformMergeJob initial value', () => {
        const formGroup = service.createTransformMergeJobFormGroup();

        const transformMergeJob = service.getTransformMergeJob(formGroup) as any;

        expect(transformMergeJob).toMatchObject({});
      });

      it('should return ITransformMergeJob', () => {
        const formGroup = service.createTransformMergeJobFormGroup(sampleWithRequiredData);

        const transformMergeJob = service.getTransformMergeJob(formGroup) as any;

        expect(transformMergeJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransformMergeJob should not enable id FormControl', () => {
        const formGroup = service.createTransformMergeJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransformMergeJob should disable id FormControl', () => {
        const formGroup = service.createTransformMergeJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
