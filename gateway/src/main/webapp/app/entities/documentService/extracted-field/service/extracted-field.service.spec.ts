import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IExtractedField } from '../extracted-field.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../extracted-field.test-samples';

import { ExtractedFieldService, RestExtractedField } from './extracted-field.service';

const requireRestSample: RestExtractedField = {
  ...sampleWithRequiredData,
  extractedDate: sampleWithRequiredData.extractedDate?.toJSON(),
};

describe('ExtractedField Service', () => {
  let service: ExtractedFieldService;
  let httpMock: HttpTestingController;
  let expectedResult: IExtractedField | IExtractedField[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ExtractedFieldService);
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

    it('should create a ExtractedField', () => {
      const extractedField = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(extractedField).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExtractedField', () => {
      const extractedField = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(extractedField).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExtractedField', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExtractedField', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExtractedField', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ExtractedField', () => {
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

    describe('addExtractedFieldToCollectionIfMissing', () => {
      it('should add a ExtractedField to an empty array', () => {
        const extractedField: IExtractedField = sampleWithRequiredData;
        expectedResult = service.addExtractedFieldToCollectionIfMissing([], extractedField);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(extractedField);
      });

      it('should not add a ExtractedField to an array that contains it', () => {
        const extractedField: IExtractedField = sampleWithRequiredData;
        const extractedFieldCollection: IExtractedField[] = [
          {
            ...extractedField,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExtractedFieldToCollectionIfMissing(extractedFieldCollection, extractedField);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExtractedField to an array that doesn't contain it", () => {
        const extractedField: IExtractedField = sampleWithRequiredData;
        const extractedFieldCollection: IExtractedField[] = [sampleWithPartialData];
        expectedResult = service.addExtractedFieldToCollectionIfMissing(extractedFieldCollection, extractedField);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(extractedField);
      });

      it('should add only unique ExtractedField to an array', () => {
        const extractedFieldArray: IExtractedField[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const extractedFieldCollection: IExtractedField[] = [sampleWithRequiredData];
        expectedResult = service.addExtractedFieldToCollectionIfMissing(extractedFieldCollection, ...extractedFieldArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const extractedField: IExtractedField = sampleWithRequiredData;
        const extractedField2: IExtractedField = sampleWithPartialData;
        expectedResult = service.addExtractedFieldToCollectionIfMissing([], extractedField, extractedField2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(extractedField);
        expect(expectedResult).toContain(extractedField2);
      });

      it('should accept null and undefined values', () => {
        const extractedField: IExtractedField = sampleWithRequiredData;
        expectedResult = service.addExtractedFieldToCollectionIfMissing([], null, extractedField, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(extractedField);
      });

      it('should return initial array if no ExtractedField is added', () => {
        const extractedFieldCollection: IExtractedField[] = [sampleWithRequiredData];
        expectedResult = service.addExtractedFieldToCollectionIfMissing(extractedFieldCollection, undefined, null);
        expect(expectedResult).toEqual(extractedFieldCollection);
      });
    });

    describe('compareExtractedField', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExtractedField(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 31442 };
        const entity2 = null;

        const compareResult1 = service.compareExtractedField(entity1, entity2);
        const compareResult2 = service.compareExtractedField(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 31442 };
        const entity2 = { id: 8866 };

        const compareResult1 = service.compareExtractedField(entity1, entity2);
        const compareResult2 = service.compareExtractedField(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 31442 };
        const entity2 = { id: 31442 };

        const compareResult1 = service.compareExtractedField(entity1, entity2);
        const compareResult2 = service.compareExtractedField(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
