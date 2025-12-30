import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISimilarityDocumentFingerprint } from '../similarity-document-fingerprint.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../similarity-document-fingerprint.test-samples';

import { RestSimilarityDocumentFingerprint, SimilarityDocumentFingerprintService } from './similarity-document-fingerprint.service';

const requireRestSample: RestSimilarityDocumentFingerprint = {
  ...sampleWithRequiredData,
  computedDate: sampleWithRequiredData.computedDate?.toJSON(),
  lastUpdated: sampleWithRequiredData.lastUpdated?.toJSON(),
};

describe('SimilarityDocumentFingerprint Service', () => {
  let service: SimilarityDocumentFingerprintService;
  let httpMock: HttpTestingController;
  let expectedResult: ISimilarityDocumentFingerprint | ISimilarityDocumentFingerprint[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SimilarityDocumentFingerprintService);
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

    it('should create a SimilarityDocumentFingerprint', () => {
      const similarityDocumentFingerprint = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(similarityDocumentFingerprint).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SimilarityDocumentFingerprint', () => {
      const similarityDocumentFingerprint = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(similarityDocumentFingerprint).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SimilarityDocumentFingerprint', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SimilarityDocumentFingerprint', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SimilarityDocumentFingerprint', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a SimilarityDocumentFingerprint', () => {
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

    describe('addSimilarityDocumentFingerprintToCollectionIfMissing', () => {
      it('should add a SimilarityDocumentFingerprint to an empty array', () => {
        const similarityDocumentFingerprint: ISimilarityDocumentFingerprint = sampleWithRequiredData;
        expectedResult = service.addSimilarityDocumentFingerprintToCollectionIfMissing([], similarityDocumentFingerprint);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(similarityDocumentFingerprint);
      });

      it('should not add a SimilarityDocumentFingerprint to an array that contains it', () => {
        const similarityDocumentFingerprint: ISimilarityDocumentFingerprint = sampleWithRequiredData;
        const similarityDocumentFingerprintCollection: ISimilarityDocumentFingerprint[] = [
          {
            ...similarityDocumentFingerprint,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSimilarityDocumentFingerprintToCollectionIfMissing(
          similarityDocumentFingerprintCollection,
          similarityDocumentFingerprint,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SimilarityDocumentFingerprint to an array that doesn't contain it", () => {
        const similarityDocumentFingerprint: ISimilarityDocumentFingerprint = sampleWithRequiredData;
        const similarityDocumentFingerprintCollection: ISimilarityDocumentFingerprint[] = [sampleWithPartialData];
        expectedResult = service.addSimilarityDocumentFingerprintToCollectionIfMissing(
          similarityDocumentFingerprintCollection,
          similarityDocumentFingerprint,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(similarityDocumentFingerprint);
      });

      it('should add only unique SimilarityDocumentFingerprint to an array', () => {
        const similarityDocumentFingerprintArray: ISimilarityDocumentFingerprint[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const similarityDocumentFingerprintCollection: ISimilarityDocumentFingerprint[] = [sampleWithRequiredData];
        expectedResult = service.addSimilarityDocumentFingerprintToCollectionIfMissing(
          similarityDocumentFingerprintCollection,
          ...similarityDocumentFingerprintArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const similarityDocumentFingerprint: ISimilarityDocumentFingerprint = sampleWithRequiredData;
        const similarityDocumentFingerprint2: ISimilarityDocumentFingerprint = sampleWithPartialData;
        expectedResult = service.addSimilarityDocumentFingerprintToCollectionIfMissing(
          [],
          similarityDocumentFingerprint,
          similarityDocumentFingerprint2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(similarityDocumentFingerprint);
        expect(expectedResult).toContain(similarityDocumentFingerprint2);
      });

      it('should accept null and undefined values', () => {
        const similarityDocumentFingerprint: ISimilarityDocumentFingerprint = sampleWithRequiredData;
        expectedResult = service.addSimilarityDocumentFingerprintToCollectionIfMissing([], null, similarityDocumentFingerprint, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(similarityDocumentFingerprint);
      });

      it('should return initial array if no SimilarityDocumentFingerprint is added', () => {
        const similarityDocumentFingerprintCollection: ISimilarityDocumentFingerprint[] = [sampleWithRequiredData];
        expectedResult = service.addSimilarityDocumentFingerprintToCollectionIfMissing(
          similarityDocumentFingerprintCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(similarityDocumentFingerprintCollection);
      });
    });

    describe('compareSimilarityDocumentFingerprint', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSimilarityDocumentFingerprint(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 22662 };
        const entity2 = null;

        const compareResult1 = service.compareSimilarityDocumentFingerprint(entity1, entity2);
        const compareResult2 = service.compareSimilarityDocumentFingerprint(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 22662 };
        const entity2 = { id: 14373 };

        const compareResult1 = service.compareSimilarityDocumentFingerprint(entity1, entity2);
        const compareResult2 = service.compareSimilarityDocumentFingerprint(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 22662 };
        const entity2 = { id: 22662 };

        const compareResult1 = service.compareSimilarityDocumentFingerprint(entity1, entity2);
        const compareResult2 = service.compareSimilarityDocumentFingerprint(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
