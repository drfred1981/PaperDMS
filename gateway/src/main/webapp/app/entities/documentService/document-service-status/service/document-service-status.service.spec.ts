import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDocumentServiceStatus } from '../document-service-status.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../document-service-status.test-samples';

import { DocumentServiceStatusService, RestDocumentServiceStatus } from './document-service-status.service';

const requireRestSample: RestDocumentServiceStatus = {
  ...sampleWithRequiredData,
  lastProcessedDate: sampleWithRequiredData.lastProcessedDate?.toJSON(),
  processingStartDate: sampleWithRequiredData.processingStartDate?.toJSON(),
  processingEndDate: sampleWithRequiredData.processingEndDate?.toJSON(),
  updatedDate: sampleWithRequiredData.updatedDate?.toJSON(),
};

describe('DocumentServiceStatus Service', () => {
  let service: DocumentServiceStatusService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentServiceStatus | IDocumentServiceStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentServiceStatusService);
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

    it('should create a DocumentServiceStatus', () => {
      const documentServiceStatus = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentServiceStatus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentServiceStatus', () => {
      const documentServiceStatus = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentServiceStatus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentServiceStatus', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentServiceStatus', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentServiceStatus', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a DocumentServiceStatus', () => {
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

    describe('addDocumentServiceStatusToCollectionIfMissing', () => {
      it('should add a DocumentServiceStatus to an empty array', () => {
        const documentServiceStatus: IDocumentServiceStatus = sampleWithRequiredData;
        expectedResult = service.addDocumentServiceStatusToCollectionIfMissing([], documentServiceStatus);
        expect(expectedResult).toEqual([documentServiceStatus]);
      });

      it('should not add a DocumentServiceStatus to an array that contains it', () => {
        const documentServiceStatus: IDocumentServiceStatus = sampleWithRequiredData;
        const documentServiceStatusCollection: IDocumentServiceStatus[] = [
          {
            ...documentServiceStatus,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentServiceStatusToCollectionIfMissing(documentServiceStatusCollection, documentServiceStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentServiceStatus to an array that doesn't contain it", () => {
        const documentServiceStatus: IDocumentServiceStatus = sampleWithRequiredData;
        const documentServiceStatusCollection: IDocumentServiceStatus[] = [sampleWithPartialData];
        expectedResult = service.addDocumentServiceStatusToCollectionIfMissing(documentServiceStatusCollection, documentServiceStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentServiceStatus);
      });

      it('should add only unique DocumentServiceStatus to an array', () => {
        const documentServiceStatusArray: IDocumentServiceStatus[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentServiceStatusCollection: IDocumentServiceStatus[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentServiceStatusToCollectionIfMissing(
          documentServiceStatusCollection,
          ...documentServiceStatusArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentServiceStatus: IDocumentServiceStatus = sampleWithRequiredData;
        const documentServiceStatus2: IDocumentServiceStatus = sampleWithPartialData;
        expectedResult = service.addDocumentServiceStatusToCollectionIfMissing([], documentServiceStatus, documentServiceStatus2);
        expect(expectedResult).toEqual([documentServiceStatus, documentServiceStatus2]);
      });

      it('should accept null and undefined values', () => {
        const documentServiceStatus: IDocumentServiceStatus = sampleWithRequiredData;
        expectedResult = service.addDocumentServiceStatusToCollectionIfMissing([], null, documentServiceStatus, undefined);
        expect(expectedResult).toEqual([documentServiceStatus]);
      });

      it('should return initial array if no DocumentServiceStatus is added', () => {
        const documentServiceStatusCollection: IDocumentServiceStatus[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentServiceStatusToCollectionIfMissing(documentServiceStatusCollection, undefined, null);
        expect(expectedResult).toEqual(documentServiceStatusCollection);
      });
    });

    describe('compareDocumentServiceStatus', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentServiceStatus(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 1543 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentServiceStatus(entity1, entity2);
        const compareResult2 = service.compareDocumentServiceStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 1543 };
        const entity2 = { id: 16256 };

        const compareResult1 = service.compareDocumentServiceStatus(entity1, entity2);
        const compareResult2 = service.compareDocumentServiceStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 1543 };
        const entity2 = { id: 1543 };

        const compareResult1 = service.compareDocumentServiceStatus(entity1, entity2);
        const compareResult2 = service.compareDocumentServiceStatus(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
