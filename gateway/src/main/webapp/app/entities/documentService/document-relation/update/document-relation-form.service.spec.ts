import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-relation.test-samples';

import { DocumentRelationFormService } from './document-relation-form.service';

describe('DocumentRelation Form Service', () => {
  let service: DocumentRelationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentRelationFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentRelationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentRelationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sourceDocumentId: expect.any(Object),
            targetDocumentId: expect.any(Object),
            relationType: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentRelation should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentRelationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sourceDocumentId: expect.any(Object),
            targetDocumentId: expect.any(Object),
            relationType: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentRelation', () => {
      it('should return NewDocumentRelation for default DocumentRelation initial value', () => {
        const formGroup = service.createDocumentRelationFormGroup(sampleWithNewData);

        const documentRelation = service.getDocumentRelation(formGroup) as any;

        expect(documentRelation).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentRelation for empty DocumentRelation initial value', () => {
        const formGroup = service.createDocumentRelationFormGroup();

        const documentRelation = service.getDocumentRelation(formGroup) as any;

        expect(documentRelation).toMatchObject({});
      });

      it('should return IDocumentRelation', () => {
        const formGroup = service.createDocumentRelationFormGroup(sampleWithRequiredData);

        const documentRelation = service.getDocumentRelation(formGroup) as any;

        expect(documentRelation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentRelation should not enable id FormControl', () => {
        const formGroup = service.createDocumentRelationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentRelation should disable id FormControl', () => {
        const formGroup = service.createDocumentRelationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
