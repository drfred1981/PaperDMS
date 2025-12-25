import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDocumentSimilarity } from '../document-similarity.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-similarity.test-samples';

import { DocumentSimilarityService, RestDocumentSimilarity } from './document-similarity.service';

const requireRestSample: RestDocumentSimilarity = {
  ...sampleWithRequiredData,
  computedDate: sampleWithRequiredData.computedDate?.toJSON(),
  reviewedDate: sampleWithRequiredData.reviewedDate?.toJSON(),
};

describe('DocumentSimilarity Service', () => {
  let service: DocumentSimilarityService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentSimilarity | IDocumentSimilarity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentSimilarityService);
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

    it('should create a DocumentSimilarity', () => {
      const documentSimilarity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentSimilarity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentSimilarity', () => {
      const documentSimilarity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentSimilarity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentSimilarity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentSimilarity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentSimilarity', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDocumentSimilarityToCollectionIfMissing', () => {
      it('should add a DocumentSimilarity to an empty array', () => {
        const documentSimilarity: IDocumentSimilarity = sampleWithRequiredData;
        expectedResult = service.addDocumentSimilarityToCollectionIfMissing([], documentSimilarity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentSimilarity);
      });

      it('should not add a DocumentSimilarity to an array that contains it', () => {
        const documentSimilarity: IDocumentSimilarity = sampleWithRequiredData;
        const documentSimilarityCollection: IDocumentSimilarity[] = [
          {
            ...documentSimilarity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentSimilarityToCollectionIfMissing(documentSimilarityCollection, documentSimilarity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentSimilarity to an array that doesn't contain it", () => {
        const documentSimilarity: IDocumentSimilarity = sampleWithRequiredData;
        const documentSimilarityCollection: IDocumentSimilarity[] = [sampleWithPartialData];
        expectedResult = service.addDocumentSimilarityToCollectionIfMissing(documentSimilarityCollection, documentSimilarity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentSimilarity);
      });

      it('should add only unique DocumentSimilarity to an array', () => {
        const documentSimilarityArray: IDocumentSimilarity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentSimilarityCollection: IDocumentSimilarity[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentSimilarityToCollectionIfMissing(documentSimilarityCollection, ...documentSimilarityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentSimilarity: IDocumentSimilarity = sampleWithRequiredData;
        const documentSimilarity2: IDocumentSimilarity = sampleWithPartialData;
        expectedResult = service.addDocumentSimilarityToCollectionIfMissing([], documentSimilarity, documentSimilarity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentSimilarity);
        expect(expectedResult).toContain(documentSimilarity2);
      });

      it('should accept null and undefined values', () => {
        const documentSimilarity: IDocumentSimilarity = sampleWithRequiredData;
        expectedResult = service.addDocumentSimilarityToCollectionIfMissing([], null, documentSimilarity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentSimilarity);
      });

      it('should return initial array if no DocumentSimilarity is added', () => {
        const documentSimilarityCollection: IDocumentSimilarity[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentSimilarityToCollectionIfMissing(documentSimilarityCollection, undefined, null);
        expect(expectedResult).toEqual(documentSimilarityCollection);
      });
    });

    describe('compareDocumentSimilarity', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentSimilarity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18427 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentSimilarity(entity1, entity2);
        const compareResult2 = service.compareDocumentSimilarity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18427 };
        const entity2 = { id: 17777 };

        const compareResult1 = service.compareDocumentSimilarity(entity1, entity2);
        const compareResult2 = service.compareDocumentSimilarity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 18427 };
        const entity2 = { id: 18427 };

        const compareResult1 = service.compareDocumentSimilarity(entity1, entity2);
        const compareResult2 = service.compareDocumentSimilarity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
