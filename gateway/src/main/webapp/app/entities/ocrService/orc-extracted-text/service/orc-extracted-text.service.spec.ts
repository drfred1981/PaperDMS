import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOrcExtractedText } from '../orc-extracted-text.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../orc-extracted-text.test-samples';

import { OrcExtractedTextService, RestOrcExtractedText } from './orc-extracted-text.service';

const requireRestSample: RestOrcExtractedText = {
  ...sampleWithRequiredData,
  extractedDate: sampleWithRequiredData.extractedDate?.toJSON(),
};

describe('OrcExtractedText Service', () => {
  let service: OrcExtractedTextService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrcExtractedText | IOrcExtractedText[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrcExtractedTextService);
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

    it('should create a OrcExtractedText', () => {
      const orcExtractedText = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orcExtractedText).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrcExtractedText', () => {
      const orcExtractedText = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orcExtractedText).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrcExtractedText', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrcExtractedText', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrcExtractedText', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a OrcExtractedText', () => {
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

    describe('addOrcExtractedTextToCollectionIfMissing', () => {
      it('should add a OrcExtractedText to an empty array', () => {
        const orcExtractedText: IOrcExtractedText = sampleWithRequiredData;
        expectedResult = service.addOrcExtractedTextToCollectionIfMissing([], orcExtractedText);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orcExtractedText);
      });

      it('should not add a OrcExtractedText to an array that contains it', () => {
        const orcExtractedText: IOrcExtractedText = sampleWithRequiredData;
        const orcExtractedTextCollection: IOrcExtractedText[] = [
          {
            ...orcExtractedText,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrcExtractedTextToCollectionIfMissing(orcExtractedTextCollection, orcExtractedText);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrcExtractedText to an array that doesn't contain it", () => {
        const orcExtractedText: IOrcExtractedText = sampleWithRequiredData;
        const orcExtractedTextCollection: IOrcExtractedText[] = [sampleWithPartialData];
        expectedResult = service.addOrcExtractedTextToCollectionIfMissing(orcExtractedTextCollection, orcExtractedText);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orcExtractedText);
      });

      it('should add only unique OrcExtractedText to an array', () => {
        const orcExtractedTextArray: IOrcExtractedText[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orcExtractedTextCollection: IOrcExtractedText[] = [sampleWithRequiredData];
        expectedResult = service.addOrcExtractedTextToCollectionIfMissing(orcExtractedTextCollection, ...orcExtractedTextArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orcExtractedText: IOrcExtractedText = sampleWithRequiredData;
        const orcExtractedText2: IOrcExtractedText = sampleWithPartialData;
        expectedResult = service.addOrcExtractedTextToCollectionIfMissing([], orcExtractedText, orcExtractedText2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orcExtractedText);
        expect(expectedResult).toContain(orcExtractedText2);
      });

      it('should accept null and undefined values', () => {
        const orcExtractedText: IOrcExtractedText = sampleWithRequiredData;
        expectedResult = service.addOrcExtractedTextToCollectionIfMissing([], null, orcExtractedText, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orcExtractedText);
      });

      it('should return initial array if no OrcExtractedText is added', () => {
        const orcExtractedTextCollection: IOrcExtractedText[] = [sampleWithRequiredData];
        expectedResult = service.addOrcExtractedTextToCollectionIfMissing(orcExtractedTextCollection, undefined, null);
        expect(expectedResult).toEqual(orcExtractedTextCollection);
      });
    });

    describe('compareOrcExtractedText', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrcExtractedText(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23372 };
        const entity2 = null;

        const compareResult1 = service.compareOrcExtractedText(entity1, entity2);
        const compareResult2 = service.compareOrcExtractedText(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23372 };
        const entity2 = { id: 28138 };

        const compareResult1 = service.compareOrcExtractedText(entity1, entity2);
        const compareResult2 = service.compareOrcExtractedText(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 23372 };
        const entity2 = { id: 23372 };

        const compareResult1 = service.compareOrcExtractedText(entity1, entity2);
        const compareResult2 = service.compareOrcExtractedText(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
