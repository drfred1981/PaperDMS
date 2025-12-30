import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMetaPermissionGroup } from '../meta-permission-group.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../meta-permission-group.test-samples';

import { MetaPermissionGroupService, RestMetaPermissionGroup } from './meta-permission-group.service';

const requireRestSample: RestMetaPermissionGroup = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('MetaPermissionGroup Service', () => {
  let service: MetaPermissionGroupService;
  let httpMock: HttpTestingController;
  let expectedResult: IMetaPermissionGroup | IMetaPermissionGroup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MetaPermissionGroupService);
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

    it('should create a MetaPermissionGroup', () => {
      const metaPermissionGroup = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(metaPermissionGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MetaPermissionGroup', () => {
      const metaPermissionGroup = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(metaPermissionGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MetaPermissionGroup', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MetaPermissionGroup', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MetaPermissionGroup', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MetaPermissionGroup', () => {
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

    describe('addMetaPermissionGroupToCollectionIfMissing', () => {
      it('should add a MetaPermissionGroup to an empty array', () => {
        const metaPermissionGroup: IMetaPermissionGroup = sampleWithRequiredData;
        expectedResult = service.addMetaPermissionGroupToCollectionIfMissing([], metaPermissionGroup);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaPermissionGroup);
      });

      it('should not add a MetaPermissionGroup to an array that contains it', () => {
        const metaPermissionGroup: IMetaPermissionGroup = sampleWithRequiredData;
        const metaPermissionGroupCollection: IMetaPermissionGroup[] = [
          {
            ...metaPermissionGroup,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMetaPermissionGroupToCollectionIfMissing(metaPermissionGroupCollection, metaPermissionGroup);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MetaPermissionGroup to an array that doesn't contain it", () => {
        const metaPermissionGroup: IMetaPermissionGroup = sampleWithRequiredData;
        const metaPermissionGroupCollection: IMetaPermissionGroup[] = [sampleWithPartialData];
        expectedResult = service.addMetaPermissionGroupToCollectionIfMissing(metaPermissionGroupCollection, metaPermissionGroup);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaPermissionGroup);
      });

      it('should add only unique MetaPermissionGroup to an array', () => {
        const metaPermissionGroupArray: IMetaPermissionGroup[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const metaPermissionGroupCollection: IMetaPermissionGroup[] = [sampleWithRequiredData];
        expectedResult = service.addMetaPermissionGroupToCollectionIfMissing(metaPermissionGroupCollection, ...metaPermissionGroupArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const metaPermissionGroup: IMetaPermissionGroup = sampleWithRequiredData;
        const metaPermissionGroup2: IMetaPermissionGroup = sampleWithPartialData;
        expectedResult = service.addMetaPermissionGroupToCollectionIfMissing([], metaPermissionGroup, metaPermissionGroup2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaPermissionGroup);
        expect(expectedResult).toContain(metaPermissionGroup2);
      });

      it('should accept null and undefined values', () => {
        const metaPermissionGroup: IMetaPermissionGroup = sampleWithRequiredData;
        expectedResult = service.addMetaPermissionGroupToCollectionIfMissing([], null, metaPermissionGroup, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaPermissionGroup);
      });

      it('should return initial array if no MetaPermissionGroup is added', () => {
        const metaPermissionGroupCollection: IMetaPermissionGroup[] = [sampleWithRequiredData];
        expectedResult = service.addMetaPermissionGroupToCollectionIfMissing(metaPermissionGroupCollection, undefined, null);
        expect(expectedResult).toEqual(metaPermissionGroupCollection);
      });
    });

    describe('compareMetaPermissionGroup', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMetaPermissionGroup(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4839 };
        const entity2 = null;

        const compareResult1 = service.compareMetaPermissionGroup(entity1, entity2);
        const compareResult2 = service.compareMetaPermissionGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4839 };
        const entity2 = { id: 27974 };

        const compareResult1 = service.compareMetaPermissionGroup(entity1, entity2);
        const compareResult2 = service.compareMetaPermissionGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 4839 };
        const entity2 = { id: 4839 };

        const compareResult1 = service.compareMetaPermissionGroup(entity1, entity2);
        const compareResult2 = service.compareMetaPermissionGroup(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
