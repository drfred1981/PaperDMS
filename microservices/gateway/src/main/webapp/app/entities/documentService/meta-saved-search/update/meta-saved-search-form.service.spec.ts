import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../meta-saved-search.test-samples';

import { MetaSavedSearchFormService } from './meta-saved-search-form.service';

describe('MetaSavedSearch Form Service', () => {
  let service: MetaSavedSearchFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MetaSavedSearchFormService);
  });

  describe('Service methods', () => {
    describe('createMetaSavedSearchFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMetaSavedSearchFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            query: expect.any(Object),
            isPublic: expect.any(Object),
            isAlert: expect.any(Object),
            alertFrequency: expect.any(Object),
            userId: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });

      it('passing IMetaSavedSearch should create a new form with FormGroup', () => {
        const formGroup = service.createMetaSavedSearchFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            query: expect.any(Object),
            isPublic: expect.any(Object),
            isAlert: expect.any(Object),
            alertFrequency: expect.any(Object),
            userId: expect.any(Object),
            createdDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getMetaSavedSearch', () => {
      it('should return NewMetaSavedSearch for default MetaSavedSearch initial value', () => {
        const formGroup = service.createMetaSavedSearchFormGroup(sampleWithNewData);

        const metaSavedSearch = service.getMetaSavedSearch(formGroup) as any;

        expect(metaSavedSearch).toMatchObject(sampleWithNewData);
      });

      it('should return NewMetaSavedSearch for empty MetaSavedSearch initial value', () => {
        const formGroup = service.createMetaSavedSearchFormGroup();

        const metaSavedSearch = service.getMetaSavedSearch(formGroup) as any;

        expect(metaSavedSearch).toMatchObject({});
      });

      it('should return IMetaSavedSearch', () => {
        const formGroup = service.createMetaSavedSearchFormGroup(sampleWithRequiredData);

        const metaSavedSearch = service.getMetaSavedSearch(formGroup) as any;

        expect(metaSavedSearch).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMetaSavedSearch should not enable id FormControl', () => {
        const formGroup = service.createMetaSavedSearchFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMetaSavedSearch should disable id FormControl', () => {
        const formGroup = service.createMetaSavedSearchFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
