import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ai-correspondent-prediction.test-samples';

import { AICorrespondentPredictionFormService } from './ai-correspondent-prediction-form.service';

describe('AICorrespondentPrediction Form Service', () => {
  let service: AICorrespondentPredictionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AICorrespondentPredictionFormService);
  });

  describe('Service methods', () => {
    describe('createAICorrespondentPredictionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAICorrespondentPredictionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            correspondentName: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
            address: expect.any(Object),
            company: expect.any(Object),
            type: expect.any(Object),
            role: expect.any(Object),
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

      it('passing IAICorrespondentPrediction should create a new form with FormGroup', () => {
        const formGroup = service.createAICorrespondentPredictionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            correspondentName: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
            address: expect.any(Object),
            company: expect.any(Object),
            type: expect.any(Object),
            role: expect.any(Object),
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

    describe('getAICorrespondentPrediction', () => {
      it('should return NewAICorrespondentPrediction for default AICorrespondentPrediction initial value', () => {
        const formGroup = service.createAICorrespondentPredictionFormGroup(sampleWithNewData);

        const aICorrespondentPrediction = service.getAICorrespondentPrediction(formGroup) as any;

        expect(aICorrespondentPrediction).toMatchObject(sampleWithNewData);
      });

      it('should return NewAICorrespondentPrediction for empty AICorrespondentPrediction initial value', () => {
        const formGroup = service.createAICorrespondentPredictionFormGroup();

        const aICorrespondentPrediction = service.getAICorrespondentPrediction(formGroup) as any;

        expect(aICorrespondentPrediction).toMatchObject({});
      });

      it('should return IAICorrespondentPrediction', () => {
        const formGroup = service.createAICorrespondentPredictionFormGroup(sampleWithRequiredData);

        const aICorrespondentPrediction = service.getAICorrespondentPrediction(formGroup) as any;

        expect(aICorrespondentPrediction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAICorrespondentPrediction should not enable id FormControl', () => {
        const formGroup = service.createAICorrespondentPredictionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAICorrespondentPrediction should disable id FormControl', () => {
        const formGroup = service.createAICorrespondentPredictionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
