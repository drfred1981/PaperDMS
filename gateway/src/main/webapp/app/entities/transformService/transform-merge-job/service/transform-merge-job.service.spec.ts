import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITransformMergeJob } from '../transform-merge-job.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../transform-merge-job.test-samples';

import { RestTransformMergeJob, TransformMergeJobService } from './transform-merge-job.service';

const requireRestSample: RestTransformMergeJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('TransformMergeJob Service', () => {
  let service: TransformMergeJobService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransformMergeJob | ITransformMergeJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TransformMergeJobService);
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

    it('should create a TransformMergeJob', () => {
      const transformMergeJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transformMergeJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransformMergeJob', () => {
      const transformMergeJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transformMergeJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransformMergeJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransformMergeJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TransformMergeJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TransformMergeJob', () => {
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

    describe('addTransformMergeJobToCollectionIfMissing', () => {
      it('should add a TransformMergeJob to an empty array', () => {
        const transformMergeJob: ITransformMergeJob = sampleWithRequiredData;
        expectedResult = service.addTransformMergeJobToCollectionIfMissing([], transformMergeJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transformMergeJob);
      });

      it('should not add a TransformMergeJob to an array that contains it', () => {
        const transformMergeJob: ITransformMergeJob = sampleWithRequiredData;
        const transformMergeJobCollection: ITransformMergeJob[] = [
          {
            ...transformMergeJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransformMergeJobToCollectionIfMissing(transformMergeJobCollection, transformMergeJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransformMergeJob to an array that doesn't contain it", () => {
        const transformMergeJob: ITransformMergeJob = sampleWithRequiredData;
        const transformMergeJobCollection: ITransformMergeJob[] = [sampleWithPartialData];
        expectedResult = service.addTransformMergeJobToCollectionIfMissing(transformMergeJobCollection, transformMergeJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transformMergeJob);
      });

      it('should add only unique TransformMergeJob to an array', () => {
        const transformMergeJobArray: ITransformMergeJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const transformMergeJobCollection: ITransformMergeJob[] = [sampleWithRequiredData];
        expectedResult = service.addTransformMergeJobToCollectionIfMissing(transformMergeJobCollection, ...transformMergeJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transformMergeJob: ITransformMergeJob = sampleWithRequiredData;
        const transformMergeJob2: ITransformMergeJob = sampleWithPartialData;
        expectedResult = service.addTransformMergeJobToCollectionIfMissing([], transformMergeJob, transformMergeJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transformMergeJob);
        expect(expectedResult).toContain(transformMergeJob2);
      });

      it('should accept null and undefined values', () => {
        const transformMergeJob: ITransformMergeJob = sampleWithRequiredData;
        expectedResult = service.addTransformMergeJobToCollectionIfMissing([], null, transformMergeJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transformMergeJob);
      });

      it('should return initial array if no TransformMergeJob is added', () => {
        const transformMergeJobCollection: ITransformMergeJob[] = [sampleWithRequiredData];
        expectedResult = service.addTransformMergeJobToCollectionIfMissing(transformMergeJobCollection, undefined, null);
        expect(expectedResult).toEqual(transformMergeJobCollection);
      });
    });

    describe('compareTransformMergeJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransformMergeJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7858 };
        const entity2 = null;

        const compareResult1 = service.compareTransformMergeJob(entity1, entity2);
        const compareResult2 = service.compareTransformMergeJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7858 };
        const entity2 = { id: 17170 };

        const compareResult1 = service.compareTransformMergeJob(entity1, entity2);
        const compareResult2 = service.compareTransformMergeJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7858 };
        const entity2 = { id: 7858 };

        const compareResult1 = service.compareTransformMergeJob(entity1, entity2);
        const compareResult2 = service.compareTransformMergeJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
