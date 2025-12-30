import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITransformRedactionJob } from '../transform-redaction-job.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../transform-redaction-job.test-samples';

import { RestTransformRedactionJob, TransformRedactionJobService } from './transform-redaction-job.service';

const requireRestSample: RestTransformRedactionJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('TransformRedactionJob Service', () => {
  let service: TransformRedactionJobService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransformRedactionJob | ITransformRedactionJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TransformRedactionJobService);
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

    it('should create a TransformRedactionJob', () => {
      const transformRedactionJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transformRedactionJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransformRedactionJob', () => {
      const transformRedactionJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transformRedactionJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransformRedactionJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransformRedactionJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TransformRedactionJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TransformRedactionJob', () => {
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

    describe('addTransformRedactionJobToCollectionIfMissing', () => {
      it('should add a TransformRedactionJob to an empty array', () => {
        const transformRedactionJob: ITransformRedactionJob = sampleWithRequiredData;
        expectedResult = service.addTransformRedactionJobToCollectionIfMissing([], transformRedactionJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transformRedactionJob);
      });

      it('should not add a TransformRedactionJob to an array that contains it', () => {
        const transformRedactionJob: ITransformRedactionJob = sampleWithRequiredData;
        const transformRedactionJobCollection: ITransformRedactionJob[] = [
          {
            ...transformRedactionJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransformRedactionJobToCollectionIfMissing(transformRedactionJobCollection, transformRedactionJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransformRedactionJob to an array that doesn't contain it", () => {
        const transformRedactionJob: ITransformRedactionJob = sampleWithRequiredData;
        const transformRedactionJobCollection: ITransformRedactionJob[] = [sampleWithPartialData];
        expectedResult = service.addTransformRedactionJobToCollectionIfMissing(transformRedactionJobCollection, transformRedactionJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transformRedactionJob);
      });

      it('should add only unique TransformRedactionJob to an array', () => {
        const transformRedactionJobArray: ITransformRedactionJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const transformRedactionJobCollection: ITransformRedactionJob[] = [sampleWithRequiredData];
        expectedResult = service.addTransformRedactionJobToCollectionIfMissing(
          transformRedactionJobCollection,
          ...transformRedactionJobArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transformRedactionJob: ITransformRedactionJob = sampleWithRequiredData;
        const transformRedactionJob2: ITransformRedactionJob = sampleWithPartialData;
        expectedResult = service.addTransformRedactionJobToCollectionIfMissing([], transformRedactionJob, transformRedactionJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transformRedactionJob);
        expect(expectedResult).toContain(transformRedactionJob2);
      });

      it('should accept null and undefined values', () => {
        const transformRedactionJob: ITransformRedactionJob = sampleWithRequiredData;
        expectedResult = service.addTransformRedactionJobToCollectionIfMissing([], null, transformRedactionJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transformRedactionJob);
      });

      it('should return initial array if no TransformRedactionJob is added', () => {
        const transformRedactionJobCollection: ITransformRedactionJob[] = [sampleWithRequiredData];
        expectedResult = service.addTransformRedactionJobToCollectionIfMissing(transformRedactionJobCollection, undefined, null);
        expect(expectedResult).toEqual(transformRedactionJobCollection);
      });
    });

    describe('compareTransformRedactionJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransformRedactionJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 32171 };
        const entity2 = null;

        const compareResult1 = service.compareTransformRedactionJob(entity1, entity2);
        const compareResult2 = service.compareTransformRedactionJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 32171 };
        const entity2 = { id: 28775 };

        const compareResult1 = service.compareTransformRedactionJob(entity1, entity2);
        const compareResult2 = service.compareTransformRedactionJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 32171 };
        const entity2 = { id: 32171 };

        const compareResult1 = service.compareTransformRedactionJob(entity1, entity2);
        const compareResult2 = service.compareTransformRedactionJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
