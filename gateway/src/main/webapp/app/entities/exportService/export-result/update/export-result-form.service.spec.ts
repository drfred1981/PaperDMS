import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../export-result.test-samples';

import { ExportResultFormService } from './export-result-form.service';

describe('ExportResult Form Service', () => {
  let service: ExportResultFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExportResultFormService);
  });

  describe('Service methods', () => {
    describe('createExportResultFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExportResultFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            exportJobId: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            originalFileName: expect.any(Object),
            exportedPath: expect.any(Object),
            exportedFileName: expect.any(Object),
            s3ExportKey: expect.any(Object),
            fileSize: expect.any(Object),
            status: expect.any(Object),
            errorMessage: expect.any(Object),
            exportedDate: expect.any(Object),
            exportJob: expect.any(Object),
          }),
        );
      });

      it('passing IExportResult should create a new form with FormGroup', () => {
        const formGroup = service.createExportResultFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            exportJobId: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            originalFileName: expect.any(Object),
            exportedPath: expect.any(Object),
            exportedFileName: expect.any(Object),
            s3ExportKey: expect.any(Object),
            fileSize: expect.any(Object),
            status: expect.any(Object),
            errorMessage: expect.any(Object),
            exportedDate: expect.any(Object),
            exportJob: expect.any(Object),
          }),
        );
      });
    });

    describe('getExportResult', () => {
      it('should return NewExportResult for default ExportResult initial value', () => {
        const formGroup = service.createExportResultFormGroup(sampleWithNewData);

        const exportResult = service.getExportResult(formGroup) as any;

        expect(exportResult).toMatchObject(sampleWithNewData);
      });

      it('should return NewExportResult for empty ExportResult initial value', () => {
        const formGroup = service.createExportResultFormGroup();

        const exportResult = service.getExportResult(formGroup) as any;

        expect(exportResult).toMatchObject({});
      });

      it('should return IExportResult', () => {
        const formGroup = service.createExportResultFormGroup(sampleWithRequiredData);

        const exportResult = service.getExportResult(formGroup) as any;

        expect(exportResult).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExportResult should not enable id FormControl', () => {
        const formGroup = service.createExportResultFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExportResult should disable id FormControl', () => {
        const formGroup = service.createExportResultFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
