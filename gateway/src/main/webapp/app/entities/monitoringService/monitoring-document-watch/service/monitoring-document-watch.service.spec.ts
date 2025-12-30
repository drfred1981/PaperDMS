import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMonitoringDocumentWatch } from '../monitoring-document-watch.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../monitoring-document-watch.test-samples';

import { MonitoringDocumentWatchService, RestMonitoringDocumentWatch } from './monitoring-document-watch.service';

const requireRestSample: RestMonitoringDocumentWatch = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('MonitoringDocumentWatch Service', () => {
  let service: MonitoringDocumentWatchService;
  let httpMock: HttpTestingController;
  let expectedResult: IMonitoringDocumentWatch | IMonitoringDocumentWatch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MonitoringDocumentWatchService);
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

    it('should create a MonitoringDocumentWatch', () => {
      const monitoringDocumentWatch = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(monitoringDocumentWatch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MonitoringDocumentWatch', () => {
      const monitoringDocumentWatch = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(monitoringDocumentWatch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MonitoringDocumentWatch', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MonitoringDocumentWatch', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MonitoringDocumentWatch', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MonitoringDocumentWatch', () => {
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

    describe('addMonitoringDocumentWatchToCollectionIfMissing', () => {
      it('should add a MonitoringDocumentWatch to an empty array', () => {
        const monitoringDocumentWatch: IMonitoringDocumentWatch = sampleWithRequiredData;
        expectedResult = service.addMonitoringDocumentWatchToCollectionIfMissing([], monitoringDocumentWatch);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monitoringDocumentWatch);
      });

      it('should not add a MonitoringDocumentWatch to an array that contains it', () => {
        const monitoringDocumentWatch: IMonitoringDocumentWatch = sampleWithRequiredData;
        const monitoringDocumentWatchCollection: IMonitoringDocumentWatch[] = [
          {
            ...monitoringDocumentWatch,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMonitoringDocumentWatchToCollectionIfMissing(
          monitoringDocumentWatchCollection,
          monitoringDocumentWatch,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MonitoringDocumentWatch to an array that doesn't contain it", () => {
        const monitoringDocumentWatch: IMonitoringDocumentWatch = sampleWithRequiredData;
        const monitoringDocumentWatchCollection: IMonitoringDocumentWatch[] = [sampleWithPartialData];
        expectedResult = service.addMonitoringDocumentWatchToCollectionIfMissing(
          monitoringDocumentWatchCollection,
          monitoringDocumentWatch,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monitoringDocumentWatch);
      });

      it('should add only unique MonitoringDocumentWatch to an array', () => {
        const monitoringDocumentWatchArray: IMonitoringDocumentWatch[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const monitoringDocumentWatchCollection: IMonitoringDocumentWatch[] = [sampleWithRequiredData];
        expectedResult = service.addMonitoringDocumentWatchToCollectionIfMissing(
          monitoringDocumentWatchCollection,
          ...monitoringDocumentWatchArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const monitoringDocumentWatch: IMonitoringDocumentWatch = sampleWithRequiredData;
        const monitoringDocumentWatch2: IMonitoringDocumentWatch = sampleWithPartialData;
        expectedResult = service.addMonitoringDocumentWatchToCollectionIfMissing([], monitoringDocumentWatch, monitoringDocumentWatch2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monitoringDocumentWatch);
        expect(expectedResult).toContain(monitoringDocumentWatch2);
      });

      it('should accept null and undefined values', () => {
        const monitoringDocumentWatch: IMonitoringDocumentWatch = sampleWithRequiredData;
        expectedResult = service.addMonitoringDocumentWatchToCollectionIfMissing([], null, monitoringDocumentWatch, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monitoringDocumentWatch);
      });

      it('should return initial array if no MonitoringDocumentWatch is added', () => {
        const monitoringDocumentWatchCollection: IMonitoringDocumentWatch[] = [sampleWithRequiredData];
        expectedResult = service.addMonitoringDocumentWatchToCollectionIfMissing(monitoringDocumentWatchCollection, undefined, null);
        expect(expectedResult).toEqual(monitoringDocumentWatchCollection);
      });
    });

    describe('compareMonitoringDocumentWatch', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMonitoringDocumentWatch(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10460 };
        const entity2 = null;

        const compareResult1 = service.compareMonitoringDocumentWatch(entity1, entity2);
        const compareResult2 = service.compareMonitoringDocumentWatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10460 };
        const entity2 = { id: 17970 };

        const compareResult1 = service.compareMonitoringDocumentWatch(entity1, entity2);
        const compareResult2 = service.compareMonitoringDocumentWatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 10460 };
        const entity2 = { id: 10460 };

        const compareResult1 = service.compareMonitoringDocumentWatch(entity1, entity2);
        const compareResult2 = service.compareMonitoringDocumentWatch(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
