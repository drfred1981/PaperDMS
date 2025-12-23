import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../extracted-field.test-samples';

import { ExtractedFieldFormService } from './extracted-field-form.service';

describe('ExtractedField Form Service', () => {
  let service: ExtractedFieldFormService;

  beforeEach(() => {
    service = TestBed.inject(ExtractedFieldFormService);
  });

  describe('Service methods', () => {
    describe('createExtractedFieldFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExtractedFieldFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            fieldKey: expect.any(Object),
            fieldValue: expect.any(Object),
            confidence: expect.any(Object),
            extractionMethod: expect.any(Object),
            isVerified: expect.any(Object),
            extractedDate: expect.any(Object),
          }),
        );
      });

      it('passing IExtractedField should create a new form with FormGroup', () => {
        const formGroup = service.createExtractedFieldFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            fieldKey: expect.any(Object),
            fieldValue: expect.any(Object),
            confidence: expect.any(Object),
            extractionMethod: expect.any(Object),
            isVerified: expect.any(Object),
            extractedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getExtractedField', () => {
      it('should return NewExtractedField for default ExtractedField initial value', () => {
        const formGroup = service.createExtractedFieldFormGroup(sampleWithNewData);

        const extractedField = service.getExtractedField(formGroup);

        expect(extractedField).toMatchObject(sampleWithNewData);
      });

      it('should return NewExtractedField for empty ExtractedField initial value', () => {
        const formGroup = service.createExtractedFieldFormGroup();

        const extractedField = service.getExtractedField(formGroup);

        expect(extractedField).toMatchObject({});
      });

      it('should return IExtractedField', () => {
        const formGroup = service.createExtractedFieldFormGroup(sampleWithRequiredData);

        const extractedField = service.getExtractedField(formGroup);

        expect(extractedField).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExtractedField should not enable id FormControl', () => {
        const formGroup = service.createExtractedFieldFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExtractedField should disable id FormControl', () => {
        const formGroup = service.createExtractedFieldFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
