import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IAiCache } from '../ai-cache.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ai-cache.test-samples';

import { AiCacheService, RestAiCache } from './ai-cache.service';

const requireRestSample: RestAiCache = {
  ...sampleWithRequiredData,
  lastAccessDate: sampleWithRequiredData.lastAccessDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  expirationDate: sampleWithRequiredData.expirationDate?.toJSON(),
};

describe('AiCache Service', () => {
  let service: AiCacheService;
  let httpMock: HttpTestingController;
  let expectedResult: IAiCache | IAiCache[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AiCacheService);
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

    it('should create a AiCache', () => {
      const aiCache = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aiCache).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AiCache', () => {
      const aiCache = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aiCache).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AiCache', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AiCache', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AiCache', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAiCacheToCollectionIfMissing', () => {
      it('should add a AiCache to an empty array', () => {
        const aiCache: IAiCache = sampleWithRequiredData;
        expectedResult = service.addAiCacheToCollectionIfMissing([], aiCache);
        expect(expectedResult).toEqual([aiCache]);
      });

      it('should not add a AiCache to an array that contains it', () => {
        const aiCache: IAiCache = sampleWithRequiredData;
        const aiCacheCollection: IAiCache[] = [
          {
            ...aiCache,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAiCacheToCollectionIfMissing(aiCacheCollection, aiCache);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AiCache to an array that doesn't contain it", () => {
        const aiCache: IAiCache = sampleWithRequiredData;
        const aiCacheCollection: IAiCache[] = [sampleWithPartialData];
        expectedResult = service.addAiCacheToCollectionIfMissing(aiCacheCollection, aiCache);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aiCache);
      });

      it('should add only unique AiCache to an array', () => {
        const aiCacheArray: IAiCache[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aiCacheCollection: IAiCache[] = [sampleWithRequiredData];
        expectedResult = service.addAiCacheToCollectionIfMissing(aiCacheCollection, ...aiCacheArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aiCache: IAiCache = sampleWithRequiredData;
        const aiCache2: IAiCache = sampleWithPartialData;
        expectedResult = service.addAiCacheToCollectionIfMissing([], aiCache, aiCache2);
        expect(expectedResult).toEqual([aiCache, aiCache2]);
      });

      it('should accept null and undefined values', () => {
        const aiCache: IAiCache = sampleWithRequiredData;
        expectedResult = service.addAiCacheToCollectionIfMissing([], null, aiCache, undefined);
        expect(expectedResult).toEqual([aiCache]);
      });

      it('should return initial array if no AiCache is added', () => {
        const aiCacheCollection: IAiCache[] = [sampleWithRequiredData];
        expectedResult = service.addAiCacheToCollectionIfMissing(aiCacheCollection, undefined, null);
        expect(expectedResult).toEqual(aiCacheCollection);
      });
    });

    describe('compareAiCache', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAiCache(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 29414 };
        const entity2 = null;

        const compareResult1 = service.compareAiCache(entity1, entity2);
        const compareResult2 = service.compareAiCache(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 29414 };
        const entity2 = { id: 16798 };

        const compareResult1 = service.compareAiCache(entity1, entity2);
        const compareResult2 = service.compareAiCache(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 29414 };
        const entity2 = { id: 29414 };

        const compareResult1 = service.compareAiCache(entity1, entity2);
        const compareResult2 = service.compareAiCache(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
