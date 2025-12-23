import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ai-cache.test-samples';

import { AiCacheFormService } from './ai-cache-form.service';

describe('AiCache Form Service', () => {
  let service: AiCacheFormService;

  beforeEach(() => {
    service = TestBed.inject(AiCacheFormService);
  });

  describe('Service methods', () => {
    describe('createAiCacheFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAiCacheFormGroup();

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

      it('passing IAiCache should create a new form with FormGroup', () => {
        const formGroup = service.createAiCacheFormGroup(sampleWithRequiredData);

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

    describe('getAiCache', () => {
      it('should return NewAiCache for default AiCache initial value', () => {
        const formGroup = service.createAiCacheFormGroup(sampleWithNewData);

        const aiCache = service.getAiCache(formGroup);

        expect(aiCache).toMatchObject(sampleWithNewData);
      });

      it('should return NewAiCache for empty AiCache initial value', () => {
        const formGroup = service.createAiCacheFormGroup();

        const aiCache = service.getAiCache(formGroup);

        expect(aiCache).toMatchObject({});
      });

      it('should return IAiCache', () => {
        const formGroup = service.createAiCacheFormGroup(sampleWithRequiredData);

        const aiCache = service.getAiCache(formGroup);

        expect(aiCache).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAiCache should not enable id FormControl', () => {
        const formGroup = service.createAiCacheFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAiCache should disable id FormControl', () => {
        const formGroup = service.createAiCacheFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
