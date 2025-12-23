import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tag-prediction.test-samples';

import { TagPredictionFormService } from './tag-prediction-form.service';

describe('TagPrediction Form Service', () => {
  let service: TagPredictionFormService;

  beforeEach(() => {
    service = TestBed.inject(TagPredictionFormService);
  });

  describe('Service methods', () => {
    describe('createTagPredictionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTagPredictionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tagName: expect.any(Object),
            confidence: expect.any(Object),
            reason: expect.any(Object),
            modelVersion: expect.any(Object),
            predictionS3Key: expect.any(Object),
            isAccepted: expect.any(Object),
            acceptedBy: expect.any(Object),
            acceptedDate: expect.any(Object),
            predictionDate: expect.any(Object),
            job: expect.any(Object),
          }),
        );
      });

      it('passing ITagPrediction should create a new form with FormGroup', () => {
        const formGroup = service.createTagPredictionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tagName: expect.any(Object),
            confidence: expect.any(Object),
            reason: expect.any(Object),
            modelVersion: expect.any(Object),
            predictionS3Key: expect.any(Object),
            isAccepted: expect.any(Object),
            acceptedBy: expect.any(Object),
            acceptedDate: expect.any(Object),
            predictionDate: expect.any(Object),
            job: expect.any(Object),
          }),
        );
      });
    });

    describe('getTagPrediction', () => {
      it('should return NewTagPrediction for default TagPrediction initial value', () => {
        const formGroup = service.createTagPredictionFormGroup(sampleWithNewData);

        const tagPrediction = service.getTagPrediction(formGroup);

        expect(tagPrediction).toMatchObject(sampleWithNewData);
      });

      it('should return NewTagPrediction for empty TagPrediction initial value', () => {
        const formGroup = service.createTagPredictionFormGroup();

        const tagPrediction = service.getTagPrediction(formGroup);

        expect(tagPrediction).toMatchObject({});
      });

      it('should return ITagPrediction', () => {
        const formGroup = service.createTagPredictionFormGroup(sampleWithRequiredData);

        const tagPrediction = service.getTagPrediction(formGroup);

        expect(tagPrediction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITagPrediction should not enable id FormControl', () => {
        const formGroup = service.createTagPredictionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTagPrediction should disable id FormControl', () => {
        const formGroup = service.createTagPredictionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
