import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEmailImportImportMapping } from '../email-import-import-mapping.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../email-import-import-mapping.test-samples';

import { EmailImportImportMappingService } from './email-import-import-mapping.service';

const requireRestSample: IEmailImportImportMapping = {
  ...sampleWithRequiredData,
};

describe('EmailImportImportMapping Service', () => {
  let service: EmailImportImportMappingService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmailImportImportMapping | IEmailImportImportMapping[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EmailImportImportMappingService);
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

    it('should create a EmailImportImportMapping', () => {
      const emailImportImportMapping = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(emailImportImportMapping).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmailImportImportMapping', () => {
      const emailImportImportMapping = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(emailImportImportMapping).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmailImportImportMapping', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmailImportImportMapping', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmailImportImportMapping', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a EmailImportImportMapping', () => {
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

    describe('addEmailImportImportMappingToCollectionIfMissing', () => {
      it('should add a EmailImportImportMapping to an empty array', () => {
        const emailImportImportMapping: IEmailImportImportMapping = sampleWithRequiredData;
        expectedResult = service.addEmailImportImportMappingToCollectionIfMissing([], emailImportImportMapping);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(emailImportImportMapping);
      });

      it('should not add a EmailImportImportMapping to an array that contains it', () => {
        const emailImportImportMapping: IEmailImportImportMapping = sampleWithRequiredData;
        const emailImportImportMappingCollection: IEmailImportImportMapping[] = [
          {
            ...emailImportImportMapping,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmailImportImportMappingToCollectionIfMissing(
          emailImportImportMappingCollection,
          emailImportImportMapping,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmailImportImportMapping to an array that doesn't contain it", () => {
        const emailImportImportMapping: IEmailImportImportMapping = sampleWithRequiredData;
        const emailImportImportMappingCollection: IEmailImportImportMapping[] = [sampleWithPartialData];
        expectedResult = service.addEmailImportImportMappingToCollectionIfMissing(
          emailImportImportMappingCollection,
          emailImportImportMapping,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(emailImportImportMapping);
      });

      it('should add only unique EmailImportImportMapping to an array', () => {
        const emailImportImportMappingArray: IEmailImportImportMapping[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const emailImportImportMappingCollection: IEmailImportImportMapping[] = [sampleWithRequiredData];
        expectedResult = service.addEmailImportImportMappingToCollectionIfMissing(
          emailImportImportMappingCollection,
          ...emailImportImportMappingArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const emailImportImportMapping: IEmailImportImportMapping = sampleWithRequiredData;
        const emailImportImportMapping2: IEmailImportImportMapping = sampleWithPartialData;
        expectedResult = service.addEmailImportImportMappingToCollectionIfMissing([], emailImportImportMapping, emailImportImportMapping2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(emailImportImportMapping);
        expect(expectedResult).toContain(emailImportImportMapping2);
      });

      it('should accept null and undefined values', () => {
        const emailImportImportMapping: IEmailImportImportMapping = sampleWithRequiredData;
        expectedResult = service.addEmailImportImportMappingToCollectionIfMissing([], null, emailImportImportMapping, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(emailImportImportMapping);
      });

      it('should return initial array if no EmailImportImportMapping is added', () => {
        const emailImportImportMappingCollection: IEmailImportImportMapping[] = [sampleWithRequiredData];
        expectedResult = service.addEmailImportImportMappingToCollectionIfMissing(emailImportImportMappingCollection, undefined, null);
        expect(expectedResult).toEqual(emailImportImportMappingCollection);
      });
    });

    describe('compareEmailImportImportMapping', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmailImportImportMapping(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 20388 };
        const entity2 = null;

        const compareResult1 = service.compareEmailImportImportMapping(entity1, entity2);
        const compareResult2 = service.compareEmailImportImportMapping(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 20388 };
        const entity2 = { id: 30950 };

        const compareResult1 = service.compareEmailImportImportMapping(entity1, entity2);
        const compareResult2 = service.compareEmailImportImportMapping(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 20388 };
        const entity2 = { id: 20388 };

        const compareResult1 = service.compareEmailImportImportMapping(entity1, entity2);
        const compareResult2 = service.compareEmailImportImportMapping(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
