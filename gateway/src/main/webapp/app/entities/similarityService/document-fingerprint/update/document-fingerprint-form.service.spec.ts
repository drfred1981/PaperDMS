import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-fingerprint.test-samples';

import { DocumentFingerprintFormService } from './document-fingerprint-form.service';

describe('DocumentFingerprint Form Service', () => {
  let service: DocumentFingerprintFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentFingerprintFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentFingerprintFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentFingerprintFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            fingerprintType: expect.any(Object),
            fingerprint: expect.any(Object),
            vectorEmbedding: expect.any(Object),
            metadata: expect.any(Object),
            computedDate: expect.any(Object),
            lastUpdated: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentFingerprint should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentFingerprintFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            fingerprintType: expect.any(Object),
            fingerprint: expect.any(Object),
            vectorEmbedding: expect.any(Object),
            metadata: expect.any(Object),
            computedDate: expect.any(Object),
            lastUpdated: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentFingerprint', () => {
      it('should return NewDocumentFingerprint for default DocumentFingerprint initial value', () => {
        const formGroup = service.createDocumentFingerprintFormGroup(sampleWithNewData);

        const documentFingerprint = service.getDocumentFingerprint(formGroup) as any;

        expect(documentFingerprint).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentFingerprint for empty DocumentFingerprint initial value', () => {
        const formGroup = service.createDocumentFingerprintFormGroup();

        const documentFingerprint = service.getDocumentFingerprint(formGroup) as any;

        expect(documentFingerprint).toMatchObject({});
      });

      it('should return IDocumentFingerprint', () => {
        const formGroup = service.createDocumentFingerprintFormGroup(sampleWithRequiredData);

        const documentFingerprint = service.getDocumentFingerprint(formGroup) as any;

        expect(documentFingerprint).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentFingerprint should not enable id FormControl', () => {
        const formGroup = service.createDocumentFingerprintFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentFingerprint should disable id FormControl', () => {
        const formGroup = service.createDocumentFingerprintFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
