import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-similarity.test-samples';

import { DocumentSimilarityFormService } from './document-similarity-form.service';

describe('DocumentSimilarity Form Service', () => {
  let service: DocumentSimilarityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentSimilarityFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentSimilarityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentSimilarityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId1: expect.any(Object),
            documentId2: expect.any(Object),
            similarityScore: expect.any(Object),
            algorithm: expect.any(Object),
            features: expect.any(Object),
            computedDate: expect.any(Object),
            isRelevant: expect.any(Object),
            reviewedBy: expect.any(Object),
            reviewedDate: expect.any(Object),
            job: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentSimilarity should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentSimilarityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId1: expect.any(Object),
            documentId2: expect.any(Object),
            similarityScore: expect.any(Object),
            algorithm: expect.any(Object),
            features: expect.any(Object),
            computedDate: expect.any(Object),
            isRelevant: expect.any(Object),
            reviewedBy: expect.any(Object),
            reviewedDate: expect.any(Object),
            job: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentSimilarity', () => {
      it('should return NewDocumentSimilarity for default DocumentSimilarity initial value', () => {
        const formGroup = service.createDocumentSimilarityFormGroup(sampleWithNewData);

        const documentSimilarity = service.getDocumentSimilarity(formGroup) as any;

        expect(documentSimilarity).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentSimilarity for empty DocumentSimilarity initial value', () => {
        const formGroup = service.createDocumentSimilarityFormGroup();

        const documentSimilarity = service.getDocumentSimilarity(formGroup) as any;

        expect(documentSimilarity).toMatchObject({});
      });

      it('should return IDocumentSimilarity', () => {
        const formGroup = service.createDocumentSimilarityFormGroup(sampleWithRequiredData);

        const documentSimilarity = service.getDocumentSimilarity(formGroup) as any;

        expect(documentSimilarity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentSimilarity should not enable id FormControl', () => {
        const formGroup = service.createDocumentSimilarityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentSimilarity should disable id FormControl', () => {
        const formGroup = service.createDocumentSimilarityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
