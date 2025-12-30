import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITransformWatermarkJob } from '../transform-watermark-job.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../transform-watermark-job.test-samples';

import { RestTransformWatermarkJob, TransformWatermarkJobService } from './transform-watermark-job.service';

const requireRestSample: RestTransformWatermarkJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('TransformWatermarkJob Service', () => {
  let service: TransformWatermarkJobService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransformWatermarkJob | ITransformWatermarkJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TransformWatermarkJobService);
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

    it('should create a TransformWatermarkJob', () => {
      const transformWatermarkJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transformWatermarkJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransformWatermarkJob', () => {
      const transformWatermarkJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transformWatermarkJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransformWatermarkJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransformWatermarkJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TransformWatermarkJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TransformWatermarkJob', () => {
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

    describe('addTransformWatermarkJobToCollectionIfMissing', () => {
      it('should add a TransformWatermarkJob to an empty array', () => {
        const transformWatermarkJob: ITransformWatermarkJob = sampleWithRequiredData;
        expectedResult = service.addTransformWatermarkJobToCollectionIfMissing([], transformWatermarkJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transformWatermarkJob);
      });

      it('should not add a TransformWatermarkJob to an array that contains it', () => {
        const transformWatermarkJob: ITransformWatermarkJob = sampleWithRequiredData;
        const transformWatermarkJobCollection: ITransformWatermarkJob[] = [
          {
            ...transformWatermarkJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransformWatermarkJobToCollectionIfMissing(transformWatermarkJobCollection, transformWatermarkJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransformWatermarkJob to an array that doesn't contain it", () => {
        const transformWatermarkJob: ITransformWatermarkJob = sampleWithRequiredData;
        const transformWatermarkJobCollection: ITransformWatermarkJob[] = [sampleWithPartialData];
        expectedResult = service.addTransformWatermarkJobToCollectionIfMissing(transformWatermarkJobCollection, transformWatermarkJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transformWatermarkJob);
      });

      it('should add only unique TransformWatermarkJob to an array', () => {
        const transformWatermarkJobArray: ITransformWatermarkJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const transformWatermarkJobCollection: ITransformWatermarkJob[] = [sampleWithRequiredData];
        expectedResult = service.addTransformWatermarkJobToCollectionIfMissing(
          transformWatermarkJobCollection,
          ...transformWatermarkJobArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transformWatermarkJob: ITransformWatermarkJob = sampleWithRequiredData;
        const transformWatermarkJob2: ITransformWatermarkJob = sampleWithPartialData;
        expectedResult = service.addTransformWatermarkJobToCollectionIfMissing([], transformWatermarkJob, transformWatermarkJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transformWatermarkJob);
        expect(expectedResult).toContain(transformWatermarkJob2);
      });

      it('should accept null and undefined values', () => {
        const transformWatermarkJob: ITransformWatermarkJob = sampleWithRequiredData;
        expectedResult = service.addTransformWatermarkJobToCollectionIfMissing([], null, transformWatermarkJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transformWatermarkJob);
      });

      it('should return initial array if no TransformWatermarkJob is added', () => {
        const transformWatermarkJobCollection: ITransformWatermarkJob[] = [sampleWithRequiredData];
        expectedResult = service.addTransformWatermarkJobToCollectionIfMissing(transformWatermarkJobCollection, undefined, null);
        expect(expectedResult).toEqual(transformWatermarkJobCollection);
      });
    });

    describe('compareTransformWatermarkJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransformWatermarkJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15528 };
        const entity2 = null;

        const compareResult1 = service.compareTransformWatermarkJob(entity1, entity2);
        const compareResult2 = service.compareTransformWatermarkJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15528 };
        const entity2 = { id: 16524 };

        const compareResult1 = service.compareTransformWatermarkJob(entity1, entity2);
        const compareResult2 = service.compareTransformWatermarkJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 15528 };
        const entity2 = { id: 15528 };

        const compareResult1 = service.compareTransformWatermarkJob(entity1, entity2);
        const compareResult2 = service.compareTransformWatermarkJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
