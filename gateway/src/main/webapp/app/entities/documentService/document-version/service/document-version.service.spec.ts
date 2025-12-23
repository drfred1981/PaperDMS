import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDocumentVersion } from '../document-version.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-version.test-samples';

import { DocumentVersionService, RestDocumentVersion } from './document-version.service';

const requireRestSample: RestDocumentVersion = {
  ...sampleWithRequiredData,
  uploadDate: sampleWithRequiredData.uploadDate?.toJSON(),
};

describe('DocumentVersion Service', () => {
  let service: DocumentVersionService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentVersion | IDocumentVersion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentVersionService);
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

    it('should create a DocumentVersion', () => {
      const documentVersion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentVersion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentVersion', () => {
      const documentVersion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentVersion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentVersion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentVersion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentVersion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDocumentVersionToCollectionIfMissing', () => {
      it('should add a DocumentVersion to an empty array', () => {
        const documentVersion: IDocumentVersion = sampleWithRequiredData;
        expectedResult = service.addDocumentVersionToCollectionIfMissing([], documentVersion);
        expect(expectedResult).toEqual([documentVersion]);
      });

      it('should not add a DocumentVersion to an array that contains it', () => {
        const documentVersion: IDocumentVersion = sampleWithRequiredData;
        const documentVersionCollection: IDocumentVersion[] = [
          {
            ...documentVersion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentVersionToCollectionIfMissing(documentVersionCollection, documentVersion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentVersion to an array that doesn't contain it", () => {
        const documentVersion: IDocumentVersion = sampleWithRequiredData;
        const documentVersionCollection: IDocumentVersion[] = [sampleWithPartialData];
        expectedResult = service.addDocumentVersionToCollectionIfMissing(documentVersionCollection, documentVersion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentVersion);
      });

      it('should add only unique DocumentVersion to an array', () => {
        const documentVersionArray: IDocumentVersion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentVersionCollection: IDocumentVersion[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentVersionToCollectionIfMissing(documentVersionCollection, ...documentVersionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentVersion: IDocumentVersion = sampleWithRequiredData;
        const documentVersion2: IDocumentVersion = sampleWithPartialData;
        expectedResult = service.addDocumentVersionToCollectionIfMissing([], documentVersion, documentVersion2);
        expect(expectedResult).toEqual([documentVersion, documentVersion2]);
      });

      it('should accept null and undefined values', () => {
        const documentVersion: IDocumentVersion = sampleWithRequiredData;
        expectedResult = service.addDocumentVersionToCollectionIfMissing([], null, documentVersion, undefined);
        expect(expectedResult).toEqual([documentVersion]);
      });

      it('should return initial array if no DocumentVersion is added', () => {
        const documentVersionCollection: IDocumentVersion[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentVersionToCollectionIfMissing(documentVersionCollection, undefined, null);
        expect(expectedResult).toEqual(documentVersionCollection);
      });
    });

    describe('compareDocumentVersion', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentVersion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2205 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentVersion(entity1, entity2);
        const compareResult2 = service.compareDocumentVersion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2205 };
        const entity2 = { id: 25578 };

        const compareResult1 = service.compareDocumentVersion(entity1, entity2);
        const compareResult2 = service.compareDocumentVersion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 2205 };
        const entity2 = { id: 2205 };

        const compareResult1 = service.compareDocumentVersion(entity1, entity2);
        const compareResult2 = service.compareDocumentVersion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
