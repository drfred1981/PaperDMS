import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IExtractedText } from '../extracted-text.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../extracted-text.test-samples';

import { ExtractedTextService, RestExtractedText } from './extracted-text.service';

const requireRestSample: RestExtractedText = {
  ...sampleWithRequiredData,
  extractedDate: sampleWithRequiredData.extractedDate?.toJSON(),
};

describe('ExtractedText Service', () => {
  let service: ExtractedTextService;
  let httpMock: HttpTestingController;
  let expectedResult: IExtractedText | IExtractedText[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ExtractedTextService);
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

    it('should create a ExtractedText', () => {
      const extractedText = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(extractedText).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExtractedText', () => {
      const extractedText = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(extractedText).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExtractedText', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExtractedText', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExtractedText', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ExtractedText', () => {
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

    describe('addExtractedTextToCollectionIfMissing', () => {
      it('should add a ExtractedText to an empty array', () => {
        const extractedText: IExtractedText = sampleWithRequiredData;
        expectedResult = service.addExtractedTextToCollectionIfMissing([], extractedText);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(extractedText);
      });

      it('should not add a ExtractedText to an array that contains it', () => {
        const extractedText: IExtractedText = sampleWithRequiredData;
        const extractedTextCollection: IExtractedText[] = [
          {
            ...extractedText,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExtractedTextToCollectionIfMissing(extractedTextCollection, extractedText);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExtractedText to an array that doesn't contain it", () => {
        const extractedText: IExtractedText = sampleWithRequiredData;
        const extractedTextCollection: IExtractedText[] = [sampleWithPartialData];
        expectedResult = service.addExtractedTextToCollectionIfMissing(extractedTextCollection, extractedText);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(extractedText);
      });

      it('should add only unique ExtractedText to an array', () => {
        const extractedTextArray: IExtractedText[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const extractedTextCollection: IExtractedText[] = [sampleWithRequiredData];
        expectedResult = service.addExtractedTextToCollectionIfMissing(extractedTextCollection, ...extractedTextArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const extractedText: IExtractedText = sampleWithRequiredData;
        const extractedText2: IExtractedText = sampleWithPartialData;
        expectedResult = service.addExtractedTextToCollectionIfMissing([], extractedText, extractedText2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(extractedText);
        expect(expectedResult).toContain(extractedText2);
      });

      it('should accept null and undefined values', () => {
        const extractedText: IExtractedText = sampleWithRequiredData;
        expectedResult = service.addExtractedTextToCollectionIfMissing([], null, extractedText, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(extractedText);
      });

      it('should return initial array if no ExtractedText is added', () => {
        const extractedTextCollection: IExtractedText[] = [sampleWithRequiredData];
        expectedResult = service.addExtractedTextToCollectionIfMissing(extractedTextCollection, undefined, null);
        expect(expectedResult).toEqual(extractedTextCollection);
      });
    });

    describe('compareExtractedText', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExtractedText(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 16480 };
        const entity2 = null;

        const compareResult1 = service.compareExtractedText(entity1, entity2);
        const compareResult2 = service.compareExtractedText(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 16480 };
        const entity2 = { id: 28181 };

        const compareResult1 = service.compareExtractedText(entity1, entity2);
        const compareResult2 = service.compareExtractedText(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 16480 };
        const entity2 = { id: 16480 };

        const compareResult1 = service.compareExtractedText(entity1, entity2);
        const compareResult2 = service.compareExtractedText(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
