import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-audit.test-samples';

import { DocumentAuditFormService } from './document-audit-form.service';

describe('DocumentAudit Form Service', () => {
  let service: DocumentAuditFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentAuditFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentAuditFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentAuditFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            action: expect.any(Object),
            userId: expect.any(Object),
            userIp: expect.any(Object),
            actionDate: expect.any(Object),
            additionalInfo: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentAudit should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentAuditFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            documentSha256: expect.any(Object),
            action: expect.any(Object),
            userId: expect.any(Object),
            userIp: expect.any(Object),
            actionDate: expect.any(Object),
            additionalInfo: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentAudit', () => {
      it('should return NewDocumentAudit for default DocumentAudit initial value', () => {
        const formGroup = service.createDocumentAuditFormGroup(sampleWithNewData);

        const documentAudit = service.getDocumentAudit(formGroup) as any;

        expect(documentAudit).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentAudit for empty DocumentAudit initial value', () => {
        const formGroup = service.createDocumentAuditFormGroup();

        const documentAudit = service.getDocumentAudit(formGroup) as any;

        expect(documentAudit).toMatchObject({});
      });

      it('should return IDocumentAudit', () => {
        const formGroup = service.createDocumentAuditFormGroup(sampleWithRequiredData);

        const documentAudit = service.getDocumentAudit(formGroup) as any;

        expect(documentAudit).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentAudit should not enable id FormControl', () => {
        const formGroup = service.createDocumentAuditFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentAudit should disable id FormControl', () => {
        const formGroup = service.createDocumentAuditFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
