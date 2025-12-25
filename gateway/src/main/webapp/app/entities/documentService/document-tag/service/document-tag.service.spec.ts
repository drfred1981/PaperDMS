import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDocumentTag } from '../document-tag.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-tag.test-samples';

import { DocumentTagService, RestDocumentTag } from './document-tag.service';

const requireRestSample: RestDocumentTag = {
  ...sampleWithRequiredData,
  assignedDate: sampleWithRequiredData.assignedDate?.toJSON(),
};

describe('DocumentTag Service', () => {
  let service: DocumentTagService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentTag | IDocumentTag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentTagService);
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

    it('should create a DocumentTag', () => {
      const documentTag = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentTag).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentTag', () => {
      const documentTag = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentTag).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentTag', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentTag', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentTag', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDocumentTagToCollectionIfMissing', () => {
      it('should add a DocumentTag to an empty array', () => {
        const documentTag: IDocumentTag = sampleWithRequiredData;
        expectedResult = service.addDocumentTagToCollectionIfMissing([], documentTag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentTag);
      });

      it('should not add a DocumentTag to an array that contains it', () => {
        const documentTag: IDocumentTag = sampleWithRequiredData;
        const documentTagCollection: IDocumentTag[] = [
          {
            ...documentTag,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentTagToCollectionIfMissing(documentTagCollection, documentTag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentTag to an array that doesn't contain it", () => {
        const documentTag: IDocumentTag = sampleWithRequiredData;
        const documentTagCollection: IDocumentTag[] = [sampleWithPartialData];
        expectedResult = service.addDocumentTagToCollectionIfMissing(documentTagCollection, documentTag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentTag);
      });

      it('should add only unique DocumentTag to an array', () => {
        const documentTagArray: IDocumentTag[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentTagCollection: IDocumentTag[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentTagToCollectionIfMissing(documentTagCollection, ...documentTagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentTag: IDocumentTag = sampleWithRequiredData;
        const documentTag2: IDocumentTag = sampleWithPartialData;
        expectedResult = service.addDocumentTagToCollectionIfMissing([], documentTag, documentTag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentTag);
        expect(expectedResult).toContain(documentTag2);
      });

      it('should accept null and undefined values', () => {
        const documentTag: IDocumentTag = sampleWithRequiredData;
        expectedResult = service.addDocumentTagToCollectionIfMissing([], null, documentTag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentTag);
      });

      it('should return initial array if no DocumentTag is added', () => {
        const documentTagCollection: IDocumentTag[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentTagToCollectionIfMissing(documentTagCollection, undefined, null);
        expect(expectedResult).toEqual(documentTagCollection);
      });
    });

    describe('compareDocumentTag', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentTag(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 12264 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentTag(entity1, entity2);
        const compareResult2 = service.compareDocumentTag(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 12264 };
        const entity2 = { id: 31172 };

        const compareResult1 = service.compareDocumentTag(entity1, entity2);
        const compareResult2 = service.compareDocumentTag(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 12264 };
        const entity2 = { id: 12264 };

        const compareResult1 = service.compareDocumentTag(entity1, entity2);
        const compareResult2 = service.compareDocumentTag(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
