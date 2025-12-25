import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IScannedPage } from '../scanned-page.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../scanned-page.test-samples';

import { RestScannedPage, ScannedPageService } from './scanned-page.service';

const requireRestSample: RestScannedPage = {
  ...sampleWithRequiredData,
  scannedDate: sampleWithRequiredData.scannedDate?.toJSON(),
};

describe('ScannedPage Service', () => {
  let service: ScannedPageService;
  let httpMock: HttpTestingController;
  let expectedResult: IScannedPage | IScannedPage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ScannedPageService);
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

    it('should create a ScannedPage', () => {
      const scannedPage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(scannedPage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ScannedPage', () => {
      const scannedPage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(scannedPage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ScannedPage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ScannedPage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ScannedPage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addScannedPageToCollectionIfMissing', () => {
      it('should add a ScannedPage to an empty array', () => {
        const scannedPage: IScannedPage = sampleWithRequiredData;
        expectedResult = service.addScannedPageToCollectionIfMissing([], scannedPage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(scannedPage);
      });

      it('should not add a ScannedPage to an array that contains it', () => {
        const scannedPage: IScannedPage = sampleWithRequiredData;
        const scannedPageCollection: IScannedPage[] = [
          {
            ...scannedPage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addScannedPageToCollectionIfMissing(scannedPageCollection, scannedPage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ScannedPage to an array that doesn't contain it", () => {
        const scannedPage: IScannedPage = sampleWithRequiredData;
        const scannedPageCollection: IScannedPage[] = [sampleWithPartialData];
        expectedResult = service.addScannedPageToCollectionIfMissing(scannedPageCollection, scannedPage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scannedPage);
      });

      it('should add only unique ScannedPage to an array', () => {
        const scannedPageArray: IScannedPage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const scannedPageCollection: IScannedPage[] = [sampleWithRequiredData];
        expectedResult = service.addScannedPageToCollectionIfMissing(scannedPageCollection, ...scannedPageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const scannedPage: IScannedPage = sampleWithRequiredData;
        const scannedPage2: IScannedPage = sampleWithPartialData;
        expectedResult = service.addScannedPageToCollectionIfMissing([], scannedPage, scannedPage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scannedPage);
        expect(expectedResult).toContain(scannedPage2);
      });

      it('should accept null and undefined values', () => {
        const scannedPage: IScannedPage = sampleWithRequiredData;
        expectedResult = service.addScannedPageToCollectionIfMissing([], null, scannedPage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(scannedPage);
      });

      it('should return initial array if no ScannedPage is added', () => {
        const scannedPageCollection: IScannedPage[] = [sampleWithRequiredData];
        expectedResult = service.addScannedPageToCollectionIfMissing(scannedPageCollection, undefined, null);
        expect(expectedResult).toEqual(scannedPageCollection);
      });
    });

    describe('compareScannedPage', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareScannedPage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4792 };
        const entity2 = null;

        const compareResult1 = service.compareScannedPage(entity1, entity2);
        const compareResult2 = service.compareScannedPage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4792 };
        const entity2 = { id: 12588 };

        const compareResult1 = service.compareScannedPage(entity1, entity2);
        const compareResult2 = service.compareScannedPage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 4792 };
        const entity2 = { id: 4792 };

        const compareResult1 = service.compareScannedPage(entity1, entity2);
        const compareResult2 = service.compareScannedPage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
