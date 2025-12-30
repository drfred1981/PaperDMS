import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-type.test-samples';

import { DocumentTypeFormService } from './document-type-form.service';

describe('DocumentType Form Service', () => {
  let service: DocumentTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentTypeFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            code: expect.any(Object),
            icon: expect.any(Object),
            color: expect.any(Object),
            isActive: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentType should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            code: expect.any(Object),
            icon: expect.any(Object),
            color: expect.any(Object),
            isActive: expect.any(Object),
            createdDate: expect.any(Object),
            createdBy: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentType', () => {
      it('should return NewDocumentType for default DocumentType initial value', () => {
        const formGroup = service.createDocumentTypeFormGroup(sampleWithNewData);

        const documentType = service.getDocumentType(formGroup) as any;

        expect(documentType).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentType for empty DocumentType initial value', () => {
        const formGroup = service.createDocumentTypeFormGroup();

        const documentType = service.getDocumentType(formGroup) as any;

        expect(documentType).toMatchObject({});
      });

      it('should return IDocumentType', () => {
        const formGroup = service.createDocumentTypeFormGroup(sampleWithRequiredData);

        const documentType = service.getDocumentType(formGroup) as any;

        expect(documentType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentType should not enable id FormControl', () => {
        const formGroup = service.createDocumentTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentType should disable id FormControl', () => {
        const formGroup = service.createDocumentTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
