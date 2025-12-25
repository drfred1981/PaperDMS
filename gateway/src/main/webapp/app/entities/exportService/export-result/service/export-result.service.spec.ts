import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IExportResult } from '../export-result.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../export-result.test-samples';

import { ExportResultService, RestExportResult } from './export-result.service';

const requireRestSample: RestExportResult = {
  ...sampleWithRequiredData,
  exportedDate: sampleWithRequiredData.exportedDate?.toJSON(),
};

describe('ExportResult Service', () => {
  let service: ExportResultService;
  let httpMock: HttpTestingController;
  let expectedResult: IExportResult | IExportResult[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ExportResultService);
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

    it('should create a ExportResult', () => {
      const exportResult = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(exportResult).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExportResult', () => {
      const exportResult = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(exportResult).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExportResult', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExportResult', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExportResult', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExportResultToCollectionIfMissing', () => {
      it('should add a ExportResult to an empty array', () => {
        const exportResult: IExportResult = sampleWithRequiredData;
        expectedResult = service.addExportResultToCollectionIfMissing([], exportResult);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(exportResult);
      });

      it('should not add a ExportResult to an array that contains it', () => {
        const exportResult: IExportResult = sampleWithRequiredData;
        const exportResultCollection: IExportResult[] = [
          {
            ...exportResult,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExportResultToCollectionIfMissing(exportResultCollection, exportResult);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExportResult to an array that doesn't contain it", () => {
        const exportResult: IExportResult = sampleWithRequiredData;
        const exportResultCollection: IExportResult[] = [sampleWithPartialData];
        expectedResult = service.addExportResultToCollectionIfMissing(exportResultCollection, exportResult);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(exportResult);
      });

      it('should add only unique ExportResult to an array', () => {
        const exportResultArray: IExportResult[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const exportResultCollection: IExportResult[] = [sampleWithRequiredData];
        expectedResult = service.addExportResultToCollectionIfMissing(exportResultCollection, ...exportResultArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const exportResult: IExportResult = sampleWithRequiredData;
        const exportResult2: IExportResult = sampleWithPartialData;
        expectedResult = service.addExportResultToCollectionIfMissing([], exportResult, exportResult2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(exportResult);
        expect(expectedResult).toContain(exportResult2);
      });

      it('should accept null and undefined values', () => {
        const exportResult: IExportResult = sampleWithRequiredData;
        expectedResult = service.addExportResultToCollectionIfMissing([], null, exportResult, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(exportResult);
      });

      it('should return initial array if no ExportResult is added', () => {
        const exportResultCollection: IExportResult[] = [sampleWithRequiredData];
        expectedResult = service.addExportResultToCollectionIfMissing(exportResultCollection, undefined, null);
        expect(expectedResult).toEqual(exportResultCollection);
      });
    });

    describe('compareExportResult', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExportResult(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 21181 };
        const entity2 = null;

        const compareResult1 = service.compareExportResult(entity1, entity2);
        const compareResult2 = service.compareExportResult(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 21181 };
        const entity2 = { id: 16132 };

        const compareResult1 = service.compareExportResult(entity1, entity2);
        const compareResult2 = service.compareExportResult(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 21181 };
        const entity2 = { id: 21181 };

        const compareResult1 = service.compareExportResult(entity1, entity2);
        const compareResult2 = service.compareExportResult(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
