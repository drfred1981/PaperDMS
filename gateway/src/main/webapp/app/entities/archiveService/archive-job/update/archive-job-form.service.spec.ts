import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../archive-job.test-samples';

import { ArchiveJobFormService } from './archive-job-form.service';

describe('ArchiveJob Form Service', () => {
  let service: ArchiveJobFormService;

  beforeEach(() => {
    service = TestBed.inject(ArchiveJobFormService);
  });

  describe('Service methods', () => {
    describe('createArchiveJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createArchiveJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            documentQuery: expect.any(Object),
            archiveFormat: expect.any(Object),
            compressionLevel: expect.any(Object),
            encryptionEnabled: expect.any(Object),
            encryptionAlgorithm: expect.any(Object),
            password: expect.any(Object),
            s3ArchiveKey: expect.any(Object),
            archiveSha256: expect.any(Object),
            archiveSize: expect.any(Object),
            documentCount: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IArchiveJob should create a new form with FormGroup', () => {
        const formGroup = service.createArchiveJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            documentQuery: expect.any(Object),
            archiveFormat: expect.any(Object),
            compressionLevel: expect.any(Object),
            encryptionEnabled: expect.any(Object),
            encryptionAlgorithm: expect.any(Object),
            password: expect.any(Object),
            s3ArchiveKey: expect.any(Object),
            archiveSha256: expect.any(Object),
            archiveSize: expect.any(Object),
            documentCount: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getArchiveJob', () => {
      it('should return NewArchiveJob for default ArchiveJob initial value', () => {
        const formGroup = service.createArchiveJobFormGroup(sampleWithNewData);

        const archiveJob = service.getArchiveJob(formGroup);

        expect(archiveJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewArchiveJob for empty ArchiveJob initial value', () => {
        const formGroup = service.createArchiveJobFormGroup();

        const archiveJob = service.getArchiveJob(formGroup);

        expect(archiveJob).toMatchObject({});
      });

      it('should return IArchiveJob', () => {
        const formGroup = service.createArchiveJobFormGroup(sampleWithRequiredData);

        const archiveJob = service.getArchiveJob(formGroup);

        expect(archiveJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IArchiveJob should not enable id FormControl', () => {
        const formGroup = service.createArchiveJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewArchiveJob should disable id FormControl', () => {
        const formGroup = service.createArchiveJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
