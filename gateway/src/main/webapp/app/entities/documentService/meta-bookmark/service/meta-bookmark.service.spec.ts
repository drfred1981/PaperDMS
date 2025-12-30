import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMetaBookmark } from '../meta-bookmark.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../meta-bookmark.test-samples';

import { MetaBookmarkService, RestMetaBookmark } from './meta-bookmark.service';

const requireRestSample: RestMetaBookmark = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('MetaBookmark Service', () => {
  let service: MetaBookmarkService;
  let httpMock: HttpTestingController;
  let expectedResult: IMetaBookmark | IMetaBookmark[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MetaBookmarkService);
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

    it('should create a MetaBookmark', () => {
      const metaBookmark = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(metaBookmark).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MetaBookmark', () => {
      const metaBookmark = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(metaBookmark).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MetaBookmark', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MetaBookmark', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MetaBookmark', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MetaBookmark', () => {
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

    describe('addMetaBookmarkToCollectionIfMissing', () => {
      it('should add a MetaBookmark to an empty array', () => {
        const metaBookmark: IMetaBookmark = sampleWithRequiredData;
        expectedResult = service.addMetaBookmarkToCollectionIfMissing([], metaBookmark);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaBookmark);
      });

      it('should not add a MetaBookmark to an array that contains it', () => {
        const metaBookmark: IMetaBookmark = sampleWithRequiredData;
        const metaBookmarkCollection: IMetaBookmark[] = [
          {
            ...metaBookmark,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMetaBookmarkToCollectionIfMissing(metaBookmarkCollection, metaBookmark);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MetaBookmark to an array that doesn't contain it", () => {
        const metaBookmark: IMetaBookmark = sampleWithRequiredData;
        const metaBookmarkCollection: IMetaBookmark[] = [sampleWithPartialData];
        expectedResult = service.addMetaBookmarkToCollectionIfMissing(metaBookmarkCollection, metaBookmark);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaBookmark);
      });

      it('should add only unique MetaBookmark to an array', () => {
        const metaBookmarkArray: IMetaBookmark[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const metaBookmarkCollection: IMetaBookmark[] = [sampleWithRequiredData];
        expectedResult = service.addMetaBookmarkToCollectionIfMissing(metaBookmarkCollection, ...metaBookmarkArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const metaBookmark: IMetaBookmark = sampleWithRequiredData;
        const metaBookmark2: IMetaBookmark = sampleWithPartialData;
        expectedResult = service.addMetaBookmarkToCollectionIfMissing([], metaBookmark, metaBookmark2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaBookmark);
        expect(expectedResult).toContain(metaBookmark2);
      });

      it('should accept null and undefined values', () => {
        const metaBookmark: IMetaBookmark = sampleWithRequiredData;
        expectedResult = service.addMetaBookmarkToCollectionIfMissing([], null, metaBookmark, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaBookmark);
      });

      it('should return initial array if no MetaBookmark is added', () => {
        const metaBookmarkCollection: IMetaBookmark[] = [sampleWithRequiredData];
        expectedResult = service.addMetaBookmarkToCollectionIfMissing(metaBookmarkCollection, undefined, null);
        expect(expectedResult).toEqual(metaBookmarkCollection);
      });
    });

    describe('compareMetaBookmark', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMetaBookmark(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 12947 };
        const entity2 = null;

        const compareResult1 = service.compareMetaBookmark(entity1, entity2);
        const compareResult2 = service.compareMetaBookmark(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 12947 };
        const entity2 = { id: 14115 };

        const compareResult1 = service.compareMetaBookmark(entity1, entity2);
        const compareResult2 = service.compareMetaBookmark(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 12947 };
        const entity2 = { id: 12947 };

        const compareResult1 = service.compareMetaBookmark(entity1, entity2);
        const compareResult2 = service.compareMetaBookmark(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
