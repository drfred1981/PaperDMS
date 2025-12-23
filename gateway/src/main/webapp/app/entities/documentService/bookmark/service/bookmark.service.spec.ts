import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IBookmark } from '../bookmark.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../bookmark.test-samples';

import { BookmarkService, RestBookmark } from './bookmark.service';

const requireRestSample: RestBookmark = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('Bookmark Service', () => {
  let service: BookmarkService;
  let httpMock: HttpTestingController;
  let expectedResult: IBookmark | IBookmark[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BookmarkService);
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

    it('should create a Bookmark', () => {
      const bookmark = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bookmark).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bookmark', () => {
      const bookmark = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bookmark).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Bookmark', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bookmark', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Bookmark', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBookmarkToCollectionIfMissing', () => {
      it('should add a Bookmark to an empty array', () => {
        const bookmark: IBookmark = sampleWithRequiredData;
        expectedResult = service.addBookmarkToCollectionIfMissing([], bookmark);
        expect(expectedResult).toEqual([bookmark]);
      });

      it('should not add a Bookmark to an array that contains it', () => {
        const bookmark: IBookmark = sampleWithRequiredData;
        const bookmarkCollection: IBookmark[] = [
          {
            ...bookmark,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBookmarkToCollectionIfMissing(bookmarkCollection, bookmark);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bookmark to an array that doesn't contain it", () => {
        const bookmark: IBookmark = sampleWithRequiredData;
        const bookmarkCollection: IBookmark[] = [sampleWithPartialData];
        expectedResult = service.addBookmarkToCollectionIfMissing(bookmarkCollection, bookmark);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bookmark);
      });

      it('should add only unique Bookmark to an array', () => {
        const bookmarkArray: IBookmark[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bookmarkCollection: IBookmark[] = [sampleWithRequiredData];
        expectedResult = service.addBookmarkToCollectionIfMissing(bookmarkCollection, ...bookmarkArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bookmark: IBookmark = sampleWithRequiredData;
        const bookmark2: IBookmark = sampleWithPartialData;
        expectedResult = service.addBookmarkToCollectionIfMissing([], bookmark, bookmark2);
        expect(expectedResult).toEqual([bookmark, bookmark2]);
      });

      it('should accept null and undefined values', () => {
        const bookmark: IBookmark = sampleWithRequiredData;
        expectedResult = service.addBookmarkToCollectionIfMissing([], null, bookmark, undefined);
        expect(expectedResult).toEqual([bookmark]);
      });

      it('should return initial array if no Bookmark is added', () => {
        const bookmarkCollection: IBookmark[] = [sampleWithRequiredData];
        expectedResult = service.addBookmarkToCollectionIfMissing(bookmarkCollection, undefined, null);
        expect(expectedResult).toEqual(bookmarkCollection);
      });
    });

    describe('compareBookmark', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBookmark(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18281 };
        const entity2 = null;

        const compareResult1 = service.compareBookmark(entity1, entity2);
        const compareResult2 = service.compareBookmark(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18281 };
        const entity2 = { id: 28441 };

        const compareResult1 = service.compareBookmark(entity1, entity2);
        const compareResult2 = service.compareBookmark(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 18281 };
        const entity2 = { id: 18281 };

        const compareResult1 = service.compareBookmark(entity1, entity2);
        const compareResult2 = service.compareBookmark(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
