import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICorrespondentExtraction } from '../correspondent-extraction.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../correspondent-extraction.test-samples';

import { CorrespondentExtractionService, RestCorrespondentExtraction } from './correspondent-extraction.service';

const requireRestSample: RestCorrespondentExtraction = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('CorrespondentExtraction Service', () => {
  let service: CorrespondentExtractionService;
  let httpMock: HttpTestingController;
  let expectedResult: ICorrespondentExtraction | ICorrespondentExtraction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CorrespondentExtractionService);
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

    it('should create a CorrespondentExtraction', () => {
      const correspondentExtraction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(correspondentExtraction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CorrespondentExtraction', () => {
      const correspondentExtraction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(correspondentExtraction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CorrespondentExtraction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CorrespondentExtraction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CorrespondentExtraction', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCorrespondentExtractionToCollectionIfMissing', () => {
      it('should add a CorrespondentExtraction to an empty array', () => {
        const correspondentExtraction: ICorrespondentExtraction = sampleWithRequiredData;
        expectedResult = service.addCorrespondentExtractionToCollectionIfMissing([], correspondentExtraction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(correspondentExtraction);
      });

      it('should not add a CorrespondentExtraction to an array that contains it', () => {
        const correspondentExtraction: ICorrespondentExtraction = sampleWithRequiredData;
        const correspondentExtractionCollection: ICorrespondentExtraction[] = [
          {
            ...correspondentExtraction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCorrespondentExtractionToCollectionIfMissing(
          correspondentExtractionCollection,
          correspondentExtraction,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CorrespondentExtraction to an array that doesn't contain it", () => {
        const correspondentExtraction: ICorrespondentExtraction = sampleWithRequiredData;
        const correspondentExtractionCollection: ICorrespondentExtraction[] = [sampleWithPartialData];
        expectedResult = service.addCorrespondentExtractionToCollectionIfMissing(
          correspondentExtractionCollection,
          correspondentExtraction,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(correspondentExtraction);
      });

      it('should add only unique CorrespondentExtraction to an array', () => {
        const correspondentExtractionArray: ICorrespondentExtraction[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const correspondentExtractionCollection: ICorrespondentExtraction[] = [sampleWithRequiredData];
        expectedResult = service.addCorrespondentExtractionToCollectionIfMissing(
          correspondentExtractionCollection,
          ...correspondentExtractionArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const correspondentExtraction: ICorrespondentExtraction = sampleWithRequiredData;
        const correspondentExtraction2: ICorrespondentExtraction = sampleWithPartialData;
        expectedResult = service.addCorrespondentExtractionToCollectionIfMissing([], correspondentExtraction, correspondentExtraction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(correspondentExtraction);
        expect(expectedResult).toContain(correspondentExtraction2);
      });

      it('should accept null and undefined values', () => {
        const correspondentExtraction: ICorrespondentExtraction = sampleWithRequiredData;
        expectedResult = service.addCorrespondentExtractionToCollectionIfMissing([], null, correspondentExtraction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(correspondentExtraction);
      });

      it('should return initial array if no CorrespondentExtraction is added', () => {
        const correspondentExtractionCollection: ICorrespondentExtraction[] = [sampleWithRequiredData];
        expectedResult = service.addCorrespondentExtractionToCollectionIfMissing(correspondentExtractionCollection, undefined, null);
        expect(expectedResult).toEqual(correspondentExtractionCollection);
      });
    });

    describe('compareCorrespondentExtraction', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCorrespondentExtraction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25332 };
        const entity2 = null;

        const compareResult1 = service.compareCorrespondentExtraction(entity1, entity2);
        const compareResult2 = service.compareCorrespondentExtraction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25332 };
        const entity2 = { id: 2844 };

        const compareResult1 = service.compareCorrespondentExtraction(entity1, entity2);
        const compareResult2 = service.compareCorrespondentExtraction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25332 };
        const entity2 = { id: 25332 };

        const compareResult1 = service.compareCorrespondentExtraction(entity1, entity2);
        const compareResult2 = service.compareCorrespondentExtraction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
