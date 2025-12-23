import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../correspondent.test-samples';

import { CorrespondentFormService } from './correspondent-form.service';

describe('Correspondent Form Service', () => {
  let service: CorrespondentFormService;

  beforeEach(() => {
    service = TestBed.inject(CorrespondentFormService);
  });

  describe('Service methods', () => {
    describe('createCorrespondentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCorrespondentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
            address: expect.any(Object),
            company: expect.any(Object),
            type: expect.any(Object),
            role: expect.any(Object),
            confidence: expect.any(Object),
            isVerified: expect.any(Object),
            verifiedBy: expect.any(Object),
            verifiedDate: expect.any(Object),
            metadata: expect.any(Object),
            extractedDate: expect.any(Object),
            extraction: expect.any(Object),
          }),
        );
      });

      it('passing ICorrespondent should create a new form with FormGroup', () => {
        const formGroup = service.createCorrespondentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
            address: expect.any(Object),
            company: expect.any(Object),
            type: expect.any(Object),
            role: expect.any(Object),
            confidence: expect.any(Object),
            isVerified: expect.any(Object),
            verifiedBy: expect.any(Object),
            verifiedDate: expect.any(Object),
            metadata: expect.any(Object),
            extractedDate: expect.any(Object),
            extraction: expect.any(Object),
          }),
        );
      });
    });

    describe('getCorrespondent', () => {
      it('should return NewCorrespondent for default Correspondent initial value', () => {
        const formGroup = service.createCorrespondentFormGroup(sampleWithNewData);

        const correspondent = service.getCorrespondent(formGroup);

        expect(correspondent).toMatchObject(sampleWithNewData);
      });

      it('should return NewCorrespondent for empty Correspondent initial value', () => {
        const formGroup = service.createCorrespondentFormGroup();

        const correspondent = service.getCorrespondent(formGroup);

        expect(correspondent).toMatchObject({});
      });

      it('should return ICorrespondent', () => {
        const formGroup = service.createCorrespondentFormGroup(sampleWithRequiredData);

        const correspondent = service.getCorrespondent(formGroup);

        expect(correspondent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICorrespondent should not enable id FormControl', () => {
        const formGroup = service.createCorrespondentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCorrespondent should disable id FormControl', () => {
        const formGroup = service.createCorrespondentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
