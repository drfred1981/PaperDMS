import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDocumentTypeField } from '../document-type-field.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-type-field.test-samples';

import { DocumentTypeFieldService, RestDocumentTypeField } from './document-type-field.service';

const requireRestSample: RestDocumentTypeField = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('DocumentTypeField Service', () => {
  let service: DocumentTypeFieldService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentTypeField | IDocumentTypeField[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentTypeFieldService);
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

    it('should create a DocumentTypeField', () => {
      const documentTypeField = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentTypeField).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentTypeField', () => {
      const documentTypeField = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentTypeField).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentTypeField', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentTypeField', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentTypeField', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDocumentTypeFieldToCollectionIfMissing', () => {
      it('should add a DocumentTypeField to an empty array', () => {
        const documentTypeField: IDocumentTypeField = sampleWithRequiredData;
        expectedResult = service.addDocumentTypeFieldToCollectionIfMissing([], documentTypeField);
        expect(expectedResult).toEqual([documentTypeField]);
      });

      it('should not add a DocumentTypeField to an array that contains it', () => {
        const documentTypeField: IDocumentTypeField = sampleWithRequiredData;
        const documentTypeFieldCollection: IDocumentTypeField[] = [
          {
            ...documentTypeField,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentTypeFieldToCollectionIfMissing(documentTypeFieldCollection, documentTypeField);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentTypeField to an array that doesn't contain it", () => {
        const documentTypeField: IDocumentTypeField = sampleWithRequiredData;
        const documentTypeFieldCollection: IDocumentTypeField[] = [sampleWithPartialData];
        expectedResult = service.addDocumentTypeFieldToCollectionIfMissing(documentTypeFieldCollection, documentTypeField);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentTypeField);
      });

      it('should add only unique DocumentTypeField to an array', () => {
        const documentTypeFieldArray: IDocumentTypeField[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentTypeFieldCollection: IDocumentTypeField[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentTypeFieldToCollectionIfMissing(documentTypeFieldCollection, ...documentTypeFieldArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentTypeField: IDocumentTypeField = sampleWithRequiredData;
        const documentTypeField2: IDocumentTypeField = sampleWithPartialData;
        expectedResult = service.addDocumentTypeFieldToCollectionIfMissing([], documentTypeField, documentTypeField2);
        expect(expectedResult).toEqual([documentTypeField, documentTypeField2]);
      });

      it('should accept null and undefined values', () => {
        const documentTypeField: IDocumentTypeField = sampleWithRequiredData;
        expectedResult = service.addDocumentTypeFieldToCollectionIfMissing([], null, documentTypeField, undefined);
        expect(expectedResult).toEqual([documentTypeField]);
      });

      it('should return initial array if no DocumentTypeField is added', () => {
        const documentTypeFieldCollection: IDocumentTypeField[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentTypeFieldToCollectionIfMissing(documentTypeFieldCollection, undefined, null);
        expect(expectedResult).toEqual(documentTypeFieldCollection);
      });
    });

    describe('compareDocumentTypeField', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentTypeField(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 31753 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentTypeField(entity1, entity2);
        const compareResult2 = service.compareDocumentTypeField(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 31753 };
        const entity2 = { id: 30737 };

        const compareResult1 = service.compareDocumentTypeField(entity1, entity2);
        const compareResult2 = service.compareDocumentTypeField(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 31753 };
        const entity2 = { id: 31753 };

        const compareResult1 = service.compareDocumentTypeField(entity1, entity2);
        const compareResult2 = service.compareDocumentTypeField(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
