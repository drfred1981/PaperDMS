import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../comparison-job.test-samples';

import { ComparisonJobFormService } from './comparison-job-form.service';

describe('ComparisonJob Form Service', () => {
  let service: ComparisonJobFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ComparisonJobFormService);
  });

  describe('Service methods', () => {
    describe('createComparisonJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createComparisonJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId1: expect.any(Object),
            documentId2: expect.any(Object),
            comparisonType: expect.any(Object),
            differences: expect.any(Object),
            differenceCount: expect.any(Object),
            similarityPercentage: expect.any(Object),
            diffReportS3Key: expect.any(Object),
            diffVisualS3Key: expect.any(Object),
            status: expect.any(Object),
            comparedDate: expect.any(Object),
            comparedBy: expect.any(Object),
          }),
        );
      });

      it('passing IComparisonJob should create a new form with FormGroup', () => {
        const formGroup = service.createComparisonJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId1: expect.any(Object),
            documentId2: expect.any(Object),
            comparisonType: expect.any(Object),
            differences: expect.any(Object),
            differenceCount: expect.any(Object),
            similarityPercentage: expect.any(Object),
            diffReportS3Key: expect.any(Object),
            diffVisualS3Key: expect.any(Object),
            status: expect.any(Object),
            comparedDate: expect.any(Object),
            comparedBy: expect.any(Object),
          }),
        );
      });
    });

    describe('getComparisonJob', () => {
      it('should return NewComparisonJob for default ComparisonJob initial value', () => {
        const formGroup = service.createComparisonJobFormGroup(sampleWithNewData);

        const comparisonJob = service.getComparisonJob(formGroup) as any;

        expect(comparisonJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewComparisonJob for empty ComparisonJob initial value', () => {
        const formGroup = service.createComparisonJobFormGroup();

        const comparisonJob = service.getComparisonJob(formGroup) as any;

        expect(comparisonJob).toMatchObject({});
      });

      it('should return IComparisonJob', () => {
        const formGroup = service.createComparisonJobFormGroup(sampleWithRequiredData);

        const comparisonJob = service.getComparisonJob(formGroup) as any;

        expect(comparisonJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IComparisonJob should not enable id FormControl', () => {
        const formGroup = service.createComparisonJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewComparisonJob should disable id FormControl', () => {
        const formGroup = service.createComparisonJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
