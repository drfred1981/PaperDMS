import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDocumentStatistics } from '../document-statistics.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-statistics.test-samples';

import { DocumentStatisticsService, RestDocumentStatistics } from './document-statistics.service';

const requireRestSample: RestDocumentStatistics = {
  ...sampleWithRequiredData,
  lastUpdated: sampleWithRequiredData.lastUpdated?.toJSON(),
};

describe('DocumentStatistics Service', () => {
  let service: DocumentStatisticsService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentStatistics | IDocumentStatistics[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentStatisticsService);
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

    it('should create a DocumentStatistics', () => {
      const documentStatistics = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentStatistics).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentStatistics', () => {
      const documentStatistics = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentStatistics).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentStatistics', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentStatistics', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentStatistics', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a DocumentStatistics', () => {
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

    describe('addDocumentStatisticsToCollectionIfMissing', () => {
      it('should add a DocumentStatistics to an empty array', () => {
        const documentStatistics: IDocumentStatistics = sampleWithRequiredData;
        expectedResult = service.addDocumentStatisticsToCollectionIfMissing([], documentStatistics);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentStatistics);
      });

      it('should not add a DocumentStatistics to an array that contains it', () => {
        const documentStatistics: IDocumentStatistics = sampleWithRequiredData;
        const documentStatisticsCollection: IDocumentStatistics[] = [
          {
            ...documentStatistics,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentStatisticsToCollectionIfMissing(documentStatisticsCollection, documentStatistics);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentStatistics to an array that doesn't contain it", () => {
        const documentStatistics: IDocumentStatistics = sampleWithRequiredData;
        const documentStatisticsCollection: IDocumentStatistics[] = [sampleWithPartialData];
        expectedResult = service.addDocumentStatisticsToCollectionIfMissing(documentStatisticsCollection, documentStatistics);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentStatistics);
      });

      it('should add only unique DocumentStatistics to an array', () => {
        const documentStatisticsArray: IDocumentStatistics[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentStatisticsCollection: IDocumentStatistics[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentStatisticsToCollectionIfMissing(documentStatisticsCollection, ...documentStatisticsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentStatistics: IDocumentStatistics = sampleWithRequiredData;
        const documentStatistics2: IDocumentStatistics = sampleWithPartialData;
        expectedResult = service.addDocumentStatisticsToCollectionIfMissing([], documentStatistics, documentStatistics2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentStatistics);
        expect(expectedResult).toContain(documentStatistics2);
      });

      it('should accept null and undefined values', () => {
        const documentStatistics: IDocumentStatistics = sampleWithRequiredData;
        expectedResult = service.addDocumentStatisticsToCollectionIfMissing([], null, documentStatistics, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentStatistics);
      });

      it('should return initial array if no DocumentStatistics is added', () => {
        const documentStatisticsCollection: IDocumentStatistics[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentStatisticsToCollectionIfMissing(documentStatisticsCollection, undefined, null);
        expect(expectedResult).toEqual(documentStatisticsCollection);
      });
    });

    describe('compareDocumentStatistics', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentStatistics(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5208 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentStatistics(entity1, entity2);
        const compareResult2 = service.compareDocumentStatistics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5208 };
        const entity2 = { id: 4489 };

        const compareResult1 = service.compareDocumentStatistics(entity1, entity2);
        const compareResult2 = service.compareDocumentStatistics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5208 };
        const entity2 = { id: 5208 };

        const compareResult1 = service.compareDocumentStatistics(entity1, entity2);
        const compareResult2 = service.compareDocumentStatistics(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
