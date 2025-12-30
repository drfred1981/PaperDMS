import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAITypePrediction } from '../ai-type-prediction.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ai-type-prediction.test-samples';

import { AITypePredictionService, RestAITypePrediction } from './ai-type-prediction.service';

const requireRestSample: RestAITypePrediction = {
  ...sampleWithRequiredData,
  acceptedDate: sampleWithRequiredData.acceptedDate?.toJSON(),
  predictionDate: sampleWithRequiredData.predictionDate?.toJSON(),
};

describe('AITypePrediction Service', () => {
  let service: AITypePredictionService;
  let httpMock: HttpTestingController;
  let expectedResult: IAITypePrediction | IAITypePrediction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AITypePredictionService);
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

    it('should create a AITypePrediction', () => {
      const aITypePrediction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aITypePrediction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AITypePrediction', () => {
      const aITypePrediction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aITypePrediction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AITypePrediction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AITypePrediction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AITypePrediction', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AITypePrediction', () => {
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

    describe('addAITypePredictionToCollectionIfMissing', () => {
      it('should add a AITypePrediction to an empty array', () => {
        const aITypePrediction: IAITypePrediction = sampleWithRequiredData;
        expectedResult = service.addAITypePredictionToCollectionIfMissing([], aITypePrediction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aITypePrediction);
      });

      it('should not add a AITypePrediction to an array that contains it', () => {
        const aITypePrediction: IAITypePrediction = sampleWithRequiredData;
        const aITypePredictionCollection: IAITypePrediction[] = [
          {
            ...aITypePrediction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAITypePredictionToCollectionIfMissing(aITypePredictionCollection, aITypePrediction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AITypePrediction to an array that doesn't contain it", () => {
        const aITypePrediction: IAITypePrediction = sampleWithRequiredData;
        const aITypePredictionCollection: IAITypePrediction[] = [sampleWithPartialData];
        expectedResult = service.addAITypePredictionToCollectionIfMissing(aITypePredictionCollection, aITypePrediction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aITypePrediction);
      });

      it('should add only unique AITypePrediction to an array', () => {
        const aITypePredictionArray: IAITypePrediction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aITypePredictionCollection: IAITypePrediction[] = [sampleWithRequiredData];
        expectedResult = service.addAITypePredictionToCollectionIfMissing(aITypePredictionCollection, ...aITypePredictionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aITypePrediction: IAITypePrediction = sampleWithRequiredData;
        const aITypePrediction2: IAITypePrediction = sampleWithPartialData;
        expectedResult = service.addAITypePredictionToCollectionIfMissing([], aITypePrediction, aITypePrediction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aITypePrediction);
        expect(expectedResult).toContain(aITypePrediction2);
      });

      it('should accept null and undefined values', () => {
        const aITypePrediction: IAITypePrediction = sampleWithRequiredData;
        expectedResult = service.addAITypePredictionToCollectionIfMissing([], null, aITypePrediction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aITypePrediction);
      });

      it('should return initial array if no AITypePrediction is added', () => {
        const aITypePredictionCollection: IAITypePrediction[] = [sampleWithRequiredData];
        expectedResult = service.addAITypePredictionToCollectionIfMissing(aITypePredictionCollection, undefined, null);
        expect(expectedResult).toEqual(aITypePredictionCollection);
      });
    });

    describe('compareAITypePrediction', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAITypePrediction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 133 };
        const entity2 = null;

        const compareResult1 = service.compareAITypePrediction(entity1, entity2);
        const compareResult2 = service.compareAITypePrediction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 133 };
        const entity2 = { id: 17407 };

        const compareResult1 = service.compareAITypePrediction(entity1, entity2);
        const compareResult2 = service.compareAITypePrediction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 133 };
        const entity2 = { id: 133 };

        const compareResult1 = service.compareAITypePrediction(entity1, entity2);
        const compareResult2 = service.compareAITypePrediction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
