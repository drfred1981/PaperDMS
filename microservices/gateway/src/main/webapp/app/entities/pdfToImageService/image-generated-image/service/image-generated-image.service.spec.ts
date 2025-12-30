import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IImageGeneratedImage } from '../image-generated-image.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../image-generated-image.test-samples';

import { ImageGeneratedImageService, RestImageGeneratedImage } from './image-generated-image.service';

const requireRestSample: RestImageGeneratedImage = {
  ...sampleWithRequiredData,
  urlExpiresAt: sampleWithRequiredData.urlExpiresAt?.toJSON(),
  generatedAt: sampleWithRequiredData.generatedAt?.toJSON(),
};

describe('ImageGeneratedImage Service', () => {
  let service: ImageGeneratedImageService;
  let httpMock: HttpTestingController;
  let expectedResult: IImageGeneratedImage | IImageGeneratedImage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ImageGeneratedImageService);
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

    it('should create a ImageGeneratedImage', () => {
      const imageGeneratedImage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(imageGeneratedImage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ImageGeneratedImage', () => {
      const imageGeneratedImage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(imageGeneratedImage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ImageGeneratedImage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ImageGeneratedImage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ImageGeneratedImage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ImageGeneratedImage', () => {
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

    describe('addImageGeneratedImageToCollectionIfMissing', () => {
      it('should add a ImageGeneratedImage to an empty array', () => {
        const imageGeneratedImage: IImageGeneratedImage = sampleWithRequiredData;
        expectedResult = service.addImageGeneratedImageToCollectionIfMissing([], imageGeneratedImage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageGeneratedImage);
      });

      it('should not add a ImageGeneratedImage to an array that contains it', () => {
        const imageGeneratedImage: IImageGeneratedImage = sampleWithRequiredData;
        const imageGeneratedImageCollection: IImageGeneratedImage[] = [
          {
            ...imageGeneratedImage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addImageGeneratedImageToCollectionIfMissing(imageGeneratedImageCollection, imageGeneratedImage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ImageGeneratedImage to an array that doesn't contain it", () => {
        const imageGeneratedImage: IImageGeneratedImage = sampleWithRequiredData;
        const imageGeneratedImageCollection: IImageGeneratedImage[] = [sampleWithPartialData];
        expectedResult = service.addImageGeneratedImageToCollectionIfMissing(imageGeneratedImageCollection, imageGeneratedImage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageGeneratedImage);
      });

      it('should add only unique ImageGeneratedImage to an array', () => {
        const imageGeneratedImageArray: IImageGeneratedImage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const imageGeneratedImageCollection: IImageGeneratedImage[] = [sampleWithRequiredData];
        expectedResult = service.addImageGeneratedImageToCollectionIfMissing(imageGeneratedImageCollection, ...imageGeneratedImageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const imageGeneratedImage: IImageGeneratedImage = sampleWithRequiredData;
        const imageGeneratedImage2: IImageGeneratedImage = sampleWithPartialData;
        expectedResult = service.addImageGeneratedImageToCollectionIfMissing([], imageGeneratedImage, imageGeneratedImage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imageGeneratedImage);
        expect(expectedResult).toContain(imageGeneratedImage2);
      });

      it('should accept null and undefined values', () => {
        const imageGeneratedImage: IImageGeneratedImage = sampleWithRequiredData;
        expectedResult = service.addImageGeneratedImageToCollectionIfMissing([], null, imageGeneratedImage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imageGeneratedImage);
      });

      it('should return initial array if no ImageGeneratedImage is added', () => {
        const imageGeneratedImageCollection: IImageGeneratedImage[] = [sampleWithRequiredData];
        expectedResult = service.addImageGeneratedImageToCollectionIfMissing(imageGeneratedImageCollection, undefined, null);
        expect(expectedResult).toEqual(imageGeneratedImageCollection);
      });
    });

    describe('compareImageGeneratedImage', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareImageGeneratedImage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9352 };
        const entity2 = null;

        const compareResult1 = service.compareImageGeneratedImage(entity1, entity2);
        const compareResult2 = service.compareImageGeneratedImage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9352 };
        const entity2 = { id: 9650 };

        const compareResult1 = service.compareImageGeneratedImage(entity1, entity2);
        const compareResult2 = service.compareImageGeneratedImage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 9352 };
        const entity2 = { id: 9352 };

        const compareResult1 = service.compareImageGeneratedImage(entity1, entity2);
        const compareResult2 = service.compareImageGeneratedImage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
