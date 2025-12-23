import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IArchiveDocument } from '../archive-document.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../archive-document.test-samples';

import { ArchiveDocumentService, RestArchiveDocument } from './archive-document.service';

const requireRestSample: RestArchiveDocument = {
  ...sampleWithRequiredData,
  addedDate: sampleWithRequiredData.addedDate?.toJSON(),
};

describe('ArchiveDocument Service', () => {
  let service: ArchiveDocumentService;
  let httpMock: HttpTestingController;
  let expectedResult: IArchiveDocument | IArchiveDocument[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ArchiveDocumentService);
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

    it('should create a ArchiveDocument', () => {
      const archiveDocument = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(archiveDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ArchiveDocument', () => {
      const archiveDocument = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(archiveDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ArchiveDocument', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ArchiveDocument', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ArchiveDocument', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addArchiveDocumentToCollectionIfMissing', () => {
      it('should add a ArchiveDocument to an empty array', () => {
        const archiveDocument: IArchiveDocument = sampleWithRequiredData;
        expectedResult = service.addArchiveDocumentToCollectionIfMissing([], archiveDocument);
        expect(expectedResult).toEqual([archiveDocument]);
      });

      it('should not add a ArchiveDocument to an array that contains it', () => {
        const archiveDocument: IArchiveDocument = sampleWithRequiredData;
        const archiveDocumentCollection: IArchiveDocument[] = [
          {
            ...archiveDocument,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addArchiveDocumentToCollectionIfMissing(archiveDocumentCollection, archiveDocument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ArchiveDocument to an array that doesn't contain it", () => {
        const archiveDocument: IArchiveDocument = sampleWithRequiredData;
        const archiveDocumentCollection: IArchiveDocument[] = [sampleWithPartialData];
        expectedResult = service.addArchiveDocumentToCollectionIfMissing(archiveDocumentCollection, archiveDocument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(archiveDocument);
      });

      it('should add only unique ArchiveDocument to an array', () => {
        const archiveDocumentArray: IArchiveDocument[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const archiveDocumentCollection: IArchiveDocument[] = [sampleWithRequiredData];
        expectedResult = service.addArchiveDocumentToCollectionIfMissing(archiveDocumentCollection, ...archiveDocumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const archiveDocument: IArchiveDocument = sampleWithRequiredData;
        const archiveDocument2: IArchiveDocument = sampleWithPartialData;
        expectedResult = service.addArchiveDocumentToCollectionIfMissing([], archiveDocument, archiveDocument2);
        expect(expectedResult).toEqual([archiveDocument, archiveDocument2]);
      });

      it('should accept null and undefined values', () => {
        const archiveDocument: IArchiveDocument = sampleWithRequiredData;
        expectedResult = service.addArchiveDocumentToCollectionIfMissing([], null, archiveDocument, undefined);
        expect(expectedResult).toEqual([archiveDocument]);
      });

      it('should return initial array if no ArchiveDocument is added', () => {
        const archiveDocumentCollection: IArchiveDocument[] = [sampleWithRequiredData];
        expectedResult = service.addArchiveDocumentToCollectionIfMissing(archiveDocumentCollection, undefined, null);
        expect(expectedResult).toEqual(archiveDocumentCollection);
      });
    });

    describe('compareArchiveDocument', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareArchiveDocument(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 30053 };
        const entity2 = null;

        const compareResult1 = service.compareArchiveDocument(entity1, entity2);
        const compareResult2 = service.compareArchiveDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 30053 };
        const entity2 = { id: 24242 };

        const compareResult1 = service.compareArchiveDocument(entity1, entity2);
        const compareResult2 = service.compareArchiveDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 30053 };
        const entity2 = { id: 30053 };

        const compareResult1 = service.compareArchiveDocument(entity1, entity2);
        const compareResult2 = service.compareArchiveDocument(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
