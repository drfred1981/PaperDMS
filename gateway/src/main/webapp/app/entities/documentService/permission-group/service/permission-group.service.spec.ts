import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPermissionGroup } from '../permission-group.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../permission-group.test-samples';

import { PermissionGroupService, RestPermissionGroup } from './permission-group.service';

const requireRestSample: RestPermissionGroup = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('PermissionGroup Service', () => {
  let service: PermissionGroupService;
  let httpMock: HttpTestingController;
  let expectedResult: IPermissionGroup | IPermissionGroup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PermissionGroupService);
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

    it('should create a PermissionGroup', () => {
      const permissionGroup = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(permissionGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PermissionGroup', () => {
      const permissionGroup = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(permissionGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PermissionGroup', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PermissionGroup', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PermissionGroup', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPermissionGroupToCollectionIfMissing', () => {
      it('should add a PermissionGroup to an empty array', () => {
        const permissionGroup: IPermissionGroup = sampleWithRequiredData;
        expectedResult = service.addPermissionGroupToCollectionIfMissing([], permissionGroup);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(permissionGroup);
      });

      it('should not add a PermissionGroup to an array that contains it', () => {
        const permissionGroup: IPermissionGroup = sampleWithRequiredData;
        const permissionGroupCollection: IPermissionGroup[] = [
          {
            ...permissionGroup,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPermissionGroupToCollectionIfMissing(permissionGroupCollection, permissionGroup);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PermissionGroup to an array that doesn't contain it", () => {
        const permissionGroup: IPermissionGroup = sampleWithRequiredData;
        const permissionGroupCollection: IPermissionGroup[] = [sampleWithPartialData];
        expectedResult = service.addPermissionGroupToCollectionIfMissing(permissionGroupCollection, permissionGroup);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(permissionGroup);
      });

      it('should add only unique PermissionGroup to an array', () => {
        const permissionGroupArray: IPermissionGroup[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const permissionGroupCollection: IPermissionGroup[] = [sampleWithRequiredData];
        expectedResult = service.addPermissionGroupToCollectionIfMissing(permissionGroupCollection, ...permissionGroupArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const permissionGroup: IPermissionGroup = sampleWithRequiredData;
        const permissionGroup2: IPermissionGroup = sampleWithPartialData;
        expectedResult = service.addPermissionGroupToCollectionIfMissing([], permissionGroup, permissionGroup2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(permissionGroup);
        expect(expectedResult).toContain(permissionGroup2);
      });

      it('should accept null and undefined values', () => {
        const permissionGroup: IPermissionGroup = sampleWithRequiredData;
        expectedResult = service.addPermissionGroupToCollectionIfMissing([], null, permissionGroup, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(permissionGroup);
      });

      it('should return initial array if no PermissionGroup is added', () => {
        const permissionGroupCollection: IPermissionGroup[] = [sampleWithRequiredData];
        expectedResult = service.addPermissionGroupToCollectionIfMissing(permissionGroupCollection, undefined, null);
        expect(expectedResult).toEqual(permissionGroupCollection);
      });
    });

    describe('comparePermissionGroup', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePermissionGroup(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 20701 };
        const entity2 = null;

        const compareResult1 = service.comparePermissionGroup(entity1, entity2);
        const compareResult2 = service.comparePermissionGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 20701 };
        const entity2 = { id: 13105 };

        const compareResult1 = service.comparePermissionGroup(entity1, entity2);
        const compareResult2 = service.comparePermissionGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 20701 };
        const entity2 = { id: 20701 };

        const compareResult1 = service.comparePermissionGroup(entity1, entity2);
        const compareResult2 = service.comparePermissionGroup(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
