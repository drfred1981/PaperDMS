import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../export-job.test-samples';

import { ExportJobFormService } from './export-job-form.service';

describe('ExportJob Form Service', () => {
  let service: ExportJobFormService;

  beforeEach(() => {
    service = TestBed.inject(ExportJobFormService);
  });

  describe('Service methods', () => {
    describe('createExportJobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExportJobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            documentQuery: expect.any(Object),
            exportPatternId: expect.any(Object),
            exportFormat: expect.any(Object),
            includeMetadata: expect.any(Object),
            includeVersions: expect.any(Object),
            includeComments: expect.any(Object),
            includeAuditTrail: expect.any(Object),
            s3ExportKey: expect.any(Object),
            exportSize: expect.any(Object),
            documentCount: expect.any(Object),
            filesGenerated: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            exportPattern: expect.any(Object),
          }),
        );
      });

      it('passing IExportJob should create a new form with FormGroup', () => {
        const formGroup = service.createExportJobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            documentQuery: expect.any(Object),
            exportPatternId: expect.any(Object),
            exportFormat: expect.any(Object),
            includeMetadata: expect.any(Object),
            includeVersions: expect.any(Object),
            includeComments: expect.any(Object),
            includeAuditTrail: expect.any(Object),
            s3ExportKey: expect.any(Object),
            exportSize: expect.any(Object),
            documentCount: expect.any(Object),
            filesGenerated: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            errorMessage: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            exportPattern: expect.any(Object),
          }),
        );
      });
    });

    describe('getExportJob', () => {
      it('should return NewExportJob for default ExportJob initial value', () => {
        const formGroup = service.createExportJobFormGroup(sampleWithNewData);

        const exportJob = service.getExportJob(formGroup);

        expect(exportJob).toMatchObject(sampleWithNewData);
      });

      it('should return NewExportJob for empty ExportJob initial value', () => {
        const formGroup = service.createExportJobFormGroup();

        const exportJob = service.getExportJob(formGroup);

        expect(exportJob).toMatchObject({});
      });

      it('should return IExportJob', () => {
        const formGroup = service.createExportJobFormGroup(sampleWithRequiredData);

        const exportJob = service.getExportJob(formGroup);

        expect(exportJob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExportJob should not enable id FormControl', () => {
        const formGroup = service.createExportJobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExportJob should disable id FormControl', () => {
        const formGroup = service.createExportJobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
