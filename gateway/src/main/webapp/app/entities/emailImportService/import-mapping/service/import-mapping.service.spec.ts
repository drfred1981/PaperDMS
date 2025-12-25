import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IImportMapping } from '../import-mapping.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../import-mapping.test-samples';

import { ImportMappingService } from './import-mapping.service';

const requireRestSample: IImportMapping = {
  ...sampleWithRequiredData,
};

describe('ImportMapping Service', () => {
  let service: ImportMappingService;
  let httpMock: HttpTestingController;
  let expectedResult: IImportMapping | IImportMapping[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ImportMappingService);
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

    it('should create a ImportMapping', () => {
      const importMapping = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(importMapping).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ImportMapping', () => {
      const importMapping = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(importMapping).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ImportMapping', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ImportMapping', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ImportMapping', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addImportMappingToCollectionIfMissing', () => {
      it('should add a ImportMapping to an empty array', () => {
        const importMapping: IImportMapping = sampleWithRequiredData;
        expectedResult = service.addImportMappingToCollectionIfMissing([], importMapping);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(importMapping);
      });

      it('should not add a ImportMapping to an array that contains it', () => {
        const importMapping: IImportMapping = sampleWithRequiredData;
        const importMappingCollection: IImportMapping[] = [
          {
            ...importMapping,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addImportMappingToCollectionIfMissing(importMappingCollection, importMapping);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ImportMapping to an array that doesn't contain it", () => {
        const importMapping: IImportMapping = sampleWithRequiredData;
        const importMappingCollection: IImportMapping[] = [sampleWithPartialData];
        expectedResult = service.addImportMappingToCollectionIfMissing(importMappingCollection, importMapping);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(importMapping);
      });

      it('should add only unique ImportMapping to an array', () => {
        const importMappingArray: IImportMapping[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const importMappingCollection: IImportMapping[] = [sampleWithRequiredData];
        expectedResult = service.addImportMappingToCollectionIfMissing(importMappingCollection, ...importMappingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const importMapping: IImportMapping = sampleWithRequiredData;
        const importMapping2: IImportMapping = sampleWithPartialData;
        expectedResult = service.addImportMappingToCollectionIfMissing([], importMapping, importMapping2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(importMapping);
        expect(expectedResult).toContain(importMapping2);
      });

      it('should accept null and undefined values', () => {
        const importMapping: IImportMapping = sampleWithRequiredData;
        expectedResult = service.addImportMappingToCollectionIfMissing([], null, importMapping, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(importMapping);
      });

      it('should return initial array if no ImportMapping is added', () => {
        const importMappingCollection: IImportMapping[] = [sampleWithRequiredData];
        expectedResult = service.addImportMappingToCollectionIfMissing(importMappingCollection, undefined, null);
        expect(expectedResult).toEqual(importMappingCollection);
      });
    });

    describe('compareImportMapping', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareImportMapping(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8044 };
        const entity2 = null;

        const compareResult1 = service.compareImportMapping(entity1, entity2);
        const compareResult2 = service.compareImportMapping(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8044 };
        const entity2 = { id: 3649 };

        const compareResult1 = service.compareImportMapping(entity1, entity2);
        const compareResult2 = service.compareImportMapping(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 8044 };
        const entity2 = { id: 8044 };

        const compareResult1 = service.compareImportMapping(entity1, entity2);
        const compareResult2 = service.compareImportMapping(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
