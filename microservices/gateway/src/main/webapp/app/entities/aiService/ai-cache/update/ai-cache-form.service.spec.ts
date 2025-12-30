import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ai-cache.test-samples';

import { AICacheFormService } from './ai-cache-form.service';

describe('AICache Form Service', () => {
  let service: AICacheFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AICacheFormService);
  });

  describe('Service methods', () => {
    describe('createAICacheFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAICacheFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cacheKey: expect.any(Object),
            inputSha256: expect.any(Object),
            aiProvider: expect.any(Object),
            aiModel: expect.any(Object),
            operation: expect.any(Object),
            inputData: expect.any(Object),
            resultData: expect.any(Object),
            s3ResultKey: expect.any(Object),
            confidence: expect.any(Object),
            metadata: expect.any(Object),
            hits: expect.any(Object),
            cost: expect.any(Object),
            lastAccessDate: expect.any(Object),
            createdDate: expect.any(Object),
            expirationDate: expect.any(Object),
          }),
        );
      });

      it('passing IAICache should create a new form with FormGroup', () => {
        const formGroup = service.createAICacheFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cacheKey: expect.any(Object),
            inputSha256: expect.any(Object),
            aiProvider: expect.any(Object),
            aiModel: expect.any(Object),
            operation: expect.any(Object),
            inputData: expect.any(Object),
            resultData: expect.any(Object),
            s3ResultKey: expect.any(Object),
            confidence: expect.any(Object),
            metadata: expect.any(Object),
            hits: expect.any(Object),
            cost: expect.any(Object),
            lastAccessDate: expect.any(Object),
            createdDate: expect.any(Object),
            expirationDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getAICache', () => {
      it('should return NewAICache for default AICache initial value', () => {
        const formGroup = service.createAICacheFormGroup(sampleWithNewData);

        const aICache = service.getAICache(formGroup) as any;

        expect(aICache).toMatchObject(sampleWithNewData);
      });

      it('should return NewAICache for empty AICache initial value', () => {
        const formGroup = service.createAICacheFormGroup();

        const aICache = service.getAICache(formGroup) as any;

        expect(aICache).toMatchObject({});
      });

      it('should return IAICache', () => {
        const formGroup = service.createAICacheFormGroup(sampleWithRequiredData);

        const aICache = service.getAICache(formGroup) as any;

        expect(aICache).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAICache should not enable id FormControl', () => {
        const formGroup = service.createAICacheFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAICache should disable id FormControl', () => {
        const formGroup = service.createAICacheFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
