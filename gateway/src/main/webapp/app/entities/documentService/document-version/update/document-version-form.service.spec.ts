import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-version.test-samples';

import { DocumentVersionFormService } from './document-version-form.service';

describe('DocumentVersion Form Service', () => {
  let service: DocumentVersionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentVersionFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentVersionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentVersionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            versionNumber: expect.any(Object),
            sha256: expect.any(Object),
            s3Key: expect.any(Object),
            fileSize: expect.any(Object),
            uploadDate: expect.any(Object),
            isActive: expect.any(Object),
            createdBy: expect.any(Object),
            document: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentVersion should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentVersionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            versionNumber: expect.any(Object),
            sha256: expect.any(Object),
            s3Key: expect.any(Object),
            fileSize: expect.any(Object),
            uploadDate: expect.any(Object),
            isActive: expect.any(Object),
            createdBy: expect.any(Object),
            document: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentVersion', () => {
      it('should return NewDocumentVersion for default DocumentVersion initial value', () => {
        const formGroup = service.createDocumentVersionFormGroup(sampleWithNewData);

        const documentVersion = service.getDocumentVersion(formGroup) as any;

        expect(documentVersion).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentVersion for empty DocumentVersion initial value', () => {
        const formGroup = service.createDocumentVersionFormGroup();

        const documentVersion = service.getDocumentVersion(formGroup) as any;

        expect(documentVersion).toMatchObject({});
      });

      it('should return IDocumentVersion', () => {
        const formGroup = service.createDocumentVersionFormGroup(sampleWithRequiredData);

        const documentVersion = service.getDocumentVersion(formGroup) as any;

        expect(documentVersion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentVersion should not enable id FormControl', () => {
        const formGroup = service.createDocumentVersionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentVersion should disable id FormControl', () => {
        const formGroup = service.createDocumentVersionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
