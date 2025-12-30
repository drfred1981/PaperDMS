import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDocumentAudit } from '../document-audit.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-audit.test-samples';

import { DocumentAuditService, RestDocumentAudit } from './document-audit.service';

const requireRestSample: RestDocumentAudit = {
  ...sampleWithRequiredData,
  actionDate: sampleWithRequiredData.actionDate?.toJSON(),
};

describe('DocumentAudit Service', () => {
  let service: DocumentAuditService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentAudit | IDocumentAudit[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentAuditService);
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

    it('should create a DocumentAudit', () => {
      const documentAudit = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentAudit).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentAudit', () => {
      const documentAudit = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentAudit).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentAudit', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentAudit', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentAudit', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a DocumentAudit', () => {
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

    describe('addDocumentAuditToCollectionIfMissing', () => {
      it('should add a DocumentAudit to an empty array', () => {
        const documentAudit: IDocumentAudit = sampleWithRequiredData;
        expectedResult = service.addDocumentAuditToCollectionIfMissing([], documentAudit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentAudit);
      });

      it('should not add a DocumentAudit to an array that contains it', () => {
        const documentAudit: IDocumentAudit = sampleWithRequiredData;
        const documentAuditCollection: IDocumentAudit[] = [
          {
            ...documentAudit,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentAuditToCollectionIfMissing(documentAuditCollection, documentAudit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentAudit to an array that doesn't contain it", () => {
        const documentAudit: IDocumentAudit = sampleWithRequiredData;
        const documentAuditCollection: IDocumentAudit[] = [sampleWithPartialData];
        expectedResult = service.addDocumentAuditToCollectionIfMissing(documentAuditCollection, documentAudit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentAudit);
      });

      it('should add only unique DocumentAudit to an array', () => {
        const documentAuditArray: IDocumentAudit[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentAuditCollection: IDocumentAudit[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentAuditToCollectionIfMissing(documentAuditCollection, ...documentAuditArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentAudit: IDocumentAudit = sampleWithRequiredData;
        const documentAudit2: IDocumentAudit = sampleWithPartialData;
        expectedResult = service.addDocumentAuditToCollectionIfMissing([], documentAudit, documentAudit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentAudit);
        expect(expectedResult).toContain(documentAudit2);
      });

      it('should accept null and undefined values', () => {
        const documentAudit: IDocumentAudit = sampleWithRequiredData;
        expectedResult = service.addDocumentAuditToCollectionIfMissing([], null, documentAudit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentAudit);
      });

      it('should return initial array if no DocumentAudit is added', () => {
        const documentAuditCollection: IDocumentAudit[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentAuditToCollectionIfMissing(documentAuditCollection, undefined, null);
        expect(expectedResult).toEqual(documentAuditCollection);
      });
    });

    describe('compareDocumentAudit', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentAudit(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 12096 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentAudit(entity1, entity2);
        const compareResult2 = service.compareDocumentAudit(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 12096 };
        const entity2 = { id: 5164 };

        const compareResult1 = service.compareDocumentAudit(entity1, entity2);
        const compareResult2 = service.compareDocumentAudit(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 12096 };
        const entity2 = { id: 12096 };

        const compareResult1 = service.compareDocumentAudit(entity1, entity2);
        const compareResult2 = service.compareDocumentAudit(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
