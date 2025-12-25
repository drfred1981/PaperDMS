import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../similarity-job.test-samples';

import { SimilarityJobFormService } from './similarity-job-form.service';

describe('SimilarityJob Form Service', () => {
  let service: SimilarityJobFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SimilarityJobFormService);
  });

  describe('Service methods', () => {
    describe('createSimilarityJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSimilarityJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            status: expect.any(Object),
            algorithm: expect.any(Object),
            scope: expect.any(Object),
            minSimilarityThreshold: expect.any(Object),
            matchesFound: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
          }),
        );
      });

      it('passing ISimilarityJob should create a new form with FormGroup', () => {
        const formGroup = service.createSimilarityJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            status: expect.any(Object),
            algorithm: expect.any(Object),
            scope: expect.any(Object),
            minSimilarityThreshold: expect.any(Object),
            matchesFound: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
          }),
        );
      });
    });

    describe('getSimilarityJob', () => {
      it('should return NewSimilarityJob for default SimilarityJob initial value', () => {
        const formGroup = service.createSimilarityJobFormGroup(sampleWithNewData);

        const similarityJob = service.getSimilarityJob(formGroup) as any;

        expect(similarityJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewSimilarityJob for empty SimilarityJob initial value', () => {
        const formGroup = service.createSimilarityJobFormGroup();

        const similarityJob = service.getSimilarityJob(formGroup) as any;

        expect(similarityJob).toMatchObject({});
      });

      it('should return ISimilarityJob', () => {
        const formGroup = service.createSimilarityJobFormGroup(sampleWithRequiredData);

        const similarityJob = service.getSimilarityJob(formGroup) as any;

        expect(similarityJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISimilarityJob should not enable id FormControl', () => {
        const formGroup = service.createSimilarityJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSimilarityJob should disable id FormControl', () => {
        const formGroup = service.createSimilarityJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
