import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAutoTagJob } from '../auto-tag-job.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../auto-tag-job.test-samples';

import { AutoTagJobService, RestAutoTagJob } from './auto-tag-job.service';

const requireRestSample: RestAutoTagJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('AutoTagJob Service', () => {
  let service: AutoTagJobService;
  let httpMock: HttpTestingController;
  let expectedResult: IAutoTagJob | IAutoTagJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AutoTagJobService);
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

    it('should create a AutoTagJob', () => {
      const autoTagJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(autoTagJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AutoTagJob', () => {
      const autoTagJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(autoTagJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AutoTagJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AutoTagJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AutoTagJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAutoTagJobToCollectionIfMissing', () => {
      it('should add a AutoTagJob to an empty array', () => {
        const autoTagJob: IAutoTagJob = sampleWithRequiredData;
        expectedResult = service.addAutoTagJobToCollectionIfMissing([], autoTagJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(autoTagJob);
      });

      it('should not add a AutoTagJob to an array that contains it', () => {
        const autoTagJob: IAutoTagJob = sampleWithRequiredData;
        const autoTagJobCollection: IAutoTagJob[] = [
          {
            ...autoTagJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAutoTagJobToCollectionIfMissing(autoTagJobCollection, autoTagJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AutoTagJob to an array that doesn't contain it", () => {
        const autoTagJob: IAutoTagJob = sampleWithRequiredData;
        const autoTagJobCollection: IAutoTagJob[] = [sampleWithPartialData];
        expectedResult = service.addAutoTagJobToCollectionIfMissing(autoTagJobCollection, autoTagJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(autoTagJob);
      });

      it('should add only unique AutoTagJob to an array', () => {
        const autoTagJobArray: IAutoTagJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const autoTagJobCollection: IAutoTagJob[] = [sampleWithRequiredData];
        expectedResult = service.addAutoTagJobToCollectionIfMissing(autoTagJobCollection, ...autoTagJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const autoTagJob: IAutoTagJob = sampleWithRequiredData;
        const autoTagJob2: IAutoTagJob = sampleWithPartialData;
        expectedResult = service.addAutoTagJobToCollectionIfMissing([], autoTagJob, autoTagJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(autoTagJob);
        expect(expectedResult).toContain(autoTagJob2);
      });

      it('should accept null and undefined values', () => {
        const autoTagJob: IAutoTagJob = sampleWithRequiredData;
        expectedResult = service.addAutoTagJobToCollectionIfMissing([], null, autoTagJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(autoTagJob);
      });

      it('should return initial array if no AutoTagJob is added', () => {
        const autoTagJobCollection: IAutoTagJob[] = [sampleWithRequiredData];
        expectedResult = service.addAutoTagJobToCollectionIfMissing(autoTagJobCollection, undefined, null);
        expect(expectedResult).toEqual(autoTagJobCollection);
      });
    });

    describe('compareAutoTagJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAutoTagJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5115 };
        const entity2 = null;

        const compareResult1 = service.compareAutoTagJob(entity1, entity2);
        const compareResult2 = service.compareAutoTagJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5115 };
        const entity2 = { id: 3881 };

        const compareResult1 = service.compareAutoTagJob(entity1, entity2);
        const compareResult2 = service.compareAutoTagJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5115 };
        const entity2 = { id: 5115 };

        const compareResult1 = service.compareAutoTagJob(entity1, entity2);
        const compareResult2 = service.compareAutoTagJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
