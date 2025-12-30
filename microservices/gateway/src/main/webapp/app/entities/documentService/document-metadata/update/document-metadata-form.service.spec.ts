import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-metadata.test-samples';

import { DocumentMetadataFormService } from './document-metadata-form.service';

describe('DocumentMetadata Form Service', () => {
  let service: DocumentMetadataFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentMetadataFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentMetadataFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentMetadataFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            key: expect.any(Object),
            value: expect.any(Object),
            dataType: expect.any(Object),
            isSearchable: expect.any(Object),
            createdDate: expect.any(Object),
            document: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentMetadata should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentMetadataFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            key: expect.any(Object),
            value: expect.any(Object),
            dataType: expect.any(Object),
            isSearchable: expect.any(Object),
            createdDate: expect.any(Object),
            document: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentMetadata', () => {
      it('should return NewDocumentMetadata for default DocumentMetadata initial value', () => {
        const formGroup = service.createDocumentMetadataFormGroup(sampleWithNewData);

        const documentMetadata = service.getDocumentMetadata(formGroup) as any;

        expect(documentMetadata).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentMetadata for empty DocumentMetadata initial value', () => {
        const formGroup = service.createDocumentMetadataFormGroup();

        const documentMetadata = service.getDocumentMetadata(formGroup) as any;

        expect(documentMetadata).toMatchObject({});
      });

      it('should return IDocumentMetadata', () => {
        const formGroup = service.createDocumentMetadataFormGroup(sampleWithRequiredData);

        const documentMetadata = service.getDocumentMetadata(formGroup) as any;

        expect(documentMetadata).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentMetadata should not enable id FormControl', () => {
        const formGroup = service.createDocumentMetadataFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentMetadata should disable id FormControl', () => {
        const formGroup = service.createDocumentMetadataFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
