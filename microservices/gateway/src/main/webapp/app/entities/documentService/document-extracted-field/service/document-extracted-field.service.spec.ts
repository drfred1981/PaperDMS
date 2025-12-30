import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDocumentExtractedField } from '../document-extracted-field.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../document-extracted-field.test-samples';

import { DocumentExtractedFieldService, RestDocumentExtractedField } from './document-extracted-field.service';

const requireRestSample: RestDocumentExtractedField = {
  ...sampleWithRequiredData,
  extractedDate: sampleWithRequiredData.extractedDate?.toJSON(),
};

describe('DocumentExtractedField Service', () => {
  let service: DocumentExtractedFieldService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentExtractedField | IDocumentExtractedField[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentExtractedFieldService);
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

    it('should create a DocumentExtractedField', () => {
      const documentExtractedField = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentExtractedField).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentExtractedField', () => {
      const documentExtractedField = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentExtractedField).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentExtractedField', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentExtractedField', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentExtractedField', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a DocumentExtractedField', () => {
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

    describe('addDocumentExtractedFieldToCollectionIfMissing', () => {
      it('should add a DocumentExtractedField to an empty array', () => {
        const documentExtractedField: IDocumentExtractedField = sampleWithRequiredData;
        expectedResult = service.addDocumentExtractedFieldToCollectionIfMissing([], documentExtractedField);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentExtractedField);
      });

      it('should not add a DocumentExtractedField to an array that contains it', () => {
        const documentExtractedField: IDocumentExtractedField = sampleWithRequiredData;
        const documentExtractedFieldCollection: IDocumentExtractedField[] = [
          {
            ...documentExtractedField,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentExtractedFieldToCollectionIfMissing(documentExtractedFieldCollection, documentExtractedField);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentExtractedField to an array that doesn't contain it", () => {
        const documentExtractedField: IDocumentExtractedField = sampleWithRequiredData;
        const documentExtractedFieldCollection: IDocumentExtractedField[] = [sampleWithPartialData];
        expectedResult = service.addDocumentExtractedFieldToCollectionIfMissing(documentExtractedFieldCollection, documentExtractedField);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentExtractedField);
      });

      it('should add only unique DocumentExtractedField to an array', () => {
        const documentExtractedFieldArray: IDocumentExtractedField[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentExtractedFieldCollection: IDocumentExtractedField[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentExtractedFieldToCollectionIfMissing(
          documentExtractedFieldCollection,
          ...documentExtractedFieldArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentExtractedField: IDocumentExtractedField = sampleWithRequiredData;
        const documentExtractedField2: IDocumentExtractedField = sampleWithPartialData;
        expectedResult = service.addDocumentExtractedFieldToCollectionIfMissing([], documentExtractedField, documentExtractedField2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentExtractedField);
        expect(expectedResult).toContain(documentExtractedField2);
      });

      it('should accept null and undefined values', () => {
        const documentExtractedField: IDocumentExtractedField = sampleWithRequiredData;
        expectedResult = service.addDocumentExtractedFieldToCollectionIfMissing([], null, documentExtractedField, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentExtractedField);
      });

      it('should return initial array if no DocumentExtractedField is added', () => {
        const documentExtractedFieldCollection: IDocumentExtractedField[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentExtractedFieldToCollectionIfMissing(documentExtractedFieldCollection, undefined, null);
        expect(expectedResult).toEqual(documentExtractedFieldCollection);
      });
    });

    describe('compareDocumentExtractedField', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentExtractedField(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3377 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentExtractedField(entity1, entity2);
        const compareResult2 = service.compareDocumentExtractedField(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3377 };
        const entity2 = { id: 19727 };

        const compareResult1 = service.compareDocumentExtractedField(entity1, entity2);
        const compareResult2 = service.compareDocumentExtractedField(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 3377 };
        const entity2 = { id: 3377 };

        const compareResult1 = service.compareDocumentExtractedField(entity1, entity2);
        const compareResult2 = service.compareDocumentExtractedField(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
