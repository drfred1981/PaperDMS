import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ISavedSearch } from '../saved-search.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../saved-search.test-samples';

import { RestSavedSearch, SavedSearchService } from './saved-search.service';

const requireRestSample: RestSavedSearch = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('SavedSearch Service', () => {
  let service: SavedSearchService;
  let httpMock: HttpTestingController;
  let expectedResult: ISavedSearch | ISavedSearch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SavedSearchService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SavedSearch', () => {
      const savedSearch = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(savedSearch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SavedSearch', () => {
      const savedSearch = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(savedSearch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SavedSearch', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SavedSearch', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SavedSearch', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSavedSearchToCollectionIfMissing', () => {
      it('should add a SavedSearch to an empty array', () => {
        const savedSearch: ISavedSearch = sampleWithRequiredData;
        expectedResult = service.addSavedSearchToCollectionIfMissing([], savedSearch);
        expect(expectedResult).toEqual([savedSearch]);
      });

      it('should not add a SavedSearch to an array that contains it', () => {
        const savedSearch: ISavedSearch = sampleWithRequiredData;
        const savedSearchCollection: ISavedSearch[] = [
          {
            ...savedSearch,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSavedSearchToCollectionIfMissing(savedSearchCollection, savedSearch);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SavedSearch to an array that doesn't contain it", () => {
        const savedSearch: ISavedSearch = sampleWithRequiredData;
        const savedSearchCollection: ISavedSearch[] = [sampleWithPartialData];
        expectedResult = service.addSavedSearchToCollectionIfMissing(savedSearchCollection, savedSearch);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(savedSearch);
      });

      it('should add only unique SavedSearch to an array', () => {
        const savedSearchArray: ISavedSearch[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const savedSearchCollection: ISavedSearch[] = [sampleWithRequiredData];
        expectedResult = service.addSavedSearchToCollectionIfMissing(savedSearchCollection, ...savedSearchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const savedSearch: ISavedSearch = sampleWithRequiredData;
        const savedSearch2: ISavedSearch = sampleWithPartialData;
        expectedResult = service.addSavedSearchToCollectionIfMissing([], savedSearch, savedSearch2);
        expect(expectedResult).toEqual([savedSearch, savedSearch2]);
      });

      it('should accept null and undefined values', () => {
        const savedSearch: ISavedSearch = sampleWithRequiredData;
        expectedResult = service.addSavedSearchToCollectionIfMissing([], null, savedSearch, undefined);
        expect(expectedResult).toEqual([savedSearch]);
      });

      it('should return initial array if no SavedSearch is added', () => {
        const savedSearchCollection: ISavedSearch[] = [sampleWithRequiredData];
        expectedResult = service.addSavedSearchToCollectionIfMissing(savedSearchCollection, undefined, null);
        expect(expectedResult).toEqual(savedSearchCollection);
      });
    });

    describe('compareSavedSearch', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSavedSearch(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 32351 };
        const entity2 = null;

        const compareResult1 = service.compareSavedSearch(entity1, entity2);
        const compareResult2 = service.compareSavedSearch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 32351 };
        const entity2 = { id: 10324 };

        const compareResult1 = service.compareSavedSearch(entity1, entity2);
        const compareResult2 = service.compareSavedSearch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 32351 };
        const entity2 = { id: 32351 };

        const compareResult1 = service.compareSavedSearch(entity1, entity2);
        const compareResult2 = service.compareSavedSearch(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
