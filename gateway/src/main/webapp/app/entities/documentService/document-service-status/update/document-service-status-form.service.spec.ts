import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-service-status.test-samples';

import { DocumentServiceStatusFormService } from './document-service-status-form.service';

describe('DocumentServiceStatus Form Service', () => {
  let service: DocumentServiceStatusFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentServiceStatusFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentServiceStatusFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentServiceStatusFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serviceType: expect.any(Object),
            status: expect.any(Object),
            statusDetails: expect.any(Object),
            errorMessage: expect.any(Object),
            retryCount: expect.any(Object),
            lastProcessedDate: expect.any(Object),
            processingStartDate: expect.any(Object),
            processingEndDate: expect.any(Object),
            processingDuration: expect.any(Object),
            jobId: expect.any(Object),
            priority: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedDate: expect.any(Object),
            document: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentServiceStatus should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentServiceStatusFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serviceType: expect.any(Object),
            status: expect.any(Object),
            statusDetails: expect.any(Object),
            errorMessage: expect.any(Object),
            retryCount: expect.any(Object),
            lastProcessedDate: expect.any(Object),
            processingStartDate: expect.any(Object),
            processingEndDate: expect.any(Object),
            processingDuration: expect.any(Object),
            jobId: expect.any(Object),
            priority: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedDate: expect.any(Object),
            document: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentServiceStatus', () => {
      it('should return NewDocumentServiceStatus for default DocumentServiceStatus initial value', () => {
        const formGroup = service.createDocumentServiceStatusFormGroup(sampleWithNewData);

        const documentServiceStatus = service.getDocumentServiceStatus(formGroup) as any;

        expect(documentServiceStatus).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentServiceStatus for empty DocumentServiceStatus initial value', () => {
        const formGroup = service.createDocumentServiceStatusFormGroup();

        const documentServiceStatus = service.getDocumentServiceStatus(formGroup) as any;

        expect(documentServiceStatus).toMatchObject({});
      });

      it('should return IDocumentServiceStatus', () => {
        const formGroup = service.createDocumentServiceStatusFormGroup(sampleWithRequiredData);

        const documentServiceStatus = service.getDocumentServiceStatus(formGroup) as any;

        expect(documentServiceStatus).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentServiceStatus should not enable id FormControl', () => {
        const formGroup = service.createDocumentServiceStatusFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentServiceStatus should disable id FormControl', () => {
        const formGroup = service.createDocumentServiceStatusFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
