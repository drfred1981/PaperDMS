import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISimilarityJob } from '../similarity-job.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../similarity-job.test-samples';

import { RestSimilarityJob, SimilarityJobService } from './similarity-job.service';

const requireRestSample: RestSimilarityJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('SimilarityJob Service', () => {
  let service: SimilarityJobService;
  let httpMock: HttpTestingController;
  let expectedResult: ISimilarityJob | ISimilarityJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SimilarityJobService);
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

    it('should create a SimilarityJob', () => {
      const similarityJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(similarityJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SimilarityJob', () => {
      const similarityJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(similarityJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SimilarityJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SimilarityJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SimilarityJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a SimilarityJob', () => {
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

    describe('addSimilarityJobToCollectionIfMissing', () => {
      it('should add a SimilarityJob to an empty array', () => {
        const similarityJob: ISimilarityJob = sampleWithRequiredData;
        expectedResult = service.addSimilarityJobToCollectionIfMissing([], similarityJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(similarityJob);
      });

      it('should not add a SimilarityJob to an array that contains it', () => {
        const similarityJob: ISimilarityJob = sampleWithRequiredData;
        const similarityJobCollection: ISimilarityJob[] = [
          {
            ...similarityJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSimilarityJobToCollectionIfMissing(similarityJobCollection, similarityJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SimilarityJob to an array that doesn't contain it", () => {
        const similarityJob: ISimilarityJob = sampleWithRequiredData;
        const similarityJobCollection: ISimilarityJob[] = [sampleWithPartialData];
        expectedResult = service.addSimilarityJobToCollectionIfMissing(similarityJobCollection, similarityJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(similarityJob);
      });

      it('should add only unique SimilarityJob to an array', () => {
        const similarityJobArray: ISimilarityJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const similarityJobCollection: ISimilarityJob[] = [sampleWithRequiredData];
        expectedResult = service.addSimilarityJobToCollectionIfMissing(similarityJobCollection, ...similarityJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const similarityJob: ISimilarityJob = sampleWithRequiredData;
        const similarityJob2: ISimilarityJob = sampleWithPartialData;
        expectedResult = service.addSimilarityJobToCollectionIfMissing([], similarityJob, similarityJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(similarityJob);
        expect(expectedResult).toContain(similarityJob2);
      });

      it('should accept null and undefined values', () => {
        const similarityJob: ISimilarityJob = sampleWithRequiredData;
        expectedResult = service.addSimilarityJobToCollectionIfMissing([], null, similarityJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(similarityJob);
      });

      it('should return initial array if no SimilarityJob is added', () => {
        const similarityJobCollection: ISimilarityJob[] = [sampleWithRequiredData];
        expectedResult = service.addSimilarityJobToCollectionIfMissing(similarityJobCollection, undefined, null);
        expect(expectedResult).toEqual(similarityJobCollection);
      });
    });

    describe('compareSimilarityJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSimilarityJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10779 };
        const entity2 = null;

        const compareResult1 = service.compareSimilarityJob(entity1, entity2);
        const compareResult2 = service.compareSimilarityJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10779 };
        const entity2 = { id: 13718 };

        const compareResult1 = service.compareSimilarityJob(entity1, entity2);
        const compareResult2 = service.compareSimilarityJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 10779 };
        const entity2 = { id: 10779 };

        const compareResult1 = service.compareSimilarityJob(entity1, entity2);
        const compareResult2 = service.compareSimilarityJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
