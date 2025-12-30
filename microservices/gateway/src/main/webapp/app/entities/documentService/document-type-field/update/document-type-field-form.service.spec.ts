import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-type-field.test-samples';

import { DocumentTypeFieldFormService } from './document-type-field-form.service';

describe('DocumentTypeField Form Service', () => {
  let service: DocumentTypeFieldFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentTypeFieldFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentTypeFieldFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentTypeFieldFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fieldKey: expect.any(Object),
            fieldLabel: expect.any(Object),
            dataType: expect.any(Object),
            isRequired: expect.any(Object),
            isSearchable: expect.any(Object),
            createdDate: expect.any(Object),
            documentType: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentTypeField should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentTypeFieldFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fieldKey: expect.any(Object),
            fieldLabel: expect.any(Object),
            dataType: expect.any(Object),
            isRequired: expect.any(Object),
            isSearchable: expect.any(Object),
            createdDate: expect.any(Object),
            documentType: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentTypeField', () => {
      it('should return NewDocumentTypeField for default DocumentTypeField initial value', () => {
        const formGroup = service.createDocumentTypeFieldFormGroup(sampleWithNewData);

        const documentTypeField = service.getDocumentTypeField(formGroup) as any;

        expect(documentTypeField).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentTypeField for empty DocumentTypeField initial value', () => {
        const formGroup = service.createDocumentTypeFieldFormGroup();

        const documentTypeField = service.getDocumentTypeField(formGroup) as any;

        expect(documentTypeField).toMatchObject({});
      });

      it('should return IDocumentTypeField', () => {
        const formGroup = service.createDocumentTypeFieldFormGroup(sampleWithRequiredData);

        const documentTypeField = service.getDocumentTypeField(formGroup) as any;

        expect(documentTypeField).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentTypeField should not enable id FormControl', () => {
        const formGroup = service.createDocumentTypeFieldFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentTypeField should disable id FormControl', () => {
        const formGroup = service.createDocumentTypeFieldFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
