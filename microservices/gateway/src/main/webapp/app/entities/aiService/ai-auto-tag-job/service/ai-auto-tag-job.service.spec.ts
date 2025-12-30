import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAIAutoTagJob } from '../ai-auto-tag-job.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ai-auto-tag-job.test-samples';

import { AIAutoTagJobService, RestAIAutoTagJob } from './ai-auto-tag-job.service';

const requireRestSample: RestAIAutoTagJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('AIAutoTagJob Service', () => {
  let service: AIAutoTagJobService;
  let httpMock: HttpTestingController;
  let expectedResult: IAIAutoTagJob | IAIAutoTagJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AIAutoTagJobService);
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

    it('should create a AIAutoTagJob', () => {
      const aIAutoTagJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aIAutoTagJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AIAutoTagJob', () => {
      const aIAutoTagJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aIAutoTagJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AIAutoTagJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AIAutoTagJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AIAutoTagJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AIAutoTagJob', () => {
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

    describe('addAIAutoTagJobToCollectionIfMissing', () => {
      it('should add a AIAutoTagJob to an empty array', () => {
        const aIAutoTagJob: IAIAutoTagJob = sampleWithRequiredData;
        expectedResult = service.addAIAutoTagJobToCollectionIfMissing([], aIAutoTagJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aIAutoTagJob);
      });

      it('should not add a AIAutoTagJob to an array that contains it', () => {
        const aIAutoTagJob: IAIAutoTagJob = sampleWithRequiredData;
        const aIAutoTagJobCollection: IAIAutoTagJob[] = [
          {
            ...aIAutoTagJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAIAutoTagJobToCollectionIfMissing(aIAutoTagJobCollection, aIAutoTagJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AIAutoTagJob to an array that doesn't contain it", () => {
        const aIAutoTagJob: IAIAutoTagJob = sampleWithRequiredData;
        const aIAutoTagJobCollection: IAIAutoTagJob[] = [sampleWithPartialData];
        expectedResult = service.addAIAutoTagJobToCollectionIfMissing(aIAutoTagJobCollection, aIAutoTagJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aIAutoTagJob);
      });

      it('should add only unique AIAutoTagJob to an array', () => {
        const aIAutoTagJobArray: IAIAutoTagJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aIAutoTagJobCollection: IAIAutoTagJob[] = [sampleWithRequiredData];
        expectedResult = service.addAIAutoTagJobToCollectionIfMissing(aIAutoTagJobCollection, ...aIAutoTagJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aIAutoTagJob: IAIAutoTagJob = sampleWithRequiredData;
        const aIAutoTagJob2: IAIAutoTagJob = sampleWithPartialData;
        expectedResult = service.addAIAutoTagJobToCollectionIfMissing([], aIAutoTagJob, aIAutoTagJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aIAutoTagJob);
        expect(expectedResult).toContain(aIAutoTagJob2);
      });

      it('should accept null and undefined values', () => {
        const aIAutoTagJob: IAIAutoTagJob = sampleWithRequiredData;
        expectedResult = service.addAIAutoTagJobToCollectionIfMissing([], null, aIAutoTagJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aIAutoTagJob);
      });

      it('should return initial array if no AIAutoTagJob is added', () => {
        const aIAutoTagJobCollection: IAIAutoTagJob[] = [sampleWithRequiredData];
        expectedResult = service.addAIAutoTagJobToCollectionIfMissing(aIAutoTagJobCollection, undefined, null);
        expect(expectedResult).toEqual(aIAutoTagJobCollection);
      });
    });

    describe('compareAIAutoTagJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAIAutoTagJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7774 };
        const entity2 = null;

        const compareResult1 = service.compareAIAutoTagJob(entity1, entity2);
        const compareResult2 = service.compareAIAutoTagJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7774 };
        const entity2 = { id: 2294 };

        const compareResult1 = service.compareAIAutoTagJob(entity1, entity2);
        const compareResult2 = service.compareAIAutoTagJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7774 };
        const entity2 = { id: 7774 };

        const compareResult1 = service.compareAIAutoTagJob(entity1, entity2);
        const compareResult2 = service.compareAIAutoTagJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
