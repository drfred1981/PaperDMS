import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMetaSavedSearch } from '../meta-saved-search.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../meta-saved-search.test-samples';

import { MetaSavedSearchService, RestMetaSavedSearch } from './meta-saved-search.service';

const requireRestSample: RestMetaSavedSearch = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('MetaSavedSearch Service', () => {
  let service: MetaSavedSearchService;
  let httpMock: HttpTestingController;
  let expectedResult: IMetaSavedSearch | IMetaSavedSearch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MetaSavedSearchService);
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

    it('should create a MetaSavedSearch', () => {
      const metaSavedSearch = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(metaSavedSearch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MetaSavedSearch', () => {
      const metaSavedSearch = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(metaSavedSearch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MetaSavedSearch', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MetaSavedSearch', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MetaSavedSearch', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MetaSavedSearch', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addMetaSavedSearchToCollectionIfMissing', () => {
      it('should add a MetaSavedSearch to an empty array', () => {
        const metaSavedSearch: IMetaSavedSearch = sampleWithRequiredData;
        expectedResult = service.addMetaSavedSearchToCollectionIfMissing([], metaSavedSearch);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaSavedSearch);
      });

      it('should not add a MetaSavedSearch to an array that contains it', () => {
        const metaSavedSearch: IMetaSavedSearch = sampleWithRequiredData;
        const metaSavedSearchCollection: IMetaSavedSearch[] = [
          {
            ...metaSavedSearch,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMetaSavedSearchToCollectionIfMissing(metaSavedSearchCollection, metaSavedSearch);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MetaSavedSearch to an array that doesn't contain it", () => {
        const metaSavedSearch: IMetaSavedSearch = sampleWithRequiredData;
        const metaSavedSearchCollection: IMetaSavedSearch[] = [sampleWithPartialData];
        expectedResult = service.addMetaSavedSearchToCollectionIfMissing(metaSavedSearchCollection, metaSavedSearch);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaSavedSearch);
      });

      it('should add only unique MetaSavedSearch to an array', () => {
        const metaSavedSearchArray: IMetaSavedSearch[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const metaSavedSearchCollection: IMetaSavedSearch[] = [sampleWithRequiredData];
        expectedResult = service.addMetaSavedSearchToCollectionIfMissing(metaSavedSearchCollection, ...metaSavedSearchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const metaSavedSearch: IMetaSavedSearch = sampleWithRequiredData;
        const metaSavedSearch2: IMetaSavedSearch = sampleWithPartialData;
        expectedResult = service.addMetaSavedSearchToCollectionIfMissing([], metaSavedSearch, metaSavedSearch2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaSavedSearch);
        expect(expectedResult).toContain(metaSavedSearch2);
      });

      it('should accept null and undefined values', () => {
        const metaSavedSearch: IMetaSavedSearch = sampleWithRequiredData;
        expectedResult = service.addMetaSavedSearchToCollectionIfMissing([], null, metaSavedSearch, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaSavedSearch);
      });

      it('should return initial array if no MetaSavedSearch is added', () => {
        const metaSavedSearchCollection: IMetaSavedSearch[] = [sampleWithRequiredData];
        expectedResult = service.addMetaSavedSearchToCollectionIfMissing(metaSavedSearchCollection, undefined, null);
        expect(expectedResult).toEqual(metaSavedSearchCollection);
      });
    });

    describe('compareMetaSavedSearch', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMetaSavedSearch(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 619 };
        const entity2 = null;

        const compareResult1 = service.compareMetaSavedSearch(entity1, entity2);
        const compareResult2 = service.compareMetaSavedSearch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 619 };
        const entity2 = { id: 26353 };

        const compareResult1 = service.compareMetaSavedSearch(entity1, entity2);
        const compareResult2 = service.compareMetaSavedSearch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 619 };
        const entity2 = { id: 619 };

        const compareResult1 = service.compareMetaSavedSearch(entity1, entity2);
        const compareResult2 = service.compareMetaSavedSearch(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
