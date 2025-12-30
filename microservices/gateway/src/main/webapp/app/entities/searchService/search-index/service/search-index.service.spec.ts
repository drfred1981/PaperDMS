import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISearchIndex } from '../search-index.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../search-index.test-samples';

import { RestSearchIndex, SearchIndexService } from './search-index.service';

const requireRestSample: RestSearchIndex = {
  ...sampleWithRequiredData,
  indexedDate: sampleWithRequiredData.indexedDate?.toJSON(),
  lastUpdated: sampleWithRequiredData.lastUpdated?.toJSON(),
};

describe('SearchIndex Service', () => {
  let service: SearchIndexService;
  let httpMock: HttpTestingController;
  let expectedResult: ISearchIndex | ISearchIndex[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SearchIndexService);
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

    it('should create a SearchIndex', () => {
      const searchIndex = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(searchIndex).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SearchIndex', () => {
      const searchIndex = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(searchIndex).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SearchIndex', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SearchIndex', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SearchIndex', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a SearchIndex', () => {
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

    describe('addSearchIndexToCollectionIfMissing', () => {
      it('should add a SearchIndex to an empty array', () => {
        const searchIndex: ISearchIndex = sampleWithRequiredData;
        expectedResult = service.addSearchIndexToCollectionIfMissing([], searchIndex);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(searchIndex);
      });

      it('should not add a SearchIndex to an array that contains it', () => {
        const searchIndex: ISearchIndex = sampleWithRequiredData;
        const searchIndexCollection: ISearchIndex[] = [
          {
            ...searchIndex,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSearchIndexToCollectionIfMissing(searchIndexCollection, searchIndex);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SearchIndex to an array that doesn't contain it", () => {
        const searchIndex: ISearchIndex = sampleWithRequiredData;
        const searchIndexCollection: ISearchIndex[] = [sampleWithPartialData];
        expectedResult = service.addSearchIndexToCollectionIfMissing(searchIndexCollection, searchIndex);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(searchIndex);
      });

      it('should add only unique SearchIndex to an array', () => {
        const searchIndexArray: ISearchIndex[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const searchIndexCollection: ISearchIndex[] = [sampleWithRequiredData];
        expectedResult = service.addSearchIndexToCollectionIfMissing(searchIndexCollection, ...searchIndexArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const searchIndex: ISearchIndex = sampleWithRequiredData;
        const searchIndex2: ISearchIndex = sampleWithPartialData;
        expectedResult = service.addSearchIndexToCollectionIfMissing([], searchIndex, searchIndex2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(searchIndex);
        expect(expectedResult).toContain(searchIndex2);
      });

      it('should accept null and undefined values', () => {
        const searchIndex: ISearchIndex = sampleWithRequiredData;
        expectedResult = service.addSearchIndexToCollectionIfMissing([], null, searchIndex, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(searchIndex);
      });

      it('should return initial array if no SearchIndex is added', () => {
        const searchIndexCollection: ISearchIndex[] = [sampleWithRequiredData];
        expectedResult = service.addSearchIndexToCollectionIfMissing(searchIndexCollection, undefined, null);
        expect(expectedResult).toEqual(searchIndexCollection);
      });
    });

    describe('compareSearchIndex', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSearchIndex(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18426 };
        const entity2 = null;

        const compareResult1 = service.compareSearchIndex(entity1, entity2);
        const compareResult2 = service.compareSearchIndex(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18426 };
        const entity2 = { id: 27227 };

        const compareResult1 = service.compareSearchIndex(entity1, entity2);
        const compareResult2 = service.compareSearchIndex(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 18426 };
        const entity2 = { id: 18426 };

        const compareResult1 = service.compareSearchIndex(entity1, entity2);
        const compareResult2 = service.compareSearchIndex(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
