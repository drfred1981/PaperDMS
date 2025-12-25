import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ILanguageDetection } from '../language-detection.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../language-detection.test-samples';

import { LanguageDetectionService, RestLanguageDetection } from './language-detection.service';

const requireRestSample: RestLanguageDetection = {
  ...sampleWithRequiredData,
  detectedDate: sampleWithRequiredData.detectedDate?.toJSON(),
};

describe('LanguageDetection Service', () => {
  let service: LanguageDetectionService;
  let httpMock: HttpTestingController;
  let expectedResult: ILanguageDetection | ILanguageDetection[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LanguageDetectionService);
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

    it('should create a LanguageDetection', () => {
      const languageDetection = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(languageDetection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LanguageDetection', () => {
      const languageDetection = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(languageDetection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LanguageDetection', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LanguageDetection', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LanguageDetection', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLanguageDetectionToCollectionIfMissing', () => {
      it('should add a LanguageDetection to an empty array', () => {
        const languageDetection: ILanguageDetection = sampleWithRequiredData;
        expectedResult = service.addLanguageDetectionToCollectionIfMissing([], languageDetection);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(languageDetection);
      });

      it('should not add a LanguageDetection to an array that contains it', () => {
        const languageDetection: ILanguageDetection = sampleWithRequiredData;
        const languageDetectionCollection: ILanguageDetection[] = [
          {
            ...languageDetection,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLanguageDetectionToCollectionIfMissing(languageDetectionCollection, languageDetection);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LanguageDetection to an array that doesn't contain it", () => {
        const languageDetection: ILanguageDetection = sampleWithRequiredData;
        const languageDetectionCollection: ILanguageDetection[] = [sampleWithPartialData];
        expectedResult = service.addLanguageDetectionToCollectionIfMissing(languageDetectionCollection, languageDetection);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(languageDetection);
      });

      it('should add only unique LanguageDetection to an array', () => {
        const languageDetectionArray: ILanguageDetection[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const languageDetectionCollection: ILanguageDetection[] = [sampleWithRequiredData];
        expectedResult = service.addLanguageDetectionToCollectionIfMissing(languageDetectionCollection, ...languageDetectionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const languageDetection: ILanguageDetection = sampleWithRequiredData;
        const languageDetection2: ILanguageDetection = sampleWithPartialData;
        expectedResult = service.addLanguageDetectionToCollectionIfMissing([], languageDetection, languageDetection2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(languageDetection);
        expect(expectedResult).toContain(languageDetection2);
      });

      it('should accept null and undefined values', () => {
        const languageDetection: ILanguageDetection = sampleWithRequiredData;
        expectedResult = service.addLanguageDetectionToCollectionIfMissing([], null, languageDetection, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(languageDetection);
      });

      it('should return initial array if no LanguageDetection is added', () => {
        const languageDetectionCollection: ILanguageDetection[] = [sampleWithRequiredData];
        expectedResult = service.addLanguageDetectionToCollectionIfMissing(languageDetectionCollection, undefined, null);
        expect(expectedResult).toEqual(languageDetectionCollection);
      });
    });

    describe('compareLanguageDetection', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLanguageDetection(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 20981 };
        const entity2 = null;

        const compareResult1 = service.compareLanguageDetection(entity1, entity2);
        const compareResult2 = service.compareLanguageDetection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 20981 };
        const entity2 = { id: 434 };

        const compareResult1 = service.compareLanguageDetection(entity1, entity2);
        const compareResult2 = service.compareLanguageDetection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 20981 };
        const entity2 = { id: 20981 };

        const compareResult1 = service.compareLanguageDetection(entity1, entity2);
        const compareResult2 = service.compareLanguageDetection(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
