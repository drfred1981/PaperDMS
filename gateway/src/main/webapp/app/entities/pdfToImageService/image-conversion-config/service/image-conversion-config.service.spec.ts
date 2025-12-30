import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IImageConversionConfig } from '../image-conversion-config.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../image-conversion-config.test-samples';

import { ImageConversionConfigService, RestImageConversionConfig } from './image-conversion-config.service';

const requireRestSample: RestImageConversionConfig = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('ImageConversionConfig Service', () => {
  let service: ImageConversionConfigService;
  let httpMock: HttpTestingController;
  let expectedResult: IImageConversionConfig | IImageConversionConfig[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ImageConversionConfigService);
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

    it('should create a ImageConversionConfig', () => {
      const imageConversionConfig = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(imageConversionConfig).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ImageConversionConfig', () => {
      const imageConversionConfig = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(imageConversionConfig).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ImageConversionConfig', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ImageConversionConfig', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ImageConversionConfig', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ImageConversionConfig', () => {
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

    describe('addImageConversionConfigToCollectionIfMissing', () => {
      it('should add a ImageConversionConfig to an empty array', () => {
        const imageConversionConfig: IImageConversionConfig = sampleWithRequiredData;
        expectedResult = service.addImageConversionConfigToCollectionIfMissing([], imageConversionConfig);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageConversionConfig);
      });

      it('should not add a ImageConversionConfig to an array that contains it', () => {
        const imageConversionConfig: IImageConversionConfig = sampleWithRequiredData;
        const imageConversionConfigCollection: IImageConversionConfig[] = [
          {
            ...imageConversionConfig,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addImageConversionConfigToCollectionIfMissing(imageConversionConfigCollection, imageConversionConfig);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ImageConversionConfig to an array that doesn't contain it", () => {
        const imageConversionConfig: IImageConversionConfig = sampleWithRequiredData;
        const imageConversionConfigCollection: IImageConversionConfig[] = [sampleWithPartialData];
        expectedResult = service.addImageConversionConfigToCollectionIfMissing(imageConversionConfigCollection, imageConversionConfig);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageConversionConfig);
      });

      it('should add only unique ImageConversionConfig to an array', () => {
        const imageConversionConfigArray: IImageConversionConfig[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const imageConversionConfigCollection: IImageConversionConfig[] = [sampleWithRequiredData];
        expectedResult = service.addImageConversionConfigToCollectionIfMissing(
          imageConversionConfigCollection,
          ...imageConversionConfigArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const imageConversionConfig: IImageConversionConfig = sampleWithRequiredData;
        const imageConversionConfig2: IImageConversionConfig = sampleWithPartialData;
        expectedResult = service.addImageConversionConfigToCollectionIfMissing([], imageConversionConfig, imageConversionConfig2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageConversionConfig);
        expect(expectedResult).toContain(imageConversionConfig2);
      });

      it('should accept null and undefined values', () => {
        const imageConversionConfig: IImageConversionConfig = sampleWithRequiredData;
        expectedResult = service.addImageConversionConfigToCollectionIfMissing([], null, imageConversionConfig, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageConversionConfig);
      });

      it('should return initial array if no ImageConversionConfig is added', () => {
        const imageConversionConfigCollection: IImageConversionConfig[] = [sampleWithRequiredData];
        expectedResult = service.addImageConversionConfigToCollectionIfMissing(imageConversionConfigCollection, undefined, null);
        expect(expectedResult).toEqual(imageConversionConfigCollection);
      });
    });

    describe('compareImageConversionConfig', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareImageConversionConfig(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9973 };
        const entity2 = null;

        const compareResult1 = service.compareImageConversionConfig(entity1, entity2);
        const compareResult2 = service.compareImageConversionConfig(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9973 };
        const entity2 = { id: 29731 };

        const compareResult1 = service.compareImageConversionConfig(entity1, entity2);
        const compareResult2 = service.compareImageConversionConfig(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 9973 };
        const entity2 = { id: 9973 };

        const compareResult1 = service.compareImageConversionConfig(entity1, entity2);
        const compareResult2 = service.compareImageConversionConfig(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
