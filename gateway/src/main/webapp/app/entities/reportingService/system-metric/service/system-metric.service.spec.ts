import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISystemMetric } from '../system-metric.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../system-metric.test-samples';

import { RestSystemMetric, SystemMetricService } from './system-metric.service';

const requireRestSample: RestSystemMetric = {
  ...sampleWithRequiredData,
  timestamp: sampleWithRequiredData.timestamp?.toJSON(),
};

describe('SystemMetric Service', () => {
  let service: SystemMetricService;
  let httpMock: HttpTestingController;
  let expectedResult: ISystemMetric | ISystemMetric[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SystemMetricService);
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

    it('should create a SystemMetric', () => {
      const systemMetric = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(systemMetric).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SystemMetric', () => {
      const systemMetric = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(systemMetric).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SystemMetric', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SystemMetric', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SystemMetric', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSystemMetricToCollectionIfMissing', () => {
      it('should add a SystemMetric to an empty array', () => {
        const systemMetric: ISystemMetric = sampleWithRequiredData;
        expectedResult = service.addSystemMetricToCollectionIfMissing([], systemMetric);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemMetric);
      });

      it('should not add a SystemMetric to an array that contains it', () => {
        const systemMetric: ISystemMetric = sampleWithRequiredData;
        const systemMetricCollection: ISystemMetric[] = [
          {
            ...systemMetric,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSystemMetricToCollectionIfMissing(systemMetricCollection, systemMetric);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SystemMetric to an array that doesn't contain it", () => {
        const systemMetric: ISystemMetric = sampleWithRequiredData;
        const systemMetricCollection: ISystemMetric[] = [sampleWithPartialData];
        expectedResult = service.addSystemMetricToCollectionIfMissing(systemMetricCollection, systemMetric);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemMetric);
      });

      it('should add only unique SystemMetric to an array', () => {
        const systemMetricArray: ISystemMetric[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const systemMetricCollection: ISystemMetric[] = [sampleWithRequiredData];
        expectedResult = service.addSystemMetricToCollectionIfMissing(systemMetricCollection, ...systemMetricArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const systemMetric: ISystemMetric = sampleWithRequiredData;
        const systemMetric2: ISystemMetric = sampleWithPartialData;
        expectedResult = service.addSystemMetricToCollectionIfMissing([], systemMetric, systemMetric2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemMetric);
        expect(expectedResult).toContain(systemMetric2);
      });

      it('should accept null and undefined values', () => {
        const systemMetric: ISystemMetric = sampleWithRequiredData;
        expectedResult = service.addSystemMetricToCollectionIfMissing([], null, systemMetric, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemMetric);
      });

      it('should return initial array if no SystemMetric is added', () => {
        const systemMetricCollection: ISystemMetric[] = [sampleWithRequiredData];
        expectedResult = service.addSystemMetricToCollectionIfMissing(systemMetricCollection, undefined, null);
        expect(expectedResult).toEqual(systemMetricCollection);
      });
    });

    describe('compareSystemMetric', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSystemMetric(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14068 };
        const entity2 = null;

        const compareResult1 = service.compareSystemMetric(entity1, entity2);
        const compareResult2 = service.compareSystemMetric(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14068 };
        const entity2 = { id: 9908 };

        const compareResult1 = service.compareSystemMetric(entity1, entity2);
        const compareResult2 = service.compareSystemMetric(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 14068 };
        const entity2 = { id: 14068 };

        const compareResult1 = service.compareSystemMetric(entity1, entity2);
        const compareResult2 = service.compareSystemMetric(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
