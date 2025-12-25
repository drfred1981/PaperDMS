import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IWatermarkJob } from '../watermark-job.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../watermark-job.test-samples';

import { RestWatermarkJob, WatermarkJobService } from './watermark-job.service';

const requireRestSample: RestWatermarkJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('WatermarkJob Service', () => {
  let service: WatermarkJobService;
  let httpMock: HttpTestingController;
  let expectedResult: IWatermarkJob | IWatermarkJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WatermarkJobService);
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

    it('should create a WatermarkJob', () => {
      const watermarkJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(watermarkJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WatermarkJob', () => {
      const watermarkJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(watermarkJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WatermarkJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WatermarkJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WatermarkJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWatermarkJobToCollectionIfMissing', () => {
      it('should add a WatermarkJob to an empty array', () => {
        const watermarkJob: IWatermarkJob = sampleWithRequiredData;
        expectedResult = service.addWatermarkJobToCollectionIfMissing([], watermarkJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(watermarkJob);
      });

      it('should not add a WatermarkJob to an array that contains it', () => {
        const watermarkJob: IWatermarkJob = sampleWithRequiredData;
        const watermarkJobCollection: IWatermarkJob[] = [
          {
            ...watermarkJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWatermarkJobToCollectionIfMissing(watermarkJobCollection, watermarkJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WatermarkJob to an array that doesn't contain it", () => {
        const watermarkJob: IWatermarkJob = sampleWithRequiredData;
        const watermarkJobCollection: IWatermarkJob[] = [sampleWithPartialData];
        expectedResult = service.addWatermarkJobToCollectionIfMissing(watermarkJobCollection, watermarkJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(watermarkJob);
      });

      it('should add only unique WatermarkJob to an array', () => {
        const watermarkJobArray: IWatermarkJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const watermarkJobCollection: IWatermarkJob[] = [sampleWithRequiredData];
        expectedResult = service.addWatermarkJobToCollectionIfMissing(watermarkJobCollection, ...watermarkJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const watermarkJob: IWatermarkJob = sampleWithRequiredData;
        const watermarkJob2: IWatermarkJob = sampleWithPartialData;
        expectedResult = service.addWatermarkJobToCollectionIfMissing([], watermarkJob, watermarkJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(watermarkJob);
        expect(expectedResult).toContain(watermarkJob2);
      });

      it('should accept null and undefined values', () => {
        const watermarkJob: IWatermarkJob = sampleWithRequiredData;
        expectedResult = service.addWatermarkJobToCollectionIfMissing([], null, watermarkJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(watermarkJob);
      });

      it('should return initial array if no WatermarkJob is added', () => {
        const watermarkJobCollection: IWatermarkJob[] = [sampleWithRequiredData];
        expectedResult = service.addWatermarkJobToCollectionIfMissing(watermarkJobCollection, undefined, null);
        expect(expectedResult).toEqual(watermarkJobCollection);
      });
    });

    describe('compareWatermarkJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWatermarkJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19288 };
        const entity2 = null;

        const compareResult1 = service.compareWatermarkJob(entity1, entity2);
        const compareResult2 = service.compareWatermarkJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19288 };
        const entity2 = { id: 21229 };

        const compareResult1 = service.compareWatermarkJob(entity1, entity2);
        const compareResult2 = service.compareWatermarkJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 19288 };
        const entity2 = { id: 19288 };

        const compareResult1 = service.compareWatermarkJob(entity1, entity2);
        const compareResult2 = service.compareWatermarkJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
