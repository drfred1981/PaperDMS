import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-extracted-field.test-samples';

import { DocumentExtractedFieldFormService } from './document-extracted-field-form.service';

describe('DocumentExtractedField Form Service', () => {
  let service: DocumentExtractedFieldFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentExtractedFieldFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentExtractedFieldFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentExtractedFieldFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fieldKey: expect.any(Object),
            fieldValue: expect.any(Object),
            confidence: expect.any(Object),
            extractionMethod: expect.any(Object),
            isVerified: expect.any(Object),
            extractedDate: expect.any(Object),
            document: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentExtractedField should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentExtractedFieldFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fieldKey: expect.any(Object),
            fieldValue: expect.any(Object),
            confidence: expect.any(Object),
            extractionMethod: expect.any(Object),
            isVerified: expect.any(Object),
            extractedDate: expect.any(Object),
            document: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentExtractedField', () => {
      it('should return NewDocumentExtractedField for default DocumentExtractedField initial value', () => {
        const formGroup = service.createDocumentExtractedFieldFormGroup(sampleWithNewData);

        const documentExtractedField = service.getDocumentExtractedField(formGroup) as any;

        expect(documentExtractedField).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentExtractedField for empty DocumentExtractedField initial value', () => {
        const formGroup = service.createDocumentExtractedFieldFormGroup();

        const documentExtractedField = service.getDocumentExtractedField(formGroup) as any;

        expect(documentExtractedField).toMatchObject({});
      });

      it('should return IDocumentExtractedField', () => {
        const formGroup = service.createDocumentExtractedFieldFormGroup(sampleWithRequiredData);

        const documentExtractedField = service.getDocumentExtractedField(formGroup) as any;

        expect(documentExtractedField).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentExtractedField should not enable id FormControl', () => {
        const formGroup = service.createDocumentExtractedFieldFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentExtractedField should disable id FormControl', () => {
        const formGroup = service.createDocumentExtractedFieldFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
