import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ai-tag-prediction.test-samples';

import { AITagPredictionFormService } from './ai-tag-prediction-form.service';

describe('AITagPrediction Form Service', () => {
  let service: AITagPredictionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AITagPredictionFormService);
  });

  describe('Service methods', () => {
    describe('createAITagPredictionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAITagPredictionFormGroup();

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

      it('passing IAITagPrediction should create a new form with FormGroup', () => {
        const formGroup = service.createAITagPredictionFormGroup(sampleWithRequiredData);

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

    describe('getAITagPrediction', () => {
      it('should return NewAITagPrediction for default AITagPrediction initial value', () => {
        const formGroup = service.createAITagPredictionFormGroup(sampleWithNewData);

        const aITagPrediction = service.getAITagPrediction(formGroup) as any;

        expect(aITagPrediction).toMatchObject(sampleWithNewData);
      });

      it('should return NewAITagPrediction for empty AITagPrediction initial value', () => {
        const formGroup = service.createAITagPredictionFormGroup();

        const aITagPrediction = service.getAITagPrediction(formGroup) as any;

        expect(aITagPrediction).toMatchObject({});
      });

      it('should return IAITagPrediction', () => {
        const formGroup = service.createAITagPredictionFormGroup(sampleWithRequiredData);

        const aITagPrediction = service.getAITagPrediction(formGroup) as any;

        expect(aITagPrediction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAITagPrediction should not enable id FormControl', () => {
        const formGroup = service.createAITagPredictionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAITagPrediction should disable id FormControl', () => {
        const formGroup = service.createAITagPredictionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
