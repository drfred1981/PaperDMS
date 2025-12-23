import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../archive-document.test-samples';

import { ArchiveDocumentFormService } from './archive-document-form.service';

describe('ArchiveDocument Form Service', () => {
  let service: ArchiveDocumentFormService;

  beforeEach(() => {
    service = TestBed.inject(ArchiveDocumentFormService);
  });

  describe('Service methods', () => {
    describe('createArchiveDocumentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createArchiveDocumentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            archiveJobId: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            originalPath: expect.any(Object),
            archivePath: expect.any(Object),
            fileSize: expect.any(Object),
            addedDate: expect.any(Object),
            archiveJob: expect.any(Object),
          }),
        );
      });

      it('passing IArchiveDocument should create a new form with FormGroup', () => {
        const formGroup = service.createArchiveDocumentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            archiveJobId: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            originalPath: expect.any(Object),
            archivePath: expect.any(Object),
            fileSize: expect.any(Object),
            addedDate: expect.any(Object),
            archiveJob: expect.any(Object),
          }),
        );
      });
    });

    describe('getArchiveDocument', () => {
      it('should return NewArchiveDocument for default ArchiveDocument initial value', () => {
        const formGroup = service.createArchiveDocumentFormGroup(sampleWithNewData);

        const archiveDocument = service.getArchiveDocument(formGroup);

        expect(archiveDocument).toMatchObject(sampleWithNewData);
      });

      it('should return NewArchiveDocument for empty ArchiveDocument initial value', () => {
        const formGroup = service.createArchiveDocumentFormGroup();

        const archiveDocument = service.getArchiveDocument(formGroup);

        expect(archiveDocument).toMatchObject({});
      });

      it('should return IArchiveDocument', () => {
        const formGroup = service.createArchiveDocumentFormGroup(sampleWithRequiredData);

        const archiveDocument = service.getArchiveDocument(formGroup);

        expect(archiveDocument).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IArchiveDocument should not enable id FormControl', () => {
        const formGroup = service.createArchiveDocumentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewArchiveDocument should disable id FormControl', () => {
        const formGroup = service.createArchiveDocumentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
