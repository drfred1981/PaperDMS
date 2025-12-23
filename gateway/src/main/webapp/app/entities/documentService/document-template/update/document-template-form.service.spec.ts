import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-template.test-samples';

import { DocumentTemplateFormService } from './document-template-form.service';

describe('DocumentTemplate Form Service', () => {
  let service: DocumentTemplateFormService;

  beforeEach(() => {
    service = TestBed.inject(DocumentTemplateFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentTemplateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentTemplateFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            templateSha256: expect.any(Object),
            templateS3Key: expect.any(Object),
            isActive: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            documentType: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentTemplate should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentTemplateFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            templateSha256: expect.any(Object),
            templateS3Key: expect.any(Object),
            isActive: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            documentType: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentTemplate', () => {
      it('should return NewDocumentTemplate for default DocumentTemplate initial value', () => {
        const formGroup = service.createDocumentTemplateFormGroup(sampleWithNewData);

        const documentTemplate = service.getDocumentTemplate(formGroup);

        expect(documentTemplate).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentTemplate for empty DocumentTemplate initial value', () => {
        const formGroup = service.createDocumentTemplateFormGroup();

        const documentTemplate = service.getDocumentTemplate(formGroup);

        expect(documentTemplate).toMatchObject({});
      });

      it('should return IDocumentTemplate', () => {
        const formGroup = service.createDocumentTemplateFormGroup(sampleWithRequiredData);

        const documentTemplate = service.getDocumentTemplate(formGroup);

        expect(documentTemplate).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentTemplate should not enable id FormControl', () => {
        const formGroup = service.createDocumentTemplateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentTemplate should disable id FormControl', () => {
        const formGroup = service.createDocumentTemplateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
