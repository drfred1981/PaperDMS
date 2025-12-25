import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOcrJob } from '../ocr-job.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ocr-job.test-samples';

import { OcrJobService, RestOcrJob } from './ocr-job.service';

const requireRestSample: RestOcrJob = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('OcrJob Service', () => {
  let service: OcrJobService;
  let httpMock: HttpTestingController;
  let expectedResult: IOcrJob | IOcrJob[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OcrJobService);
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

    it('should create a OcrJob', () => {
      const ocrJob = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ocrJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OcrJob', () => {
      const ocrJob = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ocrJob).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OcrJob', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OcrJob', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OcrJob', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOcrJobToCollectionIfMissing', () => {
      it('should add a OcrJob to an empty array', () => {
        const ocrJob: IOcrJob = sampleWithRequiredData;
        expectedResult = service.addOcrJobToCollectionIfMissing([], ocrJob);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ocrJob);
      });

      it('should not add a OcrJob to an array that contains it', () => {
        const ocrJob: IOcrJob = sampleWithRequiredData;
        const ocrJobCollection: IOcrJob[] = [
          {
            ...ocrJob,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOcrJobToCollectionIfMissing(ocrJobCollection, ocrJob);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OcrJob to an array that doesn't contain it", () => {
        const ocrJob: IOcrJob = sampleWithRequiredData;
        const ocrJobCollection: IOcrJob[] = [sampleWithPartialData];
        expectedResult = service.addOcrJobToCollectionIfMissing(ocrJobCollection, ocrJob);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ocrJob);
      });

      it('should add only unique OcrJob to an array', () => {
        const ocrJobArray: IOcrJob[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ocrJobCollection: IOcrJob[] = [sampleWithRequiredData];
        expectedResult = service.addOcrJobToCollectionIfMissing(ocrJobCollection, ...ocrJobArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ocrJob: IOcrJob = sampleWithRequiredData;
        const ocrJob2: IOcrJob = sampleWithPartialData;
        expectedResult = service.addOcrJobToCollectionIfMissing([], ocrJob, ocrJob2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ocrJob);
        expect(expectedResult).toContain(ocrJob2);
      });

      it('should accept null and undefined values', () => {
        const ocrJob: IOcrJob = sampleWithRequiredData;
        expectedResult = service.addOcrJobToCollectionIfMissing([], null, ocrJob, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ocrJob);
      });

      it('should return initial array if no OcrJob is added', () => {
        const ocrJobCollection: IOcrJob[] = [sampleWithRequiredData];
        expectedResult = service.addOcrJobToCollectionIfMissing(ocrJobCollection, undefined, null);
        expect(expectedResult).toEqual(ocrJobCollection);
      });
    });

    describe('compareOcrJob', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOcrJob(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3289 };
        const entity2 = null;

        const compareResult1 = service.compareOcrJob(entity1, entity2);
        const compareResult2 = service.compareOcrJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3289 };
        const entity2 = { id: 11926 };

        const compareResult1 = service.compareOcrJob(entity1, entity2);
        const compareResult2 = service.compareOcrJob(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 3289 };
        const entity2 = { id: 3289 };

        const compareResult1 = service.compareOcrJob(entity1, entity2);
        const compareResult2 = service.compareOcrJob(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
