import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../search-facet.test-samples';

import { SearchFacetFormService } from './search-facet-form.service';

describe('SearchFacet Form Service', () => {
  let service: SearchFacetFormService;

  beforeEach(() => {
    service = TestBed.inject(SearchFacetFormService);
  });

  describe('Service methods', () => {
    describe('createSearchFacetFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSearchFacetFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            searchQueryId: expect.any(Object),
            facetName: expect.any(Object),
            facetType: expect.any(Object),
            values: expect.any(Object),
            counts: expect.any(Object),
            searchQuery: expect.any(Object),
          }),
        );
      });

      it('passing ISearchFacet should create a new form with FormGroup', () => {
        const formGroup = service.createSearchFacetFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            searchQueryId: expect.any(Object),
            facetName: expect.any(Object),
            facetType: expect.any(Object),
            values: expect.any(Object),
            counts: expect.any(Object),
            searchQuery: expect.any(Object),
          }),
        );
      });
    });

    describe('getSearchFacet', () => {
      it('should return NewSearchFacet for default SearchFacet initial value', () => {
        const formGroup = service.createSearchFacetFormGroup(sampleWithNewData);

        const searchFacet = service.getSearchFacet(formGroup);

        expect(searchFacet).toMatchObject(sampleWithNewData);
      });

      it('should return NewSearchFacet for empty SearchFacet initial value', () => {
        const formGroup = service.createSearchFacetFormGroup();

        const searchFacet = service.getSearchFacet(formGroup);

        expect(searchFacet).toMatchObject({});
      });

      it('should return ISearchFacet', () => {
        const formGroup = service.createSearchFacetFormGroup(sampleWithRequiredData);

        const searchFacet = service.getSearchFacet(formGroup);

        expect(searchFacet).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISearchFacet should not enable id FormControl', () => {
        const formGroup = service.createSearchFacetFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSearchFacet should disable id FormControl', () => {
        const formGroup = service.createSearchFacetFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
