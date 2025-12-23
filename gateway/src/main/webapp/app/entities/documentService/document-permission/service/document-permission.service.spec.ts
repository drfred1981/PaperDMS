import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDocumentPermission } from '../document-permission.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-permission.test-samples';

import { DocumentPermissionService, RestDocumentPermission } from './document-permission.service';

const requireRestSample: RestDocumentPermission = {
  ...sampleWithRequiredData,
  grantedDate: sampleWithRequiredData.grantedDate?.toJSON(),
};

describe('DocumentPermission Service', () => {
  let service: DocumentPermissionService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentPermission | IDocumentPermission[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentPermissionService);
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

    it('should create a DocumentPermission', () => {
      const documentPermission = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentPermission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentPermission', () => {
      const documentPermission = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentPermission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentPermission', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentPermission', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentPermission', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDocumentPermissionToCollectionIfMissing', () => {
      it('should add a DocumentPermission to an empty array', () => {
        const documentPermission: IDocumentPermission = sampleWithRequiredData;
        expectedResult = service.addDocumentPermissionToCollectionIfMissing([], documentPermission);
        expect(expectedResult).toEqual([documentPermission]);
      });

      it('should not add a DocumentPermission to an array that contains it', () => {
        const documentPermission: IDocumentPermission = sampleWithRequiredData;
        const documentPermissionCollection: IDocumentPermission[] = [
          {
            ...documentPermission,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentPermissionToCollectionIfMissing(documentPermissionCollection, documentPermission);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentPermission to an array that doesn't contain it", () => {
        const documentPermission: IDocumentPermission = sampleWithRequiredData;
        const documentPermissionCollection: IDocumentPermission[] = [sampleWithPartialData];
        expectedResult = service.addDocumentPermissionToCollectionIfMissing(documentPermissionCollection, documentPermission);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentPermission);
      });

      it('should add only unique DocumentPermission to an array', () => {
        const documentPermissionArray: IDocumentPermission[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentPermissionCollection: IDocumentPermission[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentPermissionToCollectionIfMissing(documentPermissionCollection, ...documentPermissionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentPermission: IDocumentPermission = sampleWithRequiredData;
        const documentPermission2: IDocumentPermission = sampleWithPartialData;
        expectedResult = service.addDocumentPermissionToCollectionIfMissing([], documentPermission, documentPermission2);
        expect(expectedResult).toEqual([documentPermission, documentPermission2]);
      });

      it('should accept null and undefined values', () => {
        const documentPermission: IDocumentPermission = sampleWithRequiredData;
        expectedResult = service.addDocumentPermissionToCollectionIfMissing([], null, documentPermission, undefined);
        expect(expectedResult).toEqual([documentPermission]);
      });

      it('should return initial array if no DocumentPermission is added', () => {
        const documentPermissionCollection: IDocumentPermission[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentPermissionToCollectionIfMissing(documentPermissionCollection, undefined, null);
        expect(expectedResult).toEqual(documentPermissionCollection);
      });
    });

    describe('compareDocumentPermission', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentPermission(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5224 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentPermission(entity1, entity2);
        const compareResult2 = service.compareDocumentPermission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5224 };
        const entity2 = { id: 25975 };

        const compareResult1 = service.compareDocumentPermission(entity1, entity2);
        const compareResult2 = service.compareDocumentPermission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5224 };
        const entity2 = { id: 5224 };

        const compareResult1 = service.compareDocumentPermission(entity1, entity2);
        const compareResult2 = service.compareDocumentPermission(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
