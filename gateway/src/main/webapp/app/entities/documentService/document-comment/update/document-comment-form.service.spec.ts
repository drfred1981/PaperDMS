import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-comment.test-samples';

import { DocumentCommentFormService } from './document-comment-form.service';

describe('DocumentComment Form Service', () => {
  let service: DocumentCommentFormService;

  beforeEach(() => {
    service = TestBed.inject(DocumentCommentFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentCommentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentCommentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            content: expect.any(Object),
            pageNumber: expect.any(Object),
            isResolved: expect.any(Object),
            authorId: expect.any(Object),
            createdDate: expect.any(Object),
            parentComment: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentComment should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentCommentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            content: expect.any(Object),
            pageNumber: expect.any(Object),
            isResolved: expect.any(Object),
            authorId: expect.any(Object),
            createdDate: expect.any(Object),
            parentComment: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentComment', () => {
      it('should return NewDocumentComment for default DocumentComment initial value', () => {
        const formGroup = service.createDocumentCommentFormGroup(sampleWithNewData);

        const documentComment = service.getDocumentComment(formGroup);

        expect(documentComment).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentComment for empty DocumentComment initial value', () => {
        const formGroup = service.createDocumentCommentFormGroup();

        const documentComment = service.getDocumentComment(formGroup);

        expect(documentComment).toMatchObject({});
      });

      it('should return IDocumentComment', () => {
        const formGroup = service.createDocumentCommentFormGroup(sampleWithRequiredData);

        const documentComment = service.getDocumentComment(formGroup);

        expect(documentComment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentComment should not enable id FormControl', () => {
        const formGroup = service.createDocumentCommentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentComment should disable id FormControl', () => {
        const formGroup = service.createDocumentCommentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
