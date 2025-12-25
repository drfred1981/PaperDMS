import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../similarity-cluster.test-samples';

import { SimilarityClusterFormService } from './similarity-cluster-form.service';

describe('SimilarityCluster Form Service', () => {
  let service: SimilarityClusterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SimilarityClusterFormService);
  });

  describe('Service methods', () => {
    describe('createSimilarityClusterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSimilarityClusterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            algorithm: expect.any(Object),
            centroid: expect.any(Object),
            documentCount: expect.any(Object),
            avgSimilarity: expect.any(Object),
            createdDate: expect.any(Object),
            lastUpdated: expect.any(Object),
          }),
        );
      });

      it('passing ISimilarityCluster should create a new form with FormGroup', () => {
        const formGroup = service.createSimilarityClusterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            algorithm: expect.any(Object),
            centroid: expect.any(Object),
            documentCount: expect.any(Object),
            avgSimilarity: expect.any(Object),
            createdDate: expect.any(Object),
            lastUpdated: expect.any(Object),
          }),
        );
      });
    });

    describe('getSimilarityCluster', () => {
      it('should return NewSimilarityCluster for default SimilarityCluster initial value', () => {
        const formGroup = service.createSimilarityClusterFormGroup(sampleWithNewData);

        const similarityCluster = service.getSimilarityCluster(formGroup) as any;

        expect(similarityCluster).toMatchObject(sampleWithNewData);
      });

      it('should return NewSimilarityCluster for empty SimilarityCluster initial value', () => {
        const formGroup = service.createSimilarityClusterFormGroup();

        const similarityCluster = service.getSimilarityCluster(formGroup) as any;

        expect(similarityCluster).toMatchObject({});
      });

      it('should return ISimilarityCluster', () => {
        const formGroup = service.createSimilarityClusterFormGroup(sampleWithRequiredData);

        const similarityCluster = service.getSimilarityCluster(formGroup) as any;

        expect(similarityCluster).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISimilarityCluster should not enable id FormControl', () => {
        const formGroup = service.createSimilarityClusterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSimilarityCluster should disable id FormControl', () => {
        const formGroup = service.createSimilarityClusterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
