import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-process.test-samples';

import { DocumentProcessFormService } from './document-process-form.service';

describe('DocumentProcess Form Service', () => {
  let service: DocumentProcessFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentProcessFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentProcessFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentProcessFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            documentSha256: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentProcess should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentProcessFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            documentSha256: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentProcess', () => {
      it('should return NewDocumentProcess for default DocumentProcess initial value', () => {
        const formGroup = service.createDocumentProcessFormGroup(sampleWithNewData);

        const documentProcess = service.getDocumentProcess(formGroup) as any;

        expect(documentProcess).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentProcess for empty DocumentProcess initial value', () => {
        const formGroup = service.createDocumentProcessFormGroup();

        const documentProcess = service.getDocumentProcess(formGroup) as any;

        expect(documentProcess).toMatchObject({});
      });

      it('should return IDocumentProcess', () => {
        const formGroup = service.createDocumentProcessFormGroup(sampleWithRequiredData);

        const documentProcess = service.getDocumentProcess(formGroup) as any;

        expect(documentProcess).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentProcess should not enable id FormControl', () => {
        const formGroup = service.createDocumentProcessFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentProcess should disable id FormControl', () => {
        const formGroup = service.createDocumentProcessFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
