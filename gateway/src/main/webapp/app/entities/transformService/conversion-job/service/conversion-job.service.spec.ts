import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IConversionJob } from '../conversion-job.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../conversion-job.test-samples';

import { ConversionJobService, RestConversionJob } from './conversion-job.service';

const requireRestSample: RestConversionJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('ConversionJob Service', () => {
  let service: ConversionJobService;
  let httpMock: HttpTestingController;
  let expectedResult: IConversionJob | IConversionJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ConversionJobService);
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

    it('should create a ConversionJob', () => {
      const conversionJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(conversionJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ConversionJob', () => {
      const conversionJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(conversionJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ConversionJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ConversionJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ConversionJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addConversionJobToCollectionIfMissing', () => {
      it('should add a ConversionJob to an empty array', () => {
        const conversionJob: IConversionJob = sampleWithRequiredData;
        expectedResult = service.addConversionJobToCollectionIfMissing([], conversionJob);
        expect(expectedResult).toEqual([conversionJob]);
      });

      it('should not add a ConversionJob to an array that contains it', () => {
        const conversionJob: IConversionJob = sampleWithRequiredData;
        const conversionJobCollection: IConversionJob[] = [
          {
            ...conversionJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConversionJobToCollectionIfMissing(conversionJobCollection, conversionJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ConversionJob to an array that doesn't contain it", () => {
        const conversionJob: IConversionJob = sampleWithRequiredData;
        const conversionJobCollection: IConversionJob[] = [sampleWithPartialData];
        expectedResult = service.addConversionJobToCollectionIfMissing(conversionJobCollection, conversionJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(conversionJob);
      });

      it('should add only unique ConversionJob to an array', () => {
        const conversionJobArray: IConversionJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const conversionJobCollection: IConversionJob[] = [sampleWithRequiredData];
        expectedResult = service.addConversionJobToCollectionIfMissing(conversionJobCollection, ...conversionJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const conversionJob: IConversionJob = sampleWithRequiredData;
        const conversionJob2: IConversionJob = sampleWithPartialData;
        expectedResult = service.addConversionJobToCollectionIfMissing([], conversionJob, conversionJob2);
        expect(expectedResult).toEqual([conversionJob, conversionJob2]);
      });

      it('should accept null and undefined values', () => {
        const conversionJob: IConversionJob = sampleWithRequiredData;
        expectedResult = service.addConversionJobToCollectionIfMissing([], null, conversionJob, undefined);
        expect(expectedResult).toEqual([conversionJob]);
      });

      it('should return initial array if no ConversionJob is added', () => {
        const conversionJobCollection: IConversionJob[] = [sampleWithRequiredData];
        expectedResult = service.addConversionJobToCollectionIfMissing(conversionJobCollection, undefined, null);
        expect(expectedResult).toEqual(conversionJobCollection);
      });
    });

    describe('compareConversionJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConversionJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 479 };
        const entity2 = null;

        const compareResult1 = service.compareConversionJob(entity1, entity2);
        const compareResult2 = service.compareConversionJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 479 };
        const entity2 = { id: 1993 };

        const compareResult1 = service.compareConversionJob(entity1, entity2);
        const compareResult2 = service.compareConversionJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 479 };
        const entity2 = { id: 479 };

        const compareResult1 = service.compareConversionJob(entity1, entity2);
        const compareResult2 = service.compareConversionJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
