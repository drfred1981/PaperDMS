import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDocumentProcess } from '../document-process.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-process.test-samples';

import { DocumentProcessService } from './document-process.service';

const requireRestSample: IDocumentProcess = {
  ...sampleWithRequiredData,
};

describe('DocumentProcess Service', () => {
  let service: DocumentProcessService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentProcess | IDocumentProcess[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentProcessService);
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

    it('should create a DocumentProcess', () => {
      const documentProcess = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentProcess).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentProcess', () => {
      const documentProcess = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentProcess).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentProcess', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentProcess', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentProcess', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a DocumentProcess', () => {
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

    describe('addDocumentProcessToCollectionIfMissing', () => {
      it('should add a DocumentProcess to an empty array', () => {
        const documentProcess: IDocumentProcess = sampleWithRequiredData;
        expectedResult = service.addDocumentProcessToCollectionIfMissing([], documentProcess);
        expect(expectedResult).toEqual([documentProcess]);
      });

      it('should not add a DocumentProcess to an array that contains it', () => {
        const documentProcess: IDocumentProcess = sampleWithRequiredData;
        const documentProcessCollection: IDocumentProcess[] = [
          {
            ...documentProcess,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentProcessToCollectionIfMissing(documentProcessCollection, documentProcess);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentProcess to an array that doesn't contain it", () => {
        const documentProcess: IDocumentProcess = sampleWithRequiredData;
        const documentProcessCollection: IDocumentProcess[] = [sampleWithPartialData];
        expectedResult = service.addDocumentProcessToCollectionIfMissing(documentProcessCollection, documentProcess);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentProcess);
      });

      it('should add only unique DocumentProcess to an array', () => {
        const documentProcessArray: IDocumentProcess[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentProcessCollection: IDocumentProcess[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentProcessToCollectionIfMissing(documentProcessCollection, ...documentProcessArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentProcess: IDocumentProcess = sampleWithRequiredData;
        const documentProcess2: IDocumentProcess = sampleWithPartialData;
        expectedResult = service.addDocumentProcessToCollectionIfMissing([], documentProcess, documentProcess2);
        expect(expectedResult).toEqual([documentProcess, documentProcess2]);
      });

      it('should accept null and undefined values', () => {
        const documentProcess: IDocumentProcess = sampleWithRequiredData;
        expectedResult = service.addDocumentProcessToCollectionIfMissing([], null, documentProcess, undefined);
        expect(expectedResult).toEqual([documentProcess]);
      });

      it('should return initial array if no DocumentProcess is added', () => {
        const documentProcessCollection: IDocumentProcess[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentProcessToCollectionIfMissing(documentProcessCollection, undefined, null);
        expect(expectedResult).toEqual(documentProcessCollection);
      });
    });

    describe('compareDocumentProcess', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentProcess(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3659 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentProcess(entity1, entity2);
        const compareResult2 = service.compareDocumentProcess(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3659 };
        const entity2 = { id: 28301 };

        const compareResult1 = service.compareDocumentProcess(entity1, entity2);
        const compareResult2 = service.compareDocumentProcess(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 3659 };
        const entity2 = { id: 3659 };

        const compareResult1 = service.compareDocumentProcess(entity1, entity2);
        const compareResult2 = service.compareDocumentProcess(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
