import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDocumentMetadata } from '../document-metadata.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-metadata.test-samples';

import { DocumentMetadataService, RestDocumentMetadata } from './document-metadata.service';

const requireRestSample: RestDocumentMetadata = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('DocumentMetadata Service', () => {
  let service: DocumentMetadataService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentMetadata | IDocumentMetadata[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentMetadataService);
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

    it('should create a DocumentMetadata', () => {
      const documentMetadata = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentMetadata).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentMetadata', () => {
      const documentMetadata = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentMetadata).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentMetadata', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentMetadata', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentMetadata', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDocumentMetadataToCollectionIfMissing', () => {
      it('should add a DocumentMetadata to an empty array', () => {
        const documentMetadata: IDocumentMetadata = sampleWithRequiredData;
        expectedResult = service.addDocumentMetadataToCollectionIfMissing([], documentMetadata);
        expect(expectedResult).toEqual([documentMetadata]);
      });

      it('should not add a DocumentMetadata to an array that contains it', () => {
        const documentMetadata: IDocumentMetadata = sampleWithRequiredData;
        const documentMetadataCollection: IDocumentMetadata[] = [
          {
            ...documentMetadata,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentMetadataToCollectionIfMissing(documentMetadataCollection, documentMetadata);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentMetadata to an array that doesn't contain it", () => {
        const documentMetadata: IDocumentMetadata = sampleWithRequiredData;
        const documentMetadataCollection: IDocumentMetadata[] = [sampleWithPartialData];
        expectedResult = service.addDocumentMetadataToCollectionIfMissing(documentMetadataCollection, documentMetadata);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentMetadata);
      });

      it('should add only unique DocumentMetadata to an array', () => {
        const documentMetadataArray: IDocumentMetadata[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentMetadataCollection: IDocumentMetadata[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentMetadataToCollectionIfMissing(documentMetadataCollection, ...documentMetadataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentMetadata: IDocumentMetadata = sampleWithRequiredData;
        const documentMetadata2: IDocumentMetadata = sampleWithPartialData;
        expectedResult = service.addDocumentMetadataToCollectionIfMissing([], documentMetadata, documentMetadata2);
        expect(expectedResult).toEqual([documentMetadata, documentMetadata2]);
      });

      it('should accept null and undefined values', () => {
        const documentMetadata: IDocumentMetadata = sampleWithRequiredData;
        expectedResult = service.addDocumentMetadataToCollectionIfMissing([], null, documentMetadata, undefined);
        expect(expectedResult).toEqual([documentMetadata]);
      });

      it('should return initial array if no DocumentMetadata is added', () => {
        const documentMetadataCollection: IDocumentMetadata[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentMetadataToCollectionIfMissing(documentMetadataCollection, undefined, null);
        expect(expectedResult).toEqual(documentMetadataCollection);
      });
    });

    describe('compareDocumentMetadata', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentMetadata(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10583 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentMetadata(entity1, entity2);
        const compareResult2 = service.compareDocumentMetadata(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10583 };
        const entity2 = { id: 2476 };

        const compareResult1 = service.compareDocumentMetadata(entity1, entity2);
        const compareResult2 = service.compareDocumentMetadata(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 10583 };
        const entity2 = { id: 10583 };

        const compareResult1 = service.compareDocumentMetadata(entity1, entity2);
        const compareResult2 = service.compareDocumentMetadata(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
