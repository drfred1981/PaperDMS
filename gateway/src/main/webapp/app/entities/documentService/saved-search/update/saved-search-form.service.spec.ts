import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../saved-search.test-samples';

import { SavedSearchFormService } from './saved-search-form.service';

describe('SavedSearch Form Service', () => {
  let service: SavedSearchFormService;

  beforeEach(() => {
    service = TestBed.inject(SavedSearchFormService);
  });

  describe('Service methods', () => {
    describe('createSavedSearchFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSavedSearchFormGroup();

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

      it('passing ISavedSearch should create a new form with FormGroup', () => {
        const formGroup = service.createSavedSearchFormGroup(sampleWithRequiredData);

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

    describe('getSavedSearch', () => {
      it('should return NewSavedSearch for default SavedSearch initial value', () => {
        const formGroup = service.createSavedSearchFormGroup(sampleWithNewData);

        const savedSearch = service.getSavedSearch(formGroup);

        expect(savedSearch).toMatchObject(sampleWithNewData);
      });

      it('should return NewSavedSearch for empty SavedSearch initial value', () => {
        const formGroup = service.createSavedSearchFormGroup();

        const savedSearch = service.getSavedSearch(formGroup);

        expect(savedSearch).toMatchObject({});
      });

      it('should return ISavedSearch', () => {
        const formGroup = service.createSavedSearchFormGroup(sampleWithRequiredData);

        const savedSearch = service.getSavedSearch(formGroup);

        expect(savedSearch).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISavedSearch should not enable id FormControl', () => {
        const formGroup = service.createSavedSearchFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSavedSearch should disable id FormControl', () => {
        const formGroup = service.createSavedSearchFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
