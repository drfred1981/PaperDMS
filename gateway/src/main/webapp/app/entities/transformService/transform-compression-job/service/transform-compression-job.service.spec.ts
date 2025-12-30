import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITransformCompressionJob } from '../transform-compression-job.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../transform-compression-job.test-samples';

import { RestTransformCompressionJob, TransformCompressionJobService } from './transform-compression-job.service';

const requireRestSample: RestTransformCompressionJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('TransformCompressionJob Service', () => {
  let service: TransformCompressionJobService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransformCompressionJob | ITransformCompressionJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TransformCompressionJobService);
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

    it('should create a TransformCompressionJob', () => {
      const transformCompressionJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transformCompressionJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransformCompressionJob', () => {
      const transformCompressionJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transformCompressionJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransformCompressionJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransformCompressionJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TransformCompressionJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TransformCompressionJob', () => {
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

    describe('addTransformCompressionJobToCollectionIfMissing', () => {
      it('should add a TransformCompressionJob to an empty array', () => {
        const transformCompressionJob: ITransformCompressionJob = sampleWithRequiredData;
        expectedResult = service.addTransformCompressionJobToCollectionIfMissing([], transformCompressionJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transformCompressionJob);
      });

      it('should not add a TransformCompressionJob to an array that contains it', () => {
        const transformCompressionJob: ITransformCompressionJob = sampleWithRequiredData;
        const transformCompressionJobCollection: ITransformCompressionJob[] = [
          {
            ...transformCompressionJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransformCompressionJobToCollectionIfMissing(
          transformCompressionJobCollection,
          transformCompressionJob,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransformCompressionJob to an array that doesn't contain it", () => {
        const transformCompressionJob: ITransformCompressionJob = sampleWithRequiredData;
        const transformCompressionJobCollection: ITransformCompressionJob[] = [sampleWithPartialData];
        expectedResult = service.addTransformCompressionJobToCollectionIfMissing(
          transformCompressionJobCollection,
          transformCompressionJob,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transformCompressionJob);
      });

      it('should add only unique TransformCompressionJob to an array', () => {
        const transformCompressionJobArray: ITransformCompressionJob[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const transformCompressionJobCollection: ITransformCompressionJob[] = [sampleWithRequiredData];
        expectedResult = service.addTransformCompressionJobToCollectionIfMissing(
          transformCompressionJobCollection,
          ...transformCompressionJobArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transformCompressionJob: ITransformCompressionJob = sampleWithRequiredData;
        const transformCompressionJob2: ITransformCompressionJob = sampleWithPartialData;
        expectedResult = service.addTransformCompressionJobToCollectionIfMissing([], transformCompressionJob, transformCompressionJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transformCompressionJob);
        expect(expectedResult).toContain(transformCompressionJob2);
      });

      it('should accept null and undefined values', () => {
        const transformCompressionJob: ITransformCompressionJob = sampleWithRequiredData;
        expectedResult = service.addTransformCompressionJobToCollectionIfMissing([], null, transformCompressionJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transformCompressionJob);
      });

      it('should return initial array if no TransformCompressionJob is added', () => {
        const transformCompressionJobCollection: ITransformCompressionJob[] = [sampleWithRequiredData];
        expectedResult = service.addTransformCompressionJobToCollectionIfMissing(transformCompressionJobCollection, undefined, null);
        expect(expectedResult).toEqual(transformCompressionJobCollection);
      });
    });

    describe('compareTransformCompressionJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransformCompressionJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13851 };
        const entity2 = null;

        const compareResult1 = service.compareTransformCompressionJob(entity1, entity2);
        const compareResult2 = service.compareTransformCompressionJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13851 };
        const entity2 = { id: 1867 };

        const compareResult1 = service.compareTransformCompressionJob(entity1, entity2);
        const compareResult2 = service.compareTransformCompressionJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 13851 };
        const entity2 = { id: 13851 };

        const compareResult1 = service.compareTransformCompressionJob(entity1, entity2);
        const compareResult2 = service.compareTransformCompressionJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
