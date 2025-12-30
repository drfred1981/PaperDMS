import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ai-type-prediction.test-samples';

import { AITypePredictionFormService } from './ai-type-prediction-form.service';

describe('AITypePrediction Form Service', () => {
  let service: AITypePredictionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AITypePredictionFormService);
  });

  describe('Service methods', () => {
    describe('createAITypePredictionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAITypePredictionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentTypeName: expect.any(Object),
            confidence: expect.any(Object),
            reason: expect.any(Object),
            modelVersion: expect.any(Object),
            predictionS3Key: expect.any(Object),
            isAccepted: expect.any(Object),
            acceptedBy: expect.any(Object),
            acceptedDate: expect.any(Object),
            predictionDate: expect.any(Object),
          }),
        );
      });

      it('passing IAITypePrediction should create a new form with FormGroup', () => {
        const formGroup = service.createAITypePredictionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentTypeName: expect.any(Object),
            confidence: expect.any(Object),
            reason: expect.any(Object),
            modelVersion: expect.any(Object),
            predictionS3Key: expect.any(Object),
            isAccepted: expect.any(Object),
            acceptedBy: expect.any(Object),
            acceptedDate: expect.any(Object),
            predictionDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getAITypePrediction', () => {
      it('should return NewAITypePrediction for default AITypePrediction initial value', () => {
        const formGroup = service.createAITypePredictionFormGroup(sampleWithNewData);

        const aITypePrediction = service.getAITypePrediction(formGroup) as any;

        expect(aITypePrediction).toMatchObject(sampleWithNewData);
      });

      it('should return NewAITypePrediction for empty AITypePrediction initial value', () => {
        const formGroup = service.createAITypePredictionFormGroup();

        const aITypePrediction = service.getAITypePrediction(formGroup) as any;

        expect(aITypePrediction).toMatchObject({});
      });

      it('should return IAITypePrediction', () => {
        const formGroup = service.createAITypePredictionFormGroup(sampleWithRequiredData);

        const aITypePrediction = service.getAITypePrediction(formGroup) as any;

        expect(aITypePrediction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAITypePrediction should not enable id FormControl', () => {
        const formGroup = service.createAITypePredictionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAITypePrediction should disable id FormControl', () => {
        const formGroup = service.createAITypePredictionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
