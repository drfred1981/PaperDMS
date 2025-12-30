import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDocumentType } from '../document-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-type.test-samples';

import { DocumentTypeService, RestDocumentType } from './document-type.service';

const requireRestSample: RestDocumentType = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('DocumentType Service', () => {
  let service: DocumentTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentType | IDocumentType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentTypeService);
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

    it('should create a DocumentType', () => {
      const documentType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentType', () => {
      const documentType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a DocumentType', () => {
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

    describe('addDocumentTypeToCollectionIfMissing', () => {
      it('should add a DocumentType to an empty array', () => {
        const documentType: IDocumentType = sampleWithRequiredData;
        expectedResult = service.addDocumentTypeToCollectionIfMissing([], documentType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentType);
      });

      it('should not add a DocumentType to an array that contains it', () => {
        const documentType: IDocumentType = sampleWithRequiredData;
        const documentTypeCollection: IDocumentType[] = [
          {
            ...documentType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentTypeToCollectionIfMissing(documentTypeCollection, documentType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentType to an array that doesn't contain it", () => {
        const documentType: IDocumentType = sampleWithRequiredData;
        const documentTypeCollection: IDocumentType[] = [sampleWithPartialData];
        expectedResult = service.addDocumentTypeToCollectionIfMissing(documentTypeCollection, documentType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentType);
      });

      it('should add only unique DocumentType to an array', () => {
        const documentTypeArray: IDocumentType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentTypeCollection: IDocumentType[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentTypeToCollectionIfMissing(documentTypeCollection, ...documentTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentType: IDocumentType = sampleWithRequiredData;
        const documentType2: IDocumentType = sampleWithPartialData;
        expectedResult = service.addDocumentTypeToCollectionIfMissing([], documentType, documentType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentType);
        expect(expectedResult).toContain(documentType2);
      });

      it('should accept null and undefined values', () => {
        const documentType: IDocumentType = sampleWithRequiredData;
        expectedResult = service.addDocumentTypeToCollectionIfMissing([], null, documentType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentType);
      });

      it('should return initial array if no DocumentType is added', () => {
        const documentTypeCollection: IDocumentType[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentTypeToCollectionIfMissing(documentTypeCollection, undefined, null);
        expect(expectedResult).toEqual(documentTypeCollection);
      });
    });

    describe('compareDocumentType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9974 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentType(entity1, entity2);
        const compareResult2 = service.compareDocumentType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9974 };
        const entity2 = { id: 29456 };

        const compareResult1 = service.compareDocumentType(entity1, entity2);
        const compareResult2 = service.compareDocumentType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 9974 };
        const entity2 = { id: 9974 };

        const compareResult1 = service.compareDocumentType(entity1, entity2);
        const compareResult2 = service.compareDocumentType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
