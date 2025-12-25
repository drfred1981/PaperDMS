import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISystemHealth } from '../system-health.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../system-health.test-samples';

import { RestSystemHealth, SystemHealthService } from './system-health.service';

const requireRestSample: RestSystemHealth = {
  ...sampleWithRequiredData,
  lastCheck: sampleWithRequiredData.lastCheck?.toJSON(),
};

describe('SystemHealth Service', () => {
  let service: SystemHealthService;
  let httpMock: HttpTestingController;
  let expectedResult: ISystemHealth | ISystemHealth[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SystemHealthService);
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

    it('should create a SystemHealth', () => {
      const systemHealth = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(systemHealth).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SystemHealth', () => {
      const systemHealth = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(systemHealth).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SystemHealth', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SystemHealth', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SystemHealth', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSystemHealthToCollectionIfMissing', () => {
      it('should add a SystemHealth to an empty array', () => {
        const systemHealth: ISystemHealth = sampleWithRequiredData;
        expectedResult = service.addSystemHealthToCollectionIfMissing([], systemHealth);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemHealth);
      });

      it('should not add a SystemHealth to an array that contains it', () => {
        const systemHealth: ISystemHealth = sampleWithRequiredData;
        const systemHealthCollection: ISystemHealth[] = [
          {
            ...systemHealth,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSystemHealthToCollectionIfMissing(systemHealthCollection, systemHealth);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SystemHealth to an array that doesn't contain it", () => {
        const systemHealth: ISystemHealth = sampleWithRequiredData;
        const systemHealthCollection: ISystemHealth[] = [sampleWithPartialData];
        expectedResult = service.addSystemHealthToCollectionIfMissing(systemHealthCollection, systemHealth);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemHealth);
      });

      it('should add only unique SystemHealth to an array', () => {
        const systemHealthArray: ISystemHealth[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const systemHealthCollection: ISystemHealth[] = [sampleWithRequiredData];
        expectedResult = service.addSystemHealthToCollectionIfMissing(systemHealthCollection, ...systemHealthArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const systemHealth: ISystemHealth = sampleWithRequiredData;
        const systemHealth2: ISystemHealth = sampleWithPartialData;
        expectedResult = service.addSystemHealthToCollectionIfMissing([], systemHealth, systemHealth2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemHealth);
        expect(expectedResult).toContain(systemHealth2);
      });

      it('should accept null and undefined values', () => {
        const systemHealth: ISystemHealth = sampleWithRequiredData;
        expectedResult = service.addSystemHealthToCollectionIfMissing([], null, systemHealth, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemHealth);
      });

      it('should return initial array if no SystemHealth is added', () => {
        const systemHealthCollection: ISystemHealth[] = [sampleWithRequiredData];
        expectedResult = service.addSystemHealthToCollectionIfMissing(systemHealthCollection, undefined, null);
        expect(expectedResult).toEqual(systemHealthCollection);
      });
    });

    describe('compareSystemHealth', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSystemHealth(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28359 };
        const entity2 = null;

        const compareResult1 = service.compareSystemHealth(entity1, entity2);
        const compareResult2 = service.compareSystemHealth(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28359 };
        const entity2 = { id: 10472 };

        const compareResult1 = service.compareSystemHealth(entity1, entity2);
        const compareResult2 = service.compareSystemHealth(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 28359 };
        const entity2 = { id: 28359 };

        const compareResult1 = service.compareSystemHealth(entity1, entity2);
        const compareResult2 = service.compareSystemHealth(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
