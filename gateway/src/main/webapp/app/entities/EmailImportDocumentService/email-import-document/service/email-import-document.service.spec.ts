import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEmailImportDocument } from '../email-import-document.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../email-import-document.test-samples';

import { EmailImportDocumentService, RestEmailImportDocument } from './email-import-document.service';

const requireRestSample: RestEmailImportDocument = {
  ...sampleWithRequiredData,
  receivedDate: sampleWithRequiredData.receivedDate?.toJSON(),
  processedDate: sampleWithRequiredData.processedDate?.toJSON(),
};

describe('EmailImportDocument Service', () => {
  let service: EmailImportDocumentService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmailImportDocument | IEmailImportDocument[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EmailImportDocumentService);
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

    it('should create a EmailImportDocument', () => {
      const emailImportDocument = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(emailImportDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmailImportDocument', () => {
      const emailImportDocument = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(emailImportDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmailImportDocument', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmailImportDocument', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmailImportDocument', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a EmailImportDocument', () => {
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

    describe('addEmailImportDocumentToCollectionIfMissing', () => {
      it('should add a EmailImportDocument to an empty array', () => {
        const emailImportDocument: IEmailImportDocument = sampleWithRequiredData;
        expectedResult = service.addEmailImportDocumentToCollectionIfMissing([], emailImportDocument);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(emailImportDocument);
      });

      it('should not add a EmailImportDocument to an array that contains it', () => {
        const emailImportDocument: IEmailImportDocument = sampleWithRequiredData;
        const emailImportDocumentCollection: IEmailImportDocument[] = [
          {
            ...emailImportDocument,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmailImportDocumentToCollectionIfMissing(emailImportDocumentCollection, emailImportDocument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmailImportDocument to an array that doesn't contain it", () => {
        const emailImportDocument: IEmailImportDocument = sampleWithRequiredData;
        const emailImportDocumentCollection: IEmailImportDocument[] = [sampleWithPartialData];
        expectedResult = service.addEmailImportDocumentToCollectionIfMissing(emailImportDocumentCollection, emailImportDocument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(emailImportDocument);
      });

      it('should add only unique EmailImportDocument to an array', () => {
        const emailImportDocumentArray: IEmailImportDocument[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const emailImportDocumentCollection: IEmailImportDocument[] = [sampleWithRequiredData];
        expectedResult = service.addEmailImportDocumentToCollectionIfMissing(emailImportDocumentCollection, ...emailImportDocumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const emailImportDocument: IEmailImportDocument = sampleWithRequiredData;
        const emailImportDocument2: IEmailImportDocument = sampleWithPartialData;
        expectedResult = service.addEmailImportDocumentToCollectionIfMissing([], emailImportDocument, emailImportDocument2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(emailImportDocument);
        expect(expectedResult).toContain(emailImportDocument2);
      });

      it('should accept null and undefined values', () => {
        const emailImportDocument: IEmailImportDocument = sampleWithRequiredData;
        expectedResult = service.addEmailImportDocumentToCollectionIfMissing([], null, emailImportDocument, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(emailImportDocument);
      });

      it('should return initial array if no EmailImportDocument is added', () => {
        const emailImportDocumentCollection: IEmailImportDocument[] = [sampleWithRequiredData];
        expectedResult = service.addEmailImportDocumentToCollectionIfMissing(emailImportDocumentCollection, undefined, null);
        expect(expectedResult).toEqual(emailImportDocumentCollection);
      });
    });

    describe('compareEmailImportDocument', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmailImportDocument(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13070 };
        const entity2 = null;

        const compareResult1 = service.compareEmailImportDocument(entity1, entity2);
        const compareResult2 = service.compareEmailImportDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13070 };
        const entity2 = { id: 27775 };

        const compareResult1 = service.compareEmailImportDocument(entity1, entity2);
        const compareResult2 = service.compareEmailImportDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 13070 };
        const entity2 = { id: 13070 };

        const compareResult1 = service.compareEmailImportDocument(entity1, entity2);
        const compareResult2 = service.compareEmailImportDocument(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
