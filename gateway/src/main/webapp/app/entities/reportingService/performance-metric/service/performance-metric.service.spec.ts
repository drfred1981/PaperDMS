import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPerformanceMetric } from '../performance-metric.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../performance-metric.test-samples';

import { PerformanceMetricService, RestPerformanceMetric } from './performance-metric.service';

const requireRestSample: RestPerformanceMetric = {
  ...sampleWithRequiredData,
  timestamp: sampleWithRequiredData.timestamp?.toJSON(),
};

describe('PerformanceMetric Service', () => {
  let service: PerformanceMetricService;
  let httpMock: HttpTestingController;
  let expectedResult: IPerformanceMetric | IPerformanceMetric[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PerformanceMetricService);
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

    it('should create a PerformanceMetric', () => {
      const performanceMetric = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(performanceMetric).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PerformanceMetric', () => {
      const performanceMetric = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(performanceMetric).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PerformanceMetric', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PerformanceMetric', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PerformanceMetric', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPerformanceMetricToCollectionIfMissing', () => {
      it('should add a PerformanceMetric to an empty array', () => {
        const performanceMetric: IPerformanceMetric = sampleWithRequiredData;
        expectedResult = service.addPerformanceMetricToCollectionIfMissing([], performanceMetric);
        expect(expectedResult).toEqual([performanceMetric]);
      });

      it('should not add a PerformanceMetric to an array that contains it', () => {
        const performanceMetric: IPerformanceMetric = sampleWithRequiredData;
        const performanceMetricCollection: IPerformanceMetric[] = [
          {
            ...performanceMetric,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPerformanceMetricToCollectionIfMissing(performanceMetricCollection, performanceMetric);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PerformanceMetric to an array that doesn't contain it", () => {
        const performanceMetric: IPerformanceMetric = sampleWithRequiredData;
        const performanceMetricCollection: IPerformanceMetric[] = [sampleWithPartialData];
        expectedResult = service.addPerformanceMetricToCollectionIfMissing(performanceMetricCollection, performanceMetric);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(performanceMetric);
      });

      it('should add only unique PerformanceMetric to an array', () => {
        const performanceMetricArray: IPerformanceMetric[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const performanceMetricCollection: IPerformanceMetric[] = [sampleWithRequiredData];
        expectedResult = service.addPerformanceMetricToCollectionIfMissing(performanceMetricCollection, ...performanceMetricArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const performanceMetric: IPerformanceMetric = sampleWithRequiredData;
        const performanceMetric2: IPerformanceMetric = sampleWithPartialData;
        expectedResult = service.addPerformanceMetricToCollectionIfMissing([], performanceMetric, performanceMetric2);
        expect(expectedResult).toEqual([performanceMetric, performanceMetric2]);
      });

      it('should accept null and undefined values', () => {
        const performanceMetric: IPerformanceMetric = sampleWithRequiredData;
        expectedResult = service.addPerformanceMetricToCollectionIfMissing([], null, performanceMetric, undefined);
        expect(expectedResult).toEqual([performanceMetric]);
      });

      it('should return initial array if no PerformanceMetric is added', () => {
        const performanceMetricCollection: IPerformanceMetric[] = [sampleWithRequiredData];
        expectedResult = service.addPerformanceMetricToCollectionIfMissing(performanceMetricCollection, undefined, null);
        expect(expectedResult).toEqual(performanceMetricCollection);
      });
    });

    describe('comparePerformanceMetric', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePerformanceMetric(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7414 };
        const entity2 = null;

        const compareResult1 = service.comparePerformanceMetric(entity1, entity2);
        const compareResult2 = service.comparePerformanceMetric(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7414 };
        const entity2 = { id: 23965 };

        const compareResult1 = service.comparePerformanceMetric(entity1, entity2);
        const compareResult2 = service.comparePerformanceMetric(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7414 };
        const entity2 = { id: 7414 };

        const compareResult1 = service.comparePerformanceMetric(entity1, entity2);
        const compareResult2 = service.comparePerformanceMetric(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
