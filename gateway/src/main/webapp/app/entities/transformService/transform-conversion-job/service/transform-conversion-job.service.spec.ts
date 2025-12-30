import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITransformConversionJob } from '../transform-conversion-job.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../transform-conversion-job.test-samples';

import { RestTransformConversionJob, TransformConversionJobService } from './transform-conversion-job.service';

const requireRestSample: RestTransformConversionJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('TransformConversionJob Service', () => {
  let service: TransformConversionJobService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransformConversionJob | ITransformConversionJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TransformConversionJobService);
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

    it('should create a TransformConversionJob', () => {
      const transformConversionJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transformConversionJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransformConversionJob', () => {
      const transformConversionJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transformConversionJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransformConversionJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransformConversionJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TransformConversionJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TransformConversionJob', () => {
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

    describe('addTransformConversionJobToCollectionIfMissing', () => {
      it('should add a TransformConversionJob to an empty array', () => {
        const transformConversionJob: ITransformConversionJob = sampleWithRequiredData;
        expectedResult = service.addTransformConversionJobToCollectionIfMissing([], transformConversionJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transformConversionJob);
      });

      it('should not add a TransformConversionJob to an array that contains it', () => {
        const transformConversionJob: ITransformConversionJob = sampleWithRequiredData;
        const transformConversionJobCollection: ITransformConversionJob[] = [
          {
            ...transformConversionJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransformConversionJobToCollectionIfMissing(transformConversionJobCollection, transformConversionJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransformConversionJob to an array that doesn't contain it", () => {
        const transformConversionJob: ITransformConversionJob = sampleWithRequiredData;
        const transformConversionJobCollection: ITransformConversionJob[] = [sampleWithPartialData];
        expectedResult = service.addTransformConversionJobToCollectionIfMissing(transformConversionJobCollection, transformConversionJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transformConversionJob);
      });

      it('should add only unique TransformConversionJob to an array', () => {
        const transformConversionJobArray: ITransformConversionJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const transformConversionJobCollection: ITransformConversionJob[] = [sampleWithRequiredData];
        expectedResult = service.addTransformConversionJobToCollectionIfMissing(
          transformConversionJobCollection,
          ...transformConversionJobArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transformConversionJob: ITransformConversionJob = sampleWithRequiredData;
        const transformConversionJob2: ITransformConversionJob = sampleWithPartialData;
        expectedResult = service.addTransformConversionJobToCollectionIfMissing([], transformConversionJob, transformConversionJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transformConversionJob);
        expect(expectedResult).toContain(transformConversionJob2);
      });

      it('should accept null and undefined values', () => {
        const transformConversionJob: ITransformConversionJob = sampleWithRequiredData;
        expectedResult = service.addTransformConversionJobToCollectionIfMissing([], null, transformConversionJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transformConversionJob);
      });

      it('should return initial array if no TransformConversionJob is added', () => {
        const transformConversionJobCollection: ITransformConversionJob[] = [sampleWithRequiredData];
        expectedResult = service.addTransformConversionJobToCollectionIfMissing(transformConversionJobCollection, undefined, null);
        expect(expectedResult).toEqual(transformConversionJobCollection);
      });
    });

    describe('compareTransformConversionJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransformConversionJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 1719 };
        const entity2 = null;

        const compareResult1 = service.compareTransformConversionJob(entity1, entity2);
        const compareResult2 = service.compareTransformConversionJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 1719 };
        const entity2 = { id: 9217 };

        const compareResult1 = service.compareTransformConversionJob(entity1, entity2);
        const compareResult2 = service.compareTransformConversionJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 1719 };
        const entity2 = { id: 1719 };

        const compareResult1 = service.compareTransformConversionJob(entity1, entity2);
        const compareResult2 = service.compareTransformConversionJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
