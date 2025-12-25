import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../search-query.test-samples';

import { SearchQueryFormService } from './search-query-form.service';

describe('SearchQuery Form Service', () => {
  let service: SearchQueryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchQueryFormService);
  });

  describe('Service methods', () => {
    describe('createSearchQueryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSearchQueryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            query: expect.any(Object),
            filters: expect.any(Object),
            resultCount: expect.any(Object),
            executionTime: expect.any(Object),
            userId: expect.any(Object),
            searchDate: expect.any(Object),
            isRelevant: expect.any(Object),
          }),
        );
      });

      it('passing ISearchQuery should create a new form with FormGroup', () => {
        const formGroup = service.createSearchQueryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            query: expect.any(Object),
            filters: expect.any(Object),
            resultCount: expect.any(Object),
            executionTime: expect.any(Object),
            userId: expect.any(Object),
            searchDate: expect.any(Object),
            isRelevant: expect.any(Object),
          }),
        );
      });
    });

    describe('getSearchQuery', () => {
      it('should return NewSearchQuery for default SearchQuery initial value', () => {
        const formGroup = service.createSearchQueryFormGroup(sampleWithNewData);

        const searchQuery = service.getSearchQuery(formGroup) as any;

        expect(searchQuery).toMatchObject(sampleWithNewData);
      });

      it('should return NewSearchQuery for empty SearchQuery initial value', () => {
        const formGroup = service.createSearchQueryFormGroup();

        const searchQuery = service.getSearchQuery(formGroup) as any;

        expect(searchQuery).toMatchObject({});
      });

      it('should return ISearchQuery', () => {
        const formGroup = service.createSearchQueryFormGroup(sampleWithRequiredData);

        const searchQuery = service.getSearchQuery(formGroup) as any;

        expect(searchQuery).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISearchQuery should not enable id FormControl', () => {
        const formGroup = service.createSearchQueryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSearchQuery should disable id FormControl', () => {
        const formGroup = service.createSearchQueryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
