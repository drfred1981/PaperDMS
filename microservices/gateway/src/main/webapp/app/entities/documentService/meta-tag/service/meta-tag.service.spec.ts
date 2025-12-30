import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMetaTag } from '../meta-tag.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../meta-tag.test-samples';

import { MetaTagService, RestMetaTag } from './meta-tag.service';

const requireRestSample: RestMetaTag = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('MetaTag Service', () => {
  let service: MetaTagService;
  let httpMock: HttpTestingController;
  let expectedResult: IMetaTag | IMetaTag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MetaTagService);
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

    it('should create a MetaTag', () => {
      const metaTag = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(metaTag).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MetaTag', () => {
      const metaTag = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(metaTag).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MetaTag', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MetaTag', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MetaTag', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MetaTag', () => {
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

    describe('addMetaTagToCollectionIfMissing', () => {
      it('should add a MetaTag to an empty array', () => {
        const metaTag: IMetaTag = sampleWithRequiredData;
        expectedResult = service.addMetaTagToCollectionIfMissing([], metaTag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaTag);
      });

      it('should not add a MetaTag to an array that contains it', () => {
        const metaTag: IMetaTag = sampleWithRequiredData;
        const metaTagCollection: IMetaTag[] = [
          {
            ...metaTag,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMetaTagToCollectionIfMissing(metaTagCollection, metaTag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MetaTag to an array that doesn't contain it", () => {
        const metaTag: IMetaTag = sampleWithRequiredData;
        const metaTagCollection: IMetaTag[] = [sampleWithPartialData];
        expectedResult = service.addMetaTagToCollectionIfMissing(metaTagCollection, metaTag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaTag);
      });

      it('should add only unique MetaTag to an array', () => {
        const metaTagArray: IMetaTag[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const metaTagCollection: IMetaTag[] = [sampleWithRequiredData];
        expectedResult = service.addMetaTagToCollectionIfMissing(metaTagCollection, ...metaTagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const metaTag: IMetaTag = sampleWithRequiredData;
        const metaTag2: IMetaTag = sampleWithPartialData;
        expectedResult = service.addMetaTagToCollectionIfMissing([], metaTag, metaTag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaTag);
        expect(expectedResult).toContain(metaTag2);
      });

      it('should accept null and undefined values', () => {
        const metaTag: IMetaTag = sampleWithRequiredData;
        expectedResult = service.addMetaTagToCollectionIfMissing([], null, metaTag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaTag);
      });

      it('should return initial array if no MetaTag is added', () => {
        const metaTagCollection: IMetaTag[] = [sampleWithRequiredData];
        expectedResult = service.addMetaTagToCollectionIfMissing(metaTagCollection, undefined, null);
        expect(expectedResult).toEqual(metaTagCollection);
      });
    });

    describe('compareMetaTag', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMetaTag(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11753 };
        const entity2 = null;

        const compareResult1 = service.compareMetaTag(entity1, entity2);
        const compareResult2 = service.compareMetaTag(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11753 };
        const entity2 = { id: 29128 };

        const compareResult1 = service.compareMetaTag(entity1, entity2);
        const compareResult2 = service.compareMetaTag(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 11753 };
        const entity2 = { id: 11753 };

        const compareResult1 = service.compareMetaTag(entity1, entity2);
        const compareResult2 = service.compareMetaTag(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
