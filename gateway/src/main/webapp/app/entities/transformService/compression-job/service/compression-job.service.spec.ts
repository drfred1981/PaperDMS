import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICompressionJob } from '../compression-job.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../compression-job.test-samples';

import { CompressionJobService, RestCompressionJob } from './compression-job.service';

const requireRestSample: RestCompressionJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('CompressionJob Service', () => {
  let service: CompressionJobService;
  let httpMock: HttpTestingController;
  let expectedResult: ICompressionJob | ICompressionJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CompressionJobService);
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

    it('should create a CompressionJob', () => {
      const compressionJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(compressionJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CompressionJob', () => {
      const compressionJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(compressionJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CompressionJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CompressionJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CompressionJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCompressionJobToCollectionIfMissing', () => {
      it('should add a CompressionJob to an empty array', () => {
        const compressionJob: ICompressionJob = sampleWithRequiredData;
        expectedResult = service.addCompressionJobToCollectionIfMissing([], compressionJob);
        expect(expectedResult).toEqual([compressionJob]);
      });

      it('should not add a CompressionJob to an array that contains it', () => {
        const compressionJob: ICompressionJob = sampleWithRequiredData;
        const compressionJobCollection: ICompressionJob[] = [
          {
            ...compressionJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCompressionJobToCollectionIfMissing(compressionJobCollection, compressionJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CompressionJob to an array that doesn't contain it", () => {
        const compressionJob: ICompressionJob = sampleWithRequiredData;
        const compressionJobCollection: ICompressionJob[] = [sampleWithPartialData];
        expectedResult = service.addCompressionJobToCollectionIfMissing(compressionJobCollection, compressionJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compressionJob);
      });

      it('should add only unique CompressionJob to an array', () => {
        const compressionJobArray: ICompressionJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const compressionJobCollection: ICompressionJob[] = [sampleWithRequiredData];
        expectedResult = service.addCompressionJobToCollectionIfMissing(compressionJobCollection, ...compressionJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const compressionJob: ICompressionJob = sampleWithRequiredData;
        const compressionJob2: ICompressionJob = sampleWithPartialData;
        expectedResult = service.addCompressionJobToCollectionIfMissing([], compressionJob, compressionJob2);
        expect(expectedResult).toEqual([compressionJob, compressionJob2]);
      });

      it('should accept null and undefined values', () => {
        const compressionJob: ICompressionJob = sampleWithRequiredData;
        expectedResult = service.addCompressionJobToCollectionIfMissing([], null, compressionJob, undefined);
        expect(expectedResult).toEqual([compressionJob]);
      });

      it('should return initial array if no CompressionJob is added', () => {
        const compressionJobCollection: ICompressionJob[] = [sampleWithRequiredData];
        expectedResult = service.addCompressionJobToCollectionIfMissing(compressionJobCollection, undefined, null);
        expect(expectedResult).toEqual(compressionJobCollection);
      });
    });

    describe('compareCompressionJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCompressionJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5819 };
        const entity2 = null;

        const compareResult1 = service.compareCompressionJob(entity1, entity2);
        const compareResult2 = service.compareCompressionJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5819 };
        const entity2 = { id: 923 };

        const compareResult1 = service.compareCompressionJob(entity1, entity2);
        const compareResult2 = service.compareCompressionJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5819 };
        const entity2 = { id: 5819 };

        const compareResult1 = service.compareCompressionJob(entity1, entity2);
        const compareResult2 = service.compareCompressionJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
