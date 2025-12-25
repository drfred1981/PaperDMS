import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IArchiveJob } from '../archive-job.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../archive-job.test-samples';

import { ArchiveJobService, RestArchiveJob } from './archive-job.service';

const requireRestSample: RestArchiveJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('ArchiveJob Service', () => {
  let service: ArchiveJobService;
  let httpMock: HttpTestingController;
  let expectedResult: IArchiveJob | IArchiveJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ArchiveJobService);
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

    it('should create a ArchiveJob', () => {
      const archiveJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(archiveJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ArchiveJob', () => {
      const archiveJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(archiveJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ArchiveJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ArchiveJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ArchiveJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addArchiveJobToCollectionIfMissing', () => {
      it('should add a ArchiveJob to an empty array', () => {
        const archiveJob: IArchiveJob = sampleWithRequiredData;
        expectedResult = service.addArchiveJobToCollectionIfMissing([], archiveJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(archiveJob);
      });

      it('should not add a ArchiveJob to an array that contains it', () => {
        const archiveJob: IArchiveJob = sampleWithRequiredData;
        const archiveJobCollection: IArchiveJob[] = [
          {
            ...archiveJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addArchiveJobToCollectionIfMissing(archiveJobCollection, archiveJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ArchiveJob to an array that doesn't contain it", () => {
        const archiveJob: IArchiveJob = sampleWithRequiredData;
        const archiveJobCollection: IArchiveJob[] = [sampleWithPartialData];
        expectedResult = service.addArchiveJobToCollectionIfMissing(archiveJobCollection, archiveJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(archiveJob);
      });

      it('should add only unique ArchiveJob to an array', () => {
        const archiveJobArray: IArchiveJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const archiveJobCollection: IArchiveJob[] = [sampleWithRequiredData];
        expectedResult = service.addArchiveJobToCollectionIfMissing(archiveJobCollection, ...archiveJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const archiveJob: IArchiveJob = sampleWithRequiredData;
        const archiveJob2: IArchiveJob = sampleWithPartialData;
        expectedResult = service.addArchiveJobToCollectionIfMissing([], archiveJob, archiveJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(archiveJob);
        expect(expectedResult).toContain(archiveJob2);
      });

      it('should accept null and undefined values', () => {
        const archiveJob: IArchiveJob = sampleWithRequiredData;
        expectedResult = service.addArchiveJobToCollectionIfMissing([], null, archiveJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(archiveJob);
      });

      it('should return initial array if no ArchiveJob is added', () => {
        const archiveJobCollection: IArchiveJob[] = [sampleWithRequiredData];
        expectedResult = service.addArchiveJobToCollectionIfMissing(archiveJobCollection, undefined, null);
        expect(expectedResult).toEqual(archiveJobCollection);
      });
    });

    describe('compareArchiveJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareArchiveJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 16105 };
        const entity2 = null;

        const compareResult1 = service.compareArchiveJob(entity1, entity2);
        const compareResult2 = service.compareArchiveJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 16105 };
        const entity2 = { id: 11882 };

        const compareResult1 = service.compareArchiveJob(entity1, entity2);
        const compareResult2 = service.compareArchiveJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 16105 };
        const entity2 = { id: 16105 };

        const compareResult1 = service.compareArchiveJob(entity1, entity2);
        const compareResult2 = service.compareArchiveJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
