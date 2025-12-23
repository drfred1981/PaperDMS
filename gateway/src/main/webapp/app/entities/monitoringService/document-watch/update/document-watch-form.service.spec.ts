import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../document-watch.test-samples';

import { DocumentWatchFormService } from './document-watch-form.service';

describe('DocumentWatch Form Service', () => {
  let service: DocumentWatchFormService;

  beforeEach(() => {
    service = TestBed.inject(DocumentWatchFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentWatchFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentWatchFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            userId: expect.any(Object),
            watchType: expect.any(Object),
            notifyOnView: expect.any(Object),
            notifyOnDownload: expect.any(Object),
            notifyOnModify: expect.any(Object),
            notifyOnShare: expect.any(Object),
            notifyOnDelete: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentWatch should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentWatchFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            userId: expect.any(Object),
            watchType: expect.any(Object),
            notifyOnView: expect.any(Object),
            notifyOnDownload: expect.any(Object),
            notifyOnModify: expect.any(Object),
            notifyOnShare: expect.any(Object),
            notifyOnDelete: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentWatch', () => {
      it('should return NewDocumentWatch for default DocumentWatch initial value', () => {
        const formGroup = service.createDocumentWatchFormGroup(sampleWithNewData);

        const documentWatch = service.getDocumentWatch(formGroup);

        expect(documentWatch).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentWatch for empty DocumentWatch initial value', () => {
        const formGroup = service.createDocumentWatchFormGroup();

        const documentWatch = service.getDocumentWatch(formGroup);

        expect(documentWatch).toMatchObject({});
      });

      it('should return IDocumentWatch', () => {
        const formGroup = service.createDocumentWatchFormGroup(sampleWithRequiredData);

        const documentWatch = service.getDocumentWatch(formGroup);

        expect(documentWatch).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentWatch should not enable id FormControl', () => {
        const formGroup = service.createDocumentWatchFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentWatch should disable id FormControl', () => {
        const formGroup = service.createDocumentWatchFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
