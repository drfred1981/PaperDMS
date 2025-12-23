import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IManualChapter } from '../manual-chapter.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../manual-chapter.test-samples';

import { ManualChapterService } from './manual-chapter.service';

const requireRestSample: IManualChapter = {
  ...sampleWithRequiredData,
};

describe('ManualChapter Service', () => {
  let service: ManualChapterService;
  let httpMock: HttpTestingController;
  let expectedResult: IManualChapter | IManualChapter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ManualChapterService);
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

    it('should create a ManualChapter', () => {
      const manualChapter = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(manualChapter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ManualChapter', () => {
      const manualChapter = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(manualChapter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ManualChapter', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ManualChapter', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ManualChapter', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addManualChapterToCollectionIfMissing', () => {
      it('should add a ManualChapter to an empty array', () => {
        const manualChapter: IManualChapter = sampleWithRequiredData;
        expectedResult = service.addManualChapterToCollectionIfMissing([], manualChapter);
        expect(expectedResult).toEqual([manualChapter]);
      });

      it('should not add a ManualChapter to an array that contains it', () => {
        const manualChapter: IManualChapter = sampleWithRequiredData;
        const manualChapterCollection: IManualChapter[] = [
          {
            ...manualChapter,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addManualChapterToCollectionIfMissing(manualChapterCollection, manualChapter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ManualChapter to an array that doesn't contain it", () => {
        const manualChapter: IManualChapter = sampleWithRequiredData;
        const manualChapterCollection: IManualChapter[] = [sampleWithPartialData];
        expectedResult = service.addManualChapterToCollectionIfMissing(manualChapterCollection, manualChapter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(manualChapter);
      });

      it('should add only unique ManualChapter to an array', () => {
        const manualChapterArray: IManualChapter[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const manualChapterCollection: IManualChapter[] = [sampleWithRequiredData];
        expectedResult = service.addManualChapterToCollectionIfMissing(manualChapterCollection, ...manualChapterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const manualChapter: IManualChapter = sampleWithRequiredData;
        const manualChapter2: IManualChapter = sampleWithPartialData;
        expectedResult = service.addManualChapterToCollectionIfMissing([], manualChapter, manualChapter2);
        expect(expectedResult).toEqual([manualChapter, manualChapter2]);
      });

      it('should accept null and undefined values', () => {
        const manualChapter: IManualChapter = sampleWithRequiredData;
        expectedResult = service.addManualChapterToCollectionIfMissing([], null, manualChapter, undefined);
        expect(expectedResult).toEqual([manualChapter]);
      });

      it('should return initial array if no ManualChapter is added', () => {
        const manualChapterCollection: IManualChapter[] = [sampleWithRequiredData];
        expectedResult = service.addManualChapterToCollectionIfMissing(manualChapterCollection, undefined, null);
        expect(expectedResult).toEqual(manualChapterCollection);
      });
    });

    describe('compareManualChapter', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareManualChapter(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 31776 };
        const entity2 = null;

        const compareResult1 = service.compareManualChapter(entity1, entity2);
        const compareResult2 = service.compareManualChapter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 31776 };
        const entity2 = { id: 10019 };

        const compareResult1 = service.compareManualChapter(entity1, entity2);
        const compareResult2 = service.compareManualChapter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 31776 };
        const entity2 = { id: 31776 };

        const compareResult1 = service.compareManualChapter(entity1, entity2);
        const compareResult2 = service.compareManualChapter(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
