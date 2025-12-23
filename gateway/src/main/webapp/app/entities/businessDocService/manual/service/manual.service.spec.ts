import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IManual } from '../manual.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../manual.test-samples';

import { ManualService, RestManual } from './manual.service';

const requireRestSample: RestManual = {
  ...sampleWithRequiredData,
  publicationDate: sampleWithRequiredData.publicationDate?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('Manual Service', () => {
  let service: ManualService;
  let httpMock: HttpTestingController;
  let expectedResult: IManual | IManual[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ManualService);
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

    it('should create a Manual', () => {
      const manual = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(manual).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Manual', () => {
      const manual = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(manual).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Manual', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Manual', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Manual', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Manual', () => {
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

    describe('addManualToCollectionIfMissing', () => {
      it('should add a Manual to an empty array', () => {
        const manual: IManual = sampleWithRequiredData;
        expectedResult = service.addManualToCollectionIfMissing([], manual);
        expect(expectedResult).toEqual([manual]);
      });

      it('should not add a Manual to an array that contains it', () => {
        const manual: IManual = sampleWithRequiredData;
        const manualCollection: IManual[] = [
          {
            ...manual,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addManualToCollectionIfMissing(manualCollection, manual);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Manual to an array that doesn't contain it", () => {
        const manual: IManual = sampleWithRequiredData;
        const manualCollection: IManual[] = [sampleWithPartialData];
        expectedResult = service.addManualToCollectionIfMissing(manualCollection, manual);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(manual);
      });

      it('should add only unique Manual to an array', () => {
        const manualArray: IManual[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const manualCollection: IManual[] = [sampleWithRequiredData];
        expectedResult = service.addManualToCollectionIfMissing(manualCollection, ...manualArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const manual: IManual = sampleWithRequiredData;
        const manual2: IManual = sampleWithPartialData;
        expectedResult = service.addManualToCollectionIfMissing([], manual, manual2);
        expect(expectedResult).toEqual([manual, manual2]);
      });

      it('should accept null and undefined values', () => {
        const manual: IManual = sampleWithRequiredData;
        expectedResult = service.addManualToCollectionIfMissing([], null, manual, undefined);
        expect(expectedResult).toEqual([manual]);
      });

      it('should return initial array if no Manual is added', () => {
        const manualCollection: IManual[] = [sampleWithRequiredData];
        expectedResult = service.addManualToCollectionIfMissing(manualCollection, undefined, null);
        expect(expectedResult).toEqual(manualCollection);
      });
    });

    describe('compareManual', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareManual(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 16259 };
        const entity2 = null;

        const compareResult1 = service.compareManual(entity1, entity2);
        const compareResult2 = service.compareManual(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 16259 };
        const entity2 = { id: 17096 };

        const compareResult1 = service.compareManual(entity1, entity2);
        const compareResult2 = service.compareManual(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 16259 };
        const entity2 = { id: 16259 };

        const compareResult1 = service.compareManual(entity1, entity2);
        const compareResult2 = service.compareManual(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
