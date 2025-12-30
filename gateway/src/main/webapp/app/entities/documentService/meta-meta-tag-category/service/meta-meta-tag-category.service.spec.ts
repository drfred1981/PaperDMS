import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMetaMetaTagCategory } from '../meta-meta-tag-category.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../meta-meta-tag-category.test-samples';

import { MetaMetaTagCategoryService, RestMetaMetaTagCategory } from './meta-meta-tag-category.service';

const requireRestSample: RestMetaMetaTagCategory = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('MetaMetaTagCategory Service', () => {
  let service: MetaMetaTagCategoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IMetaMetaTagCategory | IMetaMetaTagCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MetaMetaTagCategoryService);
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

    it('should create a MetaMetaTagCategory', () => {
      const metaMetaTagCategory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(metaMetaTagCategory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MetaMetaTagCategory', () => {
      const metaMetaTagCategory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(metaMetaTagCategory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MetaMetaTagCategory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MetaMetaTagCategory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MetaMetaTagCategory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MetaMetaTagCategory', () => {
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

    describe('addMetaMetaTagCategoryToCollectionIfMissing', () => {
      it('should add a MetaMetaTagCategory to an empty array', () => {
        const metaMetaTagCategory: IMetaMetaTagCategory = sampleWithRequiredData;
        expectedResult = service.addMetaMetaTagCategoryToCollectionIfMissing([], metaMetaTagCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaMetaTagCategory);
      });

      it('should not add a MetaMetaTagCategory to an array that contains it', () => {
        const metaMetaTagCategory: IMetaMetaTagCategory = sampleWithRequiredData;
        const metaMetaTagCategoryCollection: IMetaMetaTagCategory[] = [
          {
            ...metaMetaTagCategory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMetaMetaTagCategoryToCollectionIfMissing(metaMetaTagCategoryCollection, metaMetaTagCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MetaMetaTagCategory to an array that doesn't contain it", () => {
        const metaMetaTagCategory: IMetaMetaTagCategory = sampleWithRequiredData;
        const metaMetaTagCategoryCollection: IMetaMetaTagCategory[] = [sampleWithPartialData];
        expectedResult = service.addMetaMetaTagCategoryToCollectionIfMissing(metaMetaTagCategoryCollection, metaMetaTagCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaMetaTagCategory);
      });

      it('should add only unique MetaMetaTagCategory to an array', () => {
        const metaMetaTagCategoryArray: IMetaMetaTagCategory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const metaMetaTagCategoryCollection: IMetaMetaTagCategory[] = [sampleWithRequiredData];
        expectedResult = service.addMetaMetaTagCategoryToCollectionIfMissing(metaMetaTagCategoryCollection, ...metaMetaTagCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const metaMetaTagCategory: IMetaMetaTagCategory = sampleWithRequiredData;
        const metaMetaTagCategory2: IMetaMetaTagCategory = sampleWithPartialData;
        expectedResult = service.addMetaMetaTagCategoryToCollectionIfMissing([], metaMetaTagCategory, metaMetaTagCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaMetaTagCategory);
        expect(expectedResult).toContain(metaMetaTagCategory2);
      });

      it('should accept null and undefined values', () => {
        const metaMetaTagCategory: IMetaMetaTagCategory = sampleWithRequiredData;
        expectedResult = service.addMetaMetaTagCategoryToCollectionIfMissing([], null, metaMetaTagCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaMetaTagCategory);
      });

      it('should return initial array if no MetaMetaTagCategory is added', () => {
        const metaMetaTagCategoryCollection: IMetaMetaTagCategory[] = [sampleWithRequiredData];
        expectedResult = service.addMetaMetaTagCategoryToCollectionIfMissing(metaMetaTagCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(metaMetaTagCategoryCollection);
      });
    });

    describe('compareMetaMetaTagCategory', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMetaMetaTagCategory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7139 };
        const entity2 = null;

        const compareResult1 = service.compareMetaMetaTagCategory(entity1, entity2);
        const compareResult2 = service.compareMetaMetaTagCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7139 };
        const entity2 = { id: 29219 };

        const compareResult1 = service.compareMetaMetaTagCategory(entity1, entity2);
        const compareResult2 = service.compareMetaMetaTagCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7139 };
        const entity2 = { id: 7139 };

        const compareResult1 = service.compareMetaMetaTagCategory(entity1, entity2);
        const compareResult2 = service.compareMetaMetaTagCategory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
