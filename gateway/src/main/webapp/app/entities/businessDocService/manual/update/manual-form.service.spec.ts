import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../manual.test-samples';

import { ManualFormService } from './manual-form.service';

describe('Manual Form Service', () => {
  let service: ManualFormService;

  beforeEach(() => {
    service = TestBed.inject(ManualFormService);
  });

  describe('Service methods', () => {
    describe('createManualFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createManualFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            title: expect.any(Object),
            manualType: expect.any(Object),
            version: expect.any(Object),
            language: expect.any(Object),
            publicationDate: expect.any(Object),
            pageCount: expect.any(Object),
            status: expect.any(Object),
            isPublic: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IManual should create a new form with FormGroup', () => {
        const formGroup = service.createManualFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentId: expect.any(Object),
            title: expect.any(Object),
            manualType: expect.any(Object),
            version: expect.any(Object),
            language: expect.any(Object),
            publicationDate: expect.any(Object),
            pageCount: expect.any(Object),
            status: expect.any(Object),
            isPublic: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getManual', () => {
      it('should return NewManual for default Manual initial value', () => {
        const formGroup = service.createManualFormGroup(sampleWithNewData);

        const manual = service.getManual(formGroup);

        expect(manual).toMatchObject(sampleWithNewData);
      });

      it('should return NewManual for empty Manual initial value', () => {
        const formGroup = service.createManualFormGroup();

        const manual = service.getManual(formGroup);

        expect(manual).toMatchObject({});
      });

      it('should return IManual', () => {
        const formGroup = service.createManualFormGroup(sampleWithRequiredData);

        const manual = service.getManual(formGroup);

        expect(manual).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IManual should not enable id FormControl', () => {
        const formGroup = service.createManualFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewManual should disable id FormControl', () => {
        const formGroup = service.createManualFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
