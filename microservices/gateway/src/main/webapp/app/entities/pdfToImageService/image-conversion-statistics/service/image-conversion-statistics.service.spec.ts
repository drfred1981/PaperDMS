import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IImageConversionStatistics } from '../image-conversion-statistics.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../image-conversion-statistics.test-samples';

import { ImageConversionStatisticsService, RestImageConversionStatistics } from './image-conversion-statistics.service';

const requireRestSample: RestImageConversionStatistics = {
  ...sampleWithRequiredData,
  statisticsDate: sampleWithRequiredData.statisticsDate?.format(DATE_FORMAT),
  calculatedAt: sampleWithRequiredData.calculatedAt?.toJSON(),
};

describe('ImageConversionStatistics Service', () => {
  let service: ImageConversionStatisticsService;
  let httpMock: HttpTestingController;
  let expectedResult: IImageConversionStatistics | IImageConversionStatistics[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ImageConversionStatisticsService);
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

    it('should create a ImageConversionStatistics', () => {
      const imageConversionStatistics = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(imageConversionStatistics).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ImageConversionStatistics', () => {
      const imageConversionStatistics = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(imageConversionStatistics).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ImageConversionStatistics', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ImageConversionStatistics', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ImageConversionStatistics', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ImageConversionStatistics', () => {
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

    describe('addImageConversionStatisticsToCollectionIfMissing', () => {
      it('should add a ImageConversionStatistics to an empty array', () => {
        const imageConversionStatistics: IImageConversionStatistics = sampleWithRequiredData;
        expectedResult = service.addImageConversionStatisticsToCollectionIfMissing([], imageConversionStatistics);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageConversionStatistics);
      });

      it('should not add a ImageConversionStatistics to an array that contains it', () => {
        const imageConversionStatistics: IImageConversionStatistics = sampleWithRequiredData;
        const imageConversionStatisticsCollection: IImageConversionStatistics[] = [
          {
            ...imageConversionStatistics,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addImageConversionStatisticsToCollectionIfMissing(
          imageConversionStatisticsCollection,
          imageConversionStatistics,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ImageConversionStatistics to an array that doesn't contain it", () => {
        const imageConversionStatistics: IImageConversionStatistics = sampleWithRequiredData;
        const imageConversionStatisticsCollection: IImageConversionStatistics[] = [sampleWithPartialData];
        expectedResult = service.addImageConversionStatisticsToCollectionIfMissing(
          imageConversionStatisticsCollection,
          imageConversionStatistics,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageConversionStatistics);
      });

      it('should add only unique ImageConversionStatistics to an array', () => {
        const imageConversionStatisticsArray: IImageConversionStatistics[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const imageConversionStatisticsCollection: IImageConversionStatistics[] = [sampleWithRequiredData];
        expectedResult = service.addImageConversionStatisticsToCollectionIfMissing(
          imageConversionStatisticsCollection,
          ...imageConversionStatisticsArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const imageConversionStatistics: IImageConversionStatistics = sampleWithRequiredData;
        const imageConversionStatistics2: IImageConversionStatistics = sampleWithPartialData;
        expectedResult = service.addImageConversionStatisticsToCollectionIfMissing(
          [],
          imageConversionStatistics,
          imageConversionStatistics2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageConversionStatistics);
        expect(expectedResult).toContain(imageConversionStatistics2);
      });

      it('should accept null and undefined values', () => {
        const imageConversionStatistics: IImageConversionStatistics = sampleWithRequiredData;
        expectedResult = service.addImageConversionStatisticsToCollectionIfMissing([], null, imageConversionStatistics, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageConversionStatistics);
      });

      it('should return initial array if no ImageConversionStatistics is added', () => {
        const imageConversionStatisticsCollection: IImageConversionStatistics[] = [sampleWithRequiredData];
        expectedResult = service.addImageConversionStatisticsToCollectionIfMissing(imageConversionStatisticsCollection, undefined, null);
        expect(expectedResult).toEqual(imageConversionStatisticsCollection);
      });
    });

    describe('compareImageConversionStatistics', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareImageConversionStatistics(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23721 };
        const entity2 = null;

        const compareResult1 = service.compareImageConversionStatistics(entity1, entity2);
        const compareResult2 = service.compareImageConversionStatistics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23721 };
        const entity2 = { id: 29406 };

        const compareResult1 = service.compareImageConversionStatistics(entity1, entity2);
        const compareResult2 = service.compareImageConversionStatistics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 23721 };
        const entity2 = { id: 23721 };

        const compareResult1 = service.compareImageConversionStatistics(entity1, entity2);
        const compareResult2 = service.compareImageConversionStatistics(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
