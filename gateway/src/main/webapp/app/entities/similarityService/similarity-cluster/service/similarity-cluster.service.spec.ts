import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ISimilarityCluster } from '../similarity-cluster.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../similarity-cluster.test-samples';

import { RestSimilarityCluster, SimilarityClusterService } from './similarity-cluster.service';

const requireRestSample: RestSimilarityCluster = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastUpdated: sampleWithRequiredData.lastUpdated?.toJSON(),
};

describe('SimilarityCluster Service', () => {
  let service: SimilarityClusterService;
  let httpMock: HttpTestingController;
  let expectedResult: ISimilarityCluster | ISimilarityCluster[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SimilarityClusterService);
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

    it('should create a SimilarityCluster', () => {
      const similarityCluster = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(similarityCluster).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SimilarityCluster', () => {
      const similarityCluster = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(similarityCluster).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SimilarityCluster', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SimilarityCluster', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SimilarityCluster', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSimilarityClusterToCollectionIfMissing', () => {
      it('should add a SimilarityCluster to an empty array', () => {
        const similarityCluster: ISimilarityCluster = sampleWithRequiredData;
        expectedResult = service.addSimilarityClusterToCollectionIfMissing([], similarityCluster);
        expect(expectedResult).toEqual([similarityCluster]);
      });

      it('should not add a SimilarityCluster to an array that contains it', () => {
        const similarityCluster: ISimilarityCluster = sampleWithRequiredData;
        const similarityClusterCollection: ISimilarityCluster[] = [
          {
            ...similarityCluster,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSimilarityClusterToCollectionIfMissing(similarityClusterCollection, similarityCluster);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SimilarityCluster to an array that doesn't contain it", () => {
        const similarityCluster: ISimilarityCluster = sampleWithRequiredData;
        const similarityClusterCollection: ISimilarityCluster[] = [sampleWithPartialData];
        expectedResult = service.addSimilarityClusterToCollectionIfMissing(similarityClusterCollection, similarityCluster);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(similarityCluster);
      });

      it('should add only unique SimilarityCluster to an array', () => {
        const similarityClusterArray: ISimilarityCluster[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const similarityClusterCollection: ISimilarityCluster[] = [sampleWithRequiredData];
        expectedResult = service.addSimilarityClusterToCollectionIfMissing(similarityClusterCollection, ...similarityClusterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const similarityCluster: ISimilarityCluster = sampleWithRequiredData;
        const similarityCluster2: ISimilarityCluster = sampleWithPartialData;
        expectedResult = service.addSimilarityClusterToCollectionIfMissing([], similarityCluster, similarityCluster2);
        expect(expectedResult).toEqual([similarityCluster, similarityCluster2]);
      });

      it('should accept null and undefined values', () => {
        const similarityCluster: ISimilarityCluster = sampleWithRequiredData;
        expectedResult = service.addSimilarityClusterToCollectionIfMissing([], null, similarityCluster, undefined);
        expect(expectedResult).toEqual([similarityCluster]);
      });

      it('should return initial array if no SimilarityCluster is added', () => {
        const similarityClusterCollection: ISimilarityCluster[] = [sampleWithRequiredData];
        expectedResult = service.addSimilarityClusterToCollectionIfMissing(similarityClusterCollection, undefined, null);
        expect(expectedResult).toEqual(similarityClusterCollection);
      });
    });

    describe('compareSimilarityCluster', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSimilarityCluster(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 27919 };
        const entity2 = null;

        const compareResult1 = service.compareSimilarityCluster(entity1, entity2);
        const compareResult2 = service.compareSimilarityCluster(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 27919 };
        const entity2 = { id: 15350 };

        const compareResult1 = service.compareSimilarityCluster(entity1, entity2);
        const compareResult2 = service.compareSimilarityCluster(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 27919 };
        const entity2 = { id: 27919 };

        const compareResult1 = service.compareSimilarityCluster(entity1, entity2);
        const compareResult2 = service.compareSimilarityCluster(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
