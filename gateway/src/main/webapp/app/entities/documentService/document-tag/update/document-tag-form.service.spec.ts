import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-tag.test-samples';

import { DocumentTagFormService } from './document-tag-form.service';

describe('DocumentTag Form Service', () => {
  let service: DocumentTagFormService;

  beforeEach(() => {
    service = TestBed.inject(DocumentTagFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentTagFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentTagFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            assignedDate: expect.any(Object),
            assignedBy: expect.any(Object),
            confidence: expect.any(Object),
            isAutoTagged: expect.any(Object),
            source: expect.any(Object),
            document: expect.any(Object),
            tag: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentTag should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentTagFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            assignedDate: expect.any(Object),
            assignedBy: expect.any(Object),
            confidence: expect.any(Object),
            isAutoTagged: expect.any(Object),
            source: expect.any(Object),
            document: expect.any(Object),
            tag: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentTag', () => {
      it('should return NewDocumentTag for default DocumentTag initial value', () => {
        const formGroup = service.createDocumentTagFormGroup(sampleWithNewData);

        const documentTag = service.getDocumentTag(formGroup);

        expect(documentTag).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentTag for empty DocumentTag initial value', () => {
        const formGroup = service.createDocumentTagFormGroup();

        const documentTag = service.getDocumentTag(formGroup);

        expect(documentTag).toMatchObject({});
      });

      it('should return IDocumentTag', () => {
        const formGroup = service.createDocumentTagFormGroup(sampleWithRequiredData);

        const documentTag = service.getDocumentTag(formGroup);

        expect(documentTag).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentTag should not enable id FormControl', () => {
        const formGroup = service.createDocumentTagFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentTag should disable id FormControl', () => {
        const formGroup = service.createDocumentTagFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
