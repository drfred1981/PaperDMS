import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../search-index.test-samples';

import { SearchIndexFormService } from './search-index-form.service';

describe('SearchIndex Form Service', () => {
  let service: SearchIndexFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchIndexFormService);
  });

  describe('Service methods', () => {
    describe('createSearchIndexFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSearchIndexFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            indexedContent: expect.any(Object),
            metadata: expect.any(Object),
            tags: expect.any(Object),
            correspondents: expect.any(Object),
            extractedEntities: expect.any(Object),
            indexedDate: expect.any(Object),
            lastUpdated: expect.any(Object),
          }),
        );
      });

      it('passing ISearchIndex should create a new form with FormGroup', () => {
        const formGroup = service.createSearchIndexFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentSha256: expect.any(Object),
            indexedContent: expect.any(Object),
            metadata: expect.any(Object),
            tags: expect.any(Object),
            correspondents: expect.any(Object),
            extractedEntities: expect.any(Object),
            indexedDate: expect.any(Object),
            lastUpdated: expect.any(Object),
          }),
        );
      });
    });

    describe('getSearchIndex', () => {
      it('should return NewSearchIndex for default SearchIndex initial value', () => {
        const formGroup = service.createSearchIndexFormGroup(sampleWithNewData);

        const searchIndex = service.getSearchIndex(formGroup) as any;

        expect(searchIndex).toMatchObject(sampleWithNewData);
      });

      it('should return NewSearchIndex for empty SearchIndex initial value', () => {
        const formGroup = service.createSearchIndexFormGroup();

        const searchIndex = service.getSearchIndex(formGroup) as any;

        expect(searchIndex).toMatchObject({});
      });

      it('should return ISearchIndex', () => {
        const formGroup = service.createSearchIndexFormGroup(sampleWithRequiredData);

        const searchIndex = service.getSearchIndex(formGroup) as any;

        expect(searchIndex).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISearchIndex should not enable id FormControl', () => {
        const formGroup = service.createSearchIndexFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSearchIndex should disable id FormControl', () => {
        const formGroup = service.createSearchIndexFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
