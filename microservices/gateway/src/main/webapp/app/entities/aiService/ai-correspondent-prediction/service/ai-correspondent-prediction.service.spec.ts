import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAICorrespondentPrediction } from '../ai-correspondent-prediction.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../ai-correspondent-prediction.test-samples';

import { AICorrespondentPredictionService, RestAICorrespondentPrediction } from './ai-correspondent-prediction.service';

const requireRestSample: RestAICorrespondentPrediction = {
  ...sampleWithRequiredData,
  acceptedDate: sampleWithRequiredData.acceptedDate?.toJSON(),
  predictionDate: sampleWithRequiredData.predictionDate?.toJSON(),
};

describe('AICorrespondentPrediction Service', () => {
  let service: AICorrespondentPredictionService;
  let httpMock: HttpTestingController;
  let expectedResult: IAICorrespondentPrediction | IAICorrespondentPrediction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AICorrespondentPredictionService);
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

    it('should create a AICorrespondentPrediction', () => {
      const aICorrespondentPrediction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aICorrespondentPrediction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AICorrespondentPrediction', () => {
      const aICorrespondentPrediction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aICorrespondentPrediction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AICorrespondentPrediction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AICorrespondentPrediction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AICorrespondentPrediction', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AICorrespondentPrediction', () => {
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

    describe('addAICorrespondentPredictionToCollectionIfMissing', () => {
      it('should add a AICorrespondentPrediction to an empty array', () => {
        const aICorrespondentPrediction: IAICorrespondentPrediction = sampleWithRequiredData;
        expectedResult = service.addAICorrespondentPredictionToCollectionIfMissing([], aICorrespondentPrediction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aICorrespondentPrediction);
      });

      it('should not add a AICorrespondentPrediction to an array that contains it', () => {
        const aICorrespondentPrediction: IAICorrespondentPrediction = sampleWithRequiredData;
        const aICorrespondentPredictionCollection: IAICorrespondentPrediction[] = [
          {
            ...aICorrespondentPrediction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAICorrespondentPredictionToCollectionIfMissing(
          aICorrespondentPredictionCollection,
          aICorrespondentPrediction,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AICorrespondentPrediction to an array that doesn't contain it", () => {
        const aICorrespondentPrediction: IAICorrespondentPrediction = sampleWithRequiredData;
        const aICorrespondentPredictionCollection: IAICorrespondentPrediction[] = [sampleWithPartialData];
        expectedResult = service.addAICorrespondentPredictionToCollectionIfMissing(
          aICorrespondentPredictionCollection,
          aICorrespondentPrediction,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aICorrespondentPrediction);
      });

      it('should add only unique AICorrespondentPrediction to an array', () => {
        const aICorrespondentPredictionArray: IAICorrespondentPrediction[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const aICorrespondentPredictionCollection: IAICorrespondentPrediction[] = [sampleWithRequiredData];
        expectedResult = service.addAICorrespondentPredictionToCollectionIfMissing(
          aICorrespondentPredictionCollection,
          ...aICorrespondentPredictionArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aICorrespondentPrediction: IAICorrespondentPrediction = sampleWithRequiredData;
        const aICorrespondentPrediction2: IAICorrespondentPrediction = sampleWithPartialData;
        expectedResult = service.addAICorrespondentPredictionToCollectionIfMissing(
          [],
          aICorrespondentPrediction,
          aICorrespondentPrediction2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aICorrespondentPrediction);
        expect(expectedResult).toContain(aICorrespondentPrediction2);
      });

      it('should accept null and undefined values', () => {
        const aICorrespondentPrediction: IAICorrespondentPrediction = sampleWithRequiredData;
        expectedResult = service.addAICorrespondentPredictionToCollectionIfMissing([], null, aICorrespondentPrediction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aICorrespondentPrediction);
      });

      it('should return initial array if no AICorrespondentPrediction is added', () => {
        const aICorrespondentPredictionCollection: IAICorrespondentPrediction[] = [sampleWithRequiredData];
        expectedResult = service.addAICorrespondentPredictionToCollectionIfMissing(aICorrespondentPredictionCollection, undefined, null);
        expect(expectedResult).toEqual(aICorrespondentPredictionCollection);
      });
    });

    describe('compareAICorrespondentPrediction', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAICorrespondentPrediction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18878 };
        const entity2 = null;

        const compareResult1 = service.compareAICorrespondentPrediction(entity1, entity2);
        const compareResult2 = service.compareAICorrespondentPrediction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18878 };
        const entity2 = { id: 2709 };

        const compareResult1 = service.compareAICorrespondentPrediction(entity1, entity2);
        const compareResult2 = service.compareAICorrespondentPrediction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 18878 };
        const entity2 = { id: 18878 };

        const compareResult1 = service.compareAICorrespondentPrediction(entity1, entity2);
        const compareResult2 = service.compareAICorrespondentPrediction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
