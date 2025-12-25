import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDocumentFingerprint } from '../document-fingerprint.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-fingerprint.test-samples';

import { DocumentFingerprintService, RestDocumentFingerprint } from './document-fingerprint.service';

const requireRestSample: RestDocumentFingerprint = {
  ...sampleWithRequiredData,
  computedDate: sampleWithRequiredData.computedDate?.toJSON(),
  lastUpdated: sampleWithRequiredData.lastUpdated?.toJSON(),
};

describe('DocumentFingerprint Service', () => {
  let service: DocumentFingerprintService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentFingerprint | IDocumentFingerprint[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentFingerprintService);
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

    it('should create a DocumentFingerprint', () => {
      const documentFingerprint = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentFingerprint).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentFingerprint', () => {
      const documentFingerprint = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentFingerprint).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentFingerprint', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentFingerprint', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentFingerprint', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a DocumentFingerprint', () => {
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

    describe('addDocumentFingerprintToCollectionIfMissing', () => {
      it('should add a DocumentFingerprint to an empty array', () => {
        const documentFingerprint: IDocumentFingerprint = sampleWithRequiredData;
        expectedResult = service.addDocumentFingerprintToCollectionIfMissing([], documentFingerprint);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentFingerprint);
      });

      it('should not add a DocumentFingerprint to an array that contains it', () => {
        const documentFingerprint: IDocumentFingerprint = sampleWithRequiredData;
        const documentFingerprintCollection: IDocumentFingerprint[] = [
          {
            ...documentFingerprint,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentFingerprintToCollectionIfMissing(documentFingerprintCollection, documentFingerprint);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentFingerprint to an array that doesn't contain it", () => {
        const documentFingerprint: IDocumentFingerprint = sampleWithRequiredData;
        const documentFingerprintCollection: IDocumentFingerprint[] = [sampleWithPartialData];
        expectedResult = service.addDocumentFingerprintToCollectionIfMissing(documentFingerprintCollection, documentFingerprint);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentFingerprint);
      });

      it('should add only unique DocumentFingerprint to an array', () => {
        const documentFingerprintArray: IDocumentFingerprint[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentFingerprintCollection: IDocumentFingerprint[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentFingerprintToCollectionIfMissing(documentFingerprintCollection, ...documentFingerprintArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentFingerprint: IDocumentFingerprint = sampleWithRequiredData;
        const documentFingerprint2: IDocumentFingerprint = sampleWithPartialData;
        expectedResult = service.addDocumentFingerprintToCollectionIfMissing([], documentFingerprint, documentFingerprint2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentFingerprint);
        expect(expectedResult).toContain(documentFingerprint2);
      });

      it('should accept null and undefined values', () => {
        const documentFingerprint: IDocumentFingerprint = sampleWithRequiredData;
        expectedResult = service.addDocumentFingerprintToCollectionIfMissing([], null, documentFingerprint, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentFingerprint);
      });

      it('should return initial array if no DocumentFingerprint is added', () => {
        const documentFingerprintCollection: IDocumentFingerprint[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentFingerprintToCollectionIfMissing(documentFingerprintCollection, undefined, null);
        expect(expectedResult).toEqual(documentFingerprintCollection);
      });
    });

    describe('compareDocumentFingerprint', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentFingerprint(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26494 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentFingerprint(entity1, entity2);
        const compareResult2 = service.compareDocumentFingerprint(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26494 };
        const entity2 = { id: 19955 };

        const compareResult1 = service.compareDocumentFingerprint(entity1, entity2);
        const compareResult2 = service.compareDocumentFingerprint(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 26494 };
        const entity2 = { id: 26494 };

        const compareResult1 = service.compareDocumentFingerprint(entity1, entity2);
        const compareResult2 = service.compareDocumentFingerprint(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
