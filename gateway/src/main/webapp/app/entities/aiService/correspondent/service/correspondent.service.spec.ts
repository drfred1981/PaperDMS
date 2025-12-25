import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICorrespondent } from '../correspondent.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../correspondent.test-samples';

import { CorrespondentService, RestCorrespondent } from './correspondent.service';

const requireRestSample: RestCorrespondent = {
  ...sampleWithRequiredData,
  verifiedDate: sampleWithRequiredData.verifiedDate?.toJSON(),
  extractedDate: sampleWithRequiredData.extractedDate?.toJSON(),
};

describe('Correspondent Service', () => {
  let service: CorrespondentService;
  let httpMock: HttpTestingController;
  let expectedResult: ICorrespondent | ICorrespondent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CorrespondentService);
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

    it('should create a Correspondent', () => {
      const correspondent = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(correspondent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Correspondent', () => {
      const correspondent = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(correspondent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Correspondent', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Correspondent', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Correspondent', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Correspondent', () => {
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

    describe('addCorrespondentToCollectionIfMissing', () => {
      it('should add a Correspondent to an empty array', () => {
        const correspondent: ICorrespondent = sampleWithRequiredData;
        expectedResult = service.addCorrespondentToCollectionIfMissing([], correspondent);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(correspondent);
      });

      it('should not add a Correspondent to an array that contains it', () => {
        const correspondent: ICorrespondent = sampleWithRequiredData;
        const correspondentCollection: ICorrespondent[] = [
          {
            ...correspondent,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCorrespondentToCollectionIfMissing(correspondentCollection, correspondent);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Correspondent to an array that doesn't contain it", () => {
        const correspondent: ICorrespondent = sampleWithRequiredData;
        const correspondentCollection: ICorrespondent[] = [sampleWithPartialData];
        expectedResult = service.addCorrespondentToCollectionIfMissing(correspondentCollection, correspondent);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(correspondent);
      });

      it('should add only unique Correspondent to an array', () => {
        const correspondentArray: ICorrespondent[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const correspondentCollection: ICorrespondent[] = [sampleWithRequiredData];
        expectedResult = service.addCorrespondentToCollectionIfMissing(correspondentCollection, ...correspondentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const correspondent: ICorrespondent = sampleWithRequiredData;
        const correspondent2: ICorrespondent = sampleWithPartialData;
        expectedResult = service.addCorrespondentToCollectionIfMissing([], correspondent, correspondent2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(correspondent);
        expect(expectedResult).toContain(correspondent2);
      });

      it('should accept null and undefined values', () => {
        const correspondent: ICorrespondent = sampleWithRequiredData;
        expectedResult = service.addCorrespondentToCollectionIfMissing([], null, correspondent, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(correspondent);
      });

      it('should return initial array if no Correspondent is added', () => {
        const correspondentCollection: ICorrespondent[] = [sampleWithRequiredData];
        expectedResult = service.addCorrespondentToCollectionIfMissing(correspondentCollection, undefined, null);
        expect(expectedResult).toEqual(correspondentCollection);
      });
    });

    describe('compareCorrespondent', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCorrespondent(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25663 };
        const entity2 = null;

        const compareResult1 = service.compareCorrespondent(entity1, entity2);
        const compareResult2 = service.compareCorrespondent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25663 };
        const entity2 = { id: 12790 };

        const compareResult1 = service.compareCorrespondent(entity1, entity2);
        const compareResult2 = service.compareCorrespondent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25663 };
        const entity2 = { id: 25663 };

        const compareResult1 = service.compareCorrespondent(entity1, entity2);
        const compareResult2 = service.compareCorrespondent(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
