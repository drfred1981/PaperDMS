import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ITagPrediction } from '../tag-prediction.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../tag-prediction.test-samples';

import { RestTagPrediction, TagPredictionService } from './tag-prediction.service';

const requireRestSample: RestTagPrediction = {
  ...sampleWithRequiredData,
  acceptedDate: sampleWithRequiredData.acceptedDate?.toJSON(),
  predictionDate: sampleWithRequiredData.predictionDate?.toJSON(),
};

describe('TagPrediction Service', () => {
  let service: TagPredictionService;
  let httpMock: HttpTestingController;
  let expectedResult: ITagPrediction | ITagPrediction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TagPredictionService);
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

    it('should create a TagPrediction', () => {
      const tagPrediction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tagPrediction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TagPrediction', () => {
      const tagPrediction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tagPrediction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TagPrediction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TagPrediction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TagPrediction', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTagPredictionToCollectionIfMissing', () => {
      it('should add a TagPrediction to an empty array', () => {
        const tagPrediction: ITagPrediction = sampleWithRequiredData;
        expectedResult = service.addTagPredictionToCollectionIfMissing([], tagPrediction);
        expect(expectedResult).toEqual([tagPrediction]);
      });

      it('should not add a TagPrediction to an array that contains it', () => {
        const tagPrediction: ITagPrediction = sampleWithRequiredData;
        const tagPredictionCollection: ITagPrediction[] = [
          {
            ...tagPrediction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTagPredictionToCollectionIfMissing(tagPredictionCollection, tagPrediction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TagPrediction to an array that doesn't contain it", () => {
        const tagPrediction: ITagPrediction = sampleWithRequiredData;
        const tagPredictionCollection: ITagPrediction[] = [sampleWithPartialData];
        expectedResult = service.addTagPredictionToCollectionIfMissing(tagPredictionCollection, tagPrediction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tagPrediction);
      });

      it('should add only unique TagPrediction to an array', () => {
        const tagPredictionArray: ITagPrediction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tagPredictionCollection: ITagPrediction[] = [sampleWithRequiredData];
        expectedResult = service.addTagPredictionToCollectionIfMissing(tagPredictionCollection, ...tagPredictionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tagPrediction: ITagPrediction = sampleWithRequiredData;
        const tagPrediction2: ITagPrediction = sampleWithPartialData;
        expectedResult = service.addTagPredictionToCollectionIfMissing([], tagPrediction, tagPrediction2);
        expect(expectedResult).toEqual([tagPrediction, tagPrediction2]);
      });

      it('should accept null and undefined values', () => {
        const tagPrediction: ITagPrediction = sampleWithRequiredData;
        expectedResult = service.addTagPredictionToCollectionIfMissing([], null, tagPrediction, undefined);
        expect(expectedResult).toEqual([tagPrediction]);
      });

      it('should return initial array if no TagPrediction is added', () => {
        const tagPredictionCollection: ITagPrediction[] = [sampleWithRequiredData];
        expectedResult = service.addTagPredictionToCollectionIfMissing(tagPredictionCollection, undefined, null);
        expect(expectedResult).toEqual(tagPredictionCollection);
      });
    });

    describe('compareTagPrediction', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTagPrediction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7335 };
        const entity2 = null;

        const compareResult1 = service.compareTagPrediction(entity1, entity2);
        const compareResult2 = service.compareTagPrediction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7335 };
        const entity2 = { id: 7515 };

        const compareResult1 = service.compareTagPrediction(entity1, entity2);
        const compareResult2 = service.compareTagPrediction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7335 };
        const entity2 = { id: 7335 };

        const compareResult1 = service.compareTagPrediction(entity1, entity2);
        const compareResult2 = service.compareTagPrediction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
