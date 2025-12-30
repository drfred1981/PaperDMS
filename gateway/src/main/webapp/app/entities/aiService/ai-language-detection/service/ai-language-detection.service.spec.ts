import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAILanguageDetection } from '../ai-language-detection.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../ai-language-detection.test-samples';

import { AILanguageDetectionService, RestAILanguageDetection } from './ai-language-detection.service';

const requireRestSample: RestAILanguageDetection = {
  ...sampleWithRequiredData,
  detectedDate: sampleWithRequiredData.detectedDate?.toJSON(),
};

describe('AILanguageDetection Service', () => {
  let service: AILanguageDetectionService;
  let httpMock: HttpTestingController;
  let expectedResult: IAILanguageDetection | IAILanguageDetection[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AILanguageDetectionService);
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

    it('should create a AILanguageDetection', () => {
      const aILanguageDetection = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aILanguageDetection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AILanguageDetection', () => {
      const aILanguageDetection = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aILanguageDetection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AILanguageDetection', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AILanguageDetection', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AILanguageDetection', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AILanguageDetection', () => {
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

    describe('addAILanguageDetectionToCollectionIfMissing', () => {
      it('should add a AILanguageDetection to an empty array', () => {
        const aILanguageDetection: IAILanguageDetection = sampleWithRequiredData;
        expectedResult = service.addAILanguageDetectionToCollectionIfMissing([], aILanguageDetection);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aILanguageDetection);
      });

      it('should not add a AILanguageDetection to an array that contains it', () => {
        const aILanguageDetection: IAILanguageDetection = sampleWithRequiredData;
        const aILanguageDetectionCollection: IAILanguageDetection[] = [
          {
            ...aILanguageDetection,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAILanguageDetectionToCollectionIfMissing(aILanguageDetectionCollection, aILanguageDetection);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AILanguageDetection to an array that doesn't contain it", () => {
        const aILanguageDetection: IAILanguageDetection = sampleWithRequiredData;
        const aILanguageDetectionCollection: IAILanguageDetection[] = [sampleWithPartialData];
        expectedResult = service.addAILanguageDetectionToCollectionIfMissing(aILanguageDetectionCollection, aILanguageDetection);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aILanguageDetection);
      });

      it('should add only unique AILanguageDetection to an array', () => {
        const aILanguageDetectionArray: IAILanguageDetection[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aILanguageDetectionCollection: IAILanguageDetection[] = [sampleWithRequiredData];
        expectedResult = service.addAILanguageDetectionToCollectionIfMissing(aILanguageDetectionCollection, ...aILanguageDetectionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aILanguageDetection: IAILanguageDetection = sampleWithRequiredData;
        const aILanguageDetection2: IAILanguageDetection = sampleWithPartialData;
        expectedResult = service.addAILanguageDetectionToCollectionIfMissing([], aILanguageDetection, aILanguageDetection2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aILanguageDetection);
        expect(expectedResult).toContain(aILanguageDetection2);
      });

      it('should accept null and undefined values', () => {
        const aILanguageDetection: IAILanguageDetection = sampleWithRequiredData;
        expectedResult = service.addAILanguageDetectionToCollectionIfMissing([], null, aILanguageDetection, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aILanguageDetection);
      });

      it('should return initial array if no AILanguageDetection is added', () => {
        const aILanguageDetectionCollection: IAILanguageDetection[] = [sampleWithRequiredData];
        expectedResult = service.addAILanguageDetectionToCollectionIfMissing(aILanguageDetectionCollection, undefined, null);
        expect(expectedResult).toEqual(aILanguageDetectionCollection);
      });
    });

    describe('compareAILanguageDetection', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAILanguageDetection(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3507 };
        const entity2 = null;

        const compareResult1 = service.compareAILanguageDetection(entity1, entity2);
        const compareResult2 = service.compareAILanguageDetection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3507 };
        const entity2 = { id: 26399 };

        const compareResult1 = service.compareAILanguageDetection(entity1, entity2);
        const compareResult2 = service.compareAILanguageDetection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 3507 };
        const entity2 = { id: 3507 };

        const compareResult1 = service.compareAILanguageDetection(entity1, entity2);
        const compareResult2 = service.compareAILanguageDetection(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
