import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISimilarityDocumentComparison } from '../similarity-document-comparison.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../similarity-document-comparison.test-samples';

import { RestSimilarityDocumentComparison, SimilarityDocumentComparisonService } from './similarity-document-comparison.service';

const requireRestSample: RestSimilarityDocumentComparison = {
  ...sampleWithRequiredData,
  computedDate: sampleWithRequiredData.computedDate?.toJSON(),
  reviewedDate: sampleWithRequiredData.reviewedDate?.toJSON(),
};

describe('SimilarityDocumentComparison Service', () => {
  let service: SimilarityDocumentComparisonService;
  let httpMock: HttpTestingController;
  let expectedResult: ISimilarityDocumentComparison | ISimilarityDocumentComparison[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SimilarityDocumentComparisonService);
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

    it('should create a SimilarityDocumentComparison', () => {
      const similarityDocumentComparison = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(similarityDocumentComparison).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SimilarityDocumentComparison', () => {
      const similarityDocumentComparison = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(similarityDocumentComparison).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SimilarityDocumentComparison', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SimilarityDocumentComparison', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SimilarityDocumentComparison', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a SimilarityDocumentComparison', () => {
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

    describe('addSimilarityDocumentComparisonToCollectionIfMissing', () => {
      it('should add a SimilarityDocumentComparison to an empty array', () => {
        const similarityDocumentComparison: ISimilarityDocumentComparison = sampleWithRequiredData;
        expectedResult = service.addSimilarityDocumentComparisonToCollectionIfMissing([], similarityDocumentComparison);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(similarityDocumentComparison);
      });

      it('should not add a SimilarityDocumentComparison to an array that contains it', () => {
        const similarityDocumentComparison: ISimilarityDocumentComparison = sampleWithRequiredData;
        const similarityDocumentComparisonCollection: ISimilarityDocumentComparison[] = [
          {
            ...similarityDocumentComparison,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSimilarityDocumentComparisonToCollectionIfMissing(
          similarityDocumentComparisonCollection,
          similarityDocumentComparison,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SimilarityDocumentComparison to an array that doesn't contain it", () => {
        const similarityDocumentComparison: ISimilarityDocumentComparison = sampleWithRequiredData;
        const similarityDocumentComparisonCollection: ISimilarityDocumentComparison[] = [sampleWithPartialData];
        expectedResult = service.addSimilarityDocumentComparisonToCollectionIfMissing(
          similarityDocumentComparisonCollection,
          similarityDocumentComparison,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(similarityDocumentComparison);
      });

      it('should add only unique SimilarityDocumentComparison to an array', () => {
        const similarityDocumentComparisonArray: ISimilarityDocumentComparison[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const similarityDocumentComparisonCollection: ISimilarityDocumentComparison[] = [sampleWithRequiredData];
        expectedResult = service.addSimilarityDocumentComparisonToCollectionIfMissing(
          similarityDocumentComparisonCollection,
          ...similarityDocumentComparisonArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const similarityDocumentComparison: ISimilarityDocumentComparison = sampleWithRequiredData;
        const similarityDocumentComparison2: ISimilarityDocumentComparison = sampleWithPartialData;
        expectedResult = service.addSimilarityDocumentComparisonToCollectionIfMissing(
          [],
          similarityDocumentComparison,
          similarityDocumentComparison2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(similarityDocumentComparison);
        expect(expectedResult).toContain(similarityDocumentComparison2);
      });

      it('should accept null and undefined values', () => {
        const similarityDocumentComparison: ISimilarityDocumentComparison = sampleWithRequiredData;
        expectedResult = service.addSimilarityDocumentComparisonToCollectionIfMissing([], null, similarityDocumentComparison, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(similarityDocumentComparison);
      });

      it('should return initial array if no SimilarityDocumentComparison is added', () => {
        const similarityDocumentComparisonCollection: ISimilarityDocumentComparison[] = [sampleWithRequiredData];
        expectedResult = service.addSimilarityDocumentComparisonToCollectionIfMissing(
          similarityDocumentComparisonCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(similarityDocumentComparisonCollection);
      });
    });

    describe('compareSimilarityDocumentComparison', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSimilarityDocumentComparison(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8038 };
        const entity2 = null;

        const compareResult1 = service.compareSimilarityDocumentComparison(entity1, entity2);
        const compareResult2 = service.compareSimilarityDocumentComparison(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8038 };
        const entity2 = { id: 3431 };

        const compareResult1 = service.compareSimilarityDocumentComparison(entity1, entity2);
        const compareResult2 = service.compareSimilarityDocumentComparison(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 8038 };
        const entity2 = { id: 8038 };

        const compareResult1 = service.compareSimilarityDocumentComparison(entity1, entity2);
        const compareResult2 = service.compareSimilarityDocumentComparison(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
