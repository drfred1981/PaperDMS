import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IComparisonJob } from '../comparison-job.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../comparison-job.test-samples';

import { ComparisonJobService, RestComparisonJob } from './comparison-job.service';

const requireRestSample: RestComparisonJob = {
  ...sampleWithRequiredData,
  comparedDate: sampleWithRequiredData.comparedDate?.toJSON(),
};

describe('ComparisonJob Service', () => {
  let service: ComparisonJobService;
  let httpMock: HttpTestingController;
  let expectedResult: IComparisonJob | IComparisonJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ComparisonJobService);
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

    it('should create a ComparisonJob', () => {
      const comparisonJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(comparisonJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ComparisonJob', () => {
      const comparisonJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(comparisonJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ComparisonJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ComparisonJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ComparisonJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addComparisonJobToCollectionIfMissing', () => {
      it('should add a ComparisonJob to an empty array', () => {
        const comparisonJob: IComparisonJob = sampleWithRequiredData;
        expectedResult = service.addComparisonJobToCollectionIfMissing([], comparisonJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(comparisonJob);
      });

      it('should not add a ComparisonJob to an array that contains it', () => {
        const comparisonJob: IComparisonJob = sampleWithRequiredData;
        const comparisonJobCollection: IComparisonJob[] = [
          {
            ...comparisonJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addComparisonJobToCollectionIfMissing(comparisonJobCollection, comparisonJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ComparisonJob to an array that doesn't contain it", () => {
        const comparisonJob: IComparisonJob = sampleWithRequiredData;
        const comparisonJobCollection: IComparisonJob[] = [sampleWithPartialData];
        expectedResult = service.addComparisonJobToCollectionIfMissing(comparisonJobCollection, comparisonJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(comparisonJob);
      });

      it('should add only unique ComparisonJob to an array', () => {
        const comparisonJobArray: IComparisonJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const comparisonJobCollection: IComparisonJob[] = [sampleWithRequiredData];
        expectedResult = service.addComparisonJobToCollectionIfMissing(comparisonJobCollection, ...comparisonJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const comparisonJob: IComparisonJob = sampleWithRequiredData;
        const comparisonJob2: IComparisonJob = sampleWithPartialData;
        expectedResult = service.addComparisonJobToCollectionIfMissing([], comparisonJob, comparisonJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(comparisonJob);
        expect(expectedResult).toContain(comparisonJob2);
      });

      it('should accept null and undefined values', () => {
        const comparisonJob: IComparisonJob = sampleWithRequiredData;
        expectedResult = service.addComparisonJobToCollectionIfMissing([], null, comparisonJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(comparisonJob);
      });

      it('should return initial array if no ComparisonJob is added', () => {
        const comparisonJobCollection: IComparisonJob[] = [sampleWithRequiredData];
        expectedResult = service.addComparisonJobToCollectionIfMissing(comparisonJobCollection, undefined, null);
        expect(expectedResult).toEqual(comparisonJobCollection);
      });
    });

    describe('compareComparisonJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareComparisonJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 29962 };
        const entity2 = null;

        const compareResult1 = service.compareComparisonJob(entity1, entity2);
        const compareResult2 = service.compareComparisonJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 29962 };
        const entity2 = { id: 31301 };

        const compareResult1 = service.compareComparisonJob(entity1, entity2);
        const compareResult2 = service.compareComparisonJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 29962 };
        const entity2 = { id: 29962 };

        const compareResult1 = service.compareComparisonJob(entity1, entity2);
        const compareResult2 = service.compareComparisonJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
