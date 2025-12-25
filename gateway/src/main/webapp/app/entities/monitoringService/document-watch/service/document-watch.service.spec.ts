import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDocumentWatch } from '../document-watch.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-watch.test-samples';

import { DocumentWatchService, RestDocumentWatch } from './document-watch.service';

const requireRestSample: RestDocumentWatch = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('DocumentWatch Service', () => {
  let service: DocumentWatchService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentWatch | IDocumentWatch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentWatchService);
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

    it('should create a DocumentWatch', () => {
      const documentWatch = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentWatch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentWatch', () => {
      const documentWatch = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentWatch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentWatch', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentWatch', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentWatch', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDocumentWatchToCollectionIfMissing', () => {
      it('should add a DocumentWatch to an empty array', () => {
        const documentWatch: IDocumentWatch = sampleWithRequiredData;
        expectedResult = service.addDocumentWatchToCollectionIfMissing([], documentWatch);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentWatch);
      });

      it('should not add a DocumentWatch to an array that contains it', () => {
        const documentWatch: IDocumentWatch = sampleWithRequiredData;
        const documentWatchCollection: IDocumentWatch[] = [
          {
            ...documentWatch,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentWatchToCollectionIfMissing(documentWatchCollection, documentWatch);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentWatch to an array that doesn't contain it", () => {
        const documentWatch: IDocumentWatch = sampleWithRequiredData;
        const documentWatchCollection: IDocumentWatch[] = [sampleWithPartialData];
        expectedResult = service.addDocumentWatchToCollectionIfMissing(documentWatchCollection, documentWatch);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentWatch);
      });

      it('should add only unique DocumentWatch to an array', () => {
        const documentWatchArray: IDocumentWatch[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentWatchCollection: IDocumentWatch[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentWatchToCollectionIfMissing(documentWatchCollection, ...documentWatchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentWatch: IDocumentWatch = sampleWithRequiredData;
        const documentWatch2: IDocumentWatch = sampleWithPartialData;
        expectedResult = service.addDocumentWatchToCollectionIfMissing([], documentWatch, documentWatch2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentWatch);
        expect(expectedResult).toContain(documentWatch2);
      });

      it('should accept null and undefined values', () => {
        const documentWatch: IDocumentWatch = sampleWithRequiredData;
        expectedResult = service.addDocumentWatchToCollectionIfMissing([], null, documentWatch, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentWatch);
      });

      it('should return initial array if no DocumentWatch is added', () => {
        const documentWatchCollection: IDocumentWatch[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentWatchToCollectionIfMissing(documentWatchCollection, undefined, null);
        expect(expectedResult).toEqual(documentWatchCollection);
      });
    });

    describe('compareDocumentWatch', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentWatch(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13046 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentWatch(entity1, entity2);
        const compareResult2 = service.compareDocumentWatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13046 };
        const entity2 = { id: 28672 };

        const compareResult1 = service.compareDocumentWatch(entity1, entity2);
        const compareResult2 = service.compareDocumentWatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 13046 };
        const entity2 = { id: 13046 };

        const compareResult1 = service.compareDocumentWatch(entity1, entity2);
        const compareResult2 = service.compareDocumentWatch(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
