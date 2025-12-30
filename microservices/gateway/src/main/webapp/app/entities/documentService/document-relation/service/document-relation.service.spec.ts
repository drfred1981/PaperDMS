import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDocumentRelation } from '../document-relation.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-relation.test-samples';

import { DocumentRelationService, RestDocumentRelation } from './document-relation.service';

const requireRestSample: RestDocumentRelation = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('DocumentRelation Service', () => {
  let service: DocumentRelationService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentRelation | IDocumentRelation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentRelationService);
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

    it('should create a DocumentRelation', () => {
      const documentRelation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentRelation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentRelation', () => {
      const documentRelation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentRelation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentRelation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentRelation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentRelation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a DocumentRelation', () => {
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

    describe('addDocumentRelationToCollectionIfMissing', () => {
      it('should add a DocumentRelation to an empty array', () => {
        const documentRelation: IDocumentRelation = sampleWithRequiredData;
        expectedResult = service.addDocumentRelationToCollectionIfMissing([], documentRelation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentRelation);
      });

      it('should not add a DocumentRelation to an array that contains it', () => {
        const documentRelation: IDocumentRelation = sampleWithRequiredData;
        const documentRelationCollection: IDocumentRelation[] = [
          {
            ...documentRelation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentRelationToCollectionIfMissing(documentRelationCollection, documentRelation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentRelation to an array that doesn't contain it", () => {
        const documentRelation: IDocumentRelation = sampleWithRequiredData;
        const documentRelationCollection: IDocumentRelation[] = [sampleWithPartialData];
        expectedResult = service.addDocumentRelationToCollectionIfMissing(documentRelationCollection, documentRelation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentRelation);
      });

      it('should add only unique DocumentRelation to an array', () => {
        const documentRelationArray: IDocumentRelation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentRelationCollection: IDocumentRelation[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentRelationToCollectionIfMissing(documentRelationCollection, ...documentRelationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentRelation: IDocumentRelation = sampleWithRequiredData;
        const documentRelation2: IDocumentRelation = sampleWithPartialData;
        expectedResult = service.addDocumentRelationToCollectionIfMissing([], documentRelation, documentRelation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentRelation);
        expect(expectedResult).toContain(documentRelation2);
      });

      it('should accept null and undefined values', () => {
        const documentRelation: IDocumentRelation = sampleWithRequiredData;
        expectedResult = service.addDocumentRelationToCollectionIfMissing([], null, documentRelation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentRelation);
      });

      it('should return initial array if no DocumentRelation is added', () => {
        const documentRelationCollection: IDocumentRelation[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentRelationToCollectionIfMissing(documentRelationCollection, undefined, null);
        expect(expectedResult).toEqual(documentRelationCollection);
      });
    });

    describe('compareDocumentRelation', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentRelation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 21039 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentRelation(entity1, entity2);
        const compareResult2 = service.compareDocumentRelation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 21039 };
        const entity2 = { id: 4779 };

        const compareResult1 = service.compareDocumentRelation(entity1, entity2);
        const compareResult2 = service.compareDocumentRelation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 21039 };
        const entity2 = { id: 21039 };

        const compareResult1 = service.compareDocumentRelation(entity1, entity2);
        const compareResult2 = service.compareDocumentRelation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
