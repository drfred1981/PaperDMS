import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../scanned-page.test-samples';

import { ScannedPageFormService } from './scanned-page-form.service';

describe('ScannedPage Form Service', () => {
  let service: ScannedPageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScannedPageFormService);
  });

  describe('Service methods', () => {
    describe('createScannedPageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createScannedPageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            scanJobId: expect.any(Object),
            pageNumber: expect.any(Object),
            sha256: expect.any(Object),
            s3Key: expect.any(Object),
            s3PreviewKey: expect.any(Object),
            fileSize: expect.any(Object),
            width: expect.any(Object),
            height: expect.any(Object),
            dpi: expect.any(Object),
            documentId: expect.any(Object),
            scannedDate: expect.any(Object),
            scanJob: expect.any(Object),
          }),
        );
      });

      it('passing IScannedPage should create a new form with FormGroup', () => {
        const formGroup = service.createScannedPageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            scanJobId: expect.any(Object),
            pageNumber: expect.any(Object),
            sha256: expect.any(Object),
            s3Key: expect.any(Object),
            s3PreviewKey: expect.any(Object),
            fileSize: expect.any(Object),
            width: expect.any(Object),
            height: expect.any(Object),
            dpi: expect.any(Object),
            documentId: expect.any(Object),
            scannedDate: expect.any(Object),
            scanJob: expect.any(Object),
          }),
        );
      });
    });

    describe('getScannedPage', () => {
      it('should return NewScannedPage for default ScannedPage initial value', () => {
        const formGroup = service.createScannedPageFormGroup(sampleWithNewData);

        const scannedPage = service.getScannedPage(formGroup) as any;

        expect(scannedPage).toMatchObject(sampleWithNewData);
      });

      it('should return NewScannedPage for empty ScannedPage initial value', () => {
        const formGroup = service.createScannedPageFormGroup();

        const scannedPage = service.getScannedPage(formGroup) as any;

        expect(scannedPage).toMatchObject({});
      });

      it('should return IScannedPage', () => {
        const formGroup = service.createScannedPageFormGroup(sampleWithRequiredData);

        const scannedPage = service.getScannedPage(formGroup) as any;

        expect(scannedPage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IScannedPage should not enable id FormControl', () => {
        const formGroup = service.createScannedPageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewScannedPage should disable id FormControl', () => {
        const formGroup = service.createScannedPageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
