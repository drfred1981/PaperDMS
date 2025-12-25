import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IExportJob } from '../export-job.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../export-job.test-samples';

import { ExportJobService, RestExportJob } from './export-job.service';

const requireRestSample: RestExportJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('ExportJob Service', () => {
  let service: ExportJobService;
  let httpMock: HttpTestingController;
  let expectedResult: IExportJob | IExportJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ExportJobService);
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

    it('should create a ExportJob', () => {
      const exportJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(exportJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExportJob', () => {
      const exportJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(exportJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExportJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExportJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExportJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExportJobToCollectionIfMissing', () => {
      it('should add a ExportJob to an empty array', () => {
        const exportJob: IExportJob = sampleWithRequiredData;
        expectedResult = service.addExportJobToCollectionIfMissing([], exportJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(exportJob);
      });

      it('should not add a ExportJob to an array that contains it', () => {
        const exportJob: IExportJob = sampleWithRequiredData;
        const exportJobCollection: IExportJob[] = [
          {
            ...exportJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExportJobToCollectionIfMissing(exportJobCollection, exportJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExportJob to an array that doesn't contain it", () => {
        const exportJob: IExportJob = sampleWithRequiredData;
        const exportJobCollection: IExportJob[] = [sampleWithPartialData];
        expectedResult = service.addExportJobToCollectionIfMissing(exportJobCollection, exportJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(exportJob);
      });

      it('should add only unique ExportJob to an array', () => {
        const exportJobArray: IExportJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const exportJobCollection: IExportJob[] = [sampleWithRequiredData];
        expectedResult = service.addExportJobToCollectionIfMissing(exportJobCollection, ...exportJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const exportJob: IExportJob = sampleWithRequiredData;
        const exportJob2: IExportJob = sampleWithPartialData;
        expectedResult = service.addExportJobToCollectionIfMissing([], exportJob, exportJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(exportJob);
        expect(expectedResult).toContain(exportJob2);
      });

      it('should accept null and undefined values', () => {
        const exportJob: IExportJob = sampleWithRequiredData;
        expectedResult = service.addExportJobToCollectionIfMissing([], null, exportJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(exportJob);
      });

      it('should return initial array if no ExportJob is added', () => {
        const exportJobCollection: IExportJob[] = [sampleWithRequiredData];
        expectedResult = service.addExportJobToCollectionIfMissing(exportJobCollection, undefined, null);
        expect(expectedResult).toEqual(exportJobCollection);
      });
    });

    describe('compareExportJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExportJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4139 };
        const entity2 = null;

        const compareResult1 = service.compareExportJob(entity1, entity2);
        const compareResult2 = service.compareExportJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4139 };
        const entity2 = { id: 24008 };

        const compareResult1 = service.compareExportJob(entity1, entity2);
        const compareResult2 = service.compareExportJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 4139 };
        const entity2 = { id: 4139 };

        const compareResult1 = service.compareExportJob(entity1, entity2);
        const compareResult2 = service.compareExportJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
