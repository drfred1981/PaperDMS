import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDocumentComment } from '../document-comment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-comment.test-samples';

import { DocumentCommentService, RestDocumentComment } from './document-comment.service';

const requireRestSample: RestDocumentComment = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('DocumentComment Service', () => {
  let service: DocumentCommentService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentComment | IDocumentComment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentCommentService);
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

    it('should create a DocumentComment', () => {
      const documentComment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentComment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentComment', () => {
      const documentComment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentComment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentComment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentComment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentComment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDocumentCommentToCollectionIfMissing', () => {
      it('should add a DocumentComment to an empty array', () => {
        const documentComment: IDocumentComment = sampleWithRequiredData;
        expectedResult = service.addDocumentCommentToCollectionIfMissing([], documentComment);
        expect(expectedResult).toEqual([documentComment]);
      });

      it('should not add a DocumentComment to an array that contains it', () => {
        const documentComment: IDocumentComment = sampleWithRequiredData;
        const documentCommentCollection: IDocumentComment[] = [
          {
            ...documentComment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentCommentToCollectionIfMissing(documentCommentCollection, documentComment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentComment to an array that doesn't contain it", () => {
        const documentComment: IDocumentComment = sampleWithRequiredData;
        const documentCommentCollection: IDocumentComment[] = [sampleWithPartialData];
        expectedResult = service.addDocumentCommentToCollectionIfMissing(documentCommentCollection, documentComment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentComment);
      });

      it('should add only unique DocumentComment to an array', () => {
        const documentCommentArray: IDocumentComment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentCommentCollection: IDocumentComment[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentCommentToCollectionIfMissing(documentCommentCollection, ...documentCommentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentComment: IDocumentComment = sampleWithRequiredData;
        const documentComment2: IDocumentComment = sampleWithPartialData;
        expectedResult = service.addDocumentCommentToCollectionIfMissing([], documentComment, documentComment2);
        expect(expectedResult).toEqual([documentComment, documentComment2]);
      });

      it('should accept null and undefined values', () => {
        const documentComment: IDocumentComment = sampleWithRequiredData;
        expectedResult = service.addDocumentCommentToCollectionIfMissing([], null, documentComment, undefined);
        expect(expectedResult).toEqual([documentComment]);
      });

      it('should return initial array if no DocumentComment is added', () => {
        const documentCommentCollection: IDocumentComment[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentCommentToCollectionIfMissing(documentCommentCollection, undefined, null);
        expect(expectedResult).toEqual(documentCommentCollection);
      });
    });

    describe('compareDocumentComment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentComment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5301 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentComment(entity1, entity2);
        const compareResult2 = service.compareDocumentComment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5301 };
        const entity2 = { id: 7332 };

        const compareResult1 = service.compareDocumentComment(entity1, entity2);
        const compareResult2 = service.compareDocumentComment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5301 };
        const entity2 = { id: 5301 };

        const compareResult1 = service.compareDocumentComment(entity1, entity2);
        const compareResult2 = service.compareDocumentComment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
